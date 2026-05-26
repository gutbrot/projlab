package seged;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

import jarmu.*;
import terkep.*;
import jatekos.*;
import kotrofej.*;

/**
 * A Betolteskezelo osztály felelős a játékállapot XML fájlból történő hiánytalan visszaállításáért.
 * Kezeli a térképet, a járművek állapotát (eszköztár, mozgásképtelenség) és a játékosok adatait.
 */
public class Betolteskezelo {

    /**
     * Betölti a megadott XML fájlból a játék teljes állapotát a Játéktérbe
     */
    public boolean betolt(String fajlNev, Jatekter jatekter) {
        String eleresiUt = "Betoltes/" + fajlNev;
        File mentesFajl = new File(eleresiUt);

        //Fájl létezésének ellenőrzése
        if (!mentesFajl.exists()) {
            System.out.println(">>> [HIBA] A mentési fájl nem található: " + eleresiUt);
            return false;
        }

        //XML feldolgozás
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(mentesFajl);
            doc.getDocumentElement().normalize();

            //TÉRKÉP ÉS ÚTVISZONYOK BETÖLTÉSE
            //A TerkepLoader végzi az utak és a sávok alapállapotának beállítását
            Terkep ujTerkep = TerkepLoader.betolt(fajlNev);
            if (ujTerkep == null) return false;
            jatekter.setTerkep(ujTerkep);

            //Jármű adattár az ID alapú referenciák későbbi feloldásához
            Map<String, Jarmu> jarmuAdattar = new HashMap<>();

            //JÁRMŰVEK BEOLVASÁSA ÉS INICIALIZÁLÁSA
            NodeList jarmuvekNode = doc.getElementsByTagName("Jarmuvek");
            if (jarmuvekNode.getLength() > 0) {
                Element jarmuElem = (Element) jarmuvekNode.item(0);
                
                processHokotrok(jarmuElem, ujTerkep, jatekter, jarmuAdattar);
                processBuszok(jarmuElem, ujTerkep, jatekter, jarmuAdattar);
                processAutok(jarmuElem, ujTerkep, jatekter, jarmuAdattar);
            }

            //JÁTÉKOSOK BEOLVASÁSA ÉS ÖSSZEKÖTÉSE
            processJatekosok(doc, ujTerkep, jatekter, jarmuAdattar);

            System.out.println(">>> [BETÖLTÉS] Adatok sikeresen feldolgozva: " + jarmuAdattar.size() + " jármű.");
            return true;

        } catch (Exception e) {
            System.err.println(">>> [KRITIKUS HIBA] Hiba a betöltés során: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Járműtípusonkénti feldolgozás külön metódusokban a kód tisztasága és karbantarthatósága érdekében.
     * Minden járműtípusnál kezeljük a pozíciót, eszköztárat, mozgásképtelenséget és egyéb specifikus attribútumokat.
     */
    private void processHokotrok(Element root, Terkep terkep, Jatekter jatekter, Map<String, Jarmu> jarmuAdattar) {
        NodeList nodes = root.getElementsByTagName("Hokotro");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            if (!e.getParentNode().getNodeName().equals("Hokotrok")) continue;

            String id = e.getAttribute("nev");
            Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), terkep);
            
            Hokotro h = new Hokotro(id, terkep, null);
            if (pos != null) {
                h.setPozicio(pos);
                pos.getSav().setVanEJarmu(true);
            }

            //Eszköztár és fejek (0: nincs, 1: raktár, 2: felszerelt)
            NodeList eszNodes = e.getElementsByTagName("Eszkoztar");
            if (eszNodes.getLength() > 0) {
                Element esz = (Element) eszNodes.item(0);
                Element fejek = (Element) esz.getElementsByTagName("Kotrofejek").item(0);
                if (fejek != null) {
                    setupFej(h, "sopro", fejek, new SoproFej(30));
                    setupFej(h, "hanyo", fejek, new HanyoFej(40));
                    setupFej(h, "sarkany", fejek, new SarkanyFej(100, 10));
                    setupFej(h, "jegtoro", fejek, new JegtoroFej(60));
                    setupFej(h, "soszoro", fejek, new SoszoroFej(50, 5));
                    setupFej(h, "zuzott", fejek, new ZuzottFej(50, 5));
                }
                Element fogy = (Element) esz.getElementsByTagName("Fogyoanyagok").item(0);
                if (fogy != null) {
                    h.getEszkoztar().hozzaad("so", Integer.parseInt(fogy.getAttribute("so")));
                    h.getEszkoztar().hozzaad("biokerozin", Integer.parseInt(fogy.getAttribute("biokerozin")));
                    h.getEszkoztar().hozzaad("zuzalek", Integer.parseInt(fogy.getAttribute("zuzalek")));
                }
            }
            jatekter.hozzaadJarmu(h);
            jarmuAdattar.put(id, h);
        }
    }

    
    private void processBuszok(Element root, Terkep terkep, Jatekter jatekter, Map<String, Jarmu> jarmuAdattar) {
        NodeList nodes = root.getElementsByTagName("Busz");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            if (!e.getParentNode().getNodeName().equals("Buszok")) continue;

            String id = e.getAttribute("nev");
            Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), terkep);
            
            // Végállomások keresése
            Lokacio v1 = parseLokacio((Element) e.getElementsByTagName("Vegallomas").item(0), terkep);
            if (v1 == null) v1 = parseLokacio((Element) e.getElementsByTagName("Cel").item(0), terkep);

            Busz b = new Busz(id, pos, v1, null);
            if (e.hasAttribute("mozgaskeptelen")) {
                int mk = Integer.parseInt(e.getAttribute("mozgaskeptelen"));
                for(int k=0; k<mk; k++) b.mozgasKeptelen();
            }
            
            if (pos != null) pos.getSav().setVanEJarmu(true);
            jatekter.hozzaadJarmu(b);
            jarmuAdattar.put(id, b);
        }
    }

    /**
     * Csak az XML fájl &lt;Autok&gt; szekcióját tölti be a játéktérbe.
     * A grafikus menüből hívható, ahol a játékosok és a térkép már külön lett inicializálva.
     */
    public void betoltNpcAutok(String fajlNev, Terkep terkep, Jatekter jatekter) {
        String eleresiUt = "Betoltes/" + fajlNev;
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new java.io.File(eleresiUt));
            doc.getDocumentElement().normalize();
            NodeList jarmuvekNode = doc.getElementsByTagName("Jarmuvek");
            if (jarmuvekNode.getLength() == 0) return;
            Element jarmuElem = (Element) jarmuvekNode.item(0);
            processAutok(jarmuElem, terkep, jatekter, new HashMap<>());
        } catch (Exception e) {
            System.err.println(">>> [BETÖLTÉS] NPC autók betöltése sikertelen: " + e.getMessage());
        }
    }

    private void processAutok(Element root, Terkep terkep, Jatekter jatekter, Map<String, Jarmu> jarmuAdattar) {
        NodeList nodes = root.getElementsByTagName("Auto");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            if (!e.getParentNode().getNodeName().equals("Autok")) continue;

            String id = e.getAttribute("id");
            Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), terkep);
            
            // Adathiány kezelése (pl. A2 autó): ha nincs megadva helyzet, random lokációt kap
            if (pos == null) pos = terkep.getRandomLokacio();

            Lokacio lakas = parseLokacio((Element) e.getElementsByTagName("Lakas").item(0), terkep);
            Lokacio munka = parseLokacio((Element) e.getElementsByTagName("Munkahely").item(0), terkep);

            Auto a = new Auto(id, lakas, munka, pos);
            if (e.hasAttribute("mozgaskeptelen")) {
                int mk = Integer.parseInt(e.getAttribute("mozgaskeptelen"));
                for(int k=0; k<mk; k++) a.mozgasKeptelen();
            }

            if (pos != null) pos.getSav().setVanEJarmu(true);
            jatekter.hozzaadJarmu(a);
            jarmuAdattar.put(id, a);
        }
    }

    private void processJatekosok(Document doc, Terkep terkep, Jatekter jatekter, Map<String, Jarmu> jarmuAdattar) {
    //Takarítók
    NodeList takNodes = doc.getElementsByTagName("Takarito");
    for (int i = 0; i < takNodes.getLength(); i++) {
        Element e = (Element) takNodes.item(i);
        Takarito t = new Takarito(Integer.parseInt(e.getAttribute("akcio")), Integer.parseInt(e.getAttribute("penz")));
        t.setNev(e.getAttribute("id"));
        
        //ürítjük az alapértelmezett listát, hogy csak az kerüljön bele, ami az XML-ben van
        t.getIranyitottHokotrok().clear(); 

        NodeList hRefs = e.getElementsByTagName("Hokotro");
        for (int j = 0; j < hRefs.getLength(); j++) {
            String refId = ((Element) hRefs.item(j)).getAttribute("id");
            if (jarmuAdattar.containsKey(refId)) {
                t.hozzaadHokotro((Hokotro) jarmuAdattar.get(refId));
            }
        }
        jatekter.hozzaadJatekos(t);
    }

    // --- Buszvezetők ---
    NodeList bvNodes = doc.getElementsByTagName("Buszvezeto");
    for (int i = 0; i < bvNodes.getLength(); i++) {
        Element e = (Element) bvNodes.item(i);
        Buszvezeto bv = new Buszvezeto(Integer.parseInt(e.getAttribute("akcio")));
        bv.setNev(e.getAttribute("id"));
        
        // Pontszámok visszaállítása
        int pontok = e.hasAttribute("pont") ? Integer.parseInt(e.getAttribute("pont")) : 0;
        for (int p = 0; p < pontok; p++) bv.pontotKap();

        NodeList bRefs = e.getElementsByTagName("Busz"); 
        for (int j = 0; j < bRefs.getLength(); j++) {
            String refId = ((Element) bRefs.item(j)).getAttribute("id");
            
            if (jarmuAdattar.containsKey(refId)) {
                Busz busz = (Busz) jarmuAdattar.get(refId);
                bv.hozzaadBusz(busz);
                busz.setVezeto(bv); // Kétirányú kapcsolat beállítása
            }
        }
        jatekter.hozzaadJatekos(bv);
    }
}

    private void setupFej(Hokotro h, String tipus, Element fejekElem, KotroFej ujFej) {
        if (!fejekElem.hasAttribute(tipus)) return;
        int kod = Integer.parseInt(fejekElem.getAttribute(tipus));
        if (kod == 1) h.getEszkoztar().hozzaadFej(ujFej);
        else if (kod == 2) h.fejcsere(ujFej);
    }

    /**
     * XML Lokáció elem feldolgozása a negatív sávok logikájával.
     * Negatív sávérték esetén a sáv a pozitív sávok utáni tartományba képződik le.
     */
    private Lokacio parseLokacio(Element el, Terkep terkep) {
        if (el == null) return null;
        try {
            String utNev = el.getAttribute("ut");
            int szakIdx = Integer.parseInt(el.getAttribute("szakasz")) - 1;
            int savAttr = Integer.parseInt(el.getAttribute("sav"));

            for (Ut ut : terkep.getTeljesHalozat()) {
                if (ut.getNev().equals(utNev)) {
                    int savIdx;
                    // Kétirányú forgalom kezelése:
                    // Pozitív sávok (1, 2...): belső index 0, 1...
                    if (savAttr > 0) {
                        savIdx = savAttr - 1;
                    } 
                    // Negatív sávok (-1, -2...): belső index a pozitív sávok száma után kezdődik
                    else if (savAttr < 0) {
                        savIdx = ut.getPozSavokSzama() + Math.abs(savAttr) - 1;
                    } else return null;

                    if (szakIdx >= 0 && szakIdx < ut.getSzakaszok().size()) {
                        List<Sav> szakasz = ut.getSzakaszok().get(szakIdx);
                        if (savIdx >= 0 && savIdx < szakasz.size()) {
                            return new Lokacio(ut, szakasz, szakasz.get(savIdx));
                        }
                    }
                }
            }
        } catch (Exception ex) { return null; }
        return null;
    }
}