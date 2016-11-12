package Domain;


import Persistence.ControladorPersistencia;

import java.util.*;
import java.io.*;

public class Collection {
    // TODO decidir si será singleton
    private ArrayList<String> topWords;
    private SortedMap<Integer, Documento> coleccion;
    private SortedMap<Palabra,Double> nDocsPerWord;                //Valor: Número de documentos en los que aparece (Palabra) sobre
                                                                //el total de la colección
    private SortedMap<Palabra, SortedMap<Palabra,Double>> nDocPerWordTag;  //Valor: Número de documentos en los que aparece (Palabra) sobre
    private int nDocs;
    private static Collection instance = null;

    private SortedMap<String, ArrayList<Documento>> indexTitulo;
    private SortedMap<String, ArrayList<Documento>> indexAutor;
    private SortedMap<String, Documento> indexID;
    private Set<String> stopWords;

    private Collection() throws IOException {
        nDocsPerWord = new TreeMap<>();
        nDocPerWordTag = new TreeMap<>();
        coleccion = new TreeMap<>();
        indexTitulo = new TreeMap<>();
        indexAutor = new TreeMap<>();
        indexID = new TreeMap<>();
        ControladorPersistencia c = ControladorPersistencia.getInstance();
        topWords = c.getTopWords();
        stopWords = c.getStopWordsSet();
        nDocs = c.getIdCounter();




        for (String s : topWords){
            Palabra p = new Palabra(s);
            nDocsPerWord.put(p,0d);
        }


        for (int i : ControladorPersistencia.getInstance().getDocIDs()){
            Documento d = c.getDocumento(i);
            for (Palabra e : d.getEtiquetas())
                if (!nDocPerWordTag.containsKey(e)) nDocPerWordTag.put(e,nDocsPerWord);
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

        //EN AQUEST PUNT ELS VECTORS NDocsPerWord i NDocsPerWordTag HAN D'ESTAR MINIMITZATS
        /*nDocsPerWord = minimitzar(nDocsPerWord);
        nDocPerWordTag = minimitzarConj(nDocPerWordTag);*/

        //Actualitzar les Freqs de cada document, un cop estan tots carregats
        for (Map.Entry<Integer,Documento> entry : coleccion.entrySet()){
            entry.getValue().setFreqs(
                    getNDocsPerWord(),
                    getNDocsPerWordTag());
        }


    }


    //los documentos con la Etiqueta solicitada
    private void  addToFreq(Documento d){
        Set<Palabra> palabrasDoc = new HashSet<Palabra>();        //Contiene todas las palabras, sin repetir, de UN documento

        Frase fTitle = d.getTitulo();
        ArrayList<Palabra> palabrasTitle = fTitle.getFrase();

        Contenido con = d.getArticulo();
        ArrayList<Frase> frasesCon = con.getContenido();

        Set<Palabra> etiqsDoc = d.getEtiquetas();
        for (Palabra etiq : etiqsDoc)
        {
            for (int j = 0; j < fTitle.getLength(); ++j){
                palabrasDoc.add(fTitle.getWord(j));
            }

            for (int j = 0; j < frasesCon.size(); ++j){
                for (int y = 0; y < frasesCon.get(j).getLength(); ++y){
                    palabrasDoc.add(frasesCon.get(j).getWord(y));
                }
            }

            for (Palabra x : palabrasDoc){
                if (nDocsPerWord.containsKey(x)) { // palabra aun no añadida
                    nDocPerWordTag.get(etiq).put(x, nDocPerWordTag.get(etiq).get(x) + 1);
                    nDocsPerWord.put(x, nDocsPerWord.get(x) + 1);
                }
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
        ControladorPersistencia.getInstance().guardaDocumento(d, ControladorPersistencia.getInstance().getIdCounter());
        coleccion.put(d.getId(), d);
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
        for (SortedMap.Entry<Integer,Documento> e : coleccion.entrySet()){
            if (e.getValue() == doc) coleccion.remove(e);
        }
    }

    /* GETTERS */
    public SortedMap<Integer,Documento> getCollection() { return coleccion; }
    public SortedMap<Palabra,SortedMap<Palabra,Double>> getNDocsPerWordTag() { return nDocPerWordTag; }
    public SortedMap<Palabra,Double> getNDocsPerWordTag(Palabra p) { return nDocPerWordTag.get(p); }
    public SortedMap<Palabra,Double> getNDocsPerWord() { return nDocsPerWord; }
    public SortedMap<String, ArrayList<Documento>> getIndexTitulo() { return indexTitulo; }
    public SortedMap<String, ArrayList<Documento>> getIndexAutor() { return indexAutor; }
    public SortedMap<String, Documento> getIndexID() { return indexID; }
    public ArrayList<String> getTopWords() { return topWords; }
    public Set<String> getStopWords() { return stopWords; }
    public int getNDocs() {
        return nDocs;
    }

    public ArrayList<String> consultaAutorPrefijo(String s){
        ArrayList<String> ret = new ArrayList<>();
        boolean captat = false;
        for (Map.Entry<String,ArrayList<Documento>> e : indexAutor.entrySet()){
            if (e.getKey().toLowerCase().startsWith(s.toLowerCase())) {
                ret.add(e.getKey());
                captat = true;
            }

            else if (captat) break;
        }

        return ret;
    }


    public int getNumDocumentosEtiquetas(Set<Palabra> etiquetasDoc) throws IOException {
        int N = 0;
        CtrlPersistencia c = ControladorPersistencia.getInstance();
        int max = c.getNumDocumentos();
        for (int i = 0; i < max; ++i){
            for (Palabra x : c.getDocumento(i).getEtiquetas()){
                if (etiquetasDoc.contains(x)) {
                    ++N;
                    break;
                }
            }
        }
        return N;
    }

}
