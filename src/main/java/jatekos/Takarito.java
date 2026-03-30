package jatekos;

import java.util.ArrayList;
import java.util.List;

import eszkoztar.Eszkoztar;
import jarmu.Hokotro;
import kotrofej.*;
import bolt.Bolt;

public class Takarito extends Jatekos {
    private List<Hokotro> iranyitottHokotrok = new ArrayList<>();
    private Eszkoztar eszkoztar = new Eszkoztar();
    private int penz;
    
    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
    }
    
    public Hokotro hokotrotValaszt() {
        return iranyitottHokotrok.isEmpty() ? null : iranyitottHokotrok.get(0);
    }

    public boolean hokotroMozgat(Hokotro h) {
        if (h == null || !iranyitottHokotrok.contains(h) || akcioPont <= 0) return false;
        akcioPont--;
        return true;
    }

    public boolean kotrofejValt(Hokotro h) {
        if (h == null || akcioPont <= 0) return false;
        KotroFej uj = eszkoztar.kiveszFej();
        if (uj == null) return false;
        h.fejcsere(uj);
        akcioPont--;
        return true;
    }

    public boolean vasarol(Bolt bolt, String termekNev) {
        if (bolt == null || akcioPont <= 0) return false;
        boolean siker = bolt.vasarlas(this, termekNev);
        if (siker) akcioPont--;
        return siker;
    }

    public void penztKap(int p){
        this.penz += p;
    }
    
    public void hozzaadHokotro(Hokotro h) {
        if (h != null && !iranyitottHokotrok.contains(h)) iranyitottHokotrok.add(h);
    }
    
    public List<Hokotro> getIranyitottHokotrok() { return iranyitottHokotrok; }
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    public int getPenz() { return penz; }

}
