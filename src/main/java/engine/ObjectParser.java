package engine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;


/**
 * Created by Szymon Piechaczek on 19.12.2016.
 */
public class ObjectParser {
    DocumentBuilderFactory fac;
    DocumentBuilder builder;
    Document doc;
    Element root;
    private ObjectParser() throws EngineResourceException{
        try {
            fac = DocumentBuilderFactory.newInstance();
            builder = fac.newDocumentBuilder();
        }
        catch (ParserConfigurationException |ArrayIndexOutOfBoundsException ex){
            throw (EngineResourceException)
                    (new EngineResourceException("Could not initialize ObjectParser")).initCause(ex);
        }

    }
    private static ObjectParser instance;
    public static ObjectParser getInstance() throws EngineResourceException{
        if (instance == null)
            instance = new ObjectParser();
        return instance;
    }

    public void openResource(String filename) throws EngineResourceException{
        try {
            doc = builder.parse(new InputSource(new StringReader(Utils.loadResource(filename))));
            root = doc.getDocumentElement();
        }
        catch (IOException|SAXException e) {
            throw (EngineResourceException)(new EngineResourceException("Could not open resource: " + filename).initCause(e));
        }

    }
    public float getFloatValue(String name) throws EngineResourceException{
        try {
            return Float.parseFloat(root.getElementsByTagName(name).item(0).getTextContent());
        }
        catch (NumberFormatException|ArrayIndexOutOfBoundsException|NullPointerException e) {
            throw (EngineResourceException)(new EngineResourceException("Could not parse resource with param: " + name).initCause(e));
        }
    }
    public int getIntValue(String name) throws EngineResourceException{
        try {
            return Integer.parseInt(root.getElementsByTagName(name).item(0).getTextContent());
        }
        catch (NumberFormatException|ArrayIndexOutOfBoundsException|NullPointerException e) {
            throw (EngineResourceException)(new EngineResourceException("Could not parse resource with param: " + name).initCause(e));
        }
    }
    public String getStringValue(String name) throws EngineResourceException{
        try {
            return root.getElementsByTagName(name).item(0).getTextContent();
        }
        catch (ArrayIndexOutOfBoundsException|NullPointerException e) {
            throw (EngineResourceException)(new EngineResourceException("Could not parse resource with param: " + name).initCause(e));
        }
    }
}
