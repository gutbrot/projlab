package bolt;

import java.util.Map;
import java.util.HashMap;
import jatekos.Takarito;

/**
 * A játékban található boltot reprezentáló osztály.
 * Itt vásárolhatnak a Takarítók különböző eszközöket és alapanyagokat.
 */
public class Bolt {

    /** * A bolt kínálata: a termék neve (kulcs) és a hozzá tartozó ár (érték). 
     */
    private Map<String, Integer> termekek = new HashMap<>();

    /**
     * Kilistázza a konzolra a bolt aktuális kínálatát és az árakat.
     * A szkeleton fázisban ez segít ellenőrizni a bolt tartalmát.
     */
    public void listaz() {
        
    }

    /**
     * Kezeli a vásárlási folyamatot. Ellenőrzi a termék létezését, 
     * és levonja a Takarító pénzét, ha van rá fedezet.
     * * @param v A vásárlást végző Takarító objektum.
     * @param termek A megvásárolni kívánt termék pontos neve.
     */
    public void vasarlas(Takarito v, String termek) {
        
    }
}