package fada;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import utilidad.FileController;
import utilidad.Permutar;

/**
 * Clase que se encarga de calcular la subsecuencia más larga de los juegos del
 * baloto.
 * @author gianlucca y Danna
 */
public class LCSBaloto {

    private int limite = 0;
    private final String[][] matriz;
    private final PriorityQueue<int[]> balotasMasRepetidas;
    private final PriorityQueue<String> respuestas;
    private final ArrayList<Integer> listaOpciones = new ArrayList<>();
    private final Comparator compara = (Comparator) new ComparatorArray();

    /**
     * Contructor de la clase
     *
     * @param lim, int que determina la cadena de cuantas balotas a calcular
     */
    public LCSBaloto(int lim) {
        limite = lim;
        balotasMasRepetidas = new PriorityQueue<>(compara);
        respuestas = new PriorityQueue<>();
        matriz = new FileController().getFileContent();
    }

    /**
     * Se encarga de buscar las balotas que más se repiten
     * @param limite
     */
    public void buscarRepetidos(int limite) {
        int[] repetidos = new int[47];
        for (int q = 0; q < repetidos.length; q++) {
            repetidos[q] = 0;
        }
        for (String[] matriz1 : matriz) {
            for (int j = 0; j < 6; j++) {
                int temp = Integer.parseInt(matriz1[j]);
                int temp2 = repetidos[temp];
                repetidos[temp] = temp2 + 1;
            }
        }
        for (int i = 0; i < repetidos.length; i++) {
            int[] arreglo = {i, repetidos[i]};
            balotasMasRepetidas.offer(arreglo);
        }
        for (int i = 0; i < limite; i++) {
            int repetido = repetidos[i];
            listaOpciones.add(balotasMasRepetidas.poll()[0]);
        }
    }

    /**
     * Busca las posibles combinaciones de balotas entre las encontradas en el
     * metodo buscarRepetidos
     * @param limite
     */
    public void encontrarPosibilidades(int limite) {
        ArrayList<String> prob = new ArrayList<>();

        for (String[] matriz1 : matriz) {
            String temp = "";
            for (int j = 0; j < 6; j++) {
                if (compararConLista(Integer.parseInt(matriz1[j]))) {
                    temp = temp + String.valueOf(matriz1[j]);
                    if (!temp.equals("")) {
                        prob.add(temp);
                    }
                }
            }
            if (temp.length() >= limite) {
                String palabra = prob.get(prob.size() - 1);
                int pr = Permutar.getFactorial(palabra.length());
                String[] permutaciones = Permutar.permutar(palabra, pr);
                for (String permutacione : permutaciones) {
                    respuestas.offer(permutacione);
                }
            }
        }
    }

    /**
     * Compara un entero con cada elemento de una lista.
     * @param dato entero a buscar
     * @return booleano, true si encuentra el dato, de lo contrario false.
     */
    public boolean compararConLista(int dato) {
        boolean respuesta = false;
        for (int i = 0; i < listaOpciones.size(); i++) {
            if (dato == listaOpciones.get(i)) {
                respuesta = true;
                break;
            }
        }
        return respuesta;
    }

    /**
     * Procesa la lista de prioridad con las conmbinaciones posibles y retorna
     * por consola las cadenas mas largas encontradas
     */
    public void generarRespuestas() {
        ArrayList<String> cadenas = new ArrayList<>();
        cadenas.add(respuestas.poll());
        ArrayList<Integer> cantidad = new ArrayList<>();
        cantidad.add(1);

        int size = respuestas.size();

        for (int i = 0; i < size; i++) {
            String dato = respuestas.poll();
            int pos = cadenas.indexOf(dato);
            if (pos <= -1) {
                cadenas.add(dato);
                cantidad.add(1);
            } else {
                int num = cantidad.get(pos);
                num++;
                cantidad.set(pos, num);
            }
        }

        System.out.println("Las subsecuencias que mas se repiten de tamaño " + limite + " son:");
        int dato = cantidad.get(0);
        System.out.println(cadenas.get(0));

        for (int i = 1; i < cantidad.size(); i++) {
            if (dato == cantidad.get(i)) {
                System.out.println(cadenas.get(i));
                dato = cantidad.get(i);
            } else {
                break;
            }
        }
    }

    /**
     * Metodo que hace la invocacion de cada uno de los metodos que conforman
     * los principales de la ejecución del algoritmo (vease el informe)
     */
    public void retornarLCS() {
        buscarRepetidos(limite);
        encontrarPosibilidades(limite);
        generarRespuestas();
    }

    /**
     * Clase privada que implementa a Comparator y permite hacer uso de una
     * comparacion en la cola de jugadas por medio de arreglos de enteros
     */
    private class ComparatorArray implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            return o2[1] - o1[1];
        }
    }

    /**
     * Clase de ejecución
     * @param args 
     */
    public static void main(String args[]) {
        LCSBaloto baloto = new LCSBaloto(2);
        baloto.retornarLCS();
    }
}
