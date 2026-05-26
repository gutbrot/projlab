package seged;

import terkep.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Navigacio {

    /**
     * BFS-alapú legrövidebb út keresés. Visszaadja a következő sávot, amelyre
     * az autónak lépnie kell, hogy a legrövidebb úton eljusson a célhoz.
     */
    public Sav kovetkezoLepes(Lokacio jelenlegi, Lokacio cel) {
        if (jelenlegi == null || cel == null) return null;

        Sav celSav = cel.getSav();
        Sav kiindulo = jelenlegi.getSav();

        if (kiindulo == celSav) return null;

        Map<Sav, Sav> szulo = new HashMap<>();
        Queue<Lokacio> sor = new LinkedList<>();

        szulo.put(kiindulo, null);
        sor.add(jelenlegi);

        while (!sor.isEmpty()) {
            Sav talalt = bfsLepes(sor.poll(), celSav, szulo, sor);
            if (talalt != null) return elsoLepes(kiindulo, talalt, szulo);
        }

        return null;
    }

    // Egyetlen BFS iteráció: feldolgoz egy lokációt, visszaad célt ha megtalálta
    private Sav bfsLepes(Lokacio aktualis, Sav celSav, Map<Sav, Sav> szulo, Queue<Lokacio> sor) {
        for (Lokacio kovLok : getSzomszedok(aktualis)) {
            Sav kovSav = kovLok.getSav();
            if (!szulo.containsKey(kovSav)) {
                szulo.put(kovSav, aktualis.getSav());
                if (kovSav == celSav) return kovSav;
                sor.add(kovLok);
            }
        }
        return null;
    }

    // Visszakeresi az útvonal legelső lépésének sávját (kiindulótól cel felé)
    private Sav elsoLepes(Sav kiindulo, Sav cel, Map<Sav, Sav> szulo) {
        Sav aktualis = cel;
        while (true) {
            Sav szuloSav = szulo.get(aktualis);
            if (szuloSav == kiindulo) return aktualis;
            if (szuloSav == null) return null;
            aktualis = szuloSav;
        }
    }

    // Az adott lokációból közlekedési szabályok szerint elérhető szomszédos lokációk
    private ArrayList<Lokacio> getSzomszedok(Lokacio lok) {
        ArrayList<Lokacio> szomszedok = new ArrayList<>();
        if (lok == null || lok.getUt() == null || lok.getSzakasz() == null || lok.getSav() == null) {
            return szomszedok;
        }

        Ut ut = lok.getUt();
        java.util.List<Sav> szakasz = lok.getSzakasz();
        Sav sav = lok.getSav();

        int szakaszIdx = ut.getSzakaszok().indexOf(szakasz);
        int savIdx = sav.getSavSzama();
        int pozSavok = ut.getPozSavokSzama();
        boolean pozitivIrany = (savIdx < pozSavok);

        // 1. Sávváltás azonos irányban
        for (Sav s : szakasz) {
            if (s != sav && (s.getSavSzama() < pozSavok) == pozitivIrany) {
                szomszedok.add(new Lokacio(ut, szakasz, s));
            }
        }

        // 2. Előrehaladás és kereszteződések
        if (pozitivIrany) {
            addPozitivLehetosegek(szomszedok, ut, szakasz, szakaszIdx, pozSavok);
        } else {
            addNegativLehetosegek(szomszedok, ut, szakasz, szakaszIdx, pozSavok);
        }

        return szomszedok;
    }

    private void addPozitivLehetosegek(ArrayList<Lokacio> lista, Ut ut,
            java.util.List<Sav> szakasz, int szakaszIdx, int pozSavok) {
        if (szakaszIdx + 1 < ut.getSzakaszok().size()) {
            java.util.List<Sav> kov = ut.getSzakaszok().get(szakaszIdx + 1);
            for (Sav s : kov) {
                if (s.getSavSzama() < pozSavok) lista.add(new Lokacio(ut, kov, s));
            }
        } else {
            for (Ut kovUt : ut.getSzomszedok(1)) {
                if (!kovUt.getSzakaszok().isEmpty()) {
                    java.util.List<Sav> elso = kovUt.getSzakaszok().get(0);
                    for (Sav s : elso) {
                        if (s.getSavSzama() < kovUt.getPozSavokSzama()) lista.add(new Lokacio(kovUt, elso, s));
                    }
                }
            }
            for (Sav s : szakasz) {
                if (s.getSavSzama() >= pozSavok) lista.add(new Lokacio(ut, szakasz, s));
            }
        }
    }

    private void addNegativLehetosegek(ArrayList<Lokacio> lista, Ut ut,
            java.util.List<Sav> szakasz, int szakaszIdx, int pozSavok) {
        if (szakaszIdx - 1 >= 0) {
            java.util.List<Sav> kov = ut.getSzakaszok().get(szakaszIdx - 1);
            for (Sav s : kov) {
                if (s.getSavSzama() >= pozSavok) lista.add(new Lokacio(ut, kov, s));
            }
        } else {
            for (Ut kovUt : ut.getSzomszedok(1)) {
                if (!kovUt.getSzakaszok().isEmpty()) {
                    int utolso = kovUt.getSzakaszok().size() - 1;
                    java.util.List<Sav> utolsoSzakasz = kovUt.getSzakaszok().get(utolso);
                    for (Sav s : utolsoSzakasz) {
                        if (s.getSavSzama() >= kovUt.getPozSavokSzama()) lista.add(new Lokacio(kovUt, utolsoSzakasz, s));
                    }
                }
            }
            for (Sav s : szakasz) {
                if (s.getSavSzama() < pozSavok) lista.add(new Lokacio(ut, szakasz, s));
            }
        }
    }
}
