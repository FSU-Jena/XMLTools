package edu.fsuj.csb.tools.xml;
import java.io.Serializable;


/**
 * wraps a string object, so it can be extended
 * @author Stephan Richter
 *
 */
public class StringWrapper implements Serializable{

  private static final long serialVersionUID = 18;
	private String s;
	
	/**
	 * creates a new String wrapper object
	 * @param s
	 */
	public StringWrapper(String s) {
		this.s=s;
  }
	
	/**
	 * get the contained string
	 * @return the string contained in this wrapper
	 */
	public String get(){
		return this.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return s;
	}
}
