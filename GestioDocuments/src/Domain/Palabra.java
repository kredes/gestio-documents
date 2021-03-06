package Domain;

public class Palabra implements Comparable<Palabra>{
    private String word;



    /* CONSTRUCTORAS */

    /* Constructora de palabra */
    //Pre: -
    //Post: Se crea un objeto Palabra
    public Palabra(String word) {
        this.word = word;
    }


    /* SETTERS */

    /* Modificadora de palabra */
    //Pre: -
    //Post: La palabra existente se substituye por el parametro newword
    public void setPalabra(String newword) {
        word = newword;
    }


    /* GETTERS */

    /* Consultora de palabra */
    //Pre: -
    //Post: Devuelve la palabra
    @Override
    public String toString(){
        return word;
    }

    @Override
    public int compareTo(Palabra o) {
        return word.compareTo(o.word);
    }
}