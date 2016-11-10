import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class practicaADA2 {

    private static ArrayList<Integer> conjunto; //Almacena el subconjunto de entrada.
    private static int sum; //Almacena la suma que buscamos.
    private static FileWriter fichero;
    private static PrintWriter pw;
    private static boolean DEBUG = true; //si esta variable es true el algoritmo de programacion dinamica imprime la tabla
    private static String FICHERO = "./solucion.txt"; //fichero en el que se imprime los subconjuntos.
    private static int FUNC = 1; //Utilizada para elegir la funcionalidad del programa
    private static int IMPR = 1; //Utilizada para saber desde que algoritmo estamos imprimiendo
    // FUNC = 1: Solucion dinamica o backtracking en funcion de la entrada, FUNC = 2: Compara algoritmos.

    public static void main(String[] args) throws IOException{

        tomarDatos();

        fichero = new FileWriter(FICHERO);
        pw = new PrintWriter(fichero);

        depuraEntrada();

        switch(FUNC) {
            case 1: //resolucion mas eficiente
                if(sum > Math.pow(2, conjunto.size())){
                    System.out.println("Resuelto por backtracking");
                    backtracking();
                }else{
                    System.out.println("Resuelto por programacion dinamica");
                    programacionDinamica();
                }
                break;

            case 2:
                 //resuelve con los dos algoritmos midiendo los tiempos

                long cronometro; // variable utilizada para medir el tiempo de ejecucion de los algoritmos

                pw.println("Solucion dinamica:");
                cronometro = System.nanoTime();
                programacionDinamica();
                cronometro = System.nanoTime() - cronometro;
                System.out.println("Tiempo dinamica: " + cronometro + "ns");

                pw.println("Solucion backtracking:");
                cronometro = System.nanoTime();
                backtracking();
                cronometro = System.nanoTime() - cronometro;
                System.out.println("Tiempo backtracking: " + cronometro + "ns");

                break;

            default:
                System.out.print("ERROR: El valor de FUNC tiene que ser 1 o 2.");
                pw.print("ERROR: El valor de FUNC tiene que ser 1 o 2.");
        }

        fichero.close();

    }

    /*Pide al usuario los datos por teclado y los almacena en conjunto y sum*/
    private static void tomarDatos(){

        Scanner sc = new Scanner(System.in);
        conjunto = new ArrayList<>();
        int i=0, dato;

        System.out.println("Introduzca el valor sum:");
        sum =sc.nextInt();
        System.out.println("Introduzca los elementos del conjunto: (Para finalizar introduzca un -1)");
        while(true){
            System.out.println("Elemento "+i+":");
            dato=sc.nextInt();
            if(dato==-1){
                break;
            } else {
                conjunto.add(dato);
            }
            i++;
        }
    }

    /* depuraEntrada elimina las entradas mayores que la suma a buscar e introduce en el fichero solucion los valores
     * que coinciden con dicha suma, eliminandolos tambien del subconjunto de entrada.
     */
    private static void depuraEntrada (){

        for(int i = 0; i< conjunto.size(); i++){

            if(conjunto.get(i)>sum){

                conjunto.remove(i);
                i--;

            } else {

                if(conjunto.get(i)==sum){

                    pw.print(conjunto.get(i)+"\n");
                    conjunto.remove(i);
                    i--;

                }
            }
        }

    }


    /*Funcion que implementa el algoritmo de programacion dinamica*/
    private static void programacionDinamica(){
        IMPR = 1; //Cambiamos el valor de FUNC para imprimir los subconjuntos en el orden encontrado
        boolean tabla[][] = generarTablaDinamica();
        if (tabla[conjunto.size()][sum]){
            if(DEBUG) {
                imprimirTabla(tabla);
            }
            calcularSubconjuntos(new ArrayList<Integer>(),tabla,conjunto.size(),sum);
        }
    }

    /*Recorre la tabla buscando todos los caminos que lleguen a la solucion, los subconjuntos.*/
    private static void calcularSubconjuntos(ArrayList<Integer> solucion, boolean tabla[][], int i, int j) {



        if(i==0){
            /*Subconjunto encontrado.
             *Imprimimos el subconjunto en el fichero.
             */
            imprimirSubconjunto(solucion);

        } else {
            int elemento = conjunto.get(i-1);
            if (tabla[i - 1][j]) {
                /*No se utiliza el elemento para este subconjunto.
                * Continuamos la busqueda.
                */
                calcularSubconjuntos(solucion,tabla,i-1,j);
            }

            if ( (j>=elemento) && (tabla[i - 1][j - elemento]) ) {
                /*Se utiliza este elemento.
                 *Lo añadimos al subconjunto y continuamos con la busqueda
                 */
                ArrayList<Integer> sol = (ArrayList<Integer>) solucion.clone();
                sol.add(elemento);
                calcularSubconjuntos(sol,tabla,i-1,j-elemento);

            }
        }

    }

    /*Genera la matriz utilizada para la resolucion*/
    private static boolean[][] generarTablaDinamica() {
        int i, j;

        boolean tabla[][] = new boolean[conjunto.size() +1][sum +1];

        // rellenar la primera fila [1, 0, 0, ... 0]
        tabla[0][0]=true;
        for (i = 1; i < tabla[0].length; i++) {
            tabla[0][i] = false;
        }



        int elemento;
        for (i=1;i<tabla.length;i++){
            elemento= conjunto.get(i-1);
            for(j=0;j<tabla[i].length;j++){
                if(elemento>j){
                    tabla[i][j]=tabla[i-1][j];
                } else {
                    tabla[i][j]= (tabla[i-1][j]) || (tabla[i-1][j-elemento]);
                }
            }
        }

        return tabla;
    }

    /*Facilita el uso de backtracking (No es necesario conocer detalles de la implementacion).*/
    private static void backtracking (){
        IMPR = 2; //Cambiamos el valor de FUNC para imprimir los subconjuntos en el orden encontrado
        backtracking(0,0,new ArrayList<Integer>());
    }

    /*Funcion que implementa el algoritmo de backtracking*/
    private static void backtracking (int suma, int i, ArrayList<Integer> subconjunto){

        int s;

        if(suma<sum && i<(conjunto.size())){

            /*Si la suma parcial es menor que la que estamos buscando se haria una llamada por cada numero que no haya
             *sido considerado aun en la solucion parcial.
             */
            for(int j = i; j< conjunto.size(); j++) {

                s = suma + conjunto.get(j);
                ArrayList<Integer> sub = (ArrayList<Integer>) subconjunto.clone();
                sub.add(conjunto.get(j));

                backtracking(s, j + 1, sub);
            }

        } else {

            if(suma==sum){
                //Solucion encontrada. Imprimimos la solucion en el fichero
                imprimirSubconjunto(subconjunto);
            }

        }
        // Si no entra en ninguno de los ifs se cortaria el hilo de ejecucion y se relizaria backtracking
    }



    /*Imprime, en el fichero que indique la macro "FICHERO", el atributo subconjunto*/
    private static void imprimirSubconjunto(ArrayList<Integer> subconjunto){
        if (IMPR==1){
            for (int i = subconjunto.size()-1; i > 0; i--){
                pw.print(subconjunto.get(i)+", ");
            }
            pw.println(subconjunto.get(0));
        }
        else{
            for (int i = 0; i < subconjunto.size()-1; i++){
                pw.print(subconjunto.get(i)+", ");
            }
            pw.println(subconjunto.get(subconjunto.size()-1));
        }

    }

    /*Imprime en el terminal la tabla que reciva como argumento*/
    private static void imprimirTabla(boolean tabla[][]){
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.printf("%4d", (tabla[i][j]) ? 1 : 0);
            }
            System.out.printf("\n");
        }
    }


}