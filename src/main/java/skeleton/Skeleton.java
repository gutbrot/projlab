package skeleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A Skeleton osztály felelős a tesztelési keretrendszer vezérléséért.
 * Nyomon követi a metódushívásokat, tárolja az objektumok neveit,
 * és vizuálisan megjeleníti a hívási láncot (szekvenciát).
 */
public class Skeleton {

    /** Tárolja a regisztrált objektumokat és a nevüket (pl. h1 -> "hokotro1"). */
    private static final Map<Object, String> objectNames = new HashMap<>();
    private static final String tester = "Tester";
    private static final Stack<Object> callChain = new Stack<>();
    public static boolean showPrint = true;

    // ---> EZ HIÁNYZOTT! EZ A BELÉPÉSI PONT <---
    public static void main(String[] args) {
        Console.print("=== HÓKOTRÓ SZIMULÁTOR - TESZTELŐI MENÜ ===");
        
        while (true) {
            Console.print("\nAvailable tests: [1, 2, 3, 4, 5, 6, 7, 8]");
            String line = Console.readLine("Please enter test number (or 'exit' to quit): ");
            
            if (line == null || line.trim().equalsIgnoreCase("exit")) {
                Console.print(">>> Kilépés...");
                break;
            }
            
            try {
                int num = Integer.parseInt(line.trim());
                
                switch(num) {
                    case 1: TesztKornyezet.Test1(); break;
                    case 2: TesztKornyezet.Test2(); break;
                    case 3: TesztKornyezet.Test3(); break;
                    case 4: TesztKornyezet.Test4(); break;
                    case 5: TesztKornyezet.Test5(); break;
                    case 6: TesztKornyezet.Test6(); break;
                    case 7: TesztKornyezet.Test7(); break;
                    case 8: TesztKornyezet.Test8(); break;
                    default: Console.print(">>> Nincs ilyen sorszámú teszt!");
                }
                
                clear(); // Teszt utáni takarítás, hogy a következő teszt tiszta lappal induljon
                
            } catch (NumberFormatException e) {
                Console.print(">>> Hiba: Kérlek számot adj meg!");
            }
        }
    }

    // --- LOGOLÓ ÉS UML FORMÁZÓ METÓDUSOK ---

    private static void print(String msg, int depth, boolean endLine) {
        if (showPrint) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < depth; ++i) {
                sb.append("  | "); 
            }
            sb.append(msg);
            if (endLine) {
                Console.print(sb.toString());
            } else {
                Console.printInline(sb.toString());
            }
        }
    }

    public static void registerNewObject(Object o, String name) {
        if (o != null && name != null) {
            objectNames.put(o, name);
        }
    }

    public static String getObjectName(Object o) {
        if (o == null) return "null";
        if (o instanceof String || o instanceof Integer || o instanceof Boolean) return o.toString();
        if (objectNames.containsKey(o)) return objectNames.get(o) + ":" + o.getClass().getSimpleName();
        return "Unknown:" + o.getClass().getSimpleName();
    }

    private static String parseParams(Object[] params) {
        if (params == null || params.length == 0) return "()";
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < params.length; i++) {
            sb.append(getObjectName(params[i]));
            if (i < params.length - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public static void functionCalled(String name, Object whoGotCalled, String retType, Object... params) {
        String caller = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        callChain.push(whoGotCalled);
        print(caller + " -> " + getObjectName(whoGotCalled) + "." + name + parseParams(params) + " : " + retType, callChain.size() - 1, true);
    }

    public static <T> T functionReturn(T value) {
        if (callChain.isEmpty()) return value; 
        String returnFrom = getObjectName(callChain.pop());
        String returnTo = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        print(returnTo + " <- " + returnFrom + " [return: " + getObjectName(value) + "]", callChain.size(), true);
        return value;
    }

    public static void voidReturn() {
        if (callChain.isEmpty()) return;
        String returnFrom = getObjectName(callChain.pop());
        String returnTo = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        print(returnTo + " <- " + returnFrom + " [return void]", callChain.size(), true);
    }

    public static void constructorCalled(Object newObject, String itsName, Object... params) {
        registerNewObject(newObject, itsName);
        String caller = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        callChain.push(newObject);
        print(caller + " -> " + getObjectName(newObject) + " <<create>>" + parseParams(params), callChain.size() - 1, true);
    }

    public static void constructorReturned() {
        if (callChain.isEmpty()) return;
        String returnFrom = getObjectName(callChain.pop());
        String returnTo = callChain.isEmpty() ? tester : getObjectName(callChain.peek());
        print(returnTo + " <- " + returnFrom + " <<constructed>>", callChain.size(), true);
    }

    public static void clear() {
        objectNames.clear();
        callChain.clear();
    }
}