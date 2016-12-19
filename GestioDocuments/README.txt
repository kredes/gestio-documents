Jordi Mora Pruna, Andrés Insaurralde Borzani, Àlex Serra Vidal y Alberto Gómez Cabrera
-------------------------------------------------------------------------------------

Diagrama de clases y casos de uso se encuentran dentro del directorio "UML y casos de uso"

En el directorio "GestioDocuments" se encuentra la raíz del proyecto:
	- datos: ficheros de los documentos utilizados en el programa.
	- src: Main.java del programa y packages "Domain" y "Persistence"

Para poder ejecutar el fichero .JAR se debe hacer desde "GestioDocuments/" con el comando:
	java -jar GestioDocuments.jar


-------------------------------------------
Atributos duplicados en la clase Documento
-------------------------------------------
 Conceptos duplicados:
 - los de Frase son para hacer busquedas sin stop words ni palabras no top words.
 - los de String para presentarlo bien formateado al visualizar documentos y buscar secuencias de carácteres


------------
SIMILITUDES
-------------
Cálculo de Similitud Freq:
	- Devuelve el número de palabras coincidentes relacionado con la colección (top words) entre el documento1 y el documento2. Si se ejecuta desde el driver de Similitud no ofrece un valor porcentual, si se ejecuta desde el dirver de los casos de uso, se normaliza y ofrece un valor porcentual.

Cálculo de Similitud Coseno:
	- El cálculo de la similitud por coseno se realiza con los vectores normalizados que contienen la frecuencia de las palabras de sus documentos. Se calcula el coseno entre dos vectores para saber la distancia angular (y calcular el parecido) que hay entre ellos.

Cálculo de Similitud TD IDF:
	- El cálculo de la similitud por TF IDF calcula el peso de las palabras en cada documento, en forma de vector. Este peso viene definido por una relación entre el número de documentos que tienen una palabra en común (cuánto menos común es una palabra, más peso tiene) y el número de veces que aparece una palabra en el propio documento. Para comparar estos vectores se aplica la similitud del coseno.


-------------------
CÁLCULO RELEVANCIA
-------------------

Precondición:
 
	Las palabras de la query están ordenadas de más a menos importancia.

Cómo se ejecuta el cálculo de la relevancia:

	Se crea un documento ficticio que contiene varias repeticiones de las palabras según la importancia que tomen
	en la query (las palabras más importantes están repetidas más veces). Seguidamente se hace un cálculo de similitud
	TF-IDF con todos los documentos de la colección, y se devuelve los k documentos más similares al ficticio.

	Hemos utilizado la similitud TF IDF porque creemos que es la más optima de las que hemos implementado.


--------------------
JUEGOS DE PRUEBAS:
-------------------
EXPRESIÓN
----------
!sentencia & {patti dylan} & japonés
Chipre & Christy & Estepona & !móvil
!móvil&((((Barcelona))|Valencia))
a
!!a
   &,
!
a!&|b
aniversario noble & canciones
Barcelona!
a!!
a&b!&c
Chipre &
!&((((Barcelona))|Valencia))
{p1 p2 p3} & ("hola adios" | pepe & !juan
{p1 p2 p3}   & ("hola adios" |pepe )  &     ! juan


RELEVANCIA
==========
Trump Clinton política campaña
Madrid España partido gobierno
Barcelona años
americano temporada pista halloween
puñado puntualiza psicológico proyecta proveedores
extranjeros partido preguntarse samsung
árabes biblioteca verdadera