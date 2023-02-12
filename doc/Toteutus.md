# Toteutus

Ohjelman runkona on CaveGenetarorin process-metodi. Se pitää sisällään luolastoa tarvittavat tietorakenteet.

| Mikä                    | Vastuu                                                                                                                                                    |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| SpacePartitioning       | Luo puun, jonka lehdet pitää sisällä huoneet. Tämän avulla huoneista saadaan eri kokoisia ja koko kartasta mielenkiintoisempi.                            |
| DoublyConnectedEdgeList | Rakenne, jonka avulla saadaan tietoon vierekkäiset huoneet. Ei ole täydellisesti toteutettu tietorakenne, vaan sen sisältöä lasketaan uudelleen ajoittain |
| Grid                    | Sisältää 2D-taulukon, jossa on tiedot kartan sisällöstä. Esim. onko tietyssä paikassa seinää tai lattiaa                                                  |
| CorridorGenerator       | Vastaa käytävien luomisesta huoneiden välille.                                                                                                            |
| RoomInfo                | Sisältää tietoa huoneesta                                                                                                                                 |

Yritän tehdä luolaston generointia tämän pohjalta:
https://pvigier.github.io/2019/06/23/vagabond-dungeon-cave-generation.html
