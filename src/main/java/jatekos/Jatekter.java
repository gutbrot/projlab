package jatekos;

import java.util.ArrayList;
import java.util.List;

import jarmu.*;
import terkep.*;

public class Jatekter {
    private final List<Jatekos> jatekosok = new ArrayList<>();
    private final List<Jarmu> jarmuvek = new ArrayList<>();
    private final Terkep terkep;

    public Jatekter(Terkep terkep) {
        this.terkep = terkep;
    }

    public void jatekStart() {
        for (Jatekos j : jatekosok) {
            j.akcioPontKezelo(2);
        }
    }

    public void autoMozgo() {
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto) {
                // TODO: az autók mozgását itt lehet majd bővíteni
            }
        }
    }

    public void jatekosLep() {
        for (Jatekos j : jatekosok) {
            j.korVege();
        }
    }

    public void ujKor() {
        terkep.idojarasFrissites();
        for (Jatekos j : jatekosok) {
            j.akcioPontKezelo(2);
        }
    }

    public void hozzaadJatekos(Jatekos j) { if (j != null) jatekosok.add(j); }
    public void hozzaadJarmu(Jarmu j) { if (j != null) jarmuvek.add(j); }
    public List<Jatekos> getJatekosok() { return jatekosok; }
    public List<Jarmu> getJarmuvek() { return jarmuvek; }
    public Terkep getTerkep() { return terkep; }
}
