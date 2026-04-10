package skeleton;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Console{

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /* Console kiiro 
        Bovitheto idobelyeggel
    */
    public static void print(String msg){
        try {
            System.out.println(msg);
        } catch (Exception e) {
           e.printStackTrace(); 
        }
    }
}