package jarmu;

import kotrofej.KotroFej;
import terkep.*;

/*
*A játékos által irányított jármű fajta, aminek a különböző útviszonyokon való közlekedését 
*az aktuálisan felszerelt kotró fej befolyásolja. Célja, hogy letakarítsa a buszok elől 
*az akadályozó tényezőket (hó, jég, feltört jég) és ezzel segítse a buszok közlekedését.
*/
public class Hokotro extends Jarmu{
    private KotroFej felszereltFej;
    
    @Override
    public void mozgas(){ //Ennek a függvénynek a segítével fogja tudni a Takarító a járművet mozgatni
    }

    @Override
    public void utkozos(){ //Ez a függvény fogja kezelni a járművek ütközését
    }
}
