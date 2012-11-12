package edu.fsuj.csb.tools.xml;

/**
 * simple interface for XmlObjects forcing derived classes to implement the getCode method
 * @author Stephan Richter
 *
 */
public interface XmlObject {

	/**
	 * returns the xml representation of this object
	 * @return the xml code string as stringbuffer
	 */
	public StringBuffer getCode();
}
