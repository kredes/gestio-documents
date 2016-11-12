package Domain;

public class CalculadorRelevancia /* implements... */ {
    //Dadas p palabras (colectivamente denominadas query) y un entero k, obtener los k documentos más relevantes para
    //dicha query

    /*
    private static CalcRelevancia instance;

    public static CalcRelevancia getInstance() {
        if (instance == null) instance = new CalculadorRelevancia();
        return instance;
    }


    //En la key del MyPair se guarda la relevancia y en el valor se guarda el identificador del documento
    private AbstractQueue<MyPair> DocsRellevants;

    //Retorna un valor enter que defineix la rellevancia d'un document donada una query
    //HEM DE CREAR UN CONVENI
    @Override
    public int calculaRelevancia(Documento doc, Expresion query) {
        return -1; //OMPLIR
    }


    /* CONSTRUCTORA */
    //Pre: Cierto
    //Post: S'omple la cua amb els k documents més rellevants per una query


    /* Esto no va en el constructor ni es responsabilidad de la clase.
    * Sólo tiene que calcular la relevancia de un documento respecto a la query
    * (y a partir de una instancia de Documento, no de una ID). */


    /*
    public CalculadorRelevancia(Expresion query, int k) {
        int rell;
        int numDocumentos = 0; //NO EM DEIXA FER CtrlPersistencia.getNumDocumentos();
        for (int idDoc = 0; idDoc < numDocumentos; ++idDoc) {
            rell = rellDoc(idDoc, query);
            MyPair par = new MyPair(rell, idDoc);
            DocsRellevants.add(par);
        }
    }

    */
}