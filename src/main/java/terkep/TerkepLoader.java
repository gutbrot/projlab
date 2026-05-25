package terkep;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

/**
 * Javított TerkepLoader: kezeli az utak létrehozását és az elágazások 
 * alapján a szomszédsági kapcsolatok kétirányú felépítését.
 */
public class TerkepLoader {

    public static Terkep betolt(String fajlNev) {
        String eleresiUt = "Betoltes/" + fajlNev;
        Terkep terkep = new Terkep();
        Map<String, Ut> utMap = new HashMap<>();

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(eleresiUt));
            doc.getDocumentElement().normalize();

            // 1. Első menet: Utak létrehozása és tárolása a Map-ben
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

            // 2. Második menet: Kapcsolatok (Elagazodas) beolvasása és összekötés
            for (int i = 0; i < nList.getLength(); i++) {
                Element e = (Element) nList.item(i);
                String forrasNev = e.getAttribute("nev");
                Ut forrasUt = utMap.get(forrasNev);

                NodeList elagazasok = e.getElementsByTagName("Szomszed");
                for (int j = 0; j < elagazasok.getLength(); j++) {
                    Element szomszedElem = (Element) elagazasok.item(j);
                    String celNev = szomszedElem.getAttribute("nev");
                    int irany = Integer.parseInt(szomszedElem.getAttribute("irany"));
                    
                    Ut celUt = utMap.get(celNev);
                    if (forrasUt != null && celUt != null) {
                        // Kétirányú kapcsolat felépítése a modellben
                        forrasUt.addSzomszed(celUt, irany);
                    }
                }
            }
        } catch (Exception e) { 
            System.err.println("Hiba a térkép betöltésekor: " + e.getMessage());
            e.printStackTrace(); 
        }
        return terkep;
    }
}