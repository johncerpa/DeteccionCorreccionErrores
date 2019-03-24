package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Modelo {
    
    private File archivo;
    
    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
    public String getContenido() {
        
        String contenido = "";
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            contenido = br.readLine();
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado " + ex.toString());
        } catch (IOException ex) {
            System.out.println("Error de lectura " + ex.toString());
        }
        
        return contenido;
    }
    
}
