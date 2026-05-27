package grafikus;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton osztály, amely az egész alkalmazást fogja össze.
 * Ő indítja el a főmenüt, és tárolja a megnyílt játékablak (MainFrame) referenciáját.
 * Az egész programból egyetlen példány létezhet belőle.
 */
public class GraphicsApp {

    /**
     * Az osztály egyetlen, statikus példánya (Singleton minta).
     */
    private static GraphicsApp instance;

    /**
     * Az aktuális játékablak referenciája.
     */
    private MainFrame frame;

    /**
     * A megjelenített járművek grafikus objektumainak listája.
     */
    private List<GraphicObject> graphicObjects;

    /**
     * A konstruktor szándékosan privát: kívülről nem lehet új példányt létrehozni.
     * Az egyetlen példányhoz a getInstance() metóduson keresztül lehet hozzáférni.
     */
    private GraphicsApp() {
        this.graphicObjects = new ArrayList<>();
    }

    /**
     * Visszaadja az alkalmazás egyetlen példányát — ha még nem jött létre, itt hozza létre először.
     */
    public static GraphicsApp getInstance() {
        if (instance == null) {
            instance = new GraphicsApp();
        }
        return instance;
    }

    /**
     * Megnyitja a főmenü ablakot — ezzel indul el a játék.
     */
    public void start() {
        // A MenuFrame megnyitása
        MenuFrame menu = new MenuFrame();
        menu.setVisible(true);
    }

    /**
     * Frissíti a grafikus felületet a modell aktuális állapota alapján.
     * Jelenleg stub — a tényleges frissítés a MainFrame.korFrissites()-en át történik.
     */
    public void frissites() {
        if (frame != null) {
            // Ez a metódus a MainFrame-ben lesz implementálva (2. lépés)
            // frame.terkepFrissites();
        }
    }

    /**
     * Jövőbeli belépési pont: egy jármű ID alapján létrehozza a megfelelő grafikus objektumot
     * (GraphicBusz, GraphicHokotro, stb.) és hozzáadja a rajzolandók listájához.
     */
    public void jarmuHozzaad(String id) {
        // Ide kerül majd a logika, ami a Játéktérből lekéri a járművet az ID alapján,
        // és létrehozza hozzá a megfelelő GraphicBusz, GraphicHokotro, stb. példányt.
        System.out.println(">>> [GraphicsApp] Jármű grafikus objektum hozzáadása folyamatban: " + id);
    }

    /**
     * A játék indulásakor a MenuFrame ezzel regisztrálja a megnyílt főablakot,
     * hogy a frissites() és más metódusok tudják, hova kell küldeni a frissítéseket.
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.frame = mainFrame;
    }
}