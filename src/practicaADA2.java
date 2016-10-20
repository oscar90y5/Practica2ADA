import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by oscar on 13/10/16.
 */
public class practicaADA2 {
    public static void main(String[] args) throws IOException{
        //Lectura de los datos por teclado:
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca los numeros del subconjunto: (Para finalizar introduzca un 0)");
        ArrayList<Integer> subConjunto = new ArrayList<>();
        int i=0, dato, sum;
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

        imprimeSol(backtracking(subConjunto,sum));
        escribeFichero(backtracking(subConjunto,sum));




    }

    private static ArrayList<ArrayList<Integer>> backtracking(ArrayList<Integer> subcon, int sum){
        ArrayList<ArrayList<Integer>> soluciones = new ArrayList<>();
        ArrayList<Integer> puntoBack = new ArrayList<>();
        puntoBack.add(0);
        int suma=0;
        soluciones.add(new ArrayList<Integer>());
        while(puntoBack.get(0) < (subcon.size()-1)){
            suma+=subcon.get(puntoBack.get(puntoBack.size()-1));
            if(suma==sum){
                //encontramos Solucion!!
                //copiamos la parte de la solucion que teniamos para poder seguir buscando nuevas soluciones desde ahi.
                soluciones.add((ArrayList<Integer>) soluciones.get(soluciones.size()-1).clone());

                //añadimos el ultimo numero a la solucion anterior
                soluciones.get(soluciones.size()-2).add(subcon.get(puntoBack.get(puntoBack.size()-1)));
                suma-=soluciones.get(soluciones.size()-2).get(soluciones.get(soluciones.size()-2).size()-1);
                suma=sigPrueba(puntoBack,subcon.size(),soluciones,suma);
            } else{
                if(suma<sum){
                    //añadimos el numero a la solucion y seguimos buscando
                    soluciones.get(soluciones.size()-1).add(subcon.get(puntoBack.get(puntoBack.size()-1)));
                    puntoBack.add(puntoBack.get(puntoBack.size()-1));
                    suma=sigPrueba(puntoBack,subcon.size(),soluciones,suma);
                } else {
                    //eliminamos el ultimo numero de suma y seguimos buscando
                    suma-=subcon.get(puntoBack.get(puntoBack.size()-1));
                    suma=sigPrueba(puntoBack,subcon.size(),soluciones,suma);
                }
            }


        }
        soluciones.remove(soluciones.size()-1);
        return soluciones;
    }

    /* Coloca en puntoBack los indices de la siguiente prueba.
     * LLamada: sigPrueba(puntoBack, subcon.size());
     */
    private static int sigPrueba(ArrayList<Integer> puntoBack, int tamSub, ArrayList<ArrayList<Integer>> soluciones, int suma){
        if(puntoBack.get(puntoBack.size()-1)<(tamSub-1)){
            //incrementamos el ultimo indice en uno
            puntoBack.set(puntoBack.size()-1, puntoBack.get(puntoBack.size()-1)+1);
            return suma;
        } else {
            //eliminamos el ultimo indice y volvemos a llamar a esta funcion (Backtracking)
            //eliminamos de la suma total el elemento que quitamos de la solucion.
            suma-=soluciones.get(soluciones.size()-1).get(soluciones.get(soluciones.size()-1).size()-1);
            //Eliminamos el ultimo indice
            puntoBack.remove(puntoBack.size()-1);
            //eliminamos de la solucion el ultimo elemento
            soluciones.get(soluciones.size()-1).remove(soluciones.get(soluciones.size()-1).size()-1);
            return sigPrueba(puntoBack,tamSub,soluciones,suma);


        }
    }

    private static void imprimeSol(ArrayList<ArrayList<Integer>> solucion){
        for(int i=0;i<solucion.size();i++){
            System.out.print("Solucion "+(i+1)+":\n");

            for(int j=0;j<solucion.get(i).size();j++){

                System.out.print(solucion.get(i).get(j)+" ");
            }
            System.out.print("\n");
        }
    }

    private static void imprimeBack(ArrayList<Integer> solucion){

        System.out.print("Back\n");
        for(int i=0;i<solucion.size();i++) {


            System.out.print(solucion.get(i) + " ");
        }
            System.out.print("\n");

    }

    private static void escribeFichero(ArrayList<ArrayList<Integer>> solucion) throws IOException{
        FileWriter fichero = new FileWriter("../practicaADA2/solucion.txt");
        PrintWriter pw = new PrintWriter(fichero);

        for(int i=0;i<solucion.size();i++){
            pw.print("Solucion "+(i+1)+":\n");

            for(int j=0;j<solucion.get(i).size();j++){

                pw.print(solucion.get(i).get(j)+" ");
            }
            pw.print("\n");
        }
        fichero.close();
    }
}
