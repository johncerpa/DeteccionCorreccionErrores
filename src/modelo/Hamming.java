package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Hamming {

    private File archivo;
    public boolean archivoValido;
    public String error;
    private ArrayList<String> palabrasDeDato;
    private ArrayList<String> palabrasDeCodigo;

    public Hamming() {
        palabrasDeDato = new ArrayList();
        palabrasDeCodigo = new ArrayList();
    }

    public ArrayList<String> getPalabrasDeDato() {
        return palabrasDeDato;
    }

    public ArrayList<String> getPalabrasDeCodigo() {
        return palabrasDeCodigo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
        archivoValido = true;
    }

    public File getArchivo() {
        return archivo;
    }

    /* 
    * Lee las palabras de datos del archivo y las guarda en una lista
    * Verifica que las palabras de datos leidas sean correctas
     */
    public String getInfoArchivo(File archivo, int longitudMaxima) {
        String contenido = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            // Se reinician las listas
            palabrasDeDato.clear();
            palabrasDeCodigo.clear();
            OUTER:
            while ((linea = br.readLine()) != null) {
                switch (longitudMaxima) {
                    case 8:
                    case 16:
                        palabrasDeDato.add(linea);
                        break;
                    case 12:
                    case 21:
                        palabrasDeCodigo.add(linea);
                        break;
                    default:
                        archivoValido = false;
                        error = "Esta longitud no es permitida";
                        break OUTER;
                }
                contenido += (linea + System.lineSeparator());
                if (linea.length() == 8 || linea.length() == 16 || linea.length() == 12 || linea.length() == 21) {
                    for (int i = 0; i < linea.length(); i++) {
                        if (linea.charAt(i) != '0' && linea.charAt(i) != '1') {
                            archivoValido = false;
                            error = "El archivo solo puede contener bits (0 y 1)";
                            break;
                        }
                    }
                } else {
                    archivoValido = false;
                    error = "La longitud debe ser de maximo " + longitudMaxima + " bits";
                }
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

    public String listaParaImprimirPorLineas(ArrayList<String> lista) {
        String resultado = "";
        if (lista != null) {
            for (String p : lista) {
                resultado += (p + System.lineSeparator());
            }
        } else {
            System.out.println("ERROR: Lista nula");
        }
        return resultado;
    }

    public String getParidad(String data) {
        char[] b = new char[22];
        char[] b1 = new char[13];

        if (data.length() == 16) {
            b[3] = data.charAt(15);
            b[5] = data.charAt(14);
            b[6] = data.charAt(13);
            b[7] = data.charAt(12);
            b[9] = data.charAt(11);
            b[10] = data.charAt(10);
            b[11] = data.charAt(9);
            b[12] = data.charAt(8);
            b[13] = data.charAt(7);
            b[14] = data.charAt(6);
            b[15] = data.charAt(5);
            b[17] = data.charAt(3);
            b[18] = data.charAt(3);
            b[19] = data.charAt(2);
            b[20] = data.charAt(1);
            b[21] = data.charAt(0);

            //  Bits de paridad
            b[1] = XOR(b[3], b[5], b[7], b[9], b[11], b[13], b[15], b[18], b[19], b[21]);
            b[2] = XOR(b[3], b[6], b[7], b[10], b[11], b[14], b[15], b[18], b[19]);
            b[4] = XOR(b[5], b[6], b[7], b[12], b[13], b[14], b[15], b[20], b[21]);
            b[8] = XOR(b[9], b[10], b[11], b[12], b[13], b[14], b[15]);
            b[16] = XOR(b[17], b[18], b[19], b[20], b[21]);

            return "" + b[21] + b[20] + b[19] + b[18] + b[17] + b[16] + b[15] + b[14] + b[13] + b[12] + b[11] + b[10] + b[9] + b[8] + b[7] + b[6] + b[5] + b[4] + b[3] + b[2] + b[1];
        } else if (data.length() == 8) {
            b[3] = data.charAt(7);
            b[5] = data.charAt(6);
            b[6] = data.charAt(5);
            b[7] = data.charAt(4);
            b[9] = data.charAt(3);
            b[10] = data.charAt(2);
            b[11] = data.charAt(1);
            b[12] = data.charAt(0);

            // Bits de paridad
            b[1] = XOR(b[3], b[5], b[7], b[9], b[11]);
            b[2] = XOR(b[3], b[6], b[7], b[10], b[11]);
            b[4] = XOR(b[5], b[6], b[7], b[12]);
            b[8] = XOR(b[9], b[10], b[11], b[12]);

            return "" + b[12] + b[11] + b[10] + b[9] + b[8] + b[7] + b[6] + b[5] + b[4] + b[3] + b[2] + b[1];
        }
        return "";
    }

    public char XOR(char... b) {
        char ant = b[0];
        for (int i = 1; i < b.length; i++) {
            ant = ant == b[i] ? '0' : '1';
        }
        return ant;
    }

    public ArrayList<String> enviar() {
        palabrasDeCodigo = new ArrayList();
        String resultado = "";
        for (String palabra : palabrasDeDato) {
            String codigo = getParidad(palabra);
            palabrasDeCodigo.add(codigo);
            resultado += (codigo + System.lineSeparator());
        }
        try (PrintWriter pw = new PrintWriter(modelo.Modelo.getNombreSinExtension(archivo) + ".ham", "UTF-8")) {
            pw.print(resultado);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }
        return palabrasDeCodigo;
    }

    public void receptar(ArrayList<String> codigos) {
        palabrasDeDato.clear();
        String resultado = "";
        for (String codigo : codigos) {
            String dato = comprobarCodigo(codigo);
            palabrasDeDato.add(dato);
            resultado += (dato + System.lineSeparator());
        }
        try (PrintWriter pw = new PrintWriter("receivedData.txt", "UTF-8")) {
            pw.print(resultado);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String comprobarCodigo(String codigo) {
        String pDato = "";

        if (codigo.length() == 21) {
            char[] b = new char[22];
            for (int i = 1, j = 20; i < 22; i++) {
                b[i] = codigo.charAt(j);
                j--;
            }

            char c1, c2, c4, c8, c16;
            c1 = XOR(b[1], b[3], b[5], b[7], b[9], b[11], b[13], b[17], b[19], b[21]);
            c2 = XOR(b[2], b[3], b[6], b[7], b[10], b[11], b[14], b[15], b[18], b[19]);
            c4 = XOR(b[4], b[5], b[6], b[7], b[12], b[13], b[15], b[20], b[21]);
            c8 = XOR(b[8], b[9], b[10], b[11], b[12], b[13], b[14], b[15]);
            c16 = XOR(b[17], b[18], b[19], b[20], b[21]);

            int c = new BigInteger(("" + c16 + c8 + c4 + c2 + c1), 2).intValue();
            if (c == 0) {
                pDato = "" + b[21] + b[20] + b[19] + b[18] + b[17] + b[15] + b[14] + b[13] + b[12] + b[11] + b[10] + b[9] + b[7] + b[6] + b[5] + b[3];
            } else {
                b[c] = b[c] == '0' ? '1' : '0';
                pDato = "" + b[21] + b[20] + b[19] + b[18] + b[17] + b[15] + b[14] + b[13] + b[12] + b[11] + b[10] + b[9] + b[7] + b[6] + b[5] + b[3];
            }
        } else if (codigo.length() == 12) {
            char[] b = new char[13];
            b[1] = codigo.charAt(11);
            b[2] = codigo.charAt(10);
            b[3] = codigo.charAt(9);
            b[4] = codigo.charAt(8);
            b[5] = codigo.charAt(7);
            b[6] = codigo.charAt(6);
            b[7] = codigo.charAt(5);
            b[8] = codigo.charAt(4);
            b[9] = codigo.charAt(3);
            b[10] = codigo.charAt(2);
            b[11] = codigo.charAt(1);
            b[12] = codigo.charAt(0);
            char c1, c2, c4, c8;
            c1 = XOR(b[1], b[3], b[5], b[7], b[9], b[11]);
            c2 = XOR(b[2], b[3], b[6], b[7], b[10], b[11]);
            c4 = XOR(b[4], b[5], b[6], b[7], b[12]);
            c8 = XOR(b[8], b[9], b[10], b[11], b[12]);
            int c = new BigInteger(("" + c8 + c4 + c2 + c1), 2).intValue();
            if (c == 0) {
                pDato = "" + b[12] + b[11] + b[10] + b[9] + b[7] + b[6] + b[5] + b[3];
            } else {
                b[c] = b[c] == '0' ? '1' : '0';
                pDato = "" + b[12] + b[11] + b[10] + b[9] + b[7] + b[6] + b[5] + b[3];
            }
        }
        return pDato;
    }

}
