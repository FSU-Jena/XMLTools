package edu.fsuj.csb.tools.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

/**
 * a class to read xml tokens and their subordered tokens from a bufferedReader. used by XmlReader
 * @author Stephan Richter
 *
 */
public class XmlToken implements XmlObject{
	private Vector<XmlToken> subTokens; // the set of tokens contained by this token
	protected String tokenClass = null; // the class strng of this token
	private TreeMap<String, String> values; // the mapping from this token's elements to their values
	private BufferedReader reader;
	private String content=null;
	
	public XmlToken(BufferedReader br) throws IOException, NoTokenException {
		this(br,true);
	}
	
	/**
	 * creates a new token by reading the next tag from the given reader
	 * @param br the BufferedReader, from which the tag shall be read
	 * @throws IOException if BufferedReader can not be read
	 * @throws NoTokenException if there is no token in the reader
	 */
	protected XmlToken(BufferedReader br,boolean seek) throws IOException, NoTokenException {
		if (seek)	seekTag(br);
		String tag = readTag(br); // search the next tag and return the string between "<" and ">".		
		if (tag==null) throw new NoTokenException("end of file reached");
		content=seekTag(br);
		if (tag.startsWith("/")) throw new NoTokenException(tag); // stag should not start with '</'
		parseValues(tag); // read in the tag's values
		subTokens = new Vector<XmlToken>();
		if (!tag.endsWith("/")) { // check, whether we have subtokens
			while (true) { // read subordered tokens
				try {
					subTokens.add(new XmlToken(br,false));
				} catch (NoTokenException nte) {
					if (nte.getMessage().startsWith("/" + tokenClass)) break; // stop reading subordered tokens on closing tag
					throw nte;
				}
			}
		}
	}
	
	public XmlToken(String tClass) {
		subTokens = new Vector<XmlToken>();
		tokenClass=tClass.replace(" ", "");
		values=null;
		content=null;
  }

	public XmlToken() {
		this("XmlToken");
		tokenClass=getClass().getSimpleName().toLowerCase();
  }

	/**
	 * @return the token next to the current one
	 * @throws IOException
	 * @throws NoTokenException
	 */
	public XmlToken next() throws IOException, NoTokenException{
		return new XmlToken(reader);
	}

	/**
	 * parse the key-value relations within this tag
	 * @param tag
	 */
	private void parseValues(String tag) {
		try {
			if (tag.startsWith("!")) return; // ignore tags starting with '<!'
			while (tag.endsWith("/") || tag.endsWith(" ")) tag = tag.substring(0, tag.length() - 1); // trim tag strings

			int i = tag.indexOf(' '); // search for first space (this is where the tag class ends
			if (i == -1) i = tag.length(); // if no space contained: tag class fills the whole tag, i.e. we have no key-value assignments
			tokenClass = tag.substring(0, i); // read token class
			boolean inString = false; // from here on, we read key-value assignments, starting outside of strings...
			while (i < tag.length()) {
				int k = i + 1;
				while (++i < tag.length()) {
					if (tag.charAt(i) == '"') inString = !inString;
					if (tag.charAt(i) == ' ' && !inString && tag.substring(k, i).contains("=")) break; // read until space is found, which is not within a closed string
				}
				try {
					addValuePair(tag.substring(k, i)); // parse the most recent string
				} catch (IndexOutOfBoundsException e) {
					Tools.warnOnce("xml not well formed at tag <" + tag + ">");
				}
			}
		} catch (ArrayIndexOutOfBoundsException aobe) {
			aobe.printStackTrace();
			System.out.println("tag string was: " + tag);
		}
	}

	/**
	 * reads a key="value" assignment and stores it's data into the values collection of this tag
	 * @param s
	 */
	private void addValuePair(String s) {
		int i=s.indexOf('=');
		String key=s.substring(0, i).trim();
		s=s.substring(i+1).trim();
		if (s.charAt(0)=='"') s=s.substring(1,s.length()-1);
		if (values == null) values = new TreeMap<String, String>(ObjectComparator.get());
		values.put(key, s);
	}

	/**
	 * creates a informal description of this token and writes it into a StringBuffer
	 * @return the token description
	 */
	public StringBuffer description() {
		StringBuffer sb = new StringBuffer("token class=" + tokenClass + "\n");
		if (content() !=null ) sb.append(" * content: "+content+"\n");
		if (values != null) {
			for (Iterator<String> it = values.keySet().iterator(); it.hasNext();) {
				String key = it.next();
				sb.append(" - " + key + " => " + values.get(key) + "\n");
			}
		}
		if (subTokens != null) {
			for (Iterator<XmlToken> it = subTokens.iterator(); it.hasNext();) sb.append(XMLWriter.shift(it.next().description(), 1));
		}
		return sb;
	}
	
	/**
	 * @return the set of keys given in this token
	 */
	public Set<String> keys(){		
		if (values==null) return null;
		return values.keySet();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return description().toString();
	}

	/**
	 * prints the tag description to the standard output
	 */
	void print() {
		System.out.println(toString());
	}

	/**
	 * reads a xml token from the given buffered reader
	 * @param br the bufferd reader to read from
	 * @return the xml tag code
	 * @throws IOException
	 */
	private String readTag(BufferedReader br) throws IOException {
//		seekTag(br);
		boolean inString = false; // used to skip strings
		int c = br.read();
		if (c<0) return null;
		StringBuffer tag = new StringBuffer();
		while (inString || (c != '>')){
			if (c==-1) break;
			if (c=='"') inString=!inString;
			tag.append((char) c);
			c=br.read();
		}
		String result = tag.toString();
		if (result.startsWith("!--")){
			content=seekTag(br);
			return readTag(br); // ignore comment tags
		}
		return result;
	}

	/**
	 * look for the next tag in the buffered reader
	 * @param br the buffered reader to read from
	 * @throws IOException
	 */
	private String seekTag(BufferedReader br) throws IOException {
		int c;
		StringBuffer sb=new StringBuffer();
		do {
			while ((c=br.read()) != '<') {
				sb.append((char)c);
				if (c<0) return null;
			}
			br.mark(10);
			c = br.read();			
		} while (c == '?' || c == '!');
		br.reset();
		if (sb.length()==0) return null;
		return sb.toString().trim();
	}

	/**
	 * @return the class of this xml tag
	 */
	public String tokenClass() {
		return tokenClass;
	}

	/**
	 * creates an Interator on the collection of subtokens of this tag
	 * @return the iterator of the subordered tokens
	 */
	public Vector<XmlToken> subtokens() {
		return subTokens;
	}

	/**
	 * counts the number of subordered tokens owned by this token
	 * @return the number of subtokens of this class
	 */
	public int numberOfSubTokens() {
		if (subTokens == null) return 0;
		return subTokens.size();
	}

	
  /**
	 * requests the value related to a certain key for this token
	 * @param key the name of the key
	 * @return the value related to the key or null, if there is no such key present
	 */
	public String getValue(String key) {
		if (values==null) return null;
		return values.get(key);
	}

	/**
	 * tests, whether the given tag is of a certein class
	 * @param name the class name to be tested
	 * @return true, if the current tag's class equals the given name, of false if not
	 */
	public boolean instanceOf(String name) {
		return tokenClass.equals(name);
	}

	/**
	 * @return a new (empty) set of xmlTokens utilized with an objectComparator
	 */
	public static TreeSet<XmlToken> newSet() {		
	  return new TreeSet<XmlToken>(ObjectComparator.get());
  }
	
	/**
	 * @return the content of this token
	 */
	public String content(){
		if (content==null) return null;
		if (content.length()==0) return null;
		return content;
	}
	
	public void add(XmlToken token) {
		subTokens.add(token);
  }

	public void setValue(String key, Object value) {
		if (values == null) values = new TreeMap<String, String>(ObjectComparator.get());
		values.put(key, value.toString());
  }

	public void getCode(StringBuffer sb) {
		Tools.startMethod("XmlToken["+tokenClass+"].getCode()");
		sb.append("<"+tokenClass);
		if (values!=null && !values.isEmpty()){
			for (Iterator<Entry<String, String>> it = values.entrySet().iterator();it.hasNext();){
				Entry<String, String> entry = it.next();
				sb.append(" "+entry.getKey()+"=\""+entry.getValue()+"\"");
			}
		}
		if (this.subTokens.isEmpty() && content==null){
			sb.append("/>");
		} else {			
			sb.append(">\n");
			for (XmlToken subtoken:subtokens()){				
				subtoken.getCode(sb);
				sb.append("\n");
			}
			if (content()!=null) sb.append(content+"\n");
			sb.append("</"+tokenClass+">");
		}		
		Tools.endMethod();		
  }
	
	@SuppressWarnings("rawtypes")
  public void setContent(Collection c){
		setContent(c.toString().replace("[", "").replace("]", ""));
	}
	
	public void setContent(Object o){
		content=o.toString();
	}

	public void setValue(String key, int value) {
		setValue(key, ""+value);
  }

	public int getIntValue(String key) {
	  return Integer.parseInt(getValue(key));
		
  }
}
