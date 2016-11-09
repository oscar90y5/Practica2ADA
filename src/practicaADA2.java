import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by oscar on 13/10/16.
 */
public class practicaADA2 {

    private static ArrayList<Integer> conjunto; //Almacena el subconjunto de entrada.
    private static int sum; //Almacena la suma que buscamos.
    private static FileWriter fichero; //fichero en el que escribimos la solucion.
    private static PrintWriter pw;
    private static boolean DEBUG=true;
    public static void main(String[] args) throws IOException{

        //Lectura de los datos por teclado:
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca los numeros del subconjunto: (Para finalizar introduzca un 0)");
        conjunto = new ArrayList<>();
        int i=0, dato;
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

        //Solucion silvia
        boolean tabla[][] = generarTablaDinamica();
        if (!tabla[conjunto.size()][sum])
            System.out.println("No hay una subsecuencia que sume sum");
        else{
            if(DEBUG) {
                imprimirTabla(tabla);
            }

            calcularSubconjuntos(new ArrayList<Integer>(),tabla,conjunto.size(),sum);
        }
        //finnnnn


        /*backtracking
        fichero = new FileWriter("../practicaADA2/solucion.txt");
        pw = new PrintWriter(fichero);

        depuraEntrada();
        backtracking(0,0,new ArrayList<Integer>());

        fichero.close();*/

    }

    private static void backtracking (int suma, int i, ArrayList<Integer> solucion){

        int s;

        if(suma<sum && i<(conjunto.size())){

            for(int j = i; j< conjunto.size(); j++) {

                s = suma + conjunto.get(j);
                ArrayList<Integer> sol = (ArrayList<Integer>) solucion.clone();
                sol.add(conjunto.get(j));

                backtracking(s, j + 1, sol);
            }

        } else {

            if(suma==sum){

                /* Solucion encontrada, la escribimos en el fichero*/

                for(int j=0;j<solucion.size();j++){
                    pw.print(solucion.get(j)+" ");
                }

                pw.print("\n");
            }

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

    static void calcularSubconjuntos(ArrayList<Integer> solucion, boolean tabla[][], int i, int j) {



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

    static boolean[][] generarTablaDinamica() {
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


    static void imprimirSubconjunto(ArrayList<Integer> subconjunto){

        for (int i = subconjunto.size()-1; i > 0; i--){
            if (subconjunto.get(i)!=0)
                System.out.print(subconjunto.get(i)+", ");
        }
        System.out.println(subconjunto.get(0));
    }

    public static void imprimirTabla(boolean tabla[][]){
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.printf("%4d", (tabla[i][j]) ? 1 : 0);
            }
            System.out.printf("\n");
        }
    }


}
