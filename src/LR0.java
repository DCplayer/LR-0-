import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Diego Castaneda on 01/11/2017.
 */
public class LR0 {

    private HashSet<ArrayList<String>> estructuraProducciones;
    private HashSet<HashSet<String>> parser = new HashSet<>();
    private HashSet<String> cabezas = new HashSet<>();
    private HashSet<ArrayList<String>> contenidoClosure = new HashSet<>();
    private HashSet<ArrayList<String>> produccionesVistas = new HashSet<>();

    public LR0(ArrayList<ArrayList<String>> estructura){
        for(ArrayList<String> produccion: estructura){
            produccion.remove(".");
            cabezas.add(produccion.get(0));
        }

        this.estructuraProducciones = new HashSet<>();
        ArrayList<String> inicial = new ArrayList<>();

        inicial.add("§");
        inicial.add("=");
        inicial.add(estructura.get(0).get(0));
        inicial.add("$");

        estructuraProducciones.add(inicial);
        estructuraProducciones.addAll(estructura);



    }
/*------------------------------Funcion Closure----------------------------------------------------------------------*/
    public HashSet<ArrayList<String>> Closure(ArrayList<String> produccion){
        HashSet<ArrayList<String>> resultante = new HashSet<>();
        int index = produccion.indexOf(".");
        if(index == produccion.size()-1){
            return resultante;
        }
        String item = produccion.get(index+1);
        if(cabezas.contains(item)){
           resultante =  reaccionEnCadena(item );

        }
        return resultante;
    }

    public ArrayList<String> agregarPunto(ArrayList<String> resultadoClosure){
        ArrayList<String> resultado = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            resultado.add(resultadoClosure.get(i));
        }

        resultado.add(".");
        for(int i = 2; i < resultadoClosure.size(); i++){
            resultado.add(resultadoClosure.get(i));
        }
        return resultado;
    }

    public HashSet<ArrayList<String>> produccionesPorCabeza(String cabeza){
        HashSet<ArrayList<String>> resultado = new HashSet<>();
        for(ArrayList<String> produccion: estructuraProducciones){
            if(produccion.get(0).equals(cabeza)){
                ArrayList<String> nuevaaProduccion = agregarPunto(produccion);
                resultado.add(produccion);
            }
        }
        return resultado;
    }

    public HashSet<ArrayList<String>> reaccionEnCadena(String cabeza){
        contenidoClosure.clear();
        HashSet<ArrayList<String>> productos = produccionesPorCabeza(cabeza);

        for(ArrayList<String> item: productos){
            int index = item.indexOf(".");
            if(cabezas.contains(item.get(index + 1)) && !produccionesVistas.contains(item)){
                produccionesVistas.add(item);
                contenidoClosure.add(item);
                contenidoClosure.addAll(reaccionEnCadena(item.get(0)));

            }
        }
        return contenidoClosure;
    }

/*--------------------------------------------------------------------------------------------------------------------*/

/*----------------------------Funcion GOTO----------------------------------------------------------------------------*/
    public 
/*--------------------------------------------------------------------------------------------------------------------*/
}
