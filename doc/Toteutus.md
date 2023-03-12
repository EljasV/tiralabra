# Toteutus

Ohjelman runkona on CaveGenetarorin process-metodi. Se pitää sisällään luolastoa tarvittavat tietorakenteet.

| Mikä                    | Vastuu                                                                                                                                                    |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| SpacePartitioning       | Luo puun, jonka lehdet pitää sisällä huoneet. Tämän avulla huoneista saadaan eri kokoisia ja koko kartasta mielenkiintoisempi.                            |
| DoublyConnectedEdgeList | Rakenne, jonka avulla saadaan tietoon vierekkäiset huoneet. Ei ole täydellisesti toteutettu tietorakenne, vaan sen sisältöä lasketaan uudelleen ajoittain |
| Grid                    | Sisältää 2D-taulukon, jossa on tiedot kartan sisällöstä. Esim. onko tietyssä paikassa seinää tai lattiaa                                                  |
| CorridorGenerator       | Vastaa käytävien luomisesta huoneiden välille.                                                                                                            |
| RoomInfo                | Sisältää tietoa huoneesta                                                                                                                                 |
| PerlinNoise             | Sisältää tietorakenteen ja algoritmin Perlin noisen tekemiseen                                                                                            |


Ohjelma voidaan siirtää pienillä muutoksilla peliin.
Ohjelman aikatilavuus on O(leveys*korkeus), koska soluautomaatissa täytyy käydä läpi kaikki mahdolliset kohdat.


## Lähteet:
- Vigier P: Vagabond – Dungeon and Cave Generation – Part 1, internetsivu (https://pvigier.github.io/2019/06/23/vagabond-dungeon-cave-generation.html, viitattu 12.13.2023)
- monia tekijöitä: Cellular Automata Method for Generating Random Cave-Like Levels, internetsivu (http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels, viitattu 12.13.2023)
- monia tekijöitä: Perlin noise, internetsivu (https://en.wikipedia.org/wiki/Perlin_noise, viitattu 12.13.2023)
- monia tekijöitä: Doubly connected edge list, internetsivu (https://en.wikipedia.org/wiki/Doubly_connected_edge_list, viitattu 12.13.2023)



