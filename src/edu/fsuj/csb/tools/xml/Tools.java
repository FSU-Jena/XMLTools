package edu.fsuj.csb.tools.xml;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.TreeSet;

/**
 * 
 * this class collects some helper methods
 * @author Stephan Richter
 *
 */
public class Tools {

	/**
	 * create a new set of strings utilized with an object comparator
	 * @return the empty set of strings
	 */
	public static TreeSet<String> StringSet(){
  	return new TreeSet<String>(ObjectComparator.get());
  }
	
	public static String removeHtmlEntities(String text){
		return text.replace("&gt;",">").replace("&lt;","<").replace("&nbsp;", " ").replace("&amp;", "&").replace("&apos;", "'");
	}

	/**
   * strips html tags off a string
   * @param line the string to be cleaned
   * @return the string without html tags
   */
  public static String removeHtml(String line) {
  	line=replaceSymbolFont(line);
  	int open=line.indexOf("<");
  	while (open>-1){
  		int close=line.indexOf(">",open);
  		if ((close>-1) && (close+1<line.length())){
  			line=line.substring(0,open)+line.substring(close+1); // remove tag
  		} else line=line.substring(0,open);
  		open=line.indexOf("<");
  	}
  	line=removeHtmlEntities(line);
  	int i;
  	while ((i=line.indexOf("&#"))>0){
  		int j=line.indexOf(";",i);
  		String code=line.substring(i+2,j);
  		char c=((char)Integer.parseInt(code));
  		line=line.replace("&#"+code+";", ""+c);
  	}
    return  line;
  }

	public static String replaceSymbolFont(String line) {
		while (true){
			int	start=line.toUpperCase().indexOf("<FONT FACE=\"SYMBOL\">");
			//System.out.println("start: "+start);
			if (start<0) return line;
			int end=line.toUpperCase().indexOf("</FONT>",start);
			//System.out.println("end: "+end);
			StringBuffer infix=new StringBuffer();
			for (int index=start+20; index<end; index++) {
				int b=line.charAt(index);
				if ((96<b && b<121)||(64<b && b<90)) {
					infix.append((char)(line.charAt(index)+848));
				} else {
					infix.append((char)b);
				}
			}
			//System.out.println("infix: "+infix+"\n");
			line=line.substring(0,start)+infix+line.substring(end);
		}
  }

	/**
	 * create a new set of urls utilized with an object comparator
	 * @return the empty set of urls
	 */
	public static TreeSet<URL> URLSet() {
	  return new TreeSet<URL>(ObjectComparator.get());
  }

	private static TreeSet<String> givenWarnings=StringSet();
	
	/**
	 * display a message after a leading "Warning: " on the error-output, but only display it once
	 * @param message the message to be shown
	 */
	public static boolean warnOnce(String message){
		if (!givenWarnings.contains(message)) {
			warn(message);
			return true;
		}
		return false;
	}

	/**
	 * display a message after a leading "Warning: " on the error output
	 * @param message the message to be shown
	 */
	public static void warn(Object message) {
		System.err.println("Warning: "+message);
		givenWarnings.add(message.toString());
  }

	/**
	 * display a messae with a leading "Note: "
	 * @param message the message to be displayed after "Note: "
	 */
	public static void note(Object message) {
		System.out.println("Note: "+message);
		givenWarnings.add(message.toString());
  }
	
	/**
	 * resets the history of warnings and notes, so warnOnce / noteOnce will display a note, if it had been shown before
	 */
	public static void resetWarnings() {
		givenWarnings=StringSet();
  }

	/**
	 * display a text with a leading "Note: ", but only display it, if it has not been displayed before
	 * @param message the text to be displayed after "Note: "
	 */
	public static void noteOnce(String message) {
		if (!givenWarnings.contains(message)) note(message);
  }

	public static void notImplemented(String methodname) {
		warn(methodname+"() not implemented, yet!");
  }
	
	private static int intendation=0;
	private static boolean logging=true;
	private static int intendationLimit=Integer.MAX_VALUE;;

	public static void startMethod(String string) {
		reduceIntendation();
		indent("+-"+string);
		intendation+=2;
		//indent("");
  }
	
	public static void endMethod(Object object) {
		reduceIntendation();
		if (intendation<0) intendation=0;
		indent("⌊_> return "+object);
		//indent("");
  }
	
	public static void endMethod() {
		reduceIntendation();
		indent("⌊____");
		//indent("");
  }

	private static void reduceIntendation() {
		intendation--;
		if (intendation<0) intendation=0;
  }

	public static void indent(Object message) {		
		if (logging && intendation<intendationLimit){
		for (int i=0; i<intendation; i++){
			System.out.print("| ");
		}
		System.out.println(message);
		}
  }

	public static void disableLogging() {
		logging=false;	  
  }
	
	public static void enableLogging(){
		logging=true;
	}

	public static boolean logging() {
	  return logging;
  }
	
	public static void setIntendationLimit(int limit){
		if (limit<0) {
			intendationLimit=Integer.MAX_VALUE;
		} else intendationLimit=limit;
	}

	public static void enableLogging(int intendationLimit) {
		logging=true;
		setIntendationLimit(intendationLimit);
	  
  }

	public static String firstNumber(String line) {
		StringBuffer num=new StringBuffer();
		int index=0;
		int len=line.length();
		boolean inNumber=false;
		while (index<len){
			char c=line.charAt(index);
			if (Character.isDigit(c)) {
				inNumber=true;
				num.append(c);
			} else if (inNumber){
				if (c=='.') {
					num.append(c);
				} else if (c==',') {
					num.append('.');
				} else break;
			}
			index++;
		}		
	  return num.toString();
	}
	
	public static void resetIntendation(){
		intendation=0;
	}

	public static void main(String[] args) {
		System.out.println(firstNumber("      Average: 291.3853<br>Monoisotopic: 291.183443671")+"<");
	}

	public static void dieLoudly(String string) {
		int a=0;
		int b=0;
		System.err.println(string);
		System.out.println(a/b);
  }
	
	public static TreeSet<Integer> nonNullSet(TreeSet<Integer> set) {
		if (set==null) return new TreeSet<Integer>();
	  return set;
  }

	public static void restoreSysOut() {
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }

	public static void endMethod(Object o, int maxLen) {		
		String s=o.toString();
		if (s.length()>maxLen-5){
			endMethod("{"+s.substring(0,maxLen-5)+"...}");
		} else endMethod(s);
  }

}