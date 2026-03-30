package terkep;

/**
 * A SimaUt osztály az Ut egy általános, kültéri típusát reprezentálja.
 * Felelőssége az alapvető útszakaszok kezelése, ahol az időjárási körülmények 
 * (példányosan a havazás) közvetlenül és egyenletesen érintik az összes forgalmi sávot.
 * Ez az osztály nem rendelkezik speciális védelemmel vagy fokozott érzékenységgel a csapadékkal szemben.
 */
public class SimaUt extends Ut {
    
    /**
     * Konstruktor a SimaUt példányosításához.
     * Meghívja az ősosztály konstruktorát az út alapvető paramétereinek beállításához.
     * @param nev Az út egyedi megnevezése.
     * @param hossz Az út hossza, amely meghatározza a szakaszok számát.
     * @param savokSzama Az úton egymás mellett futó forgalmi sávok mennyisége.
     */
    public SimaUt(String nev, int hossz, int savokSzama) { 
        super(nev, hossz, savokSzama); 
    }

    /**
     * Megvalósítja az út havazáskor tanúsított viselkedését.
     * Mivel nyitott útszakaszról van szó, a lehullott hómennyiség minden 
     * egyes szakasz minden egyes sávjában növeli a hóréteg vastagságát.
     * @param h A lehullott hó mennyisége (milliméterben), amellyel a sávok hóvastagsága nő.
     */
    @Override
    public void havazik(int h) {
        // Végigiterálunk az út összes keresztmetszeti szakaszán
        for (var szakasz : szakaszok) {
            // Minden szakaszon belül végigmegyünk az összes párhuzamos sávon
            for (var sav : szakasz) {
                // Az adott sáv hóvastagságát növeljük a lehullott csapadék mértékével
                sav.setHo(sav.getHo() + h);
            }
        }
    }
}