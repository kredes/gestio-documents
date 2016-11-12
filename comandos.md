```
[] = opcional

documento : <titulo> <autor>
lista : <autor>
prefijo : <prefijo>
parecidos : <titulo> <autor> <k>
bool : <expresion>
relevantes : <query> <k>
```

#### Clase `Comando`
```java
Class Comando {
	public enum Tipo {
    	DOCUMENTO, LISTA, PREFIJO, PARECIDOS, BOOL, RELEVANTES
    }
    ...
}
```

#### Uso
La ejecución de cualquier comando retorna su resultado como un objecto de tipo `Object`. Es responsabilidad del llamador castear al tipo de retorno real del comando ejecutado. Por ejemplo:


```java
Comando c = new Comando("documento : \"Blah blah blah\" \"Jordi Petit\"");
CtrlDominio cd = CtrlDominio.getInstance();
Documento d = (Documento) cd.ejecutar(c);
```

Dentro del controlador de dominio:

```java
public ejecutar(Comando c) {
    switch (c.parse()) {
    	case Comando.Tipo.DOCUMENTO:
        	...
    }
}
```


#### Formato de la documentación
`comando : [<argumento_1> ... <argumento_N>] : tipo_retorno`

---

`documento : <título> <autor> : Documento`. Retorna el documento identificado por *título* y *autor*.

`lista : <autor> : ArrayList<String>`. Retorna los títulos (como strings) de todos los documentos asociados a un autor, en orden alfabético.

`prefijo : <prefijo> : ArrayList<String>`. Retorna todos los autores (como strings) que comienzan por *prefijo*, ordenados alfabéticamente.

`parecidos : <título> <autor> <k> : ArrayList<Documento>`. Retorna los *k* documentos más parecidos al documento identificado por *título* y *autor*, ordenados de mayor a menor parecido.

`expresion : <expresión> : Set<Documento>`. Retorna todos los documentos tales que al menos una de sus frases satisface la expresión booleana *expresión*.

`relevantes : <query> <k> : ArrayList<Documento>`. Retorna los *k* documentos más relevantes para la query *query*, ordenados de mayor a menor relevancia.