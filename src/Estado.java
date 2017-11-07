import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Diego Castaneda on 04/11/2017.
 */
public class Estado {
    private int numero;
    private ArrayList<ArrayList<String>> contenido;
    private boolean revisado;

    public Estado(int numero, ArrayList<ArrayList<String>> contenido){
        this.contenido = contenido;
        this.numero = numero;
        revisado = false;

    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }

    public ArrayList<ArrayList<String>> getContenido() {
        return contenido;
    }

    public void setContenido(ArrayList<ArrayList<String>> contenido) {
        this.contenido = contenido;
    }

    public boolean isRevisado() {
        return revisado;
    }


}
