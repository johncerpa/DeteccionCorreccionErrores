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
            boolean esPrimeraLinea = true;
            int longitud = 0;
            while ((linea = br.readLine()) != null) {
                contenido += linea;
                
                if (esPrimeraLinea) {
                    longitud = linea.length();
                    esPrimeraLinea = false;
                }
                
                if (linea.length() > 16) {
                    archivoValido = false;
                    error = "La longitud maxima es de 16 bits";
                    break;
                }
                
                if (!esPrimeraLinea && linea.length() != longitud) {
                    archivoValido = false;
                    error = "La longitud de todas las palabras de datos deben ser iguales.";
                    break;
                }
                
                for (int i = 0; i < linea.length(); i++) {
                    if (linea.charAt(i) != '0' && linea.charAt(i) != '1') {
                        archivoValido = false;
                        error = "El archivo solo puede contener bits (0 y 1)";
                        break;
                    }
                } 
                
                // En caso de que haya un caracter invalido
                if (!archivoValido) {
                    break;
                }
                
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
