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

}
