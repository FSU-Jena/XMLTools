package edu.fsuj.csb.tools.xml;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * 
 * implements a reader to parse xml files
 * @author Stephan Richter
 *
 */
public class XMLReader {
		

	private BufferedReader bufferedReader; // holds the file handle
	private boolean firstToken;

	/**
	 * creates a new reader on the xml file determined by its url
	 * @param url the url to a xml file
	 * @throws IOException
	 */
	public XMLReader(URL url) throws IOException {
		bufferedReader=new BufferedReader(new InputStreamReader(url.openStream()));
		firstToken=true;
	}
	
	public XMLReader(String filename) throws FileNotFoundException {
		bufferedReader=new BufferedReader(new FileReader(filename));
		firstToken=true;
  }
	
	public XMLReader(File file) throws FileNotFoundException {
		bufferedReader=new BufferedReader(new FileReader(file));
		firstToken=true;
  }
	
	/**
	 * reads the nextmost xml token from the xml file
	 * @return the xml token object (including its subordered tokens)
	 * @throws IOException if error on file read occurs
	 * @throws NoTokenException if file contains no more tokens
	 */
	public XmlToken readToken() throws IOException, NoTokenException{
			XmlToken result = new XmlToken(bufferedReader,firstToken);
			firstToken=false;
			return result; 		
	}
	
	/**
	 * close the file reader
	 * @throws IOException if error occurs on bufferedReader.close()
	 */
	public void close() throws IOException{
		bufferedReader.close();
	}
}
