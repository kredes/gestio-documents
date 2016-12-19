package Domain;


import Persistence.ControladorPersistencia;

import java.util.*;
import java.io.*;

public class Collection {
    private SortedMap<Integer, Documento> coleccion;
    private SortedMap<Palabra,Double> nDocsPerWord;                //Valor: Número de documentos en los que aparece (Palabra) sobre
                                                                //el total de la colección
    private int nDocs = ControladorPersistencia.getInstance().getNumDocumentos();
    private static Collection instance = null;

    private SortedMap<String, ArrayList<Documento>> indexTitulo;
    private SortedMap<String, ArrayList<Documento>> indexAutor;
    private SortedMap<String, Documento> indexID;

    private Collection() throws IOException {
        nDocsPerWord = new TreeMap<>();
        coleccion = new TreeMap<>();
        indexTitulo = new TreeMap<>();
        indexAutor = new TreeMap<>();
        indexID = new TreeMap<>();
        ControladorPersistencia c = ControladorPersistencia.getInstance();
        ArrayList<String> topWords = c.getTopWords();


        for (String s : topWords){
            Palabra p = new Palabra(s);
            nDocsPerWord.put(p,0d);
        }


        for (int i : ControladorPersistencia.getInstance().getDocIDs()){
            Documento d = c.getDocumento(i);

            addToFreq(d);

            coleccion.put(i, d);

            //Actualizar indexTitulo
            if (!indexTitulo.containsKey(d.getTituloString())){
                ArrayList<Documento> arrayd = new ArrayList<>();
                arrayd.add(d);
                indexTitulo.put(d.getTituloString(),arrayd);
            }
            else{
                indexTitulo.get(d.getTituloString()).add(d);
            }


            //Actualizar indexAutor
            for (String s : d.getAutoresStrings()){
                ArrayList<Documento> ad2 = indexAutor.get(s);
                if (ad2 == null){
                    ArrayList<Documento> arrayd = new ArrayList<>();
                    arrayd.add(d);
                    indexAutor.put(s, arrayd);
                }

                else{
                    ad2.add(d);
                    indexAutor.put(s, ad2);
                }
            }


            //Actualizar indexID
            indexID.put(String.valueOf(i), d);
        }

        //Actualitzar les Freqs de cada document, un cop estan tots carregats
        for (Map.Entry<Integer,Documento> entry : coleccion.entrySet()){
            entry.getValue().setFreqs( getNDocsPerWord() );
        }


    }

    private void  addToFreq(Documento d){
        Set<Palabra> palabrasDoc = new HashSet<>();        //Contiene todas las palabras, sin repetir, de UN documento

        Frase fTitle = d.getTitulo();

        Contenido con = d.getArticulo();
        ArrayList<Frase> frasesCon = con.getContenido();

        for (int j = 0; j < fTitle.getLength(); ++j){
            palabrasDoc.add(fTitle.getWord(j));
        }

        for (Frase aFrasesCon : frasesCon) {
            for (int y = 0; y < aFrasesCon.getLength(); ++y) {
                palabrasDoc.add(aFrasesCon.getWord(y));
            }
        }

        for (Palabra x : palabrasDoc){
            if (nDocsPerWord.containsKey(x)) { // palabra aun no añadida
                nDocsPerWord.put(x, nDocsPerWord.get(x) + 1);
            }
        }
    }


    public static Collection getInstance() throws IOException {
        if (instance == null) {
            instance = new Collection();
        }
        return instance;
    }


    public void afegirDoc(Documento d) throws IOException
    {
        // TODO: Comprobar que actualiza bien los indexs
        ControladorPersistencia.getInstance().guardaDocumento(d, ControladorPersistencia.getInstance().getIdCounter());
        coleccion.put(d.getId(), d);

        // Actualizar indexAutor
        for (String autor : d.getAutoresStrings()) {
            ArrayList<Documento> docsAutor = indexAutor.get(autor);
            if (docsAutor != null) docsAutor.add(d);
            else {
                docsAutor = new ArrayList<>();
                docsAutor.add(d);
                indexAutor.put(autor, docsAutor);
            }
        }
        // Actualizar indexTitulo
        ArrayList<Documento> docsTitulo = indexTitulo.get(d.getTituloString());
        if (docsTitulo != null) docsTitulo.add(d);
        else {
            docsTitulo = new ArrayList<>();
            docsTitulo.add(d);
            indexTitulo.put(d.getTituloString(), docsTitulo);
        }
        // Actualizar indexID
        indexID.put(String.valueOf(d.getId()), d);
    }

    //Pre: IdModificado tiene que ser el ID de un Doc existente que se quiere modificar
    public void modificarDoc(Integer IdModificado, Documento docNuevo) throws IOException{
        Documento d = ControladorPersistencia.getInstance().getDocumento(IdModificado);
        d.setTitulo(docNuevo.getTituloString());
        d.setAutores(docNuevo.getAutoresStrings());
        d.setContent(docNuevo.getArticuloString());
        d.setEtiquetas(docNuevo.getEtiquetasStrings());
        ControladorPersistencia.getInstance().sobreescribirDocumento(d);
    }

    //Pre: cert
    public void eliminarDoc(Documento doc) throws IOException{
        ControladorPersistencia.getInstance().eliminarDocumento(doc);
        coleccion.remove(doc.getId());
    }

    /* GETTERS */
    public SortedMap<Integer,Documento> getCollection() { return coleccion; }
    public SortedMap<Palabra,Double> getNDocsPerWord() { return nDocsPerWord; }
    public SortedMap<String, ArrayList<Documento>> getIndexTitulo() { return indexTitulo; }
    public SortedMap<String, ArrayList<Documento>> getIndexAutor() { return indexAutor; }
    public SortedMap<String, Documento> getIndexID() { return indexID; }
    public int getNDocs() {
        return nDocs;
    }

    public Set<Documento> queryContainsWordOrSequence(String s) {
        Set<Documento> ret = new HashSet<>();
        for (Documento d : coleccion.values()){
            if (d.getTituloString().toLowerCase().contains(s.toLowerCase()))
                ret.add(d);

            else if (d.getArticuloString().toLowerCase().contains(s.toLowerCase()))
                ret.add(d);
        }
        return ret;
    }


    public Set<Documento> queryContainsWordSet(Set<String> words2Query) {
        Set<Documento> ret = new HashSet<>();
        for (Documento d : coleccion.values()){
            boolean b = true;
            for (String s : words2Query){
                if (!d.getTituloString().toLowerCase().contains(s.toLowerCase()) && !d.getArticuloString().toLowerCase().contains(s.toLowerCase())) {
                    b = false;
                    break;
                }
            }

            if (b) ret.add(d);
        }
        return ret;
    }

    public Set<Documento> queryAllDocs() {
        Set<Documento> ret = new HashSet<>();
        for (Documento d : coleccion.values()){
            ret.add(d);
        }
        return ret;
    }
}
