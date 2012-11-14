package edu.fsuj.csb.tools.xml;

import java.io.IOException;
import java.net.URL;

import edu.fsuj.csb.tools.newtork.pagefetcher.PageFetcher;

public class CachedXMLReader extends XMLReader {

	public CachedXMLReader(URL url) throws IOException {
	  super(PageFetcher.fetch(url)==null?null:PageFetcher.cachedFile(url));
  }

}
