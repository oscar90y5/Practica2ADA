import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class practicaADA2 {

    private static ArrayList<Integer> conjunto; //Almacena el subconjunto de entrada.
    private static int sum; //Almacena la suma que buscamos.
    private static FileWriter fichero; //fichero en el que escribimos la solucion.
    private static PrintWriter pw;
    private static boolean DEBUG = false;
    private static int FUNC = 1;
    // FUNC = 1: Solucion dinamica, FUNC = 2: Solucion backtracking, FUNC = 3: compara algoritmos.

    public static void main(String[] args) throws IOException{

        tomarDatos();

        fichero = new FileWriter("../practicaADA2/solucion.txt");
        pw = new PrintWriter(fichero);

        depuraEntrada();

        switch(FUNC) {
            case 1:
                programacionDinamica();
                break;

            case 2:
                backtracking(0, 0, new ArrayList<Integer>());
                break;

            case 3:

                long cronometro;

                pw.println("Solucion dinamica:");
                cronometro = System.nanoTime();
                programacionDinamica();
                cronometro = System.nanoTime() - cronometro;
                System.out.println("Tiempo dinamica: " + cronometro + "ns");

                pw.println("Solucion backtracking:");
                cronometro = System.nanoTime();
                backtracking(0, 0, new ArrayList<Integer>());
                cronometro = System.nanoTime() - cronometro;
                System.out.println("Tiempo backtracking: " + cronometro + "ns");

                break;

            default:
                System.out.print("ERROR: El valor de FUNC tiene que ser 1, 2 o 3.");
                pw.print("ERROR: El valor de FUNC tiene que ser 1, 2 o 3.");
        }

        fichero.close();

    }

    private static void tomarDatos(){

        Scanner sc = new Scanner(System.in);
        conjunto = new ArrayList<>();
        int i=0, dato;

        System.out.println("Introduzca los numeros del subconjunto: (Para finalizar introduzca un 0)");
        while(true){
            System.out.println("Dato "+(i+1)+":");
            dato=sc.nextInt();
            if(dato==0){
                System.out.println("Introduzca el valor sum:");
                sum =sc.nextInt();
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



    private static void programacionDinamica(){
        boolean tabla[][] = generarTablaDinamica();
        if (tabla[conjunto.size()][sum]){
            if(DEBUG) {
                imprimirTabla(tabla);
            }
            calcularSubconjuntos(new ArrayList<Integer>(),tabla,conjunto.size(),sum);
        }
    }

    private static void calcularSubconjuntos(ArrayList<Integer> solucion, boolean tabla[][], int i, int j) {



        if(i==0){
            //imprime solucion
            imprimirSubconjunto(solucion);

        } else {
            int elemento = conjunto.get(i-1);
            if (tabla[i - 1][j]) {
                //llama recursivo
                calcularSubconjuntos(solucion,tabla,i-1,j);
            }

            if ( (j>=elemento) && (tabla[i - 1][j - elemento]) ) {
                //añade solucion
                ArrayList<Integer> sol = (ArrayList<Integer>) solucion.clone();
                sol.add(elemento);
                //llama recursivo
                calcularSubconjuntos(sol,tabla,i-1,j-elemento);

            }
        }

    }

    private static boolean[][] generarTablaDinamica() {
        int i, j;
        // el valor del tabla[i][j] es true si hay
        // un subset del set[0..j-1] con la suma igual a i
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



    private static void backtracking (int suma, int i, ArrayList<Integer> subconjunto){

        int s;

        if(suma<sum && i<(conjunto.size())){

            //Si la suma parcial es menor que la que estamos buscando se haria una llamada por cada numero que no haya
            //sido considerado aun en la solucion parcial.
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



    private static void imprimirSubconjunto(ArrayList<Integer> subconjunto){
        if (FUNC==1){
            for (int i = subconjunto.size(); i > 0; i--){
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

    private static void imprimirTabla(boolean tabla[][]){
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.printf("%4d", (tabla[i][j]) ? 1 : 0);
            }
            System.out.printf("\n");
        }
    }


}
