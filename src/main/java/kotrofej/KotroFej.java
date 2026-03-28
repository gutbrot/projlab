package kotrofej;

import jatekos.Takarito;

public abstract class KotroFej {
    private int ar; //Az az ár amennyiért a Takarító meg tudja vásárolni a kotró fejet a boltban

    public abstract void tisztit(Object cel, Object melle, Object ut);
    public abstract String getNev();
    
    public void atadVevonek(Takarito v) {
        // Alapértelmezett átadási logika
    }

    public int getAr() {
        return ar;
    }

    public KotroFej getKotroFej() {
        return this;
    }
}