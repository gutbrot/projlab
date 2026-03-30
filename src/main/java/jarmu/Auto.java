package jarmu;

import terkep.Lokacio;

public class Auto extends Jarmu{
	private final Lokacio[] vegallomasok = new Lokacio[2];

	public Auto(Lokacio elso, Lokacio masodik, Lokacio kezdo) {
	    super(kezdo);
	    vegallomasok[0] = elso;
	    vegallomasok[1] = masodik;
	}

	@Override
	public void utkozos() {
	    mozgasKeptelen();
	}

	public Lokacio[] getVegallomasok() { return vegallomasok.clone(); }
}
