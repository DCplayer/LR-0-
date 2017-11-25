import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Diego Castaneda on 01/11/2017.
 */
public class Transicion {
    private HashSet<ArrayList<String>> salida = new HashSet<>();
    private HashSet<ArrayList<String>> llegada = new HashSet<>();
    private String transicion = "";

    public Transicion(HashSet<ArrayList<String>> salida , HashSet<ArrayList<String>> llegada, String transicion){
        this.transicion = transicion;
        this.salida = salida;
        this.llegada = llegada;

    }

    @Override
    public String toString(){
        return "------------------------------------------\nOrigen: " +this.salida + "\nTransicion: " + this.transicion + "\nDestino: " + this.llegada + "\n------------------------------------------" ;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transicion that = (Transicion) o;

        if (salida != null ? !salida.equals(that.salida) : that.salida != null) return false;
        if (llegada != null ? !llegada.equals(that.llegada) : that.llegada != null) return false;
        return transicion != null ? transicion.equals(that.transicion) : that.transicion == null;

    }

    @Override
    public int hashCode() {
        int result = salida != null ? salida.hashCode() : 0;
        result = 31 * result + (llegada != null ? llegada.hashCode() : 0);
        result = 31 * result + (transicion != null ? transicion.hashCode() : 0);
        return result;
    }
}
