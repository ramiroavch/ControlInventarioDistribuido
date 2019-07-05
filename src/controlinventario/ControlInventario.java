/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlinventario;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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
public class ControlInventario {

    /**
     * @param args the command line arguments
     */
    private File file;
    private String ruta="src//archivos//";
    
    public static void main(String[] args) {
        // TODO code application logic here 
        if(args[0].startsWith("server")) {
            try { 
                InventarioServer serer = new InventarioServer();
                serer.start(new Integer(args[1]), args[2]);
                
            } catch (IOException ex) {
                System.out.println("error server");
                //Logger.getLogger(Taller1sd.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            try {
                //participante
                
                ClientMessage message = new ClientMessage();
                message.startConnection(args[1], new Integer(args[2]));
                
                String response = message.sendMessage(args[3]);
                DAOInventario dao= new DAOInventario();
                if("ImprimirArticulosxTienda".equals(response)){
                    
                    List tiendas = dao.root.getChildren("tienda");
                    Iterator i = tiendas.iterator();
                    while (i.hasNext()) {
                        Element e = (Element) i.next();
                        List articulos = e.getChildren("articulo");
                        Iterator j = articulos.iterator();
                        if(!j.hasNext()){
                            System.out.print(e.getAttribute("codigo").getValue());
                            System.out.println();
                        }
                    while (j.hasNext()) {
                        Element f = (Element) j.next();
                        System.out.print(e.getAttribute("codigo").getValue()+"#"+f.getChild("codigo").getValue()+"#"+f.getChild("cantidad").getValue());
                        System.out.println();
                    }
                    System.out.println();
                    }
                }
                if("ImprimirArticulosEmpresa".equals(response)){
                    List tiendas = dao.sumarArray();
                    Iterator i = tiendas.iterator();
                    while (i.hasNext()) {
                        String val=(String)i.next();
                        String valores[]=val.split("#");
                        System.out.println(valores[0]+"#"+valores[1]);    
                    }   
                }
                if("ImprimirCompras".equals(response)){
                
                }
                
                System.out.println("Respuesta server: " + response);
                
                
            } catch (IOException ex) {
                System.out.println("error client message");
                Logger.getLogger(ControlInventario.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        
        }
        
        
    }
    
}
