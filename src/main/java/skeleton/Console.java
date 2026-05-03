package skeleton;

import java.util.Scanner;

/**
 * A Console osztály egy segédosztály a Skeleton és a Prototípus teszteléséhez.
 * Egységesíti a standard kimenetre (stdout) történő írást és beolvasást.
 */
public class Console {

    /** Statikus beolvasó a standard bemenethez (System.in). */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Egységesített konzol kiíró metódus.
     * @param msg A kiírandó üzenet.
     */
    public static void print(String msg) {
        System.out.println(msg);
    }

    /**
     * Szöveges üzenet kiírása soremelés nélkül (prompt).
     * @param msg A kiírandó üzenet.
     */
    public static void printInline(String msg) {
        System.out.print(msg);
    }

    /**
     * Bemenet beolvasása a felhasználótól.
     * @param prompt Opcionális üzenet, amit beolvasás előtt kiír.
     * @return A felhasználó által begépelt szöveg.
     */
    public static String readLine(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
        } catch (Exception e) {
            System.err.println(">>> Hiba történt a konzolról való beolvasás során!");
        }
        return "";
    }
    
    /**
     * Túlterhelt beolvasó metódus prompt nélkül.
     * @return A felhasználó által begépelt szöveg.
     */
    public static String readLine() {
        return readLine("");
    }
}