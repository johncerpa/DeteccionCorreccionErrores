package modelo;

import java.io.File;

public class Modelo {
    
    public DeteccionBitParidad modeloDBP;
    public Hamming modeloHamming;
    
    public Modelo() {
        this.modeloDBP = new DeteccionBitParidad();
        this.modeloHamming = new Hamming();
    }
    
    public String getUbicacionCarpeta(File archivo) {
        return archivo.getParentFile().getAbsolutePath();
    }
    
    public String getNombreConExtension(File archivo) {
        return archivo.getName();
    }
    
}
