/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlinventario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author RAM
 */
public class DaoCompras {
    public Element root;
     private String fileLocation = "src//archivos//Compras.xml";

    public DaoCompras() {
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = null;
            doc = builder.build(fileLocation);
            root = doc.getRootElement();
        } catch (JDOMException ex) {
            System.out.println("No se pudo iniciar la operacion por: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("No se pudo iniciar la operacion por: " + ex.getMessage());
        }
    }
    
    private Element ClientetoXmlElement(String codigo,String nombre) {
        Element cliente = new Element("cliente");
        cliente.setAttribute("codigo",codigo);
        cliente.setAttribute("nombre",nombre);
        return cliente;
    }
    
    private Element ArticulotoXmlElement(String codigo, int cantidad,String codigoTienda) {
        Element articulo = new Element("articulo");
        Element key2 = new Element("codigo");
        key2.setText(codigo);
        Element key = new Element("tienda");
        key.setText(codigoTienda);  
        Element qty = new Element("cantidad_compra");
        qty.setText(String.valueOf(cantidad));
        articulo.addContent(key2);
        articulo.addContent(key);
        articulo.addContent(qty);
        return articulo;
    }
    private boolean updateDocument() {
        try {
            XMLOutputter out = new XMLOutputter(org.jdom.output.Format.getPrettyFormat());
            FileOutputStream file = new FileOutputStream(fileLocation);
            out.output(root, file);
            file.flush();
            file.close();
            return true;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean agregarCliente(String clientecod,String clientenombre) {
        boolean resultado;
        root.addContent(ClientetoXmlElement(clientecod,clientenombre));
        resultado = updateDocument();
        return resultado;
    }
    public boolean agregarCompra(String codigoCliente,String codigoArticulo,int cantidad,String tienda) {
        List clientes = this.root.getChildren("cliente");
        Iterator i = clientes.iterator();
        boolean resultado ;
            System.out.println("Consegui la tienda");
            while (i.hasNext()) {
                Element e = (Element) i.next();
                if (codigoCliente.equals(e.getAttribute("codigo").getValue())) {
                    //List articulos = e.getChildren("articulo");
                    e.addContent(ArticulotoXmlElement(codigoArticulo, cantidad,tienda));      
                }
            }
        resultado = updateDocument();
        return resultado;
    }
    public boolean buscarCliente(String codigo) {
        List raiz = this.root.getChildren("cliente");
        Iterator i = raiz.iterator();
        try{
            while (i.hasNext()) {
                Element e = (Element) i.next();
                if (codigo.equals(e.getAttribute("codigo").getValue())) {
                    return true;
                } 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean buscarCompra(String codigoCliente,String codigoArticulo,String tienda){
        List tiendas = this.root.getChildren("cliente");
        Iterator i = tiendas.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
                if (codigoCliente.equals(e.getAttribute("codigo").getValue())&& (tienda.equals(e.getChild("tienda")))){
                List articulos = e.getChildren("articulo");
                return buscarporCliente(articulos,codigoArticulo,tienda);
            }
        }
        return false;
    }
    
    public static boolean buscarporCliente(List raiz, String codigo,String tienda) {
        Iterator i = raiz.iterator();
        try{
            while (i.hasNext()) {
                Element e = (Element) i.next();
                if (codigo.equals(e.getChild("codigo").getValue())&& (tienda.equals(e.getChild("tienda")))) {
                    return true;
                }         
            }     
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean actualizarCompra(String codigoCliente,String codigoArticulo,int cantidad,String tienda) {
        List tiendas = this.root.getChildren("cliente");
        boolean resultado = false;
        Iterator i = tiendas.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            if (codigoCliente.equals(e.getAttribute("codigo").getValue())) {
                List articulos = e.getChildren("articulo");
                Iterator j=articulos.iterator();
                while (j.hasNext()) {
                    Element f = (Element) j.next();
                    if (codigoArticulo.equals(f.getChild("codigo").getValue())&&(tienda.equals(f.getChild("tienda")))) {
                        int nueva_cantidad=Integer.parseInt(f.getChild("cantidad").getValue())+cantidad;
                        f.getChild("cantidad").setText(String.valueOf(nueva_cantidad));
                    }
                }
                
            }
        }
        resultado = updateDocument();
        return resultado;
    }
    public List sumarCompras(){
        List clientes = this.root.getChildren("cliente");
        Iterator i = clientes.iterator();
        List compras=null;
        while (i.hasNext()) {
            Element e = (Element) i.next();
            List articulos = e.getChildren("articulo");
            Iterator j = articulos.iterator();
            
            while (j.hasNext()) {
                int z=0;
                Element f = (Element) j.next();
                if(compras!=null){
                        String value=(String) compras.get(z);
                        String valores[]=value.split("#");
                        String client[]=valores[0].split("-");
                        
                        if(valores[1].equals(f.getChild("codigo").getValue())&&(client[0].equals(e.getAttribute("codigo").getValue()))){
                            compras.set(z,e.getAttribute("codigo").getValue()+"-"
                                    +e.getAttribute("nombre").getValue()+"#"
                                    +f.getChild("codigo").getValue()+"#"
                                    +String.valueOf(Integer.parseInt(valores[2])+Integer.parseInt(f.getChild("cantidad_compra").getValue())));
                        
                        }else{
                            compras.add(e.getAttribute("codigo").getValue()+"-"+e.getAttribute("nombre").getValue()+"#"+f.getChild("codigo").getValue()+"#"+f.getChild("cantidad_compra").getValue());
                        }
                    }
                else{
                    compras=new ArrayList();
                    compras.add(e.getAttribute("codigo").getValue()+"-"+e.getAttribute("nombre").getValue()+"#"+f.getChild("codigo").getValue()+"#"+f.getChild("cantidad_compra").getValue());
                }
                
            }     
        }
        return(compras);
    }
    
     public List sumarCompras2(){
        List z=sumarCompras();
        List aux=sumarCompras();
        List listf=new ArrayList();
        int i=0;
        int j=0;
        int suma=0;
        while(i<z.size()){
            String valor = z.get(i).toString();
            String[] valores = valor.split("#");
            suma = Integer.parseInt(valores[2]);
            String client[]=valores[0].split("-");
            j = i+1;
            while(j < aux.size()){
                String valor2 = aux.get(j).toString();
                String[] valores2 = valor2.split("#");  
                String client2[]=valores2[0].split("-");
                
                if( valores[1].equals(valores2[1])&& (client[0].equals(client2[0]))){
                    suma=suma+Integer.parseInt(valores2[2]);
                    z.remove(j);
                    aux.remove(j);
                }
                else{
                j++;
                }
            }
            listf.add(valores[0]+"#"+valores[1]+"#"+String.valueOf(suma));
            i++;
        }
        return listf;
    }
}
