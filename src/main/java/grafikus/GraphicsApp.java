package grafikus;

import java.util.ArrayList;
import java.util.List;

/**
 * Az alkalmazás belépési pontja és a grafikus felület inicializálásáért felelős singleton osztály.
 * Létrehozza a főablakokat, kezeli az aktuális frame-eket és biztosítja a GUI indítását.
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
     * Privát konstruktor a Singleton tervezési minta biztosítására.
     * Inicializálja a grafikus objektumok listáját.
     */
    private GraphicsApp() {
        this.graphicObjects = new ArrayList<>();
    }

    /**
     * Visszaadja az alkalmazás singleton példányát.
     * Ha még nem létezik, létrehozza azt.
     * * @return A GraphicsApp egyetlen példánya.
     */
    public static GraphicsApp getInstance() {
        if (instance == null) {
            instance = new GraphicsApp();
        }
        return instance;
    }

    /**
     * Elindítja a grafikus alkalmazást a főmenü megjelenítésével.
     */
    public void start() {
        // A MenuFrame megnyitása
        MenuFrame menu = new MenuFrame();
        menu.setVisible(true);
    }

    /**
     * Frissíti a grafikus felület állapotát.
     * Meghívja a főablak (MainFrame) frissítő metódusait.
     */
    public void frissites() {
        if (frame != null) {
            // Ez a metódus a MainFrame-ben lesz implementálva (2. lépés)
            // frame.terkepFrissites();
        }
    }

    /**
     * Új jármű grafikus reprezentációját hozza létre és adja hozzá a listához.
     * * @param id A hozzáadandó jármű azonosítója.
     */
    public void jarmuHozzaad(String id) {
        // Ide kerül majd a logika, ami a Játéktérből lekéri a járművet az ID alapján,
        // és létrehozza hozzá a megfelelő GraphicBusz, GraphicHokotro, stb. példányt.
        System.out.println(">>> [GraphicsApp] Jármű grafikus objektum hozzáadása folyamatban: " + id);
    }

    /**
     * Beállítja az aktív főablakot (MainFrame).
     * Ezt a MenuFrame fogja meghívni a játék indításakor.
     * * @param mainFrame A beállítandó főablak.
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.frame = mainFrame;
    }
}