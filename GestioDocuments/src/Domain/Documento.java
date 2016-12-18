package Domain;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.*;

public class Documento {
    private int idDocumento;

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
    public Set<Palabra> getEtiquetas() {
        return etiquetas;
    }
    public Contenido getArticulo() {
        return content;
    }
    public SortedMap<Palabra,Double> getTermFreq() { return termFreq; }
    //public SortedMap<Palabra,Double> getTermFreqConj() { return termFreqConj; }

    /*
    public String getTituloStringSimplificado() {
        ArrayList<String> titulo_ret = new ArrayList<String>();
        for (int i = 0; i < titulo.getLength(); ++i) {
           titulo_ret.add(titulo.getFrase().get(i).toString());
        }
        String tit = String.join(" ", titulo_ret);
        return tit;
    }*/

    public String getTituloString() {
        return tituloString;
    }

    /*
    public ArrayList<String> getAutoresStringsSimplificado() {
        ArrayList<String> autoresString = new ArrayList<String>();
        for (int i = 0; i < autores.size(); ++i){
            String aut = "";
            for (int j = 0; j < autores.get(i).getLength(); ++j){
                aut += autores.get(i).getFrase().get(j).toString();
                if (j < autores.get(i).getLength()-1) aut += " ";
            }
            autoresString.add(aut);
        }
        return autoresString;
    }*/

    public ArrayList<String> getAutoresStrings() {
        return autoresString;
    }

    /*
    public ArrayList<String> getEtiquetasStringsSimplificado () {
        ArrayList<String> etiquetasString = new ArrayList<String>();
        for (Palabra et : etiquetas){
            etiquetasString.add(et.toString());
        }
        return etiquetasString;
    }*/

    public ArrayList<String> getEtiquetasStrings () {
        return etiquetasString;
    }

    /*
    public String getArticuloStringSimplificado() {
        String fr = "";
        for (int i = 0; i < content.getContenido().size(); ++i){
            for (int j = 0; j < content.getContenido().get(i).getLength(); ++j){
                fr += content.getContenido().get(i).getFrase().get(j).toString() + " ";
            }
        }
        return fr;
    }*/

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

