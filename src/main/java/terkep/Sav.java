package terkep;

import jarmu.*;

public class Sav {
	private int hoVastagsag;
	private boolean vanEJarmu;
	private final int savSzama;
	private int athaladokSzama;
	private int soMennyiseg;
	private boolean jegesE;

	public Sav(int savSzama) {
	    this.savSzama = savSzama;
	}

	public boolean atjarhatoE(Jarmu jarmu) {
	    return !vanEJarmu && hoVastagsag < 30 && (!jegesE || jarmu != null);
	}
	
    public void soOlvadas() {
        if (soMennyiseg > 0) {
            hoVastagsag -= 10;
        }
    }

    public void setHo(int h){ hoVastagsag += h; }
    public boolean jegesE() { return jegesE; }
    public void setJeges(boolean jeges) { this.jegesE = jeges; }
    public int getHo() { return hoVastagsag; }
    public boolean isVanEJarmu() { return vanEJarmu; }
    public void setVanEJarmu(boolean vanEJarmu) { this.vanEJarmu = vanEJarmu; }
    public int getSavSzama() { return savSzama; }
    public int getAthaladokSzama() { return athaladokSzama; }
    public void novelAthaladok() { athaladokSzama++; }
    public int getSoMennyiseg() { return soMennyiseg; }
}