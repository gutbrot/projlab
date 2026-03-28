package terkep;

import java.util.List;

public abstract class Ut {
    protected String nev;
    protected List<List<Sav>> szakaszok;

    public abstract void havazik(int h);
}

class SimaUt extends Ut {
    @Override
    public void havazik(int h) {
        for(List<Sav> szakasz : szakaszok) {
            for(Sav s : szakasz) {
                s.setHoVastagsag(h);
            }
        }
    }
}

class Alagut extends Ut {
    @Override
    public void havazik(int h){
        // nem csinal semmit
    }
}

class Hid extends Ut {
    @Override
    public void havazik(int h) {
        for(List<Sav> szakasz : szakaszok) {
            for(Sav s : szakasz) {
                s.setHoVastagsag(h);
            }
        }
    }
}