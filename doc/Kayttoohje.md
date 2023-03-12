

Ohjelma suoritetaan suoraan lähdekoodista projektin juurihakemistossa komennolla

``mvn compile exec:java -Dexec.mainClass=veijalainen.eljas.tiralabra.Main``


Ohjelma voidaan suorittaa myös .jar- tiedostona.

.jar-tiedosto muodostetaan komennolla

``mvn package``

Tiedosto _tiralabra-1.0-SNAPSHOT.jar_ ilmestyy target-kansioon

.jar-tiedosto voidaan suorittaa komennolla

``java -jar tiralabra-1.0-SNAPSHOT.jar``


Ohjelma kysyy kartan korkeutta, leveyttä ja huoneiden minimimäärää. Tiedot suoritetaan kirjoittamalla luku konsoliin ja painamalla enter aina kunkin tarvitun tiedon kohdalla.
Ohjelma luo tiedoston o.png, joka sisältää luolaston.