package gatech_Ziyi;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The DataFilter performs to filter the xml in order to implement the the four scenarios
 *
 * It applies the org.xml.sax xml parsing package developed by IBM. And it overrides the 
 * XMLFilterImpl methods, like startElement(..), endElement(..) and characters(..) in order 
 * to manipulate the tag name, attributes and content between open tag and corresponding 
 * closing tags.
 *
 * @author Ziyi Jiang
 * @version 1.0 11/08/2013
 */
public class DataFilter extends XMLFilterImpl
{
	 boolean istype = false;// used to control the search for"AppHostType" tag 
	 
	 // Override the startElement(...) method to manipulate the tag name and its attributes
	 public void startElement (String namespaceUri, String localName,
			            String qualifiedName, Attributes attributes)
	                       throws SAXException
	 {
		// retrieve the attributes here
	    AttributesImpl attributesImpl = new AttributesImpl(attributes);
	    
	    // scenario 3: -The attribute StretchedColumnNumber is deprecated,
	    // and should now be StretchColumnNumber 
	    if (localName.equals("Control")){
		    if (attributesImpl.getValue("StretchedColumnNumber")!=null){
		    	attributesImpl.setAttribute(1, "", "StretchColumnNumber",
		    			"StretchColumnNumber", "String", 
		    			attributesImpl.getValue("StretchedColumnNumber"));
		    }
	    }
	    // scenario 2: -The label NumberNameLabel, with value ‘Number’ is missing. 
		if(localName.equals("label")&& attributesImpl.getLength()==0){
			attributesImpl.addAttribute("", "Key", "Key", "String", "NumberNameLabel");
		} 
		// scenario 1: -	The AppHostType ‘Kahua.Silverlight’ is deprecated and should be ‘Kahua.Desktop’
		if((attributesImpl.getValue("AppHostType")!=null)&&(attributesImpl.
				getValue("AppHostType").equals("Kahua.Silverlight"))){
			attributesImpl.setAttribute(attributesImpl.getIndex("AppHostType"), "",
					"AppHostType", "AppHostType", "String", "Kahua.Desktop");
		}
		// scenario 4: VerticalAlignment should appear before HorizontalAlignment.
		if((attributesImpl.getValue("VerticalAlignment")!= null) 
				&&(attributesImpl.getValue("HorizontalAlignment")!= null)){
			if(attributesImpl.getIndex("VerticalAlignment")
					<=attributesImpl.getIndex("HorizontalAlignment")){
				 // no need to change the order
				 super.startElement(namespaceUri, localName, qualifiedName, attributesImpl);
				 return;
			}else{
				// exchange the order of the two
				String tempH = attributesImpl.getValue("HorizontalAlignment");
				int indexV = attributesImpl.getIndex("VerticalAlignment");
				attributesImpl.setAttribute(attributesImpl.getIndex("HorizontalAlignment"),
						"", "VerticalAlignment", "VerticalAlignment", "String", attributesImpl.getValue("VerticalAlignment"));
				attributesImpl.setAttribute(indexV, "", "HorizontalAlignment", "HorizontalAlignment", "String", tempH);
			}
		
		}
		super.startElement(namespaceUri, localName, qualifiedName, attributesImpl);
		
		// help control the scenario 1 characters(..)
		if(localName.equals("AppHostType")){
		    	istype = true;
		    }
		 }  
	// Override the endElement(..) method to insert new tags before the end of the parent tag    
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
		AttributesImpl attr = new AttributesImpl();
		
		// scenario 2: -The label NumberNameLabel, with value ‘Number’ is missing. 
	    attr.addAttribute("", "Key", "Key", "String", "NumberNameLabel");
	    if(localName.equals("Labels")){
	    	super.startElement("", "label", "label", attr);
	    	char[] chars = String.valueOf("Number").toCharArray();
	        super.characters(chars, 0, chars.length);
	        super.endElement("", "label", "label");
	    }
	    // help control the scenario 1 characters(..)
	    if(localName.equals("AppHostType")){
	    	istype = false;
	    }
	    super.endElement(namespaceURI, localName, qualifiedName);
		}
	// Overide the characters(..) method to manipulate the content between the tags
	public void characters(char[] text, int start, int length) throws SAXException {
		
		// scenario 1: -	The AppHostType ‘Kahua.Silverlight’ is deprecated and should be ‘Kahua.Desktop’
		if(istype){
			String str = new String(text);
			String strInner = str.substring(start, start+length);
			
			strInner = strInner.replaceAll("Kahua.Silverlight", "Kahua.Desktop");
			super.characters(strInner.toCharArray(), 0, strInner.length());
			return;
		}
	    super.characters(text, start, length);
	}
}
