package jatekos;

import java.util.List;

import eszkoztar.Eszkoztar;
import jarmu.Hokotro;
import kotrofej.*;
import bolt.Bolt;

public class Takarito extends Jatekos {
    private List<Hokotro> iranyitottHokotrok; //Az a lista amiben a Takarító által irányított hokotrók vannak tárolva
    private Eszkoztar eszkoztar; //Az a tárgy amiben a Takarító eszközei és fogyóanyagai vannak tárolva
    private int penz; //Az a pénz mennyiség amivel a Takarító rendelkezik

    public Hokotro hokotroValaszt(){ //Ez a függvény fogja kezelni, hogy a Takarító melyik hokotróval szeretne mozogni
        return null;
    }

    public void hokotroMozgat(Hokotro hokotro){ //Ennek a függvénynek a segítével fogja tudni a Takarító a hokotróját mozgatni

    }

    public KotroFej kotroFejValaszt(){ //Ez a függvény fogja kezelni, hogy a Takarító melyik kotrófejet szeretné felszereni a hokotrójára
        return null;
    }

    public void vasarol(Bolt bolt, String termekNev){ //Ez a függvény fogja kezelni, hogy a Takarító vásárolni szeretne-e valamit a boltban

    }
    public void peztKap(int p){ //Ez a függvény fogja kezelni, hogy a Takarító pénzt kapjon a takarításért cserébe
        this.penz += p;
    }

}
