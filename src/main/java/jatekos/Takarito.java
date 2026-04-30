package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import eszkoztar.Eszkoztar;
import jarmu.Hokotro;
import kotrofej.*;
import bolt.Bolt;
import skeleton.Skeleton;

/**
 * A Takarito osztály felelős a rendszerben lévő hókotrók irányításáért. 
 * Feladata a hókotrók és a rendelkezésre álló fejek közül a leghasznosabb kiválasztása, 
 * azok mozgatása, valamint az útszakaszok sávjainak feltakarítása. 
 * Mozgásokat az akciópontok felhasználásával végez
 */
public class Takarito extends Jatekos {
    
    /** A takarító által vezérelt hókotrók listája. */
    private List<Hokotro> iranyitottHokotrok = new ArrayList<>();
    
    /** Egyes játékosok összegyűjtött pénze. */
    private int penz;
    
    /** 
     * A játékos saját eszköztára. 
     * A dokumentáció szerint ez tárolja a vásárolt eszközöket.
     */
    private Eszkoztar eszkoztar = new Eszkoztar();
    
    /** Segédeszköz a prototípus interaktív választásaihoz. */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Konstruktor a Takarító játékos létrehozásához.
     * @param akcioPont Kezdő akciópontok.
     * @param penz Kezdő tőke.
     */
    public Takarito(int akcioPont, int penz) {
        super(akcioPont);
        this.penz = penz;
    }

    /**
     * Egy konkrét hókotró kiválasztása a takarító játékos által.
     * A dokumentáció szerinti "kiválasztási logika (felhasználói input)" alapján.
     * 
     * @return A választott Busz objektum (itt: Hokotro).
     */
    public Hokotro hokotrotValaszt() {
        Skeleton.functionCalled("hokotrotValaszt", this, "Hokotro");
        
        if (iranyitottHokotrok.isEmpty()) {
            return Skeleton.functionReturn(null);
        }

        // A dokumentáció aktivitásdiagramja szerinti választási folyamat
        System.out.println(">>> Elérhető hókotrók:");
        for (int i = 0; i < iranyitottHokotrok.size(); i++) {
            System.out.println("    [" + i + "] " + iranyitottHokotrok.get(i).getId());
        }

        System.out.print("? Válasszon indexet: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < iranyitottHokotrok.size()) {
                Hokotro kivalasztott = iranyitottHokotrok.get(index);
                // A választás akciópontba kerülhet a játékmenet szerint
                akcioPontKezelo(); 
                return Skeleton.functionReturn(kivalasztott);
            }
        } catch (Exception e) {
            System.out.println(">>> Érvénytelen választás.");
        }
        
        return Skeleton.functionReturn(null);
    }

    /**
     * A kiválasztott járművek mozgatásáért felelős logika[
     * Az aktivitásdiagram alapján: Ellenőrzi az AP-t, meghatározza a célt, 
     * mozgat, majd levonja a pontot
     * 
     * @param h A mozgatni kívánt hókotró
     */
    public void hokotrotMozgat(Hokotro h) {
        Skeleton.functionCalled("hokotrotMozgat", this, "void", h);
        
        // Ellenőrizzük, van-e elég akciópont
        if (getAkcioPont() > 0 && h != null) {
            // A tényleges cél sávot a Jatekter/Skeleton tesztkörnyezet adja meg
            // Itt a mozgatási szándékot jelezzük
            akcioPontKezelo(); // Levonunk egy akciópontot
        }
        
        Skeleton.voidReturn();
    }

    /**
     * Az aktuális kotró fej lecserélése egy másik típusra a listából
     * @param h A hókotró, amin a cserét végezzük.
     */
    public void kotrofejValt(Hokotro h) {
        Skeleton.functionCalled("kotrofejValt", this, "void", h);
        
        if (getAkcioPont() > 0 && h != null) {
            KotroFej ujFej = eszkoztar.kiveszFej();
            if (ujFej != null) {
                h.fejcsere(ujFej); // Meghívja a Hokotro fejcsere metódusát
                akcioPontKezelo();
            }
        }
        
        Skeleton.voidReturn();
    }

    /**
     * Általános vásárlási művelet egy Bolt objektumon keresztül
     * Követi a dokumentáció diagramját: Kínálat ellenőrzés -> Ár lekérés -> 
     * Pénz ellenőrzés -> Pénz levonás -> Átadás
     * 
     * @param bolt A bolt, ahol vásárolunk.
     * @param termekNev A termék neve.
     */
    public void vasarol(Bolt bolt, String termekNev) {
        Skeleton.functionCalled("vasarol", this, "void", bolt, termekNev);
        
        if (getAkcioPont() > 0 && bolt != null) {
            // A tranzakciót a Bolt indítja és vezényli le a diagram szerint
            boolean siker = bolt.vasarlas(this, termekNev);
            if (siker) {
                akcioPontKezelo(); // Csak sikeres vásárlásnál vonunk le AP-t
            }
        }
        
        Skeleton.voidReturn();
    }

    /**
     * A metódus a játékos pénzét növeli
     * @param p A kapott összeg.
     */
    public void penztKap(int p) {
        Skeleton.functionCalled("penztKap", this, "void", p);
        this.penz += p;
        Skeleton.voidReturn();
    }
    
    /**
     * Levonja a megadott összeget a játékostól.
     * @param osszeg A fizetendő ár.
     */
    public void penztLevon(int osszeg) {
        this.penz -= osszeg;
    }

    /** Új hókotró hozzáadása a játékoshoz. */
    public void hozzaadHokotro(Hokotro h) {
        if (h != null) iranyitottHokotrok.add(h);
    }

    // --- GETTEREK ---
    public int getPenz() { return penz; }
    public Eszkoztar getEszkoztar() { return eszkoztar; }
    public List<Hokotro> getIranyitottHokotrok() { return iranyitottHokotrok; }
}