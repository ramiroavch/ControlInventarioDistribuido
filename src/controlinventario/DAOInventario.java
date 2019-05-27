/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlinventario;

import java.io.BufferedWriter;
import java.io.File;
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

public class DAOInventario {
     private Element root;
     private String fileLocation = "src//archivos//ListaLocal.xml";
     private File file;
     private String ruta="src//archivos//";

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

        
    public void imprimirArticulosxTienda() throws IOException{
        file= new File(ruta+"ListaProductosPorTienda.txt");
        BufferedWriter bw;
        bw=new BufferedWriter(new FileWriter(file));
        List tiendas = this.root.getChildren("tienda");
        Iterator i = tiendas.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            bw.write("Tienda: "+e.getAttribute("codigo").getValue());
            bw.newLine();
            List articulos = e.getChildren("articulo");
            Iterator j = articulos.iterator();
            while (j.hasNext()) {
                Element f = (Element) j.next();
                bw.write("Articulo: "+f.getChild("codigo").getValue()+" ,Cantidad: "+f.getChild("cantidad").getValue());
                bw.newLine();
            }     
        }
        bw.close();
    }
    
    public List get_articulos(){
        List tiendas = this.root.getChildren("tienda");
        Iterator i = tiendas.iterator();
        List art=null;
        while (i.hasNext()) {
            Element e = (Element) i.next();
            List articulos = e.getChildren("articulo");
            Iterator j = articulos.iterator();
            int z=0;
            while (j.hasNext()) {
                Element f = (Element) j.next();
                if(art!=null){
                        String value=(String) art.get(z);
                        String valores[]=value.split("#");
                        if(valores[0]==f.getChild("codigo").getValue()){
                        art.set(z,f.getChild("codigo").getValue()+"#"+String.valueOf(Integer.parseInt(valores[1])+Integer.parseInt(f.getChild("cantidad").getValue())));
                        }else{
                            art.add(f.getChild("codigo").getValue()+"#"+f.getChild("cantidad").getValue());
                        }
                }
                else{
                    art=new ArrayList();
                    art.add(f.getChild("codigo").getValue()+"#"+f.getChild("cantidad").getValue());
                }
                
            }     
        }
        return(art);
    }
    public List sumarArray(){
        List z=get_articulos();
        List aux=get_articulos();
        List listf=new ArrayList();
        int i=0;
        int j=0;
        int suma=0;
        System.out.println(z);
        while(i<z.size()){
            String valor = z.get(i).toString();
            String[] valores = valor.split("#");
            suma = Integer.parseInt(valores[1]);
            j = i+1;
            while(j < aux.size()){
                String valor2 = aux.get(j).toString();
                String[] valores2 = valor2.split("#");           
                if( valores[0].equals(valores2[0]) ){
                    suma=suma+Integer.parseInt(valores2[1]);
                    z.remove(j);
                    aux.remove(j);
                }
                else{
                j++;
                }
            }
            listf.add(valores[0]+"#"+String.valueOf(suma));
            i++;
        }
        return listf;
    }
    public void imprimirArticulosEmpresa() throws IOException{
        file= new File(ruta+"ListaProductosEmpresa.txt");
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(file));
        List tiendas = sumarArray();
        Iterator i = tiendas.iterator();
        while (i.hasNext()) {
            String val=(String)i.next();
            String valores[]=val.split("#");
            bw.write("Articulo: "+valores[0]+", Cantidad: "+valores[1]);
            bw.newLine();    
        }
        bw.close();
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
