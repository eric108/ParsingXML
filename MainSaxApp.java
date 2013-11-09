package gatech_Ziyi;

import org.xml.sax.*;

import org.xml.sax.helpers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*; 

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * The MainSaxApp performs to implement the four scenarios
 *
 * It applies the org.xml.sax xml parsing package developed by IBM. It generally 
 * uses customized DataFilter to modify the content of the xml and output the xml info.
 *
 * @author Ziyi Jiang
 * @version 1.0 11/08/2013
 */
public class MainSaxApp {
	
    public static File INPUT_FILE = new File("src\\gatech_Ziyi\\QuickNote.xml");
    // intermediate file with <xml> hearder and I deleted it at the end of the program
    public static File OUTPUT_FILE = new File("src\\gatech_Ziyi\\NewQuickNote.xml"); 
    // removed the <xml> header
    public static File FINAL_FILE = new File("src\\gatech_Ziyi\\FinalQuickNote.xml");
    
    /**
	 * Main method inpu the file and using filter to manipulate the xml 
	 * and then ouput the final result
	 * 
	 * @param args input from user
	 */
    public final static void main(String[] args) {
        try {
        	
            FileReader fr = new FileReader(INPUT_FILE);
            BufferedReader br = new BufferedReader(fr);
            InputSource is = new InputSource(br);
            // initial the file reader 
            XMLReader parser = XMLReaderFactory.createXMLReader();
            // initial the file filter 
            DataFilter filter = new DataFilter();
            filter.setParent(parser);
            // link the file and the filter
            SAXSource source = new SAXSource(filter, is);
            // set up the output stream
            FileWriter fw = new FileWriter(OUTPUT_FILE);
            BufferedWriter bw = new BufferedWriter(fw);
            StreamResult result = new StreamResult(bw);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();      
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");            
            transformer.transform(source, result);
            
            fw.close();
            
            // read the intermediate file and remove the <xml> header
            BufferedReader br1 = null;
            Writer writer = null;
    		try {
    			String sCurrentLine;
    			String previousLine;
    			br1 = new BufferedReader(new FileReader(OUTPUT_FILE));
    			writer = new BufferedWriter(new OutputStreamWriter(
      		          new FileOutputStream(FINAL_FILE), "utf-8"));
      		   //sCurrentLine = br1.readLine();
      		   sCurrentLine = br1.readLine();
      		  // previousLine = sCurrentLine;
      		   previousLine = sCurrentLine.substring(sCurrentLine.indexOf('>')+1);
    			while ((sCurrentLine = br1.readLine()) != null) {
    				writer.write(previousLine+"\n");
	    			previousLine = sCurrentLine;
    			}
    			writer.write(previousLine);
    			writer.flush();
    			br1.close();
    			writer.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		} 
    		// delete the intermediate file
    		try {
    			Path path = Paths.get("src\\gatech_Ziyi\\NewQuickNote.xml");
    		    Files.delete(path);
    		} catch (NoSuchFileException x) {
    		    System.err.format("%s: no such" + " file or directory%n", OUTPUT_FILE);
    		} catch (DirectoryNotEmptyException x) {
    		    System.err.format("%s not empty%n", OUTPUT_FILE);
    		} catch (IOException x) {
    		    // File permission problems are caught here.
    		    System.err.println(x);
    		}
            System.out.println("DONE");
        }
        catch (TransformerConfigurationException e) {
            System.out.println("Transformer Configuration Exception: " + e.getMessage());
        }
        catch (TransformerException e) {
            System.out.println("Transformer Exception: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }           
        catch (SAXException se) {
            System.out.println("SAX exception: " + se.getMessage());
        }
    }
}
