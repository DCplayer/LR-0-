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
                                if(!estados.contains(nuevo)){
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
        ArrayList<ArrayList<String>> tablaDeParseo = new ArrayList<>();
        for (Estado state: estados){
            ArrayList<ArrayList<String>> reduces = new ArrayList<>();
            ArrayList<String> linea = new ArrayList<>();
            int numeroSalida = state.getNumero();
            linea.add(numeroSalida + "");

            ArrayList<Transicion> transicionesActuales = new ArrayList<>();
            for(Transicion transition: transiciones){
                int numeroOrigenTrans = transition.getNumeroSalida();
                if(numeroSalida == numeroOrigenTrans){
                    transicionesActuales.add(transition);
                }
            }
            for(String letra: alfabeto){
                String respuesta = "";
                int indexPunto = 0;
                ArrayList<String> noKernel = new ArrayList<>();
                int numeroLlegada = 0;
                for(Transicion trans: transicionesActuales) {
                    numeroLlegada = trans.getNumeroLlegada();
                    if (trans.getTransicion().equals(letra)) {

                        for (ArrayList<String> product : trans.getLlegada()) {
                            if (product.indexOf(".") != 2) {
                                noKernel = product;
                                indexPunto = product.indexOf(".");
                                break;
                            }
                        }
                        /* Erroneo, el estado 0 nunca aparecera como llegada de ninguno!!
                        //Revisar si tiene algun almenos un estado no kernel
                        if (noTienePuntoMovido) {
                            for (ArrayList<String> product : trans.getLlegada()) {
                                if (product.get(0).equals("§")) {
                                    noKernel = product;
                                    indexPunto = product.indexOf(".");
                                }
                            }
                        }
                        */
                    }
                }

                //Trabajar con el ente NoKernel y el Punto, esto te dira si lo que tenes que agregar es Shift
                // o reduce o accept

                //Caso Shift
                if (indexPunto != noKernel.size() - 1) {
                    respuesta = "S" + String.valueOf(numeroLlegada);

                }
                //Caso Reduce && accept
                else if (indexPunto == noKernel.size() - 1) {
                    //Esto solo servira para comprobar si es Accept o solo un reduce
                    ArrayList<String> inicialProducciones = estructuraProducciones.get(0);
                    ArrayList<String> inicialReal = new ArrayList<>();
                    for(String s : inicialProducciones){
                        if(!s.equals(".")){
                            inicialReal.add(s);
                        }
                    }
                    //inicialReal = produccion Inicial sin el punto
                    ArrayList<String> noKernelComparativo = new ArrayList<>();
                    for(int i = 0; i < noKernel.size()-1; i++){
                        noKernelComparativo.add(noKernel.get(i));
                    }

                    //NoKernelComparativo = sabiendo que esta el punto al final, se lo quitamos y miramos si es la
                    // produccion inicial

                    if(noKernelComparativo.equals(inicialReal)){
                        respuesta = "accep.";
                    }
                    else{//Revisar el Reduce en donde se encuentra y si hay error shift Reduce o Reduce Reduce
                        int numeroProduccion = 0;
                        for(int i = 1; i < estructuraProducciones.size(); i++){
                            if(estructuraProducciones.get(i).equals(noKernelComparativo)){
                                numeroProduccion = i;
                            }
                        }
                        String titulo = "R" + String.valueOf(numeroProduccion);
                        ArrayList<String> reduccion = new ArrayList<>();
                        reduccion.add(titulo);
                        HashSet<String> elFollowDeNoKernel = follow(noKernelComparativo.get(0));
                        ArrayList<String> abecedario = new ArrayList<>();
                        abecedario.addAll(alfabeto);
                        for(String s: elFollowDeNoKernel){
                            for(int i = 0; i < abecedario.size(); i++){
                                if(s.equals(abecedario.get(i))){
                                    reduccion.add(String.valueOf(i+1));
                                }
                            }
                        }
                        reduces.add(reduccion);


                    }
                }
                linea.add(respuesta);
            }
            //POner aqui para revisar en donde hay problemas de Shift reduce o simplemente poner los Reduce en la linea
            //En linea, revisar si, para cada reduce puesto en reduces existe problemas SR o RR, si no, solo colocarlo

            for(ArrayList<String> reduccion: reduces){
                for(int i = 1; i < reduccion.size(); i++){
                    int numeroIndex = Integer.parseInt(reduccion.get(i));
                    String dato = linea.get(numeroIndex);
                    if(dato.equals("")){
                        linea.set(numeroIndex, reduccion.get(0));
                    }
                    else{
                        String subDato = dato.substring(0,1);
                        if(subDato.equals("S")){
                            System.out.println("Problema Shift Reduce, linea" + state.getNumero());
                            String nuevoDato = dato + "/R" + reduccion.get(0);
                            linea.set(numeroIndex, nuevoDato);
                        }
                    }

                }
            }

            tablaDeParseo.add(linea);
        }
        return tablaDeParseo;
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
            if(produccion.contains(Nodo)&& produccion.indexOf(Nodo) != 0) {
                if(produccion.indexOf(Nodo) == produccion.size()-2){
                    HashSet<String> temporal = follow(produccion.get(0));
                    resultado.addAll(temporal);
                }
                else{
                    if(produccion.indexOf(Nodo)+1 < produccion.size()){
                        resultado.add(produccion.get(produccion.indexOf(Nodo)+1));
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

