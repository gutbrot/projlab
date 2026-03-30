package jatekos;

import java.util.ArrayList;
import jarmu.Busz;
import java.util.List;

public class Buszvezeto extends Jatekos {
	private final List<Busz> iranyithatoBuszok = new ArrayList<>();
	private int pont;
	
	public Buszvezeto(int akcioPont) {
	    super(akcioPont);
	}
	
	public void hozzaadBusz(Busz busz) {
	    if (busz != null) iranyithatoBuszok.add(busz);
	}

	public Busz buszValaszt() {
	    return iranyithatoBuszok.isEmpty() ? null : iranyithatoBuszok.get(0);
	}

	public boolean buszMozgat(Busz busz) {
	    if (busz == null || !iranyithatoBuszok.contains(busz) || akcioPont <= 0) return false;
	    akcioPont--;
	    return true;
	}

	public void pontotKap() {
	    pont++;
	}
	
	public int getPont() { return pont; }
	public List<Busz> getIranyithatoBuszok() { return iranyithatoBuszok; }
}