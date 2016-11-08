import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by oscar on 13/10/16.
 */
public class practicaADA2 {

    private static ArrayList<Integer> subConjunto; //Almacena el subconjunto de entrada.
    private static int sum; //Almacena la suma que buscamos.
    private static FileWriter fichero; //fichero en el que escribimos la solucion.
    private static PrintWriter pw;

    public static void main(String[] args) throws IOException{

        //Lectura de los datos por teclado:
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca los numeros del subconjunto: (Para finalizar introduzca un 0)");
        subConjunto = new ArrayList<>();
        int i=0, dato;
        while(true){
            System.out.println("Dato "+(i+1)+":");
            dato=sc.nextInt();
            if(dato==0){
                System.out.println("Introduzca el valor sum:");
                sum =sc.nextInt();
                break;
            } else {
                subConjunto.add(dato);
            }
            i++;
        }

        //Solucion silvia
        int solsubset[]=new int [subConjunto.size()];
        boolean tabla[][] = tableSubsetSum(subConjunto, sum);
        if (tabla[sum][subConjunto.size()]==false)
            System.out.println("No hay una subsecuencia que sume sum");
        else{
            imprimirTabla(tabla);
            iniciarsolsubset(solsubset);
            calcularSolSubSet(solsubset,subConjunto,tabla,sum,solsubset.length);
        }
        //finnnnn

        fichero = new FileWriter("../practicaADA2/solucion.txt");
        pw = new PrintWriter(fichero);

        depuraEntrada();
        backtracking(0,0,new ArrayList<Integer>());

        fichero.close();
    }

    private static void backtracking (int suma, int i, ArrayList<Integer> solucion){

        int s;

        if(suma<sum && i<(subConjunto.size())){

            for(int j=i;j<subConjunto.size();j++) {

                s = suma + subConjunto.get(j);
                ArrayList<Integer> sol = (ArrayList<Integer>) solucion.clone();
                sol.add(subConjunto.get(j));

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

        for(int i = 0; i<subConjunto.size();i++){

            if(subConjunto.get(i)>sum){

                subConjunto.remove(i);
                i--;

            } else {

                if(subConjunto.get(i)==sum){

                    pw.print(subConjunto.get(i)+"\n");
                    subConjunto.remove(i);
                    i--;

                }
            }
        }

    }

    static void calcularSolSubSet(int solsubset[], ArrayList<Integer> set, boolean tabla[][], int sum, int n) {

        if(( sum == 0)) {
            imprimirSolSubSet(solsubset);
            iniciarsolsubset(solsubset);
        }

        if(n>0) {

            if ((set.get(n - 1) <= sum)&&(tabla[sum][n]==true)) {
                calcularSolSubSet(solsubset, set, tabla, sum, n - 1);
                solsubset[n - 1] = set.get(n - 1);
                calcularSolSubSet(solsubset, set, tabla, sum - set.get(n - 1), n - 1);

            } else {
                calcularSolSubSet(solsubset, set, tabla, sum, n - 1);

            }

        }

    }

    static boolean[][] tableSubsetSum(ArrayList<Integer> set, int sum) {
        int i, j;
        // el valor del subset[i][j] es true si hay
        // un subset del set[0..j-1] con la suma igual a i
        boolean subset[][] = new boolean[sum + 1][set.size() + 1];
        // si sum es 0, es true
        for (i = 0; i <= set.size(); i++)
            subset[0][i] = true;
        // si la suma no es 0 y la secuencia está facía, es falso
        for (i = 1; i <= sum; i++)
            subset[i][0] = false;
        // Llena la tabla de subsecuencias
        for (i = 1; i <= sum; i++) {
            for (j = 1; j <= set.size(); j++) {
                subset[i][j] = subset[i][j - 1];
                if (i >= set.get(j - 1))
                    subset[i][j] = subset[i][j] || subset[i - set.get(j - 1)][j - 1];
            }
        }
        return subset;
    }

    static void iniciarsolsubset(int solsubset[]) {
        for (int i = 0; i < solsubset.length; i++) {
            solsubset[i] = 0;
        }
    }

    static void imprimirSolSubSet(int solsubset[]){
        System.out.print("Subsecuencia: [");
        for (int i = 0; i < solsubset.length; i++){
            if (solsubset[i]!=0)
                System.out.print(solsubset[i]+",");
        }
        System.out.println("]");
    }

    public static void imprimirTabla(boolean tabla[][]){
        for (int i = 0; i <= tabla.length; i++) {
            for (int j = 0; j <= tabla[i].length; j++) {
                System.out.printf("%4d", (tabla[i][j]) ? 1 : 0);
            }
            System.out.printf("\n");
        }
    }


}
