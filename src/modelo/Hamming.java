package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Hamming {

    private File archivo;
    public boolean archivoValido;
    public String error;

    public void setArchivo(File archivo) {
        this.archivo = archivo;
        archivoValido = true;
    }

    public File getArchivo() {
        return archivo;
    }

    public String getInfoArchivo(File archivo) {
        String contenido = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido += linea;
            }
            // Comprueba validez del archivo (max. longitud: 16 y solo binario)
            if (contenido.length() <= 16) {
                for (int i = 0; i < contenido.length(); i++) {
                    if (contenido.charAt(i) != '0' && contenido.charAt(i) != '1') {
                        archivoValido = false;
                        error = "El archivo solo puede conter bits (0 y 1)";
                    }
                }
            } else {
                archivoValido = false;
                error = "El archivo debe contener maximo 16 bits";
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        if (contenido.compareToIgnoreCase("") == 0) {
            error = "Ocurrio un error de lectura con el archivo seleccionado. Intentelo de nuevo, por favor.";
        }
        return contenido;
    }

}
