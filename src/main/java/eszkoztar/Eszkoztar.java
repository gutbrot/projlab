package eszkoztar;

import java.util.List;
import kotrofej.KotroFej;

public class Eszkoztar {
    private int soKeszlet; //Az a só mennyiség amivel a Takarító rendelkezik
    private int biokerozinKeszlet; //Az a biokerozin mennyiség amivel a Takarító rendelkezik
    private List<KotroFej> kotroFejek; //Az a lista amiben a Takarító rendelkezésére álló kotrófejek vannak tárolva

    public boolean levon(String tipus, int mennyiseg){ //Ez a függvény fogja kezelni, hogy a Takarító elhasznál egy bizonyos mennyiségű eszközt
        return false;
    }
    public void hozzaad(String tipus, int mennyiseg){ //Ez a függvény fogja kezelni, hogy a Takarító új eszközt kapjon vagy új adag fogyóanyagot kapjon

    }
    public boolean vanE(String tipus, int mennyiseg){ //Ez a függvény fogja kezelni, hogy a Takarító rendelkezésére áll-e egy bizonyos eszköz vagy egy bizonyos mennyiségű eszköz/fogyóanyag
        return false;
    }
}
