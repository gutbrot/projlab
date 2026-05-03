package seged;

import jarmu.*;
import jatekos.*;
import terkep.*;
import kotrofej.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MentesKezelo {

    public static boolean allapototMent(Jatekter jatekter, String fajlNev) {
        try (PrintWriter writer = new PrintWriter(fajlNev, "UTF-8")) {
            writer.println("<Init>");
            writer.println("    <Jatekallapot korok=\"1\" />");
            writer.println();
            
            // --- 1. TÉRKÉP ---
            writer.println("    <Terkep>");
            List<Ut> utak = new ArrayList<>(jatekter.getTerkep().getTeljesHalozat());
            utak.sort(Comparator.comparing(Ut::getNev));
            
            for (Ut ut : utak) {
                String tipus = (ut instanceof SimaUt) ? "sima" : (ut instanceof Hid) ? "hid" : "alagut";
                writer.printf("        <Ut tipus=\"%s\" nev=\"%s\" hossz=\"%d\" pozsav=\"%d\" negsav=\"0\">\n", 
                        tipus, ut.getNev(), ut.getHossz(), ut.getSzakaszok().get(0).size());
                
                writer.println("            <Elagazodas>");
                for (Ut szomszed : ut.getSzomszedok(1)) {
                    writer.printf("                <Szomszed nev=\"%s\" irany=\"1\" />\n", szomszed.getNev());
                }
                for (Ut szomszed : ut.getSzomszedok(-1)) {
                    writer.printf("                <Szomszed nev=\"%s\" irany=\"-1\" />\n", szomszed.getNev());
                }
                writer.println("            </Elagazodas>");
                writer.println("        </Ut>");
            }
            writer.println("    </Terkep>");
            writer.println();
            
            // --- 2. ÚTVISZONYOK ---
            writer.println("    <!-- alapbol minden utszakasz 0 0 0 0 -->");
            writer.println("    <Utviszonyok>");
            for (Ut ut : utak) {
                boolean utKiirva = false;
                List<List<Sav>> szakaszok = ut.getSzakaszok();
                for (int szIdx = 0; szIdx < szakaszok.size(); szIdx++) {
                    List<Sav> sávok = szakaszok.get(szIdx);
                    for (int savIdx = 0; savIdx < sávok.size(); savIdx++) {
                        Sav s = sávok.get(savIdx);
                        if (s.getHo() > 0 || s.getSoMennyiseg() > 0 || s.getAthaladokSzama() > 0 || s.isZuzalekos() || s.jegesE()) {
                            if (!utKiirva) {
                                writer.println("        <!-- " + ut.getNev() + " -->");
                                utKiirva = true;
                            }
                            writer.println("        <Sav>");
                            writer.printf("            <Lokacio ut=\"%s\" szakasz=\"%d\" sav=\"%d\" />\n", ut.getNev(), (szIdx + 1), (savIdx + 1));
                            int zuz = s.isZuzalekos() ? 1 : 0;
                            int jeg = s.jegesE() ? 1 : 0;
                            // Az XML tesztfájlod alapján a "jeges" attribútum csak opcionális, de beletesszük ha van jég
                            if (s.jegesE()) {
                                writer.printf("            <Allapot athaladokszama=\"%d\" ho=\"%d\" so=\"%d\" zuzalek=\"%d\" jeges=\"1\" />\n", 
                                        s.getAthaladokSzama(), s.getHo(), s.getSoMennyiseg(), zuz);
                            } else {
                                writer.printf("            <Allapot athaladokszama=\"%d\" ho=\"%d\" so=\"%d\" zuzalek=\"%d\" />\n", 
                                        s.getAthaladokSzama(), s.getHo(), s.getSoMennyiseg(), zuz);
                            }
                            writer.println("        </Sav>");
                        }
                    }
                }
            }
            writer.println("    </Utviszonyok>");
            writer.println();
            
            // --- 3. JÁRMŰVEK ÉS JÁTÉKOSOK GENERÁLÁSA ---
            
            List<Hokotro> hokotrok = new ArrayList<>();
            List<Busz> buszok = new ArrayList<>();
            List<Auto> autok = new ArrayList<>();
            
            // Szétválogatjuk a járműveket
            if (jatekter.getJarmuvek() != null) {
                for (Jarmu j : jatekter.getJarmuvek()) {
                    if (j instanceof Hokotro) hokotrok.add((Hokotro) j);
                    else if (j instanceof Busz) buszok.add((Busz) j);
                    else if (j instanceof Auto) autok.add((Auto) j);
                }
            }
            
            // Név/ID szerinti rendezés a determinisztikus kimenethez
            hokotrok.sort(Comparator.comparing(Hokotro::getId));
            buszok.sort(Comparator.comparing(Busz::getId));
            autok.sort(Comparator.comparing(Auto::getId));
            
            writer.println("    <Jarmuvek>");
            
            // HÓKOTRÓK kiírása
            if (!hokotrok.isEmpty()) {
                writer.println("        <Hokotrok>");
                for (Hokotro h : hokotrok) {
                    writer.printf("            <Hokotro nev=\"%s\">\n", h.getId());
                    kiirLokacio(writer, "Lokacio", h.getPozicio(), "                ");
                    writer.println("                <Eszkoztar>");
                    writer.println("                    <!-- 0=nincs ilyen 1=van ilyen 2=felszerelt -->");
                    
                    // Kotrófejek állapotának kiszámítása (0, 1 vagy 2)
                    int sop = 0, han = 0, sar = 0, jeg = 0, sos = 0, zuz = 0;
                    
                    // Felszerelt fej (2-es érték)
                    KotroFej felsz = h.getFelszereltFej();
                    if (felsz instanceof SoproFej) sop = 2;
                    else if (felsz instanceof HanyoFej) han = 2;
                    else if (felsz instanceof SarkanyFej) sar = 2;
                    else if (felsz instanceof JegtoroFej) jeg = 2;
                    else if (felsz instanceof SoszoroFej) sos = 2;
                    else if (felsz instanceof ZuzottFej) zuz = 2;
                    
                    // Eszköztárban lévő fejek (1-es érték, ha még nem 2)
                    if (h.getEszkoztar() != null) {
                        for (KotroFej f : h.getEszkoztar().getKotroFejek()) {
                            if (f instanceof SoproFej && sop == 0) sop = 1;
                            else if (f instanceof HanyoFej && han == 0) han = 1;
                            else if (f instanceof SarkanyFej && sar == 0) sar = 1;
                            else if (f instanceof JegtoroFej && jeg == 0) jeg = 1;
                            else if (f instanceof SoszoroFej && sos == 0) sos = 1;
                            else if (f instanceof ZuzottFej && zuz == 0) zuz = 1;
                        }
                    }
                    
                    writer.printf("                    <Kotrofejek sopro=\"%d\" hanyo=\"%d\" sarkany=\"%d\" jegtoro=\"%d\" soszoro=\"%d\" zuzott=\"%d\" />\n", 
                            sop, han, sar, jeg, sos, zuz);
                    
                    int soKeszlet = h.getEszkoztar() != null ? h.getEszkoztar().getSoKeszlet() : 0;
                    int bioKeszlet = h.getEszkoztar() != null ? h.getEszkoztar().getBiokerozinKeszlet() : 0;
                    int zuzKeszlet = h.getEszkoztar() != null ? h.getEszkoztar().getZuzalekKeszlet() : 0;
                    
                    writer.printf("                    <Fogyoanyagok so=\"%d\" biokerozin=\"%d\" zuzalek=\"%d\" />\n", soKeszlet, bioKeszlet, zuzKeszlet);
                    writer.println("                </Eszkoztar>");
                    writer.println("             </Hokotro>");
                }
                writer.println("        </Hokotrok>");
            }
            
            // BUSZOK kiírása
            if (!buszok.isEmpty()) {
                writer.println("        <Buszok>");
                for (Busz b : buszok) {
                    writer.printf("            <Busz nev=\"%s\" mozgaskeptelen=\"%d\">\n", b.getId(), b.getMozgaskeptelenKorokSzama());
                    kiirLokacio(writer, "Lokacio", b.getPozicio(), "                ");
                    // Opcionálisan: Célok/Végállomások kiírása, ha azok le vannak mentve
                    writer.println("            </Busz>");
                }
                writer.println("        </Buszok>");
            }
            
            // AUTÓK kiírása
            if (!autok.isEmpty()) {
                writer.println("        <Autok>");
                for (Auto a : autok) {
                    writer.printf("            <Auto id=\"%s\" mozgaskeptelen=\"%d\">\n", a.getId(), a.getMozgaskeptelenKorokSzama());
                    kiirLokacio(writer, "Lokacio", a.getPozicio(), "                ");
                    Lokacio[] vegallomasok = a.getVegallomasok();
                    if (vegallomasok != null && vegallomasok.length >= 2) {
                        kiirLokacio(writer, "Lakas", vegallomasok[0], "                ");
                        kiirLokacio(writer, "Munkahely", vegallomasok[1], "                ");
                    }
                    writer.println("            </Auto>");
                }
                writer.println("        </Autok>");
            }
            writer.println("    </Jarmuvek>");
            
            // --- JÁTÉKOSOK KIÍRÁSA ---
            
            List<Takarito> takaritok = new ArrayList<>();
            List<Buszvezeto> vezetok = new ArrayList<>();
            
            if (jatekter.getJatekosok() != null) {
                for (Jatekos j : jatekter.getJatekosok()) {
                    if (j instanceof Takarito) takaritok.add((Takarito) j);
                    else if (j instanceof Buszvezeto) vezetok.add((Buszvezeto) j);
                }
            }
            
            // A Játékosok ősosztályában lévő getNev()-re érdemes rendezni, ha van ID, azt vedd
            takaritok.sort(Comparator.comparing(t -> t.getNev() != null ? t.getNev() : "T"));
            vezetok.sort(Comparator.comparing(b -> b.getNev() != null ? b.getNev() : "B"));
            
            writer.println("    <Jatekosok>");
            if (!takaritok.isEmpty()) {
                writer.println("        <Takaritok>");
                for (Takarito t : takaritok) {
                    String nev = t.getNev() != null ? t.getNev() : "Ismeretlen";
                    writer.printf("            <Takarito id=\"%s\" akcio=\"%d\" penz=\"%d\">\n", nev, t.getAkcioPont(), t.getPenz());
                    for (Hokotro iranyitott : t.getIranyitottHokotrok()) {
                        writer.printf("                <Hokotro id=\"%s\" />\n", iranyitott.getId());
                    }
                    writer.println("            </Takarito>");
                }
                writer.println("        </Takaritok>");
            }
            
            if (!vezetok.isEmpty()) {
                writer.println("        <Buszvezetok>");
                for (Buszvezeto b : vezetok) {
                    String nev = b.getNev() != null ? b.getNev() : "Ismeretlen";
                    writer.printf("            <Buszvezeto id=\"%s\" akcio=\"%d\" pont=\"%d\">\n", nev, b.getAkcioPont(), b.getPont());
                    for (Busz iranyitott : b.getIranyithatoBuszok()) {
                        writer.printf("                <Busz id=\"%s\" />\n", iranyitott.getId());
                    }
                    writer.println("            </Buszvezeto>");
                }
                writer.println("        </Buszvezetok>");
            }
            writer.println("    </Jatekosok>");

            writer.println("</Init>");
            
            System.out.println(">>> [MENTÉS] A játékállapot sikeresen exportálva (Determinisztikus XML): " + fajlNev);
            return true;
        } catch (Exception e) {
            System.out.println(">>> [HIBA] Nem sikerült menteni a fájlt: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Segédmetódus a Lokáció XML formátumú kiírásához.
     * Kiszámolja a szakasz és a sáv 1-es alapú indexét.
     */
    private static void kiirLokacio(PrintWriter writer, String tagName, Lokacio lok, String behuzas) {
        if (lok != null && lok.getUt() != null && lok.getSzakasz() != null && lok.getSav() != null) {
            int szakaszIdx = lok.getUt().getSzakaszok().indexOf(lok.getSzakasz()) + 1;
            int savIdx = lok.getSzakasz().indexOf(lok.getSav()) + 1;
            writer.printf("%s<%s ut=\"%s\" szakasz=\"%d\" sav=\"%d\" />\n", behuzas, tagName, lok.getUt().getNev(), szakaszIdx, savIdx);
        }
    }
}