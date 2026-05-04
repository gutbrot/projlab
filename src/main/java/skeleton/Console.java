package skeleton;

import java.util.Scanner;

/**
 * A Console osztály egy segédosztály a Skeleton és a Prototípus teszteléséhez.
 * Egységesíti a standard kimenetre (stdout) történő írást és beolvasást.
 */
public class Console {

    //Statikus beolvasó a standard bemenethez
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Egységesített konzol kiíró metódus
     */
    public static void print(String msg) {
        System.out.println(msg);
    }

    /**
     * Szöveges üzenet kiírása soremelés nélkül
     */
    public static void printInline(String msg) {
        System.out.print(msg);
    }

    /**
     * Bemenet beolvasása a felhasználótól
     */
    public static String readLine(String prompt) {
        //Ha van prompt, akkor kiírjuk a konzolra
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        
        //Megpróbáljuk beolvasni a következő sort a konzolról
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
     * Túlterhelt beolvasó metódus prompt nélkül
     */
    public static String readLine() {
        return readLine("");
    }
}