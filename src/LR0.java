import com.sun.imageio.stream.CloseableDisposerRecord;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    private HashSet<ArrayList<String>> T = new HashSet<>();
    private HashSet<Transicion> E = new HashSet<>();

    private int numeroEstado = 0;
    private HashSet<String> alfabeto = new HashSet<>();
    private HashSet<Estado> estados = new HashSet<>();
    private HashSet<Transicion> transiciones = new HashSet<>();

    private ArrayList<ArrayList<String>> contenidoTemporalEstado = new ArrayList<>();

    private ArrayList<String> peticiones = new ArrayList<>();
    private int numeroDePeticion = 0;
    private boolean tieneHashtag = false;


    public LR0(ArrayList<ArrayList<String>> estructura){
        for(ArrayList<String> produccion: estructura){
            produccion.remove(".");
            cabezas.add(produccion.get(0));
        }
        this.estructuraProducciones = new ArrayList<>();
        ArrayList<String> inicial = new ArrayList<>();
        inicial.add("§");
        inicial.add("=");
        inicial.add(".");
        inicial.add(estructura.get(0).get(0));
        inicial.add("$");

        estructuraProducciones.add(inicial);
        estructuraProducciones.addAll(estructura);

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
            if(!resultante.contains(produccion)){
                ArrayList<ArrayList<String>> sustitucion = new ArrayList<>();

                if(produccion.indexOf(".") == -1 ){
                    ArrayList<String> prueba = agregarPunto(produccion);
                    if(!resultante.contains(prueba)){
                        sustitucion.add(prueba);
                    }

                }
                else{
                    sustitucion.add(produccion);
                }

                sustitucion.addAll(resultante);
                resultante.clear();
                resultante.addAll(sustitucion);
            }

        }else{
            resultante.add(produccion);
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
        temporal.addAll(productos);
        for(ArrayList<String> i: temporal){
            contenidoClosure.remove(i);
        }
        temporal.addAll(contenidoClosure);
        return temporal;
    }

    public  void initialize(){
        System.out.println("-------------------------------------------------");
        for(ArrayList<String> arreglo: estructuraProducciones){
            System.out.println("Produccion");
            System.out.println(arreglo);
            System.out.println();

            System.out.println("Closure: ");
            ArrayList<ArrayList<String>> nodazo = Closure(arreglo);
            HashSet<ArrayList<String>> nodazazo = new HashSet<>();
            nodazazo.addAll(nodazo);
            System.out.println(nodazo);
            System.out.println();

            System.out.println("GOTO E");
            System.out.println(GOTO(nodazazo, "E"));
            System.out.println();
            System.out.println("-------------------------------------------------");

        }


    }

/*----------------------------Termina Funcion Closure-----------------------------------------------------------------*/

/*----------------------------Funcion GOTO----------------------------------------------------------------------------*/
    public ArrayList<ArrayList<String>> GOTO(HashSet<ArrayList<String>> nodo, String transicion){
        ArrayList<ArrayList<String>> resultado = new ArrayList<>();
        for(ArrayList<String> i: nodo){
            int index = i.indexOf(".");
            if(index < i.size()-1){
                if(i.get(index+1).equals(transicion)){
                    resultado.add(moverPunto(i));
                }
            }


        }
        return resultado;

    }

    public ArrayList<String> moverPunto(ArrayList<String> Nodo){
        ArrayList<String> resultado = new ArrayList<>();
        int index = Nodo.indexOf(".");
        for(int i = 0; i < index; i++){
            resultado.add(Nodo.get(i));
        }
        if(index +1 < Nodo.size()){
            resultado.add(Nodo.get(index+1));
            resultado.add(Nodo.get(index));

            for(int i = index + 2; i < Nodo.size(); i++){
                resultado.add(Nodo.get(i));
            }
        }else{
            resultado.add(".");

        }
        return resultado;
    }
/*-----------------------------Terminado Funcion GOTO-----------------------------------------------------------------*/

/*-----------------------------Creando el Parser LR(0)-----------------------------------------------------------------*/
    public void crearElParser(){
        //Crear el Abecedario
        for(ArrayList<String> produccion: estructuraProducciones){
            for(String s: produccion){
                if(!s.equals(".") && !s.equals("=")){
                    alfabeto.add(s);
                }

            }
        }
        for(int i = 0; i< estructuraProducciones.size(); i++){
            if(estructuraProducciones.get(i).contains("$")){
                if(!estructuraProducciones.get(i).get(0).equals("§")){
                    ArrayList<String> remplazo = new ArrayList<>();
                    for(String s: estructuraProducciones.get(i)){
                        if(!s.equals("$")){
                            remplazo.add(s);
                        }
                    }
                    estructuraProducciones.set(i, remplazo);
                }
            }
        }


        System.out.println("Alfabeto: ");
        System.out.println(alfabeto);
        System.out.println("Producciones Iniciales: ");
        System.out.println(estructuraProducciones);

        //Creando el primer Estado;
        ArrayList<ArrayList<String>> elementos = Closure(estructuraProducciones.get(0));
        HashSet<ArrayList<String>> inicial = new HashSet<>();
        inicial.addAll(elementos);
        Estado primerEstado = new Estado(numeroEstado, inicial);
        numeroEstado++;

        //Comenzar con los parametros que nos diran hasta cuando parar las iteraciones
        int tamanoInicialEstados = estados.size();
        int tamanoInicialTransiciones = transiciones.size();

        for(ArrayList<String> producciones: primerEstado.getContenido()){
            if(producciones.get(producciones.size()-1).equals("$") && !producciones.get(0).equals("§")){
                producciones.remove("$");
            }
        }
        estados.add(primerEstado);

        int tamanoFinalEstados = estados.size();
        int tamanoFinalTransiciones = transiciones.size();

        System.out.println("Estado1");
        System.out.println(primerEstado.getContenido());



        while (tamanoFinalEstados != tamanoInicialEstados || tamanoInicialTransiciones != tamanoFinalTransiciones){
            tamanoInicialEstados = tamanoFinalEstados;
            tamanoInicialTransiciones = tamanoFinalTransiciones;
            HashSet<Estado> temporalEstados = new HashSet<>();
            HashSet<Transicion> temporalTransicion = new HashSet<>();
            for(Estado state: estados){
                if(!state.isRevisado()){
                    state.setRevisado(true);
                    for(String letra: alfabeto){
                        contenidoTemporalEstado.clear();
                        contenidoTemporalEstado.addAll(state.getContenido());
                        HashSet<ArrayList<String>> comportamientoEstados = new HashSet<>();
                        comportamientoEstados.addAll(contenidoTemporalEstado);

                        ArrayList<ArrayList<String>> nodo = GotoClosure(state.getContenido(), letra);
                        ArrayList<ArrayList<String>> remplazoRapido = noRepetidos(nodo);
                        nodo.clear();
                        nodo.addAll(remplazoRapido);

                        //--------Crear un sustituto de estados solo para revisar nuevos estados------------------------

                        ArrayList<Estado> sustitutoEstados = new ArrayList<>();
                        ArrayList<Transicion> sustitutoTransiciones = new ArrayList<>();
                        sustitutoEstados.addAll(estados);
                        sustitutoTransiciones.addAll(transiciones);

                        if(nodo.isEmpty()){

                        }
                        else{//Nodo no vacio
                            //Comprobar si el estado recien procesado ya existe
                            boolean existente = false;
                            int indexEstados = 0;
                            for(int i = 0; i < estados.size(); i++){
                                if(sustitutoEstados.get(i).equals(nodo)){
                                    existente = true;
                                    indexEstados = i;
                                }
                            }

                            if(existente){
                                Transicion trans = new Transicion(state.getContenido(), state.getNumero(),  sustitutoEstados.get(indexEstados).getContenido(), sustitutoEstados.get(indexEstados).getNumero(),  letra);
                                if(!transiciones.contains(trans)){
                                    temporalTransicion.add(trans);
                                }

                            }
                            else{
                                HashSet<ArrayList<String>> formato = new HashSet<>();
                                formato.addAll(nodo);
                                Estado nuevo = new Estado(numeroEstado, formato);
                                boolean adentroDeEstados = false;
                                int numeroTemporal = 0;
                                for (Estado EST: estados){
                                    if(EST.getContenido().equals(nuevo.getContenido())){
                                        nuevo = EST;
                                        adentroDeEstados = true;
                                        break;
                                    }
                                }
                                if(!adentroDeEstados){
                                    temporalEstados.add(nuevo);
                                    numeroEstado++;
                                }

                                Transicion trans = new Transicion(state.getContenido(), state.getNumero(),   nuevo.getContenido(), nuevo.getNumero(), letra);
                                if(!transiciones.contains(trans)){
                                    temporalTransicion.add(trans);
                                }
                            }
                        }
                        state.setContenido(comportamientoEstados);
                    }
                }
            }
            estados.addAll(temporalEstados);
            tamanoFinalEstados = estados.size();
            transiciones.addAll(temporalTransicion);
            tamanoFinalTransiciones = transiciones.size();

            System.out.println("NUMERO DE ESTADOS");
            System.out.println(estados.size());
            System.out.println("NUMERO DE TRANSICIONES");
            System.out.println(transiciones.size());


        }

        System.out.println(estados);
        System.out.println(transiciones);
        System.out.println(estructuraProducciones);

        ArrayList<ArrayList<String>> tabla = CrearLaTabla();
        System.out.println(tabla);
        System.out.println(alfabeto);

    }

    public ArrayList<ArrayList<String>> GotoClosure(HashSet<ArrayList<String>> NodoInterno, String transicion){
        HashSet<ArrayList<String>> resultado = new HashSet<>();
        ArrayList<ArrayList<String>> tiempo = new ArrayList<>();
        ArrayList<ArrayList<String>> movimiento = GOTO(NodoInterno,  transicion);
        ArrayList<ArrayList<String>> señuelo = new ArrayList<>();
        señuelo.addAll(movimiento);
        resultado.addAll(movimiento);
        for(ArrayList<String> produccion: señuelo){
            tiempo = Closure(produccion);
            resultado.addAll(tiempo);
        }

        tiempo.clear();
        tiempo.addAll(resultado);
        for(int i = 0; i < tiempo.size(); i++){
            if(tiempo.get(i).contains("$")){
                if(!tiempo.get(i).get(0).equals("§")){
                    ArrayList<String> remplazo = new ArrayList<>();
                    for(String s: tiempo.get(i)){
                        if(!s.equals("$")){
                            remplazo.add(s);
                        }

                    }
                    tiempo.set(i, remplazo);
                }
            }
        }


        return tiempo;
    }

    public ArrayList<ArrayList<String>> noRepetidos(ArrayList<ArrayList<String>> lista){
        HashSet<ArrayList<String>> nuevo = new HashSet<>();
        for(ArrayList<String> ente: lista){
            if(!ente.isEmpty()){
                nuevo.add(ente);
            }
        }
        ArrayList<ArrayList<String>> respuesta = new ArrayList<>();
        respuesta.addAll(nuevo);
        return respuesta;
    }

    public ArrayList<ArrayList<String>> CrearLaTabla(){
        ArrayList<ArrayList<String>> tabla = new ArrayList<>();
        ArrayList<String> abecedario = new ArrayList<>();
        abecedario.addAll(alfabeto);

        //Crear la Tabla inicial, vacia----------------------------------------
        ArrayList<String> primeralinea = new ArrayList<>();
        primeralinea.add("#");
        for(String s: abecedario){
            if(!s.equals("§")){
                primeralinea.add(s);
            }

        }
        abecedario.remove("§");
        int tamanoLinea = primeralinea.size();
        tabla.add(primeralinea);


        for(int i = 0; i < estados.size(); i++ ){
            ArrayList<String> linea = new ArrayList<>();
            linea.add(String.valueOf(i));
            for(int j = 1; j<tamanoLinea; j++){
                linea.add("-");
            }
            tabla.add(linea);
        }
        //---------------------------------------------------------------------
        //Creando la tabla en relacion a las transiciones que existen.---------
        for(Transicion transition: transiciones) {
            int numeroSalida = transition.getNumeroSalida() + 1;
            String letra = transition.getTransicion();
            int indexColumna = abecedario.indexOf(letra) + 1;

            //Linea de la tabla, que tendra que ser remplazada dentro de este algoritmo y luego en tabla(numeroSalida)
            ArrayList<String> lineaRepuesto = tabla.get(numeroSalida);
            HashSet<ArrayList<String>> produccionLlegada = transition.getLlegada();

            //Variables para encontrar lo que se debe de sobreescribir segun produccion de llegada
            boolean HayTransiciones = false;
            ArrayList<Integer> indexPunto = new ArrayList<>();
            ArrayList<ArrayList<String>> noKernel = new ArrayList<>();
            for (ArrayList<String> product : produccionLlegada) {
                if (product.indexOf(".") != 2) {
                    noKernel.add(product);
                    indexPunto.add(product.indexOf("."));

                }
            }

            //POSIBLES ERRORES
            if (noKernel.size() >= 1) {

                String respuesta = "";
                for (int i = 0; i < noKernel.size(); i++) {
                    int punto = indexPunto.get(i);
                    ArrayList<String> producto = noKernel.get(i);

                    String titulo = "S" + String.valueOf(transition.getNumeroLlegada());
                    lineaRepuesto.set(indexColumna, titulo);
                    tabla.set(numeroSalida, lineaRepuesto);

                    if (punto != producto.size() - 1) {
                        if (!producto.get(punto + 1).equals("$")) {


                        }
                    }

                    //Crear en donde se van a poner las producciones y si presenta error por ponerse encima de algo
                }
            }
        }
        for(Estado state: estados){
            int numero = state.getNumero();
            String titulo = "R";
            ArrayList<String> linea = tabla.get(numero +1);
            ArrayList<ArrayList<String>> noKernel2 = new ArrayList<>();

            for (ArrayList<String> product : state.getContenido()) {
                if (product.indexOf(".") == product.size()-1) {
                    noKernel2.add(product);

                }
            }
            for(ArrayList<String> reducciones: noKernel2){
                HashSet<String> followKernel = follow(reducciones.get(0));
                //Determinar columnas en las que va a ir R#, indicesAlfabeto = columnasParaR#
                ArrayList<Integer> indicesAlfabeto = new ArrayList<>();
                for(String s: followKernel){
                    int numeral = abecedario.indexOf(s) + 1;
                    indicesAlfabeto.add(numeral);
                }
                //Determinar el # de R#, mejorProduct = produccionSinPunto

                ArrayList<String> mejorProduct = new ArrayList<>();
                for(int i = 0; i < reducciones.size()-1; i++){
                    mejorProduct.add(reducciones.get(i));
                }
                //Comprobar que produccion se esta viendo
                int numeroHashtag = 0;
                for(int i = 0; i < estructuraProducciones.size(); i++){
                    ArrayList<String> comparativo = estructuraProducciones.get(i);
                    if(i == 0){
                        //Mejorar la primera produccion para que no tenga punto
                        comparativo.remove(".");

                    }

                    if(mejorProduct.equals(comparativo)){
                        numeroHashtag = i;
                    }
                }
                if(numeroHashtag == 0){
                    //Caso accept
                    titulo = "accep";
                    linea.set(1, titulo);
                }
                else{
                    //Reduce
                    titulo = titulo + String.valueOf(numeroHashtag);
                    for(Integer x: indicesAlfabeto){
                        String resultado = linea.get(x);
                        if(resultado.equals("-")){
                            linea.set(x, titulo);
                        }
                        else{
                            if(!titulo.equals(resultado)){

                                if(resultado.startsWith("S")){
                                    System.out.println("Error Shift Reduce, linea: " + numero);
                                    titulo = titulo + "/" + resultado;
                                    linea.set(x, titulo);
                                }
                                else{
                                    System.out.println("Error Reduce-Reduce, linea: " + numero);
                                    titulo = titulo + "/" + resultado;
                                    linea.set(x, titulo);
                                }
                            }
                        }
                    }
                }
            }
        }
        //---------------------------------------------------------------------
        return tabla;
    }

/*-----------------------------Terminando  el Parser LR(0)-------------------------------------------------------------*/
    public HashSet<String> first(String peticion){
        numeroDePeticion = 0;
        tieneHashtag = false;
        HashSet<String> resultado = new HashSet<>();
        String item = "";
        for(int i = 0; i < peticion.length(); i++){
            String parte = peticion.substring(i, i+1);
            if(!parte.equals(" ")){
                item = item + parte;
            }
            else{
                peticiones.add(item);
                item = "";
            }
        }
        peticiones.add(item);
        boolean pruebaDeFuego = true;
        while(pruebaDeFuego){
            if(!chequear(peticiones.get(numeroDePeticion))){
                resultado.add(peticiones.get(numeroDePeticion));
            }
            else{
                resultado.addAll(encontrado(peticiones.get(numeroDePeticion)));
            }


            if(!resultado.contains("#") || numeroDePeticion > peticiones.size()-1){
                pruebaDeFuego = false;
            }

            if(resultado.contains("#")){
                resultado.remove("#");
                tieneHashtag = true;
            }
            numeroDePeticion = numeroDePeticion +1;
        }



        return resultado;
    }

    public HashSet<String>  follow(String Nodo){
        Nodo = Nodo.replaceAll("\\s", "");
        HashSet<String> resultado = new HashSet<>();
        int tamanoInicial = 0;
        int tamanoFinal = 0;
        for(ArrayList<String> produccion: estructuraProducciones){
            ArrayList<Integer> numeroIndices = new ArrayList<>();

            for(int i = 0; i < produccion.size(); i++){
                if(produccion.get(i).equals(Nodo)){
                    numeroIndices.add(i);
                }

            }
            for(Integer numero: numeroIndices){
                if(produccion.contains(Nodo)&& numero != 0) {
                    if(numero == produccion.size()-2){
                        HashSet<String> temporal = follow(produccion.get(0));
                        resultado.addAll(temporal);
                        resultado.add(produccion.get(produccion.indexOf(Nodo)+1));
                    }
                    else{
                        if(numero+1 < produccion.size()){
                            resultado.add(produccion.get(numero+1));
                        }

                    }

                }

            }
        }
        HashSet<String> cabezas = new HashSet<>();
        for(ArrayList<String> inicio: estructuraProducciones){
            cabezas.add(inicio.get(0));
        }
        tamanoFinal = resultado.size();
        while(tamanoFinal != tamanoInicial){

            ArrayList<String> temporal = new ArrayList<>();
            temporal.addAll(resultado);
            for(String s: temporal){
                if(cabezas.contains(s)){
                    HashSet<String> agregado = follow(s);
                    HashSet<String> inicio = first(s);
                    resultado.addAll(agregado);
                    resultado.addAll(inicio);

                    //Metodo para limpiar espacios vacios
                    HashSet<String> tempo  = new HashSet<>();
                    for(String r: resultado){
                        if(!r.equals("")){
                            tempo.add(r);
                        }
                    }
                    resultado.clear();
                    resultado.addAll(tempo);

                }
            }

            tamanoInicial = tamanoFinal;
            tamanoFinal = resultado.size();
        }
        if(tieneHashtag){
            resultado.add("#");
        }

        return resultado;
    }

    public boolean chequear(String s){
        boolean respuesta = false;
        for(ArrayList<String> i: estructuraProducciones){
            if(i.get(0).equals(s)){
                respuesta = true;
                break;
            }
        }
        return respuesta;

    }

    public HashSet<String> encontrado(String conocido){
        HashSet<String> contenidoFirsteno = new HashSet<>();
        for(ArrayList<String> producciones: estructuraProducciones){
            if(producciones.get(0).equals(conocido)){

                String firstino = producciones.get(2);
                if(chequear(firstino)){
                    contenidoFirsteno.addAll(encontrado(firstino));
                }
                else{
                    contenidoFirsteno.add(firstino);
                }

            }
        }
        if(contenidoFirsteno.size() == 0){
            contenidoFirsteno.add(conocido);
        }
        return contenidoFirsteno;
    }

}

