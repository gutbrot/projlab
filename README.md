# Zúzmaraváros / Frost City
**BMEVIIIAB06 2026 Szoftver projekt laboratórium / Software Project Lab**

## Felhasznált technológiák
- Java (code)
- XML (input / output format)
- Word (documentation)
- GUI (Swing)

## Csapattagok / Team members (wiwiwi)
* Alpár Dóra
* Gutbrod Ádám
* Sára István
* Szücs Kristóf
* Töltősi Máté

**Tanszék hivatalos oldala:** [BME IIT](https://www.iit.bme.hu/)
(Department of Control Engineering and Information Technology)

## Rövid leírás / Description

A program Zúzmaraváros téli közlekedését modellezi és szimulálja egy körökre osztott rendszerben:
* **Autók (NPC):** A város dolgozói autóikkal automatikusan ingáznak az otthonaik és a munkahelyeik között. Mozgásukat az útviszonyok (hóvastagság, jég, balesetek) közvetlenül befolyásolják.
* **Takarító (Játékos):** A takarító játékos feladata az úthálózat tisztántartása, amiért fizetséget kap. A megszerzett pénzt a boltban új, cserélhető kotrófejekre (pl. jégtörő, sárkány) és fogyóanyagokra (só, biokerozin) költheti el, hogy hatékonyabban tudja elhárítani a téli akadályokat.
* **Buszsofőr (Játékos):** A buszsofőr játékos a kijelölt megállók és végállomások között navigálja a buszt. A célja, hogy a zord időjárás ellenére a lehető legtöbb sikeres fordulót teljesítse, amivel pontokat szerez.

A program egy **körökre osztott stratégiai szimuláció**, ahol minden játékos meghatározott számú akcióponttal gazdálkodik körönként. Ha mindenki elhasználta a pontjait, új kör kezdődik, az időjárás pedig dinamikusan változtatja (pl. havazás útján) a pálya állapotát.

---

The program models and simulates the winter traffic and logistics of Frost City in a turn-based system:
* **Cars (NPC):** The city's workers automatically commute between their homes and workplaces using their cars. Their movement is directly affected by current road conditions (snow depth, ice, or accidents).
* **Cleaner (Player):** The cleaner player is responsible for maintaining and clearing the road network, for which they receive monetary rewards. The earned money can be spent in the shop on various interchangeable plow heads (e.g., icebreaker, dragon) and consumables (salt, biokerosene) to clear winter obstacles more efficiently.
* **Bus Driver (Player):** The bus driver player navigates a bus between designated stops and terminals. Their goal is to complete as many successful rounds as possible despite the harsh weather conditions to score points.

The program is a **turn-based strategic simulation** where each player manages a limited number of action points per turn. Once all players have exhausted their points, a new round begins, and environmental factors (such as snowfall) dynamically alter the conditions of the map.
