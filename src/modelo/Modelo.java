package modelo;

public class Modelo {
    
    public DeteccionBitParidad modeloDBP;
    public Hamming modeloHamming;
    
    public Modelo() {
        this.modeloDBP = new DeteccionBitParidad();
        this.modeloHamming = new Hamming();
    }
    
}
