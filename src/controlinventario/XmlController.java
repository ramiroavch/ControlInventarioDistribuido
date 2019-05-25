/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlinventario;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author RAM
 */
public class XmlController {
    File archivo = new File ("ListaLocal.xml");
    DocumentBuilderFactory dbf;
    Document document = null;
    DocumentBuilder documentBuilder = null;
    DOMImplementation implementation;
    
    public boolean buscarArchivo(){
        try{
            //archivo = new File("ListaLocal.xml");
            if (archivo.exists()) {
                dbf = DocumentBuilderFactory.newInstance();
                documentBuilder = dbf.newDocumentBuilder();
                document = documentBuilder.parse(archivo);
                return(true);
            }
        }catch (org.xml.sax.SAXException ex) {
        
        }catch (ParserConfigurationException ex) {
            
        } catch (IOException ex) {
            
        }
        return(false);
    }
    
    public boolean buscarArticulo(String codigo){
        if(buscarArchivo()==true){
            NodeList listaArticulos = document.getElementsByTagName("articulo");
            int i=0;
            while(i<listaArticulos.getLength()){
                Node nodo = listaArticulos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    String codigo_element = element.getAttribute("codigo");
                    if(codigo_element.equals(codigo)){
                        return(true);   
                    }
                }
                i++;
            }
        }
        return(false);
    }
    public boolean aumentarCantidad(String codigo,int cantidad){
        NodeList listaArticulos = document.getElementsByTagName("articulo");
        int i=0;
        while(i<listaArticulos.getLength()){
            Node nodo = listaArticulos.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                String codigo_element = nodo.getAttributes().item(0).getNodeValue();
                System.out.println(codigo_element);
                try{
                if(codigo_element.equals(codigo)){
                    Node nodo_cantidad = nodo.getChildNodes().item(0);
                    System.out.println(nodo_cantidad.getTextContent().trim());
                    int cantidad_nueva=Integer.parseInt(nodo_cantidad.getTextContent().trim());
                    cantidad_nueva+=cantidad;
                    System.out.println(cantidad_nueva);
                    nodo_cantidad.setNodeValue(String.valueOf(cantidad_nueva));
                    return(true);
                }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            i++;
        }
        return(false);
    }
    public boolean agregarArticulo(String codigo,int cantidad){
        try{
            System.out.println("Agrego nuevo");
               
        dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();
        document = documentBuilder.parse(archivo);
        
        //implementation= documentBuilder.getDOMImplementation();
        //document=implementation.createDocument(null,"ListaLocal",null);
        //document.setXmlVersion("1.0");
        
        
       // Element rootElement;
       // rootElement = 
       document.getDocumentElement().normalize();
        
        Element articulo = document.createElement("articulo");
        Attr attr = document.createAttribute("codigo");
        attr.setValue("pupu"); 
        //articulo.appendChild(articulo)
        //rootElement.appendChild(articulo);
        //Attr attr;
        

        
        
        Element nombre = document.createElement("cantidad");
        
	
        NodeList producto = document.getDocumentElement().getElementsByTagName("ListaLocal");
        producto.item(0).appendChild(articulo);
        
        nombre.appendChild(document.createTextNode(String.valueOf(cantidad)));
        articulo.setAttributeNode(attr);
        articulo.appendChild(nombre);
        /*Source source = new DOMSource(document);
	StreamResult result;
        result = new StreamResult(new File("ListaLocal.xml"));
        Transformer transformer=TransformerFactory.newInstance().newTransformer();
        transformer.transform(source,result);*/
        }catch (Exception pce) {
            pce.printStackTrace();
        }
        return(true);
    }
}
