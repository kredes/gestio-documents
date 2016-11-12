package Domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Frase {

    // Frase
    private ArrayList<Palabra> palabras;
    private static final String separadores = "\n\t .,;¿?¡!'\"/\\+\\(\\)\\[\\]–&“”‘’…«»º:";


    /* CONSTRUCTORAS */

    /* Constructora de Frase */
    //Pre: Cierto
    //Post: Se crea un objeto Frase
    public Frase() {
        palabras = new ArrayList<Palabra>();
    }

    /* Constructora de Frase */
    //Pre: Cierto
    //Post: Se crea un objeto Frase añadiendo las palabras al vector.
    public Frase(String fraseString) throws IOException {
        palabras = new ArrayList<Palabra>();

        String separadores = "[ .,;?!¡¿\'\"\\[\\]]+";
        String[] pals = fraseString.split(separadores);
        for(int i = 0; i < pals.length; ++i){
            Palabra p = new Palabra(pals[i]);
            if(!stopWord(p.toString())) palabras.add(p);
        }



        /*StringTokenizer st = new StringTokenizer(fraseString, separadores, false);

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if(!stopWord(s)
                    && s.charAt(s.length()-1) != '%'
                    && s.length()>1)
                palabras.add(new Palabra(s));
        }*/
    }

    private boolean stopWord (String w) throws IOException {
        //TODO mover a persistencia, de momento la dejo aquí para que se puedan hacer prueba funcionales

        String stockwords = "a,actualmente,adelante,además,afirmó,agregó,ahora,ahí,al,algo,alguna,algunas,alguno,algunos,algún,alrededor,ambos,ante,anterior,antes,apenas,aproximadamente,aquí,aseguró,así,aunque,ayer,añadió,aún,bajo,bien,buen,buena,buenas,bueno,buenos,cada,casi,cerca,cierto,cinco,comentó,como,con,conocer,considera,consideró,contra,cosas,creo,cual,cuales,cualquier,cuando,cuanto,cuatro,cuenta,cómo,da,dado,dan,dar,de,debe,deben,debido,decir,dejó,del,demás,dentro,desde,después,dice,dicen,dicho,dieron,diferente,diferentes,dijeron,dijo,dio,donde,dos,durante,e,ejemplo,el,ella,ellas,ello,ellos,embargo,en,encuentra,entonces,entre,era,eran,es,esa,esas,ese,eso,esos,esta,estaba,estaban,estamos,estar,estará,estas,este,esto,estos,estoy,estuvo,está,están,ex,existe,existen,explicó,expresó,fin,fue,fuera,fueron,gran,grandes,ha,haber,habrá,había,habían,hace,hacen,hacer,hacerlo,hacia,haciendo,han,hasta,hay,haya,he,hecho,hemos,hicieron,hizo,hoy,hubo,igual,incluso,indicó,informó,junto,la,lado,las,le,les,llegó,lleva,llevar,lo,los,luego,lugar,manera,manifestó,mayor,me,mediante,mejor,mencionó,menos,mi,mientras,misma,mismas,mismo,mismos,momento,mucha,muchas,mucho,muchos,muy,más,nada,nadie,ni,ninguna,ningunas,ninguno,ningunos,ningún,no,nos,nosotras,nosotros,nuestra,nuestras,nuestro,nuestros,nueva,nuevas,nuevo,nuevos,nunca,o,ocho,otra,otras,otro,otros,para,parece,parte,partir,pasada,pasado,pero,pesar,poca,pocas,poco,pocos,podemos,podrá,podrán,podría,podrían,poner,por,porque,posible,primer,primera,primero,primeros,principalmente,propia,propias,propio,propios,próximo,próximos,pudo,pueda,puede,pueden,pues,que,quedó,queremos,quien,quienes,quiere,quién,qué,realizado,realizar,realizó,respecto,se,sea,sean,segunda,segundo,según,seis,ser,será,serán,sería,señaló,si,sido,siempre,siendo,siete,sigue,siguiente,sin,sino,sobre,sola,solamente,solas,solo,solos,son,su,sus,sí,sólo,tal,también,tampoco,tan,tanto,tendrá,tendrán,tenemos,tener,tenga,tengo,tenido,tenía,tercera,tiene,tienen,toda,todas,todavía,todo,todos,total,tras,trata,través,tres,tuvo,un,una,unas,uno,unos,usted,va,vamos,van,varias,varios,veces,ver,vez,y,ya,yo,él,ésta,éstas,éste,éstos,última,últimas,último,últimos";
        String[] sArray = stockwords.split("\\,");
        Set<String> stockArray = new HashSet<>();
        for (int i = 0; i < sArray.length; ++i){
            stockArray.add(sArray[i]);
        }

        return stockArray.contains(w);
    }



    /* SETTERS */

    /* Añadir una palabra a una instancia Frase */
    //Pre: -
    //Post: El parametro implicito queda modificado con una palabra añadida
    public void addWord(Palabra w) {
        palabras.add(w);
    }


    /* GEETERS */

    /* Consultora de una palabra de una frase */
    //Pre: -
    //Post: Se devuelve la palabra i del parametro implicito
    public Palabra getWord(int i){
        return palabras.get(i);
    }

    /* Consultora de las palabras de una Frase */
    //Pre: -
    //Post: Se devuelve la cantidad de palabras que tiene el p.i.
    public int getLength() { return palabras.size(); }

    /* Consultora de la Frase */
    //Pre: -
    //Post: Retorna la frase.
   public ArrayList<Palabra> getFrase() {
       return palabras;
   }


}