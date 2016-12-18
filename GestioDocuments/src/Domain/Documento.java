package Domain;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.*;

public class Documento {
    private int idDocumento;

    /** Conceptos duplicados:
     *  - los de Frase son para hacer busquedas sin stop words ni palabras no top words.
     *  - los de String para presentarlo bien formateado al visualizar documentos y buscar secuencias de carácteres
     */
    private Frase titulo;
    private String tituloString;

    private ArrayList<Frase> autores; // autors.get(i): un autor
    private ArrayList<String> autoresString;

    private Contenido content;
    private String contentString;

    private Set<Palabra> etiquetas;
    private ArrayList<String> etiquetasString;

    private SortedMap<Palabra,Double> termFreq;          //Map amb totes les paraules que apareixen per la COLECCIÓ
                                                    //i el nombre de cops que apareixen les paraules en aquest Doc.

/* CONSTRUCTORA */

    /* Constructora de Documento */
    //Pre: Cierto
    //Post: Se crea un objeto Documento

    public Documento(@NotNull int idDocumento, @NotNull String titulo, @NotNull ArrayList<String> autores, @NotNull ArrayList<String> etiquetas, @NotNull String articulo) throws IOException {
        termFreq = new TreeMap<>();

        this.idDocumento = idDocumento;
        this.titulo = new Frase(titulo);
        this.tituloString = titulo;
        this.autores = new ArrayList<>();
        for (String autor : autores) {
            Frase aut = new Frase(autor);
            this.autores.add(aut);
        }
        this.autoresString = autores;
        this.etiquetas = new HashSet<>();
        for (String etiqueta : etiquetas) {
            Palabra etiq = new Palabra(etiqueta);
            this.etiquetas.add(etiq);
        }
        this.etiquetasString = etiquetas;
        this.content = new Contenido(articulo);
        this.contentString = articulo;
    }

    /* SETTERS */


    public void setTitulo (String titulo) throws IOException {
        this.titulo = new Frase(titulo);
        this.tituloString = titulo;
    }
    public void setContent (String content) throws IOException {
        this.content = new Contenido(content);
        this.contentString = content;
    }
    public void setEtiquetas(ArrayList<String> etiquetas){
        Set<Palabra> et = new HashSet<>();
        for (String etiqueta : etiquetas) {
            Palabra p = new Palabra(etiqueta);
            et.add(p);
        }
        this.etiquetas = et;
        this.etiquetasString = etiquetas;
    }
    public void setAutores(ArrayList<String> autores) throws IOException {
        ArrayList<Frase> aut = new ArrayList<>();
        for (String autor : autores) {
            Frase f = new Frase(autor);
            aut.add(f);
        }
        this.autores = aut;
        this.autoresString = autores;
    }

    public void setFreqs (SortedMap<Palabra,Double> WordsCol){
        for (Map.Entry<Palabra,Double> entry : WordsCol.entrySet()){
            termFreq.put(entry.getKey(),0.0);
        }

        Frase fTitle = this.getTitulo();
        for (int i = 0; i < fTitle.getLength(); ++i)
        {
            if (termFreq.containsKey(fTitle.getWord(i)))
                termFreq.put(fTitle.getWord(i),termFreq.get(fTitle.getWord(i))+1);
        }

        Contenido c = this.getArticulo();
        ArrayList<Frase> fContent = c.getContenido();
        for (Frase aFContent : fContent) {
            for (int y = 0; y < aFContent.getLength(); ++y) {
                if (termFreq.containsKey(aFContent.getWord(y)))
                    termFreq.put(aFContent.getWord(y), termFreq.get(aFContent.getWord(y)) + 1);
            }
        }
    }


    /* GETTERS */

    public int getId() { return idDocumento; }
    public Frase getTitulo() {
        return titulo;
    }
    public ArrayList<Frase> getAutores() {
        return autores;
    }
    public Contenido getArticulo() {
        return content;
    }
    public SortedMap<Palabra,Double> getTermFreq() { return termFreq; }

    public String getTituloString() {
        return tituloString;
    }

    public ArrayList<String> getAutoresStrings() {
        return autoresString;
    }

    public ArrayList<String> getEtiquetasStrings () {
        return etiquetasString;
    }

    public String getArticuloString() {
        return contentString;
    }

    public double getNumeroPalabras() {
        double count = 0d;

        count += titulo.getLength();
        for (Frase f : content.getContenido()) {
            count += f.getLength();
        }

        return count;
    }
}

