package Domain;

import Persistence.ControladorPersistencia;

import java.io.IOException;
import java.text.Collator;
import java.util.*;


public class ControladorDominio implements CtrlDominio {

    private static ControladorDominio instance;

    private ControladorPersistencia ctrlPersistencia;
    private Collection collection;


    /* CONSTRUCTOR */
    public ControladorDominio() throws IOException {
        ctrlPersistencia = ControladorPersistencia.getInstance();
        collection = Collection.getInstance();
    }


    /* GETTERS/SETTERS */
    public static ControladorDominio getInstance() throws IOException {
        if (instance == null) instance = new ControladorDominio();
        return instance;
    }


    /* AUXILIARES */
    private int getPos(ArrayList<String> list, String x, int left, int right) {
        int m = (left + right)/2;
        if (left <= right) {
            Collator collator = Collator.getInstance();
            collator.setStrength(Collator.PRIMARY);

            int comp = collator.compare(list.get(m) , x);
            if (comp < 0) return getPos(list, x, left, m-1);
            else if (comp > 0) return getPos(list, x, m+1, right);
            else return m;
        }
        return -1;
    }

    private int getPosSubstring(ArrayList<String> list, String substring, int left, int right) {
        int m = (left + right)/2;
        if (left <= right) {
            if (substring.regionMatches(true, 0, list.get(m), 0, substring.length())) {
                while (substring.regionMatches(true, 0, list.get(m), 0, substring.length())) {
                    --m;
                }
                return ++m;
            }
            else {
                Collator collator = Collator.getInstance();
                collator.setStrength(Collator.PRIMARY);

                int comp = collator.compare(
                        list.get(m).substring(0, substring.length()-1),
                        substring
                );

                if (comp < 0) return getPosSubstring(list, substring, left, m-1);
                else return getPosSubstring(list, substring, m+1, right);
            }
        }
        return -1;
    }


    /* INTERFACE */
    @Override
    public Documento buscarDocumento(String titulo, String autor) throws DocumentoNoExiste {
        Map<String, ArrayList<Documento>> index = collection.getIndexTitulo();

        ArrayList<Documento> docs = index.get(titulo);

        if (docs == null) throw new DocumentoNoExiste();

        for (Documento d : docs) {
            //if (d.getTituloString().equals(titulo)) return d;
            for (String a : d.getAutoresStrings()) {
                if (autor.equals(a)) return d;
            }
        }

        throw new DocumentoNoExiste();
    }

    @Override
    public ArrayList<String> librosAutor(String autor) throws AutorNoExiste {
        Map<String, ArrayList<Documento>> index = collection.getIndexAutor();

        ArrayList<Documento> docs = index.get(autor);

        if (docs == null) throw new AutorNoExiste();

        ArrayList<String> result = new ArrayList<>();
        for (Documento d : docs) result.add(d.getTituloString());

        return result;
    }

    @Override
    public ArrayList<String> autoresPorPrefijo(String prefijo) {
        Map<String, ArrayList<Documento>> docs = collection.getIndexAutor();

        ArrayList<String> result = new ArrayList<>();
        for (String autor : docs.keySet()) {
            if (autor.regionMatches(true, 0, prefijo, 0, prefijo.length())) {
                result.add(autor);
            }
        }
        return result;
    }

    @Override
    public ArrayList<MyPair<Documento, Double>> buscarParecidos(String titulo, String autor, int k, CalcSimilitud cs) throws DocumentoNoExiste, IOException {
        Map<String, Documento> docs = collection.getIndexID();
        Double maxSimilitud = 0.0;             // Sólo para CalcSimilitudFreq

        Boolean similitudFreq = cs.getClass().equals(CalcSimilitudFreq.class);

        Documento d = buscarDocumento(titulo, autor);
        ArrayList<MyPair<Documento, Double>> result = new ArrayList<>();
        for (Documento doc : docs.values()) {
            Double similitud = cs.calculaSimilitud(doc, d);

            result.add(new MyPair<>(doc, similitud));

            if (similitudFreq && (similitud > maxSimilitud)) {
                maxSimilitud = similitud;
            }
        }

        for (MyPair<Documento, Double> par : result) {
            if (similitudFreq) par.setValue((par.getValue()/maxSimilitud)*100);
            else par.setValue(par.getValue()*100);
        }

        result.sort(new Comparator<MyPair<Documento, Double>>() {
            @Override
            public int compare(MyPair<Documento, Double> o1, MyPair<Documento, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        result = new ArrayList<>(result.subList(1, k+1));    // El primero es él mismo
        return result;
    }

    @Override
    public ArrayList<MyPair<Documento, Double>> buscarParecidos(Documento d, int k, CalcSimilitud cs) throws DocumentoNoExiste, IOException {
        Map<String, Documento> docs = collection.getIndexID();

        ArrayList<MyPair<Documento, Double>> result = new ArrayList<>();
        for (Documento doc : docs.values()) {
            Double similitud = cs.calculaSimilitud(doc, d);

            result.add(new MyPair<>(doc, similitud*100));
        }

        result.sort(new Comparator<MyPair<Documento, Double>>() {
            @Override
            public int compare(MyPair<Documento, Double> o1, MyPair<Documento, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return new ArrayList<>(result.subList(0, k));    // El primero NO es él mismo (es ficiticio y no está en la colección)
    }


    @Override
    public Set<Documento> buscarExpresion(String expresion) throws SyntaxErrorException, IOException {
        return Expresion.validaYEvalua(expresion);
    }

    @Override
    public ArrayList<MyPair<Documento, Double>> buscarRelevantes(String query, int k) throws IOException, DocumentoNoExiste {
        CalcRelevancia cr = new CalcRelevancia();
        Frase f = new Frase(query);

        ArrayList<String> palabras = new ArrayList<>();
        for (Palabra p : f.getFrase()) palabras.add(p.toString());

        ArrayList<MyPair<Documento, Double>> relevantes = cr.consultaRelevantes(palabras, k);

        return relevantes;
    }

    @Override
    public Documento anadirDocumento(String titulo, String autores, String etiqueta, String contenido) throws IOException {
        Documento nuevo = new Documento(
                ctrlPersistencia.nextId(),
                titulo,
                new ArrayList<String>(Arrays.asList(autores.split(","))),
                new ArrayList<String>(Arrays.asList(etiqueta)),
                contenido
        );
        collection.afegirDoc(nuevo);
        return nuevo;
    }

    @Override
    public void modificarDocumento(Documento d) throws IOException {
        collection.modificarDoc(d.getId(), d);
    }

    @Override
    public void eliminarDocumento(Documento d) throws IOException {
        collection.eliminarDoc(d);
    }
}
