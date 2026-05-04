package kotrofej;

import terkep.*;
import java.util.List;

/**
 * A HanyoFej (hómaró) nagy teljesítményű fej, amely képes a havat 2 sávval arrébb szórni.
 */
public class HanyoFej extends KotroFej {

    //KONSTRUKTOR
    public HanyoFej(int ar) {
        super(ar);
    }

    //GETTEREK
    @Override
    public String getNev() {
        return "HanyoFej";
    }

    @Override
    public KotroFej getKotroFej() {
        return new HanyoFej(getAr());
    }

    /**
     * A HanyoFej tisztító művelete:
     * - Ha van hó, felszedi azt a sávból.
     * - A felszedett havat 2 sávval arrébb szórja (ha lehetséges).
     * - Nem tudja felszedni a havat, ha jég van a sávban.
     */
    @Override
    public void tisztit(Sav cel, Sav melle, Ut ut) {
        //Null ellenőrzés
        if (cel == null || ut == null) return;
        
        System.out.println(">>> [HÓKOTRÓ AKCIÓ] Hányófej (Hómaró) felpörgetve a(z) " + cel.getSavSzama() + ". sávban...");

        //Jég ellenőrzése
        if (cel.jegesE()) {
            System.out.println("    >>> [KUDARC] A Hányófej nem tudja felszedni a havat, a jég miatt elakadt a maró!");
            return;
        }
        
        int ho = cel.getHo();
        if (ho == 0) {
            System.out.println("    >>> [INFO] Nincs hó, a gép üresjáratban pörög.");
            return;
        }
        
        //Hó felszedése
        cel.setHo(0);
        System.out.println("    >>> [SIKER] " + ho + " cm hó bedarálva a(z) " + cel.getSavSzama() + ". sávból.");
        
        //2 sávval arrébb lévő célpont megkeresése listaindexeléssel
        List<Sav> aktSzakasz = null;
        for (List<Sav> szakasz : ut.getSzakaszok()) {
            if (szakasz.contains(cel)) {
                aktSzakasz = szakasz;
                break;
            }
        }
        
        //Ha megvan a szakasz, kiszámoljuk a cél indexet
        if (aktSzakasz != null) {
            int aktIndex = aktSzakasz.indexOf(cel);
            Sav hobaDobasCelpontja = null;
            
            //Jobbra próbáljuk dobni
            if (aktIndex + 2 < aktSzakasz.size()) {
                hobaDobasCelpontja = aktSzakasz.get(aktIndex + 2);
            } 
            //Vagy balra
            else if (aktIndex - 2 >= 0) {
                hobaDobasCelpontja = aktSzakasz.get(aktIndex - 2);
            }
            
            //Hó szórása a célpontba
            if (hobaDobasCelpontja != null) {
                hobaDobasCelpontja.setHo(hobaDobasCelpontja.getHo() + ho);
                System.out.println("    >>> [INFO] A hó átszórva a 2 sávval arrébb lévő (" + hobaDobasCelpontja.getSavSzama() + ".) sávba.");
            } else {
                System.out.println("    >>> [INFO] A hó kirepült a pálya szélére.");
            }
        }
    }

}