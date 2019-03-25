package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Modelo {

    private File archivo;
    private String contenidoArchivo;
    public boolean archivoValido;
    public String error;

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
    public String getNombreConExtension() {
        return archivo.getName();
    }

    public String getNombreSinExtension() {
        int indice = archivo.getName().indexOf('.');
        return archivo.getName().substring(0, indice);
    }

    public Boolean charEsValido(char a) {
        return (a >= 65 && a <= 90) || (a >= 97 && a <= 122) || a == 58 || a == 59 || a == 44 || a == 46 || a == 32;
    }

    public String getInfoArchivo() {

        String contenido;
        Boolean invalido = false;

        try {

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            contenido = br.readLine();

            for (int i = 0; i < contenido.length(); i++) {
                char letra = contenido.charAt(i);
                if (!charEsValido(letra)) {
                    invalido = true;
                }
            }

            if (!invalido) {
                contenidoArchivo = contenido;
                archivoValido = true;
                return contenido;
            }
            
            error = "La frase a enviar no es valida";
            
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado " + ex.toString());
            error = "El archivo no ha sido encontrado";
        } catch (IOException ex) {
            System.out.println("Error de lectura " + ex.toString());
            error = "Disculpe. Ocurrio un error de lectura";
        }

        return "";
    }

    // Se convierte la frase a binario y se guarda el archivo
    public String procesarFrase() {
        
        String resultado = "";
        byte[] bytes = contenidoArchivo.getBytes(StandardCharsets.US_ASCII); // Se obtienen los valores ASCII
        try (PrintWriter pw = new PrintWriter(getNombreSinExtension() + ".btp", "UTF-8")) {
            int cont = 0;
            for (int i = 0; i < bytes.length; i++) {
                String bin = Integer.toBinaryString(bytes[i]); // Se convierte a binario
                if (bin.length() < 8) {
                    String nuevo = "";
                    for (int j = bin.length(); j < 8; j++) {
                        nuevo += "0";
                    }
                    bin = nuevo + bin;
                }
                
                if (cont == 16) {
                    resultado += System.lineSeparator();
                    cont = 0;
                } 
                cont++;
                resultado += bin;
            }
            pw.print(resultado);            
            return resultado;
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex.toString());
            error = "El archivo no fue encontrado";
        }
        
        return "";
    }

}
