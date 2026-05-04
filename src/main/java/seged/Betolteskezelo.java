package seged;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

import jarmu.*;
import terkep.*;
import jatekos.*;
import kotrofej.*;

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

            // 1. TÉRKÉP betöltése (TerkepLoader használatával)
            Terkep ujTerkep = TerkepLoader.betolt(fajlNev);
            if (ujTerkep == null) return false;
            jatekter.setTerkep(ujTerkep);

            // Járművek gyűjtője az ID alapú összekötéshez
            Map<String, Jarmu> jarmuAdattar = new HashMap<>();

            // 2. JÁRMŰVEK BEOLVASÁSA
            
            // --- HÓKOTRÓK ---
            NodeList hokotroNodes = doc.getElementsByTagName("Hokotro");
            for (int i = 0; i < hokotroNodes.getLength(); i++) {
                Element e = (Element) hokotroNodes.item(i);
                if (e.getParentNode().getNodeName().equals("Hokotrok")) {
                    String id = e.getAttribute("nev");
                    Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), ujTerkep);
                    Hokotro h = new Hokotro(id, ujTerkep, null);
                    h.setPozicio(pos);
                    if (pos != null && pos.getSav() != null) pos.getSav().setVanEJarmu(true);

                    // Eszköztár (Kotrófejek + Fogyóanyagok)
                    Element eszElem = (Element) e.getElementsByTagName("Eszkoztar").item(0);
                    if (eszElem != null) {
                        Element fejek = (Element) eszElem.getElementsByTagName("Kotrofejek").item(0);
                        if (fejek != null) {
                            setupFej(h, "sopro", fejek, new SoproFej(30));
                            setupFej(h, "hanyo", fejek, new HanyoFej(40));
                            setupFej(h, "sarkany", fejek, new SarkanyFej(100, 10));
                            setupFej(h, "jegtoro", fejek, new JegtoroFej(60));
                            setupFej(h, "soszoro", fejek, new SoszoroFej(50, 5));
                            setupFej(h, "zuzott", fejek, new ZuzottFej(50, 5));
                        }
                        Element fogyo = (Element) eszElem.getElementsByTagName("Fogyoanyagok").item(0);
                        if (fogyo != null) {
                            h.getEszkoztar().hozzaad("so", Integer.parseInt(fogyo.getAttribute("so")));
                            h.getEszkoztar().hozzaad("biokerozin", Integer.parseInt(fogyo.getAttribute("biokerozin")));
                            h.getEszkoztar().hozzaad("zuzalek", Integer.parseInt(fogyo.getAttribute("zuzalek")));
                        }
                    }
                    jatekter.hozzaadJarmu(h);
                    jarmuAdattar.put(id, h);
                }
            }

            // --- BUSZOK ---
            NodeList buszNodes = doc.getElementsByTagName("Busz");
            for (int i = 0; i < buszNodes.getLength(); i++) {
                Element e = (Element) buszNodes.item(i);
                if (e.getParentNode().getNodeName().equals("Buszok")) {
                    String id = e.getAttribute("nev");
                    Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), ujTerkep);
                    
                    // Végállomások keresése (Vegallomas vagy Cel tag alatt)
                    Lokacio v1 = parseLokacio((Element) e.getElementsByTagName("Vegallomas").item(0), ujTerkep);
                    if (v1 == null) v1 = parseLokacio((Element) e.getElementsByTagName("Cel").item(0), ujTerkep);
                    
                    Busz b = new Busz(id, pos, v1, null);
                    if (e.hasAttribute("mozgaskeptelen")) {
                        int mk = Integer.parseInt(e.getAttribute("mozgaskeptelen"));
                        for(int k=0; k<mk; k++) b.mozgasKeptelen();
                    }
                    if (pos != null && pos.getSav() != null) pos.getSav().setVanEJarmu(true);
                    jatekter.hozzaadJarmu(b);
                    jarmuAdattar.put(id, b);
                }
            }

            // --- NPC AUTÓK (ÚJ!) ---
            NodeList autoNodes = doc.getElementsByTagName("Auto");
            for (int i = 0; i < autoNodes.getLength(); i++) {
                Element e = (Element) autoNodes.item(i);
                if (e.getParentNode().getNodeName().equals("Autok")) {
                    String id = e.getAttribute("id");
                    Lokacio pos = parseLokacio((Element) e.getElementsByTagName("Lokacio").item(0), ujTerkep);
                    Lokacio lakas = parseLokacio((Element) e.getElementsByTagName("Lakas").item(0), ujTerkep);
                    Lokacio munka = parseLokacio((Element) e.getElementsByTagName("Munkahely").item(0), ujTerkep);
                    
                    Auto a = new Auto(id, lakas, munka, pos);
                    if (e.hasAttribute("mozgaskeptelen")) {
                        int mk = Integer.parseInt(e.getAttribute("mozgaskeptelen"));
                        for(int k=0; k<mk; k++) a.mozgasKeptelen();
                    }
                    if (pos != null && pos.getSav() != null) pos.getSav().setVanEJarmu(true);
                    jatekter.hozzaadJarmu(a);
                    jarmuAdattar.put(id, a);
                }
            }

            // 3. JÁTÉKOSOK ÉS ÖSSZEKÖTÉSEK
            
            // --- TAKARÍTÓK ---
            NodeList takNodes = doc.getElementsByTagName("Takarito");
            for (int i = 0; i < takNodes.getLength(); i++) {
                Element e = (Element) takNodes.item(i);
                Takarito t = new Takarito(Integer.parseInt(e.getAttribute("akcio")), Integer.parseInt(e.getAttribute("penz")));
                t.setNev(e.getAttribute("id"));
                t.getIranyitottHokotrok().clear();

                NodeList refs = e.getElementsByTagName("Hokotro");
                for (int j = 0; j < refs.getLength(); j++) {
                    String refId = ((Element)refs.item(j)).getAttribute("id");
                    if (jarmuAdattar.containsKey(refId)) t.hozzaadHokotro((Hokotro) jarmuAdattar.get(refId));
                }
                jatekter.hozzaadJatekos(t);
            }

            // --- BUSZVEZETŐK ---
            NodeList bvNodes = doc.getElementsByTagName("Buszvezeto");
            for (int i = 0; i < bvNodes.getLength(); i++) {
                Element e = (Element) bvNodes.item(i);
                Buszvezeto bv = new Buszvezeto(Integer.parseInt(e.getAttribute("akcio")));
                bv.setNev(e.getAttribute("id"));
                
                // Pontok visszaállítása (ciklussal, mert nincs közvetlen setter)
                int pont = Integer.parseInt(e.getAttribute("pont"));
                for(int p=0; p<pont; p++) bv.pontotKap();

                NodeList refs = e.getElementsByTagName("Bus");
                for (int j = 0; j < refs.getLength(); j++) {
                    String refId = ((Element)refs.item(j)).getAttribute("id");
                    if (jarmuAdattar.containsKey(refId)) {
                        Busz busz = (Busz) jarmuAdattar.get(refId);
                        bv.hozzaadBusz(busz);
                        busz.setVezeto(bv); // Oda-vissza összekötés
                    }
                }
                jatekter.hozzaadJatekos(bv);
            }

            return true;
        } catch (Exception e) {
            System.err.println(">>> [BETÖLTÉS HIBA] " + e.getMessage());
            return false;
        }
    }

    private void setupFej(Hokotro h, String tipus, Element fejekElem, KotroFej ujFej) {
        int kod = Integer.parseInt(fejekElem.getAttribute(tipus));
        if (kod == 1) h.getEszkoztar().hozzaadFej(ujFej);
        else if (kod == 2) h.fejcsere(ujFej);
    }

    private Lokacio parseLokacio(Element el, Terkep terkep) {
        if (el == null) return null;
        try {
            String utNev = el.getAttribute("ut");
            int szakIdx = Integer.parseInt(el.getAttribute("szakasz")) - 1;
            int savIdx = Integer.parseInt(el.getAttribute("sav")) - 1;
            for (Ut ut : terkep.getTeljesHalozat()) {
                if (ut.getNev().equals(utNev)) {
                    List<List<Sav>> szakaszok = ut.getSzakaszok();
                    if (szakIdx >= 0 && szakIdx < szakaszok.size()) {
                        List<Sav> savok = szakaszok.get(szakIdx);
                        if (savIdx >= 0 && savIdx < savok.size()) return new Lokacio(ut, savok, savok.get(savIdx));
                    }
                }
            }
        } catch (Exception ex) { return null; }
        return null;
    }
}