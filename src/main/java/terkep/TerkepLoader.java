package terkep;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML fájlból tölti be a térkép struktúráját, az elágazásokat és az utak aktuális állapotát.
 */
public class TerkepLoader {

    public static Terkep betolt(String fajlNev) {
        String eleresiUt = "Betoltes/" + fajlNev;
        Terkep terkep = new Terkep();
        Map<String, Ut> utMap = new HashMap<>();

        try {
            File xmlFile = new File(eleresiUt);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // --- 1. LÉPÉS: Utak példányosítása ---
            // Először létrehozzuk az összes út objektumot, hogy később hivatkozhassunk rájuk.
            NodeList nList = doc.getElementsByTagName("Ut");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    String nev = e.getAttribute("nev");
                    String tipus = e.getAttribute("tipus");
                    int hossz = Integer.parseInt(e.getAttribute("hossz"));
                    int osszSav = Integer.parseInt(e.getAttribute("pozsav")) + 
                                  Integer.parseInt(e.getAttribute("negsav"));

                    Ut ujUt;
                    switch (tipus.toLowerCase()) {
                        case "hid":    ujUt = new Hid(nev, hossz, osszSav); break;
                        case "alagut": ujUt = new Alagut(nev, hossz, osszSav); break;
                        default:       ujUt = new SimaUt(nev, hossz, osszSav); break;
                    }
                    
                    utMap.put(nev, ujUt);
                    terkep.addUt(ujUt);
                }
            }

            // --- 2. LÉPÉS: Elágazások (Szomszédok) összekötése ---
            // Most, hogy minden út létezik a memóriában, felépítjük a gráf éleit.
            for (int i = 0; i < nList.getLength(); i++) {
                Element e = (Element) nList.item(i);
                String nev = e.getAttribute("nev");
                Ut aktualisUt = utMap.get(nev);

                NodeList szomszedok = e.getElementsByTagName("Szomszed");
                for (int j = 0; j < szomszedok.getLength(); j++) {
                    Element szElem = (Element) szomszedok.item(j);
                    String szomszedNev = szElem.getAttribute("nev");
                    int irany = Integer.parseInt(szElem.getAttribute("irany"));

                    Ut szomszedUt = utMap.get(szomszedNev);
                    if (aktualisUt != null && szomszedUt != null) {
                        // Az irány (1 vagy -1) határozza meg, hogy melyik végéhez kapcsolódik
                        aktualisUt.addSzomszed(szomszedUt, irany);
                    }
                }
            }

            // --- 3. LÉPÉS: Sávok állapotának (útviszonyok) frissítése ---
            // Beállítjuk a specifikus hó, só és forgalmi adatokat ott, ahol megadták.
            NodeList vList = doc.getElementsByTagName("Sav");
            for (int i = 0; i < vList.getLength(); i++) {
                Element savElem = (Element) vList.item(i);
                Element lokacio = (Element) savElem.getElementsByTagName("Lokacio").item(0);
                Element allapot = (Element) savElem.getElementsByTagName("Allapot").item(0);

                String utNev = lokacio.getAttribute("ut");
                int szakaszIdx = Integer.parseInt(lokacio.getAttribute("szakasz")) - 1;
                int savIdx = Integer.parseInt(lokacio.getAttribute("sav")) - 1;

                Ut celUt = utMap.get(utNev);
                
                if (celUt != null && szakaszIdx >= 0 && szakaszIdx < celUt.getSzakaszok().size()) {
                    List<Sav> szakaszSavjai = celUt.getSzakaszok().get(szakaszIdx);
                    
                    if (savIdx >= 0 && savIdx < szakaszSavjai.size()) {
                        Sav celSav = szakaszSavjai.get(savIdx);

                        int ho = Integer.parseInt(allapot.getAttribute("ho"));
                        int athaladok = Integer.parseInt(allapot.getAttribute("athaladokszama"));
                        int so = Integer.parseInt(allapot.getAttribute("so"));
                        
                        boolean zuzalek = allapot.getAttribute("zuzalek").equals("1") || 
                                          allapot.getAttribute("zuzalek").equalsIgnoreCase("true");
                        
                        boolean jeges = allapot.hasAttribute("jeges") && 
                                        (allapot.getAttribute("jeges").equals("1") || 
                                         allapot.getAttribute("jeges").equalsIgnoreCase("true"));

                        celSav.adatokFrissitese(ho, athaladok, so, zuzalek, jeges);
                    }
                }
            }

            System.out.println(">>> [RENDSZER] Terkep betoltve: " + utMap.size() + " ut es elagazasai feldolgozva.");

        } catch (Exception e) {
            System.err.println(">>> [HIBA] Nem sikerült a térkép betöltése: " + e.getMessage());
            e.printStackTrace();
        }

        return terkep;
    }
}