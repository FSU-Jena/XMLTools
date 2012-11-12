package edu.fsuj.csb.tools.xml;
import java.io.Serializable;
import java.util.Comparator;


/**
 * implements a comparator for any Object type. the comaprison is done by comparison of the string representations of the objects
 * @author Stephan Richter
 *
 */
public class ObjectComparator implements Serializable,Comparator<Object>{

  private static final long serialVersionUID = 17;
	private static ObjectComparator staticComparator;
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object t1,Object t2){
		//System.out.println("Compared "+t1.getClass().getName()+" ("+t1.toString()+")\nwith "+t2.getClass().getName()+" ("+t2.toString()+").");
		return t1.toString().compareTo(t2.toString());
	}

	/**
	 * returns an ObjectComparator object, which can be used to compare objects by their string representation
	 * @return the ObjectComparator 
	 */
	public static ObjectComparator get() {
	  if (staticComparator==null) staticComparator=new ObjectComparator();
	  return staticComparator;
  }
}