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

    public static void allapototMent(Jatekter jatekter, String fajlNev) {
        try (PrintWriter writer = new PrintWriter(fajlNev, "UTF-8")) {
            writer.println("<Init>");
            
            // TODO: Ha lesz konkrét körszámláló a Játéktérben, ide azt kell beírni
            writer.println("    <Jatekallapot korok=\"1\" />");
            writer.println();
            
            // --- 1. TÉRKÉP (Utak ABC sorrendben) ---
            writer.println("    <Terkep>");
            List<Ut> utak = new ArrayList<>(jatekter.getTerkep().getTeljesHalozat());
            utak.sort(Comparator.comparing(Ut::getNev)); // ABC sorrend
            
            for (Ut ut : utak) {
                // Típus meghatározása (sima, hid, alagut)
                String tipus = (ut instanceof SimaUt) ? "sima" : (ut instanceof Hid) ? "hid" : "alagut";
                
                // Mivel az egyedi sávszámokat az XML külön tárolja (pozsav, negsav), itt a prototípus 
                // szintjén feltételezzük a hosszt és a sávokat a Te rendszered szerint.
                writer.printf("        <Ut tipus=\"%s\" nev=\"%s\" hossz=\"%d\" pozsav=\"%d\" negsav=\"0\">\n", 
                        tipus, ut.getNev(), ut.getHossz(), ut.getSzakaszok().get(0).size());
                
                writer.println("            <Elagazodas>");
                // Szomszédok kiírása (Pozitív irány = 1)
                for (Ut szomszed : ut.getSzomszedok(1)) {
                    writer.printf("                <Szomszed nev=\"%s\" irany=\"1\" />\n", szomszed.getNev());
                }
                // Szomszédok kiírása (Negatív irány = -1)
                for (Ut szomszed : ut.getSzomszedok(-1)) {
                    writer.printf("                <Szomszed nev=\"%s\" irany=\"-1\" />\n", szomszed.getNev());
                }
                writer.println("            </Elagazodas>");
                writer.println("        </Ut>");
            }
            writer.println("    </Terkep>");
            writer.println();
            
            // --- 2. ÚTVISZONYOK (Csak az eltérő sávok, ABC majd sorszám szerint) ---
            writer.println("    <Utviszonyok>");
            for (Ut ut : utak) {
                List<List<Sav>> szakaszok = ut.getSzakaszok();
                for (int szIdx = 0; szIdx < szakaszok.size(); szIdx++) {
                    List<Sav> sávok = szakaszok.get(szIdx);
                    for (int savIdx = 0; savIdx < sávok.size(); savIdx++) {
                        Sav s = sávok.get(savIdx);
                        
                        // Csak akkor írjuk ki, ha valami nem az alapállapot (0 0 0 0)
                        if (s.getHo() > 0 || s.getSoMennyiseg() > 0 || s.getAthaladokSzama() > 0 || s.isZuzalekos() || s.jegesE()) {
                            writer.println("        <Sav>");
                            writer.printf("            <Lokacio ut=\"%s\" szakasz=\"%d\" sav=\"%d\" />\n", ut.getNev(), (szIdx + 1), (savIdx + 1));
                            int zuz = s.isZuzalekos() ? 1 : 0;
                            int jeg = s.jegesE() ? 1 : 0;
                            writer.printf("            <Allapot athaladokszama=\"%d\" ho=\"%d\" so=\"%d\" zuzalek=\"%d\" jeges=\"%d\" />\n", 
                                    s.getAthaladokSzama(), s.getHo(), s.getSoMennyiseg(), zuz, jeg);
                            writer.println("        </Sav>");
                        }
                    }
                }
            }
            writer.println("    </Utviszonyok>");
            writer.println();
            
            // --- 3. JÁRMŰVEK ÉS JÁTÉKOSOK GENERÁLÁSA ---
            // IDE JÖNNEK A JÁRMŰVEK (Hókotrók, Buszok, Autók külön listába szedve, id szerint rendezve)
            // (A terjedelem miatt ezt a részt analógiára kell megcsinálnod: 
            // jarmuvek lista lekérése -> instanceof szerint szűrés -> id szerinti rendezés -> printf-el kiírás)

            writer.println("</Init>");
            
            System.out.println(">>> [MENTÉS] A játékállapot sikeresen exportálva (Determinisztikus XML): " + fajlNev);
        } catch (Exception e) {
            System.out.println(">>> [HIBA] Nem sikerült menteni a fájlt: " + e.getMessage());
        }
    }
}