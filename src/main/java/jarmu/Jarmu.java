package jarmu;

import terkep.*;

public abstract class Jarmu {
	protected Lokacio pozicio;
	protected int mozgaskepetlenKorokSzama;
	
	protected Jarmu(Lokacio pozicio) {
	    this.pozicio = pozicio;
	}
	
	public boolean mozgas(Sav sav) {
	    if (mozgaskepetlenKorokSzama > 0) {
	        mozgaskepetlenKorokSzama--;
	        return false;
	    }
	    if (sav == null || !sav.atjarhatoE(this)) {
	        return false;
	    }
	    if (pozicio != null && pozicio.getSav() != null) {
	        pozicio.getSav().setVanEJarmu(false);
	    }
	    sav.setVanEJarmu(true);
	    sav.novelAthaladok();
	    return true;
	}

	public abstract void utkozos();

	public void mozgasKeptelen() {
	    mozgaskepetlenKorokSzama++;
	}

	public Lokacio getPozicio() { return pozicio; }
	public void setPozicio(Lokacio pozicio) { this.pozicio = pozicio; }  
}
