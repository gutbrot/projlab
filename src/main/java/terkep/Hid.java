package terkep;

public class Hid extends Ut {
    public Hid(String nev, int hossz, int savokSzama) { super(nev, hossz, savokSzama); }

    @Override
    public void havazik(int h) {
        for (var szakasz : szakaszok) {
            for (var sav : szakasz) {
                sav.setHo(sav.getHo() + h);
            }
        }
    }
}
