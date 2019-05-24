/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlinventario;

import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author RAM
 */
public class JsonController {
    JSONObject json;
    
    public void iniciarJson(String ruta){
        String resourceName = ruta+"articulos.json";
        InputStream is = JsonController.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        json=object;
    }
    
    public void agregarElemento(String articulo,int cantidad){
        
      JsonArray array = obj.getJSONArray("interests");
        for(int i = 0 ; i < array.length() ; i++){
            list.add(array.getJSONObject(i).getString("interestKey"));
}   
    }
}
