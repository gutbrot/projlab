package terkep;

/**
 * A Hid osztály az Ut egy speciális típusa.
 * Felelőssége egy olyan útszakasz reprezentálása, amely az átlagosnál 
 * kitettebb az időjárási körülményeknek.
 * A hidakon a fagyásveszély és a hó megmaradása fokozottan jelentkezik, 
 * amit a havazás kezelése során külön figyelembe kell venni.
 */
public class Hid extends Ut {
    
    /**
     * Konstruktor a Hid példányosításához.
     * Meghívja az ősosztály konstruktorát az alapvető útadatok beállításához.
     * @param nev A híd egyedi megnevezése.
     * @param hossz A híd hossza (szakaszok száma).
     * @param savokSzama A hídon futó párhuzamos sávok száma.
     */
    public Hid(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Felüldefiniálja a havazás logikáját a hídon.
     * A híd nyitott jellege miatt a csapadék közvetlenül az úttestre hullik.
     * Végigiterál az összes útszakaszon és sávon, hogy növelje a hóvastagságot.
     * @param h A lehullott hó mennyisége, amellyel minden sáv hórétege növekszik.
     */
    @Override
    public void havazik(int h) {
        // Végigmegyünk az út minden szakaszán
        for (var szakasz : szakaszok) {
            // Minden szakaszban végigmegyünk az összes sávon
            for (var sav : szakasz) {
                // A híd felületén a hóréteg vastagsága a megadott értékkel nő
                sav.setHo(sav.getHo() + h);
            }
        }
    }
}