package seged;

import java.util.ArrayList;
import java.util.List;

import terkep.Lokacio;

public class Navigacio {
    public List<Lokacio> legrovidebbUt(Lokacio hova) {
        List<Lokacio> eredmeny = new ArrayList<>();
        if (hova != null) eredmeny.add(hova);
        return eredmeny;
    }
}