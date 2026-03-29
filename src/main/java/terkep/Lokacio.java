package terkep;

import java.util.List;

public class Lokacio {
    private final Ut ut;
    private final List<Sav> szakasz;
    private final Sav sav;

    public Lokacio(Ut ut, List<Sav> szakasz, Sav sav) {
        this.ut = ut;
        this.szakasz = szakasz;
        this.sav = sav;
    }

    public Ut getUt() { return ut; }
    public List<Sav> getSzakasz() { return szakasz; }
    public Sav getSav() { return sav; }
}
