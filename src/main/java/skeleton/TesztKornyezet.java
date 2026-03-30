package skeleton;
 
import bolt.*;
import jarmu.*;
import terkep.*;
import eszkoztar.*;
import jatekos.*;
import kotrofej.*;
import seged.*;

public class TesztKornyezet {
    private TesztKornyezet() {}
    
    public static void main(String[] args) {
        Terkep terkep = new Terkep();
        terkep.addUt(new SimaUt("FoUt", 3, 2));

        Jatekter jatekter = new Jatekter(terkep);
        Takarito takarito = new Takarito(2, 500);
        Hokotro hokotro = new Hokotro(null, new SoproFej(100));
        takarito.hozzaadHokotro(hokotro);
        jatekter.hozzaadJatekos(takarito);
        jatekter.hozzaadJarmu(hokotro);

        Bolt bolt = new Bolt();
        bolt.felveszTermek("so", new SoCsomag(5, 100));
        bolt.felveszTermek("biokerozin", new BiokerozinCsomag(2, 150));

        Skeleton.log("Teszt környezet inicializálva.");
    }
}
