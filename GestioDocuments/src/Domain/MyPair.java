package Domain;

public class MyPair<K, V>
{

    /* HECHA GENÉRICA PARA PODER REUTILIZARLA */

    //ESTA CLASE SE USA PARA CALCULADORRELEVANCIA Y CALCULADORSIMILITUD
    private K key;      //key = rellevancia / similitud
    private V value;    //value = idDoc

    public MyPair() {}

    public MyPair(K aKey, V aValue)
    {
        key   = aKey;
        value = aValue;
    }

    public K getKey()   { return key; }
    public V getValue() { return value; }

    public void setKey(K key) {
        this.key = key;
    }
    public void setValue(V value) {
        this.value = value;
    }

}