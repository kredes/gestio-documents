package Domain;

import java.io.IOException;
import java.util.ArrayList;

public class Contenido {
    private ArrayList<Frase> frases;


    /* CONSTRUCTORA */

    /* Constructora de Contenido */
    //Pre: Cierto
    //Post: Se crea un objeto Contenido añadiendo las frases al vector
    public Contenido(String contenido) throws IOException {
        frases = new ArrayList<Frase>();
        //String separadores = "[\\.\\!\\?\\...\\).]";
        String[] fra = contenido.split("(?<=[a-z])\\.\\s+");
        for(int i = 0; i < fra.length; ++i){
            Frase f = new Frase(fra[i]);
            frases.add(f);
        }
    }



    /* SETTERS */

    /* Añadir una frase a una instancia Contenido */
    //Pre: El objeto Contenido existe
    //Post: El parametro implicito queda modificado con una frase añadida
    public void addFrase(Frase f) {
        frases.add(f);
    }


    /* GETTERS */

    /* Consultora de una frase de una contenido */
    //Pre: El objeto Contenido existe
    //Post: Se devuelve la frase i del parametro implicito
    public Frase getFrase(int i){
        return frases.get(i);
    }

    /* Consultora de las frases de un Contenido */
    //Pre: El objeto Contenido existe
    //Post: Se devuelve la cantidad de frases que tiene el p.i.
    public int getFrasesLength() { return frases.size(); }

    /* Consultora de las palabras de un Contenido */
    //Pre: El objeto Contenido existe
    //Post: Se devuelve la cantidad de palabras que tiene el p.i.
    public int getWordsLength() {
        int pal = 0;
        for (int i = 0; i < getFrasesLength(); ++i)
        {
            pal += frases.get(i).getLength();
        }
        return pal;
    }

    /* Consultora del Contenido */
    //Pre: El objeto Contenido existe
    //Post: Retorna el Contenido por pantalla.
    public ArrayList<Frase> getContenido() {
        return frases;
    }



}

