CÁLCULO RELEVANCIA

Precondición:
 
	Las palabras de la query están ordenadas de más a menos importancia.

Cómo se ejecuta el cálculo de la relevancia:

	Se crea un documento ficticio que contiene varias repeticiones de las palabras según la importancia que tomen
	en la query (las palabras más importantes están repetidas más veces). Seguidamente se hace un cálculo de similitud
	TF-IDF con todos los documentos de la colección, y se devuelve los k documentos más similares al ficticio.

	Hemos utilizado la similitud TF IDF porque creemos que es la más optima de las que hemos implementado.

JUEGOS DE PRUEBAS:
EXPRESIÓN
=========
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