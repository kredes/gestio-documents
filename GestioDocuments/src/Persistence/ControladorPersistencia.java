package Persistence;

import Domain.CtrlPersistencia;
import Domain.Documento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.*;

import java.util.ArrayList;
import java.util.List;


public class ControladorPersistencia implements CtrlPersistencia {

    private static final Path FOLDER = Paths.get(System.getProperty("user.dir"), "datos/docs");
    private static final Path META_FOLDER = Paths.get(System.getProperty("user.dir"), "datos/docs/meta");
    private static final String SEPARATOR = "~~~";
    private static final StandardOpenOption[] OVERWRITE = {StandardOpenOption.WRITE, StandardOpenOption.CREATE};
    private static final StandardOpenOption[] APPEND = {StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.CREATE};

    private static int idCounter;
    private static ArrayList<Integer> docIDs;
    private static ControladorPersistencia instance;

    /* GETTERS */
    public int getIdCounter() {return idCounter;}
    public List<Integer> getDocIDs() {return docIDs;}
    public Path getFolder() {return FOLDER;};
    public Path getMetaFolder() {return META_FOLDER;}



    private ControladorPersistencia() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(META_FOLDER.toString(), "highest_id"))) {
            idCounter = Integer.valueOf(reader.readLine()) + 1;
            docIDs = getAllIDs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ControladorPersistencia getInstance() {
        if (instance == null) instance = new ControladorPersistencia();
        return instance;
    }




    /* AUX */
    private ArrayList<Integer> getAllIDs() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(META_FOLDER.toString(), "ids"))) {
            String aux;
            ArrayList<Integer> ids = new ArrayList<>();
            while ((aux = reader.readLine()) != null) {
                ids.add(Integer.valueOf(aux));
            }
            return ids;
        }
    }

    private void updateMeta(boolean rewriteAllIDs) throws IOException {
        StandardOpenOption[] options = rewriteAllIDs ? OVERWRITE : APPEND;

        if (rewriteAllIDs) Files.delete(Paths.get(META_FOLDER.toString(), "ids"));

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(META_FOLDER.toString(), "ids"), options)) {
            if (rewriteAllIDs) {
                for (Integer id : docIDs) writer.write(String.valueOf(id) + "\n");
            } else {
                writer.write(String.valueOf(docIDs.get(docIDs.size() - 1)) + "\n");
            }
        }

        Files.delete(Paths.get(META_FOLDER.toString(), "highest_id"));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(META_FOLDER.toString(), "highest_id"), OVERWRITE)) {
            writer.write(String.valueOf(idCounter));
        }
    }


    @Override
    public ArrayList<String> getTopWords() throws IOException {
        ArrayList<String> topWords = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(META_FOLDER.toString(), "topwords"))) {
            String line = reader.readLine();
            while (line != null) {
                if (!line.trim().isEmpty())
                    topWords.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topWords;
    }

    /* INTERFACE */
    @Override
    public ArrayList<Documento> getAllDocumentos() throws IOException {
        ArrayList<Documento> docs = new ArrayList<>();
        for (Integer id : docIDs) {
            docs.add(getDocumento(id));
        }
        return docs;
    }

    @Override
    public int getNumDocumentos() throws IOException {
        return docIDs.size();
    }

    @Override
    public void setNumDocumentos(int n) throws IOException {}

    @Override
    public Documento getDocumento(int id) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(FOLDER.toString(), String.valueOf(id)))) {
            String titulo = reader.readLine();

            reader.readLine(); // Descartar separador

            ArrayList<String> autores = new ArrayList<>();
            String aux = new String();

            while(!(aux = reader.readLine()).equals(SEPARATOR)) { // Descartar separador
                autores.add(aux);
            }

            ArrayList<String> etiquetas = new ArrayList<>();
            etiquetas.add(reader.readLine());

            reader.readLine(); // Descartar separador

            StringBuilder contenido = new StringBuilder();
            while ((aux = reader.readLine()) != null) {
                contenido.append(aux + "\n");
            }
            return new Documento(id, titulo, autores, etiquetas, contenido.toString());
        }
    }

    @Override
    /* Guarda el documento en un archivo con su id (idCounter) como nombre. Si ya existe, lo sobreescribe. */
    public void guardaDocumento(Documento d, int id) throws IOException {
        escribirDocumento(d, idCounter);
        docIDs.add(idCounter);
        updateMeta(false);
        idCounter++;
    }

    private void escribirDocumento(Documento d, int id) throws IOException {
        try (BufferedWriter writer =  Files.newBufferedWriter(Paths.get(FOLDER.toString(), String.valueOf(id)), OVERWRITE)) {
            writer.write(d.getTituloString() + "\n");
            writer.write(SEPARATOR + "\n");

            ArrayList<String> autores = d.getAutoresStrings();
            for (String autor : autores) writer.write(autor + "\n");

            writer.write(SEPARATOR  + "\n");
            writer.write(d.getEtiquetasStrings().get(0) + "\n");

            writer.write(SEPARATOR + "\n");
            writer.write(d.getArticuloString());
        }
    }
    
    @Override
    public Set<String> getStopWordsSet() {
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(META_FOLDER.toString(), "stopwords"))) {
            String line = reader.readLine();
            while (line != null) {
                if (!line.trim().isEmpty())
                    stopWords.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    public void eliminarDocumento(Documento d) throws IOException {
        Integer id = d.getId();

        docIDs.remove(id);
        Files.delete(Paths.get(FOLDER.toString(), String.valueOf(id)));
        updateMeta(true);
    }

    public void sobreescribirDocumento(Documento d) throws IOException {
        Files.delete(Paths.get(FOLDER.toString(), String.valueOf(d.getId())));
        escribirDocumento(d, d.getId());
    }

    public int nextId() {
        int aux = idCounter;
        return aux;
    }
}
