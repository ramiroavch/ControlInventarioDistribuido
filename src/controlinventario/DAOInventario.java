/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlinventario;

import java.io.FileOutputStream;
import java.io.IOException;
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

public class DAOInventario {
     private Element root;
     private String fileLocation = "src//archivos//ListaLocal.xml";

    public DAOInventario() {
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

    private Element TiendatoXmlElement(String codigo) {
        Element tienda = new Element("tienda");
        tienda.setAttribute("codigo",codigo);
        return tienda;
    }
    
    private Element ArticulotoXmlElement(String codigo, int cantidad) {
        Element articulo = new Element("articulo");
        Element key = new Element("codigo");
        key.setText(codigo);      
        Element qty = new Element("cantidad");
        qty.setText(String.valueOf(cantidad));
        articulo.addContent(key);
        articulo.addContent(qty);
        return articulo;
    }

   /*Método que retorna un Estudiante. A este metodo se le manda un Element y con
    sus datos se hará los pasos requeridos para crear el Estudiante*/
    /*private Estudiante EstudianteToObject(Element element) throws ParseException {
        Estudiante nEstudiante = new Estudiante(Integer.parseInt(element.getChildText("cedula")),element.getChildText("nombreyapellido"),
                                        Float.parseFloat(element.getChildText("eficiencia")));
        return nEstudiante;
    }*/

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


    public static boolean buscar(List raiz, String codigo) {
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
    public static boolean buscarporArticulo(List raiz, String codigo) {
        Iterator i = raiz.iterator();
        try{
            while (i.hasNext()) {
                Element e = (Element) i.next();
                if (codigo.equals(e.getChild("codigo").getValue())) {
                    return true;
                }         
            }     
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean agregarTienda(String tiendacod) {
        boolean resultado = false;
        root.addContent(TiendatoXmlElement(tiendacod));
        resultado = updateDocument();
        return resultado;
    }
    
    public boolean agregarArticulo(String codigo, int cantidad, String tiendacod) {
        System.out.println("entre agregar articulo");
        List tiendas = this.root.getChildren("tienda");
        Iterator i = tiendas.iterator();
        boolean resultado = false;
            System.out.println("Consegui la tienda");
            while (i.hasNext()) {
                Element e = (Element) i.next();
                if (tiendacod.equals(e.getAttribute("codigo").getValue())) {
                    List articulos = e.getChildren("articulo");
                    e.addContent(ArticulotoXmlElement(codigo, cantidad));
                    resultado = updateDocument();
                    return resultado;
                }
            }
        return false;
    }
    
    public boolean buscarTienda(String codigo) {
        List tiendas = this.root.getChildren("tienda");
        return buscar(tiendas, codigo);
    }

    
    public boolean buscarArticulo(String tiendacod ,String codigo) {
        
        List tiendas = this.root.getChildren("tienda");
        Iterator i = tiendas.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
                if (tiendacod.equals(e.getAttribute("codigo").getValue())) {
                List articulos = e.getChildren("articulo");
                return buscarporArticulo(articulos,codigo);
            }
        }
        return false;
    }

    /* @param Estudiante objeto Estudiante a actualizar
     * @return valor booleano con la condicion de exito */
    public boolean actualizarArticulo(String tiendacod,String codigo,int cantidad) {
        List tiendas = this.root.getChildren("tienda");
        boolean resultado = false;
        Iterator i = tiendas.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            if (tiendacod.equals(e.getAttribute("codigo").getValue())) {
                List articulos = e.getChildren("articulo");
                Iterator j=articulos.iterator();
                while (j.hasNext()) {
                    Element f = (Element) j.next();
                    if (codigo.equals(f.getChild("codigo").getValue())) {
                        int nueva_cantidad=Integer.parseInt(f.getChild("cantidad").getValue())+cantidad;
                        f.getChild("cantidad").setText(String.valueOf(nueva_cantidad));
                    }
                }
                
            }
        }
        resultado = updateDocument();
        return resultado;
    }
   
    /* @param cedula cedula del Estudiante a borrar
     * @return valor boleano con la condicion de exito  */
   /* public boolean borrarPersona(Integer cedula) {
        boolean resultado = false;
        Element aux = new Element("Estudiante");
        List Estudiantes = this.root.getChildren("Estudiante");
        while (aux != null) {
            aux = DaoEstudianteXml.buscar(Estudiantes, Integer.toString(cedula));
            if (aux != null) {
                Estudiantes.remove(aux);
                resultado = updateDocument();
            }
        }
        return resultado;
    }*/


    /* Para obtener todos las Personas registradas
     * @return ArrayList con todos los objetos Estudiante  */
      
 /*   public ArrayList<Estudiante> todosLosEstudiantes() {
        ArrayList<Estudiante> resultado = new ArrayList<Estudiante>();
        for (Object it : root.getChildren()) {
            Element xmlElem = (Element) it;
            try {
                resultado.add(EstudianteToObject(xmlElem));
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }
*/
}
