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
    File archivo;
    DocumentBuilderFactory dbf;
    Document document = null;
    DocumentBuilder documentBuilder=null;
    DOMImplementation implementation;
    public boolean buscarArchivo(){
        try{
            archivo = new File("ListaLocal.xml");
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
                System.out.println(nodo.getNodeType());
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    String codigo_element = element.getAttribute("codigo");
                    System.out.println(codigo_element);
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
                Element element = (Element) nodo;
                String codigo_element = element.getAttribute("codigo");
                try{
                if(codigo_element.equals(codigo)){
                    NodeList cantidades;
                    cantidades= element.getElementsByTagName("cantidad");
                    Element cantidad_element= (Element) cantidades.item(0);
                    System.out.println(cantidades.item(0));
                    int cantidad_nueva=Integer.parseInt(cantidad_element.getNodeValue());
                    cantidad_nueva+=cantidad;
                    cantidad_element.setNodeValue(String.valueOf(cantidad_nueva));
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
        dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();
        implementation= documentBuilder.getDOMImplementation();
        document=implementation.createDocument(null,"ListaLocal",null);
        document.setXmlVersion("1.0");
        Element rootElement;
        rootElement = document.getDocumentElement();
        Element articulo = document.createElement("articulo");
        rootElement.appendChild(articulo);
        Attr attr;
        attr = document.createAttribute("codigo");
        attr.setValue(codigo);
        articulo.setAttributeNode(attr);
        Element nombre = document.createElement("cantidad");
        nombre.appendChild(document.createTextNode(String.valueOf(cantidad)));
        articulo.appendChild(nombre);
	Source source = new DOMSource(document);
	StreamResult result;
        result = new StreamResult(new File("ListaLocal.xml"));
        Transformer transformer=TransformerFactory.newInstance().newTransformer();
        transformer.transform(source,result);
        }catch (Exception pce) {
            pce.printStackTrace();
        }
        return(true);
    }
}
