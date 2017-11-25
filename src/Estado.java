import javax.net.ssl.SSLEngineResult;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Diego Castaneda on 04/11/2017.
 */
public class Estado {
    private int numero;
    private HashSet<ArrayList<String>> contenido;
    private boolean revisado;

    public Estado(int numero, HashSet<ArrayList<String>> contenido){
        this.contenido = contenido;
        this.numero = numero;
        revisado = false;

    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }

    public HashSet<ArrayList<String>> getContenido() {
        return contenido;
    }

    public void setContenido(HashSet<ArrayList<String>> contenido) {
        this.contenido = contenido;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString(){
        return "Estado # " + this.numero+ ", Contenido: " + this.contenido ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estado estado = (Estado) o;

        return contenido != null ? contenido.equals(estado.contenido) : estado.contenido == null;

    }

    @Override
    public int hashCode() {
        return contenido != null ? contenido.hashCode() : 0;
    }
}
