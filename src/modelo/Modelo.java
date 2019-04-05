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
    
    public static Boolean charEsValido(char a) {
        return (a >= 65 && a <= 90) || (a >= 97 && a <= 122) || a == 58 || a == 59 || a == 44 || a == 46 || a == 32;
    }
    
}
