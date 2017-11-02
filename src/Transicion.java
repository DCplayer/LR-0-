import java.util.ArrayList;

/**
 * Created by Diego Castaneda on 01/11/2017.
 */
public class Transicion {
    private ArrayList<ArrayList<String>> salida = new ArrayList<>();
    private ArrayList<ArrayList<String>> llegada = new ArrayList<>();
    private String transicion = "";

    public Transicion(ArrayList<ArrayList<String>> salida , ArrayList<ArrayList<String>> llegada, String transicion){
        this.transicion = transicion;
        this.salida = salida;
        this.llegada = llegada;
    }

}
