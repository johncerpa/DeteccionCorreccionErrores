package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class DeteccionBitParidad {

    private File archivo;
    private String contenidoArchivo;
    public boolean archivoValido;
    public String error;
    private String dataWord;
    private String codeWord;
    private String codeWordWithErrors;

    public String getDataWord() {
        return dataWord;
    }

    public void setDataWord(String data) {
        dataWord = data;
    }

    public void setCodeWordWithErrors(String data) {
        codeWordWithErrors = data;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
    public File getArchivo() {
        return archivo;
    }    

    public String getNombreSinExtension() {
        int indice = archivo.getName().indexOf('.');
        return archivo.getName().substring(0, indice);
    }

    public Boolean charEsValido(char a) {
        return (a >= 65 && a <= 90) || (a >= 97 && a <= 122) || a == 58 || a == 59 || a == 44 || a == 46 || a == 32;
    }

    /**
     * Convierte la palabra de codigo que se ha enviado o la que se desee y la
     * transforma en una palabra de datos, es decir hace el proceso contrario.
     */
    public String codeWordToDataWord() {
        String result = "";
        for (String cw : getCodeWordWithErrorsAsVector()) {
            result += (cw.substring(0, cw.length() - 1) + System.lineSeparator());
        }
        return result;
    }

    /**
     * Permite obtener la palabra de codigo, completa, que ha sido enviada por
     * el 'medio fisico' para su posterior procesamiento.
     */
    public String getCodeWordWithErrors() {
        return codeWordWithErrors;
    }

    /**
     * Me retorna la palabra que ha sido enviada por el 'medio fisico', pero en
     * forma de vector, separando cada palabra de codigo para realizar un mejor
     * manejo de la informacion
     */
    public String[] getCodeWordWithErrorsAsVector() {
        return codeWordWithErrors.split(System.lineSeparator());
    }

    public String getCodeWord() {
        return codeWord;
    }

    public String[] getCodeWordAsVector() {
        return codeWord.split(System.lineSeparator());
    }

    public String getInfoArchivo(boolean comprobar) {
        String contenido = "";
        Boolean invalido = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (comprobar) {
                    contenido += linea;
                } else {
                    contenido += (linea + System.lineSeparator());
                }
            }
            if (comprobar) {
                for (int i = 0; i < contenido.length(); i++) {
                    char letra = contenido.charAt(i);
                    if (!charEsValido(letra)) {
                        invalido = true;
                    }
                }                
            }
            if (!invalido) {
                contenidoArchivo = contenido;
                archivoValido = true;
                return contenido;
            }
            error = "La frase a enviar no es valida";
        } catch (IOException ex) {
            System.out.println("Error de lectura " + ex.toString());
            error = "Disculpe. Ocurrio un error de lectura";
        }

        return "";
    }

    /**
     * Convierte un archivo de binarios a String, pero se debe tener en cuenta
     * que hace la conversion cada 8 bits hasta alcanzar una longitud de 128
     * bits(8 caracteres)
     * @return 
     */
    public String bin2Str() {
        String result = "";
        String[] lineas = getDataWord().split(System.lineSeparator());
        for (String code : lineas) {
            for (int i = 0; i < code.length(); i += 8) {
                int ascii = Integer.parseInt(code.substring(i, i + 8), 2);
                result += (new Character((char) ascii)).toString();
            }
        }
        return result;
    }

    /**
     * Convierte la frase a binario y se guarda el archivo. El proseso de
     * conversion se realiza cada 8 bits y se va guardando.
     * @return 
     */
    public String str2Bin() {
        String resultado = "";
        byte[] bytes = contenidoArchivo.getBytes(StandardCharsets.US_ASCII); // Se obtienen los valores ASCII
        try (PrintWriter pw = new PrintWriter(getNombreSinExtension() + ".btp", "UTF-8")) {
            int cont = 0;
            String binWord = ""; //Palabra de codigo por linea
            String bin;
            for (int i = 0; i < bytes.length; i++) {
                bin = Integer.toBinaryString(bytes[i]); // Se convierte a binario. Caracter por caracter.
                if (bin.length() < 8) {
                    for (int j = bin.length(); j < 8; j++) {
                        bin = "0" + bin;
                    }
                }
                if (cont == 16) {
                    binWord = binWord + Integer.toString(contarUnos(binWord) % 2); // Bit de paridad
                    resultado += binWord;
                    resultado += System.lineSeparator(); // Siguiente palabra de codigo
                    cont = 0;
                    binWord = "";
                }
                cont++;
                binWord += bin;//Agrego los caracteres binarios a la palabra de datos final.
            }
            if (cont > 0) {
                binWord = binWord + Integer.toString(contarUnos(binWord) % 2); // Bit de paridad
                resultado += binWord;
            }
            pw.print(resultado);
            codeWord = resultado; //Lo agrego para un mejor manejo del codeWord enviado
            return resultado;
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex.toString());
            error = "El archivo no fue encontrado";
        }

        return "";
    }

    /**
     * Me da el sindrome, por palabra de codigo enviada. En cuyo caso, me dice
     * si hay error o no.
     *
     * @return retorna un valor booleano, el cual me dice si descarto todo el
     * archivo, por errores, o no. Retornara 'false' en caso de que no se
     * encuentre ningun error Retornara 'true' en caso de que se encuentre
     * almenos un error en una palabra de codigo.
     */
    public boolean Sindrome() {
        for (String i : getCodeWordWithErrorsAsVector()) {
            if (contarUnos(i) % 2 != 0) {
                error = "Se ha(n) encontrado error(es) en el archivo con las palabra de codigos recibidas.";
                return true;
            }
        }
        return false;
    }
    
    public int contarUnos(String cadena) {
        int cantidad = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == '1') {
                cantidad++;
            }
        }        
        return cantidad;
    }
}
