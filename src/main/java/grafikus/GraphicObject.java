package grafikus;

import java.awt.Graphics;
import java.awt.Point; 

/**
 * Minden a térképen megjelenő jármű (autó, busz, hókotró) ebből az osztályból örököl.
 * Tárolja a képernyőpozíciót (x, y), és megköveteli, hogy minden leszármazott
 * valósítsa meg a saját kirajzolását és pozíciófrissítését.
 */
public abstract class GraphicObject {

    /** Az objektum aktuális vízszintes (X) képernyő-koordinátája pixelben. */
    protected int x;

    /** Az objektum aktuális függőleges (Y) képernyő-koordinátája pixelben. */
    protected int y;

    /**
     * A képernyő bal felső sarkába (0, 0) helyezi az objektumot.
     * A tényleges pozíció a frissitPozicio() első hívásakor áll be.
     */
    public GraphicObject() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Kirajzolja a járművet a kapott grafikus kontextusra.
     * Minden leszármazott a saját formáját, színét és feliratát valósítja meg itt.
     */
    public abstract void rajzol(Graphics g);

    /**
     * Frissíti az x/y képernyőpozíciót a modell aktuális helyzetéből
     * (melyik úton, szakaszon és sávon áll a jármű). Rajzolás előtt mindig meghívódik.
     */
    public abstract void frissitPozicio();

    /** Visszaadja a vízszintes képernyőpozíciót pixelben. */
    public int getX() {
        return x;
    }

    /** Beállítja a vízszintes képernyőpozíciót pixelben. */
    public void setX(int x) {
        this.x = x;
    }

    /** Visszaadja a függőleges képernyőpozíciót pixelben. */
    public int getY() {
        return y;
    }

    /** Beállítja a függőleges képernyőpozíciót pixelben. */
    public void setY(int y) {
        this.y = y;
    }

    // Segédmetódus az alap pozícióhoz, amit a leszármazottak használnak
    protected Point getUtKezdopont(String utNev) {
        return TerkepPanel.getUtAlapPozicio(utNev);
    }
}