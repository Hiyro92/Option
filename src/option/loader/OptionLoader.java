/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package option.loader;



import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;





public class OptionLoader {

    private static final Logger LOG = Logger.getLogger(OptionLoader.class.getName());
    private static final String FILENAME = "config.xml";
    
    private List<ConnectionPara> connectionPara;

    private static OptionLoader option;
    

    private  OptionLoader() {
        connectionPara = new LinkedList<>();
        loadProperty();
    }

    public void saveProperty() {
        Element root = new Element("config");
        root.getChildren().add(new Element("connectionPara"));
        Element conElement = root.getChild("connectionPara");
        
        for(ConnectionPara para : connectionPara){
            Element e = new Element("Verbindung");
            e.setAttribute("name", para.name);
            e.setAttribute("ip-adresse", para.ip);
            e.setAttribute("rack", String.valueOf(para.rack));
            e.setAttribute("sockel", String.valueOf(para.sockel));
            conElement.getChildren().add(e);
        }     
        
        Document doc = new Document(root);
        XMLOutputter out = new XMLOutputter();
        try {
            Writer w = new FileWriter(FILENAME);
            out.output(doc,w);
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(OptionLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadProperty(){
        Document doc = null;
;        try {
            doc = new SAXBuilder().build(FILENAME);
        } catch (JDOMException ex) {
            Logger.getLogger(OptionLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OptionLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Element root = doc.getRootElement();
        Element conElem = root.getChild("connectionPara");
        for(Element e : conElem.getChildren()){
            ConnectionPara para = new ConnectionPara();
            
            para.name   = (e.getAttributeValue("name"));
            para.ip     = (e.getAttributeValue("ip-adresse"));
            para.rack   = (Integer.parseInt(e.getAttributeValue("rack")));
            para.sockel = (Integer.parseInt(e.getAttributeValue("sockel")));
            
            connectionPara.add(para);
        }
    }

    public static OptionLoader getInstance(){
        if(option == null) option = new OptionLoader();
        return option;
    }
    
    public List<ConnectionPara> getConnectionList(){
        return connectionPara;
    }
    
    public void setConnectionList(List<ConnectionPara> list){
        connectionPara = list;
    }
    

}
