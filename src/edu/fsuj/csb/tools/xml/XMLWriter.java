package edu.fsuj.csb.tools.xml;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * class used to write xml files
 * @author Stephan Richter
 *
 */
public class XMLWriter {
	private BufferedWriter file; // internal file handle
	
	/**
	 * create a new xml file writer object
	 * @param filename name of the file to write to
	 * @throws SecurityException
	 * @throws IOException
	 */
	public XMLWriter(String filename) throws SecurityException, IOException{
		file=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
		file.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");		
	}
	
	/**
	 * create a new xml file writer object
	 * @param url the filename of the file to write to represented as url
	 * @throws URISyntaxException if the url is not well formed and connot be converted to a URI
	 * @throws IOException
	 */
	public XMLWriter(URL url) throws URISyntaxException, IOException {
		file=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(url.toURI())), "UTF-8"));
		file.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");	
  }

	/**
	 * write xml content to the file
	 * @param o an xml object
	 * @throws IOException
	 */
	public void write(XmlObject o) throws IOException{
		file.write(o.getCode().toString());		
	}

	/**
	 * inserts i tabulators to each line of the given code
	 * @param code the xml code to be shifted
	 * @param i the number of tabs by which the code shall be shifted
	 * @return the modified code
	 */
	public static String shift(StringBuffer code, int i) {
		String space="\n";
		for (int k=0; k<i; k++) space+="\t";
		return code.toString().replaceAll("\n", space);
  }

	/**
	 * finish and close the file
	 * @throws IOException
	 */
	public void close() throws IOException {
		file.write("\n");
		file.close();
		file=null;
  }
}
