# Testaus

## Yksikkötestaus
Tällä hetkellä yksikkötesteillä testattuna on muutama tietorakenteen perustapaus

Yksikkötestauksen testikattavuus
![](media/Testikattavuus.png)

## Suorituskykytestaus
Muun koodin joukossa on PerformanceTest-luokka, jossa on main-metodi. Se mittaa aikaa 1 luolaston generaatioon. Sain omalla koneella tulokseksi

``1 generointia kesti 1419ms``

Nyt suorituskyky eroaa edellisten viikkojen suorituskyvystä, kun soluautomaatti on käytössä.
Soluautomaatin takia aikatilaavuus on O(leveys*korkeus). 
Nykyään pysäytetään, kun kartta ei enää muutu.

![](media/Profilointi.png)


## Muu testaus
Seuraava kuva on melkein valmis algoritmi. Kuva on generoitu oletussyötteillä ja se näyttää halutulta.


![](media/o.png)

Kuvassa on kasvustolla päällystettyjä ja päällystämättömiä huoneita