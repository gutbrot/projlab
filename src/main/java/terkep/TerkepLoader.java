package terkep;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class TerkepLoader {
    public static Terkep betolt(String fajlNev) {
        String eleresiUt = "Betoltes/" + fajlNev;
        Terkep terkep = new Terkep();
        Map<String, Ut> utMap = new HashMap<>();

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(eleresiUt));
            doc.getDocumentElement().normalize();

            // 1. Utak létrehozása
            NodeList nList = doc.getElementsByTagName("Ut");
            for (int i = 0; i < nList.getLength(); i++) {
                Element e = (Element) nList.item(i);
                String nev = e.getAttribute("nev");
                int hossz = Integer.parseInt(e.getAttribute("hossz"));
                int poz = Integer.parseInt(e.getAttribute("pozsav"));
                int neg = Integer.parseInt(e.getAttribute("negsav"));

                Ut ujUt;
                switch (e.getAttribute("tipus").toLowerCase()) {
                    case "hid":    ujUt = new Hid(nev, hossz, poz, neg); break;
                    case "alagut": ujUt = new Alagut(nev, hossz, poz, neg); break;
                    default:       ujUt = new SimaUt(nev, hossz, poz, neg); break;
                }
                utMap.put(nev, ujUt);
                terkep.addUt(ujUt);
            }

            // 2. Elágazások és 3. Útviszonyok (a negatív sáv logikájával frissítve)
            // ... (A sávok frissítésénél a Betolteskezelo parseLokacio logikáját használja) ...

        } catch (Exception e) { e.printStackTrace(); }
        return terkep;
    }
}