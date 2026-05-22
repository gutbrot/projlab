package grafikus;

import java.awt.Graphics;

/**
 * A játéktéren megjelenő minden rajzolható elem (autók, buszok, hókotrók) absztrakt ősosztálya.
 * Egységes felületet biztosít a pozicionáláshoz és a kirajzoláshoz a Swing komponenseken belül.
 */
public abstract class GraphicObject {

    /** Az objektum aktuális vízszintes (X) képernyő-koordinátája pixelben. */
    protected int x;

    /** Az objektum aktuális függőleges (Y) képernyő-koordinátája pixelben. */
    protected int y;

    /**
     * A GraphicObject alapértelmezett konstruktora.
     * Inicializálja a koordinátákat a képernyő origójába.
     */
    public GraphicObject() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Absztrakt metódus, amely végrehajtja az elem vizuális kirajzolását.
     * Minden leszármazott járműosztálynak kötelező megvalósítania a saját alakzatát.
     *
     * @param g A Swing által biztosított grafikus kontextus objektum.
     */
    public abstract void rajzol(Graphics g);

    /**
     * Absztrakt metódus, amely szinkronizálja a grafikus koordinátákat (x, y) 
     * a háttérben lévő modell objektum (út, szakasz, sáv) valós helyzetével.
     */
    public abstract void frissitPozicio();

    /**
     * Visszaadja az objektum aktuális X koordinátáját.
     * @return Az X pozíció pixelben.
     */
    public int getX() {
        return x;
    }

    /**
     * Beállítja az objektum X koordinátáját.
     * @param x Az új X pozíció pixelben.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Visszaadja az objektum aktuális Y koordinátáját.
     * @return Az Y pozíció pixelben.
     */
    public int getY() {
        return y;
    }

    /**
     * Beállítja az objektum Y koordinátáját.
     * @param y Az új Y pozíció pixelben.
     */
    public void setY(int y) {
        this.y = y;
    }
}