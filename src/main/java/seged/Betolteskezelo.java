package seged;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

import jarmu.*;
import terkep.*;
import jatekos.*;

public class Betolteskezelo {

    public boolean betolt(String fajlNev, Jatekter jatekter) {
        String eleresiUt = "Betoltes/" + fajlNev;
        File mentesFajl = new File(eleresiUt);

        if (!mentesFajl.exists()) {
            System.out.println(">>> [HIBA] A fájl nem létezik: " + eleresiUt);
            return false;
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(mentesFajl);
            doc.getDocumentElement().normalize();

            // 1. TÉRKÉP (Kötelező)
            Terkep ujTerkep = TerkepLoader.betolt(fajlNev);
            if (ujTerkep == null) return false;
            jatekter.setTerkep(ujTerkep);

            Map<String, Jarmu> betoltottJarmuvek = new HashMap<>();

            // 2. JÁRMŰVEK BEOLVASÁSA
            NodeList jarmuvekNode = doc.getElementsByTagName("Jarmuvek");
            if (jarmuvekNode.getLength() > 0) {
                
                // --- Hókotrók ---
                NodeList hokotroNodes = doc.getElementsByTagName("Hokotro");
                for (int i = 0; i < hokotroNodes.getLength(); i++) {
                    Element e = (Element) hokotroNodes.item(i);
                    // Szűrés, hogy ne a Játékosok alatti referenciákat vegyük
                    if (e.getParentNode().getNodeName().equals("Hokotrok")) { 
                        String id = e.getAttribute("nev");
                        Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), ujTerkep);
                        
                        Hokotro h = new Hokotro(id, ujTerkep, null); 
                        h.setPozicio(pos);

                        if (pos != null && pos.getSav() != null) {
                            pos.getSav().setVanEJarmu(true);
                            // (Ha van rá külön metódusod, hogy a sáv is tudjon a járműről, azt itt hívd meg! pl: pos.getSav().setJarmu(h); )
                        }

                        jatekter.hozzaadJarmu(h);
                        betoltottJarmuvek.put(id, h);
                    }
                }
                
                // --- Buszok ---
                NodeList buszNodes = doc.getElementsByTagName("Busz");
                for (int i = 0; i < buszNodes.getLength(); i++) {
                    Element e = (Element) buszNodes.item(i);
                    if (e.getParentNode().getNodeName().equals("Buszok")) {
                        String id = e.getAttribute("nev");
                        Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), ujTerkep);
                        
                        // Végállomás vagy Cél megkeresése
                        Lokacio cel = null;
                        if (e.getElementsByTagName("Vegallomas").getLength() > 0) {
                            cel = parseLokacio((Element) e.getElementsByTagName("Vegallomas").item(0), ujTerkep);
                        } else if (e.getElementsByTagName("Cel").getLength() > 0) {
                            cel = parseLokacio((Element) e.getElementsByTagName("Cel").item(0), ujTerkep);
                        }
                        
                        Busz b = new Busz(id, pos, cel, null); 
                        if (pos != null && pos.getSav() != null) pos.getSav().setVanEJarmu(true);
                        
                        jatekter.hozzaadJarmu(b);
                        betoltottJarmuvek.put(id, b);
                    }
                }
                
                // --- Autók (NPC) ---
                NodeList autoNodes = doc.getElementsByTagName("Auto");
                for (int i = 0; i < autoNodes.getLength(); i++) {
                    Element e = (Element) autoNodes.item(i);
                    if (e.getParentNode().getNodeName().equals("Autok")) {
                        String id = e.getAttribute("id");
                        Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), ujTerkep);
                        Lokacio lakas = parseLokacio((Element) e.getElementsByTagName("Lakas").item(0), ujTerkep);
                        Lokacio munkahely = parseLokacio((Element) e.getElementsByTagName("Munkahely").item(0), ujTerkep);
                        
                        Auto a = new Auto(id, lakas, munkahely, pos);
                        if (pos != null && pos.getSav() != null) pos.getSav().setVanEJarmu(true);
                        
                        jatekter.hozzaadJarmu(a);
                        betoltottJarmuvek.put(id, a);
                    }
                }
            }

            // 3. JÁTÉKOSOK BEOLVASÁSA
            NodeList jatekosokNode = doc.getElementsByTagName("Jatekosok");
            if (jatekosokNode.getLength() > 0) {
                
                // --- Takarítók ---
                NodeList takaritoNodes = doc.getElementsByTagName("Takarito");
                for (int i = 0; i < takaritoNodes.getLength(); i++) {
                    Element e = (Element) takaritoNodes.item(i);
                    Takarito t = new Takarito(
                        Integer.parseInt(e.getAttribute("akcio")), 
                        Integer.parseInt(e.getAttribute("penz"))
                    );
                    t.setNev(e.getAttribute("id"));
                    
                    // JAVÍTÁS: Töröljük a konstruktor által generált "H_alap" hókotrót!
                    t.getIranyitottHokotrok().clear(); 
                    
                    // Járművek hozzárendelése
                    NodeList hRefs = e.getElementsByTagName("Hokotro");
                    for (int j = 0; j < hRefs.getLength(); j++) {
                        String refId = ((Element)hRefs.item(j)).getAttribute("id");
                        if (betoltottJarmuvek.containsKey(refId)) {
                            t.hozzaadHokotro((Hokotro) betoltottJarmuvek.get(refId));
                        }
                    }
                    jatekter.hozzaadJatekos(t);
                }
                
                // --- Buszvezetők ---
                NodeList buszvezetoNodes = doc.getElementsByTagName("Buszvezeto");
                for (int i = 0; i < buszvezetoNodes.getLength(); i++) {
                    Element e = (Element) buszvezetoNodes.item(i);
                    Buszvezeto b = new Buszvezeto(Integer.parseInt(e.getAttribute("akcio")));
                    b.setNev(e.getAttribute("id"));
                    
                    // Járművek hozzárendelése
                    NodeList bRefs = e.getElementsByTagName("Busz");
                    for (int j = 0; j < bRefs.getLength(); j++) {
                        String refId = ((Element)bRefs.item(j)).getAttribute("id");
                        if (betoltottJarmuvek.containsKey(refId)) {
                            Busz busz = (Busz) betoltottJarmuvek.get(refId);
                            b.hozzaadBusz(busz);
                            busz.setVezeto(b); // Oda-vissza összekötés
                        }
                    }
                    jatekter.hozzaadJatekos(b);
                }
            }

            System.out.println(">>> [BETÖLTÉS] XML feldolgozva. Talált adatok: " + 
                betoltottJarmuvek.size() + " jármű, " + jatekter.getJatekosok().size() + " játékos.");
            return true;

        } catch (Exception e) {
            System.out.println(">>> [HIBA] Hiba a betöltés során: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

     private Lokacio parseLokacio(Element el, Terkep terkep) {
        if (el == null) return null;
        try {
            String utNev = el.getAttribute("ut");
            int szakaszIdx = Integer.parseInt(el.getAttribute("szakasz")) - 1;
            
            // VISSZAÁLLÍTVA AZ EREDETI LOGIKÁDRA:
            int savIdx = Math.abs(Integer.parseInt(el.getAttribute("sav"))) - 1;

            for (Ut ut : terkep.getTeljesHalozat()) {
                if (ut.getNev().equals(utNev)) {
                    if (szakaszIdx >= 0 && szakaszIdx < ut.getSzakaszok().size()) {
                        List<Sav> szakasz = ut.getSzakaszok().get(szakaszIdx);
                        if (savIdx >= 0 && savIdx < szakasz.size()) {
                            return new Lokacio(ut, szakasz, szakasz.get(savIdx));
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}