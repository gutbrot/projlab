package jatekos;

public abstract class Jatekos {
    protected int akcioPont;
    
    protected Jatekos(int akcioPont) {
        this.akcioPont = akcioPont;
    }
    
    public void korVege() {
    	akcioPont = 0;
    }
    
    public void akcioPontKezelo(int p){
    	akcioPont -= p;
    	if (akcioPont < 0) akcioPont = 0;
    }
    
    public int getAkcioPont() { return akcioPont; }
}