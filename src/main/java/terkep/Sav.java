package terkep;

import jarmu.*;

public class Sav {
    private int hoVastagsag;
    private boolean vanRoncs;
    private int soMennyiseg;
    private int athaladokSzama;

    public boolean atjarhatoE(Jarmu j) {
        return !vanRoncs && hoVastagsag < 50; // Példa logika
    }
    
    public void soOlvadas() {
        if (soMennyiseg > 0) {
            hoVastagsag -= 10;
        }
    }

    public void setHoVastagsag(int h){
        hoVastagsag += h;
    }
}