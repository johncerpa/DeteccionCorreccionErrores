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
    
    public static String getNombreSinExtension(File archivo) {
        int indice = archivo.getName().indexOf('.');
        return archivo.getName().substring(0, indice);
    }
    
}
