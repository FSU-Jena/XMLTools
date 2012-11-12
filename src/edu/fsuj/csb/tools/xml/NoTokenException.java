package edu.fsuj.csb.tools.xml;

/**
 * encapsulates a exception, that is thrown, if a xml reader finds no more readable tokens
 * @author Stephan Richter
 *
 */
public class NoTokenException extends Exception {


  private static final long serialVersionUID = 5232221901841189843L;

	/**
	 * creates new NoTokenException, holding the tag code of the token
	 * @param tag
	 */
	public NoTokenException(String tag) {
		super(tag);
	}

}
