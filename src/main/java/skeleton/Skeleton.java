package skeleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * A Skeleton osztály felelős a tesztelési keretrendszer vezérléséért.
 * Nyomon követi a metódushívásokat, tárolja az objektumok neveit,
 * és biztosítja a konzolos interakciót a tesztelővel.
 */
public class Skeleton {

    // Tárolja a regisztrált objektumokat és a hozzájuk rendelt egyedi neveket (pl. h1 -> "hokotro1")
    private static final Map<Object, String> objectNames = new HashMap<>();
    
    // A futtatható tesztesetek gyűjteménye: azonosító szám és a hozzá tartozó kód
    private static final Map<Integer, Runnable> tests;
    
    // A legkülső hívó fél megnevezése a naplózásban
    private static final String tester = "t: Tester";
    
    // A standard konzol bemenet olvasója a felhasználói válaszokhoz
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
    // Jelzi, hogy az inicializálás (világ felépítése) alatti hívások megjelenjenek-e
    public static boolean showInit = false;
    
    // Globális kapcsoló a naplózás ki- és bekapcsolásához
    public static boolean showPrint = true;

    // Statikus blokk a tesztesetek inicializálásához és regisztrálásához
    static {
        tests = new HashMap<>();
        tests.put(101, TesztKornyezet::Test101);
        tests.put(201, TesztKornyezet::Test201);
        tests.put(202, TesztKornyezet::Test202);
        tests.put(301, TesztKornyezet::Test301);
        tests.put(302, TesztKornyezet::Test302);
        tests.put(401, TesztKornyezet::Test401);
        tests.put(501, TesztKornyezet::Test501);
        tests.put(502, TesztKornyezet::Test502);
    }

    /**
     * A tesztprogram főhurokja. Beolvassa a teszt számát és elindítja azt.
     */
    public static void main(String[] args) {
        // A választható tesztek listájának előkészítése a menühöz
        String testText = parseListToString(List.of(tests.keySet().toArray().clone()), '[', ']');
        
        do {
            print("\nAvailable tests: " + testText, 0, true);
            print("Please enter test number (or 'show init' / 'hide init'): ", 0, false);
            
            try {
                String line = reader.readLine();
                if (line == null) break;
                
                // Speciális parancsok a naplózás mélységének állításához
                if (line.equals("hide init")) { showInit = false; continue; }
                if (line.equals("show init")) { showInit = true; continue; }
                
                int num = Integer.parseInt(line);
                if (tests.containsKey(num)) {
                    // Ha az inicializálást elrejtjük, kikapcsoljuk a printet a setup idejére
                    if (!showInit) showPrint = false;
                    
                    print("\n--- INITIALIZATION ---", 2, true);
                    TesztKornyezet.initializeWorld();
                    
                    // A tényleges teszt futtatása előtt mindenképp visszakapcsoljuk a printet
                    showPrint = true;
                    print("\n--- Running test " + num + " ---", 0, true);
                    tests.get(num).run();
                    print("--- Test Finished ---\n", 0, true);
                    
                    // Minden teszt után tiszta állapotot teremtünk a statikus tárolókban
                    objectNames.clear();
                    callChain.clear();
                }
            } catch (Exception e) {
                // Érvénytelen bemenet esetén egyszerűen újrakezdjük a ciklust
            }
        } while (true);
    }

    /**
     * Segédfüggvény listák és tömbök esztétikus, nevesített kiírásához.
     */
    public static <T> String parseListToString(List<T> list, char first, char last) {
        if (list == null || list.isEmpty()) return "" + first + last;
        StringBuilder text = new StringBuilder().append(first);
        for (T item : list) {
            text.append(getObjectName(item)).append(", ");
        }
        // Az utolsó vessző és szóköz levágása
        text.replace(text.length() - 2, text.length(), "").append(last);
        return text.toString();
    }

    /**
     * Központi kiíró metódus, amely kezeli a behúzásokat a hívási mélység alapján.
     */
    public static void print(String msg, int tabs, boolean endLine) {
        if (showPrint) {
            for (int i = 0; i < tabs; ++i) System.out.print("    ");
            System.out.print(msg + (endLine ? "\n" : ""));
        }
    }

    /**
     * Új objektum regisztrálása a tesztkörnyezetben egy barátságos névvel.
     */
    public static void registerNewObject(Object o, String name) {
        objectNames.put(o, name);
    }

    /**
     * Visszaadja az objektum regisztrált nevét és típusát (pl. "busz1: Busz").
     */
    public static String getObjectName(Object o) {
        if (o == null) return "null";
        if (objectNames.containsKey(o)) {
            return objectNames.get(o) + ": " + o.getClass().getSimpleName();
        }
        return o.toString();
    }

    // A hívási láncot (stack) tárolja a metódusok egymásba ágyazottságának követéséhez
    private static final Stack<Object> callChain = new Stack<>();

    /**
     * Metódushívás kezdetének naplózása. Behúzza a szöveget és rögzíti a hívót.
     */
    public static void functionCalled(String name, Object whoGotCalled, String retType, Object... params) {
        // Meghatározzuk, ki a hívó fél (a stack teteje vagy a tester)
        String caller = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        
        // Az aktuális objektumot betesszük a hívási láncba
        callChain.push(whoGotCalled);
        
        String paramsText = parseListToString(params == null ? null : List.of(params), '(', ')');
        
        // Formátum: Hívó -> Célpont : metódus(paraméterek) : típus
        print(caller + " -> " + getObjectName(whoGotCalled) + " : " + name + paramsText + " : " + retType, callChain.size() - 1, true);
    }

    /**
     * Értékkel visszatérő metódus befejezésének naplózása.
     */
    public static <T> T functionReturn(T value) {
        // Kivesszük az aktuális objektumot a stack tetejéről (vége a hívásnak)
        String returnFrom = getObjectName(callChain.pop());
        String returnTo = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        
        // Formátum: Hívó <- Célpont : visszatérésiÉrték
        print(returnTo + " <- " + returnFrom + " : " + getObjectName(value), callChain.size(), true);
        return value;
    }

    /**
     * Void (visszatérési érték nélküli) metódus befejezésének naplózása.
     */
    public static void voidReturn() {
        String returnFrom = getObjectName(callChain.pop());
        String returnTo = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        print(returnTo + " <- " + returnFrom, callChain.size(), true);
    }

    /**
     * Konstruktor hívás naplózása. Létrehozáskor rögtön regisztrálja is az objektumot.
     */
    public static void constructorCalled(Object newObject, String itsName, Object... params) {
        registerNewObject(newObject, itsName);
        String caller = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        callChain.push(newObject);
        
        String paramText = parseListToString(params == null ? null : List.of(params), '(', ')');
        // A <<create>> jelöli az objektum példányosítását
        print(caller + " --> " + getObjectName(newObject) + " : <<create>>" + paramText, callChain.size() - 1, true);
    }

    /**
     * Alapértelmezett konstruktor visszatérés.
     */
    public static void constructorReturned() {
        constructorReturned(null);
    }

    /**
     * Konstruktor befejezésének naplózása.
     */
    public static void constructorReturned(Object o) {
        String returnFrom = getObjectName(callChain.pop());
        String returnTo = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        // A <<constructed>> jelöli, hogy az objektum létrejött
        print(returnTo + " <-- " + returnFrom + " : <<constructed>>", callChain.size(), true);
    }

    /**
     * Interaktív kérdés feltevése a felhasználónak a teszt futása közben.
     */
    public static String askForAString(String msg, List<String> acceptedInputs) {
        // Megjeleníti a kérdést és a választható opciókat
        print("? " + msg + " " + parseListToString(acceptedInputs, '[', ']') + " : ", callChain.size(), false);
        try {
            return reader.readLine().toLowerCase();
        } catch (IOException e) { 
            return ""; 
        }
    }
}