import com.sun.imageio.stream.CloseableDisposerRecord;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Diego Castaneda on 01/11/2017.
 */
public class LR0 {

    private ArrayList<ArrayList<String>> estructuraProducciones;
    private ArrayList<ArrayList<String>> parser = new ArrayList<>();
    private ArrayList<String> cabezas = new ArrayList<>();
    private HashSet<ArrayList<String>> contenidoClosure = new HashSet<>();
    private HashSet<ArrayList<String>> produccionesVistas = new HashSet<>();
    private ArrayList<ArrayList<String>> temporal = new ArrayList<>();


    public LR0(ArrayList<ArrayList<String>> estructura){
        for(ArrayList<String> produccion: estructura){
            produccion.remove(".");
            cabezas.add(produccion.get(0));
        }
        this.estructuraProducciones = new ArrayList<>();
        ArrayList<String> inicial = new ArrayList<>();
        inicial.add("§");
        inicial.add("=");
        inicial.add(estructura.get(0).get(0));
        inicial.add("$");

        estructuraProducciones.add(inicial);
        estructuraProducciones.addAll(estructura);

    }

    public  void initialize(){

        for(ArrayList<String> arreglo: estructuraProducciones){
            System.out.println("Closure: ");
            System.out.println(arreglo);
            System.out.println(Closure(arreglo));
        }
    }

/*------------------------------Funcion Closure----------------------------------------------------------------------*/
    public ArrayList<ArrayList<String>> Closure(ArrayList<String> produccion){
        ArrayList<ArrayList<String>> resultante = new ArrayList<>();
        int index = produccion.indexOf(".");
        if(index == produccion.size()-1){
            return resultante;
        }
        String item = produccion.get(index+1);
        if(cabezas.contains(item)){
            produccionesVistas.clear();
            contenidoClosure.clear();
            temporal.clear();
            resultante =  reaccionEnCadena(item);

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

    public ArrayList<ArrayList<String>> produccionesPorCabeza(String cabeza){
        ArrayList<ArrayList<String>> resultado = new ArrayList<>();
        for(ArrayList<String> produccion: estructuraProducciones){
            if(produccion.get(0).equals(cabeza)){
                ArrayList<String> nuevaProduccion = agregarPunto(produccion);
                resultado.add(nuevaProduccion);
            }

        }
        return resultado;
    }

    public ArrayList<ArrayList<String>> reaccionEnCadena(String cabeza){
        ArrayList<ArrayList<String>> productos = produccionesPorCabeza(cabeza);

        if(!produccionesVistas.isEmpty()){
            contenidoClosure.addAll(productos);
        }

        for(ArrayList<String> item: productos){
            int index = item.indexOf(".");
            if(cabezas.contains(item.get(index + 1)) && !produccionesVistas.contains(item)){
                if(contenidoClosure.isEmpty()){
                    temporal.add(item);
                }
                produccionesVistas.add(item);
                contenidoClosure.add(item);
                contenidoClosure.addAll(reaccionEnCadena(item.get(index +1)));

            }
        }
        for(ArrayList<String> i: temporal){
            contenidoClosure.remove(i);
        }
        temporal.addAll(contenidoClosure);
        return temporal;
    }

/*--------------------------------------------------------------------------------------------------------------------*/

/*----------------------------Funcion GOTO----------------------------------------------------------------------------*/
    public void  GOTO(ArrayList<ArrayList<String>> nodo, String transicion){

    }
/*--------------------------------------------------------------------------------------------------------------------*/
}
