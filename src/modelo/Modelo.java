package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Modelo {

    private File archivo;
    private String contenidoArchivo;
    public boolean archivoValido;
    public String error;
    private String dataWord;
    private String codeWord;
    private String codeWordWithErrors = "";

    public String getDataWord() {
        return this.dataWord;
    }

    public void setDataWord(String data) {
        this.codeWord = data;
    }

    /**
     * Actualiza la informacion de la variable 'archivo'. Esta variable
     * contendra la informacion del archivo que ha sido tomado como archivo de
     * datos a enviar.
     */
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

    /**
     * Convierte la palabra de codigo que se ha enviado o la que se desee y la
     * transforma en una palabra de datos, es decir hace el proceso contrario.
     */
    public String codeWordToDataWord() {

        String result = "";

        for (String cw : getCodeWordWithErrorsAsVector()) {
            result += cw.substring(cw.length() - 1) + "\n";
        }

        return result;
    }

    /**
     * Permite obtener la palabra de codigo, completa, que ha sido enviada por
     * el 'medio fisico' para su posterior procesamiento.
     */
    public String getCodeWordWithErrors() {
        return this.codeWordWithErrors;
    }

    /**
     * Me retorna la palabra que ha sido enviada por el 'medio fisico', pero en
     * forma de vector, separando cada palabra de codigo para realizar un mejor
     * manejo de la informacion
     */
    public String[] getCodeWordWithErrorsAsVector() {
        return this.codeWordWithErrors.split("\n");
    }

    public String getCodeWord() {
        return this.codeWord;
    }

    public String[] getCodeWordAsVector() {
        return this.codeWord.split("\n");
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

    /**
     * Convierte un archivo de binarios a String, pero se debe tener en cuenta
     * que hace la conversion cada 8 bits hasta alcanzar una longitud de 128
     * bits(8 caracteres)
     */
    public String bin2Str() {

        String result = "";
        int ascii;

        for (String code : this.getCodeWord().split("\n")) {
            for (int i = 0; i < code.length(); i += 8) {
                ascii = Integer.parseInt(code.substring(i, i + 8), 2);
                result += (new Character((char) ascii)).toString();
            }
        }

        return result;
    }

    /**
     * Convierte la frase a binario y se guarda el archivo. El proseso de
     * conversion se realiza cada 8 bits y se va guardando.
     */
    public String str2Bin() {

        String resultado = "";
        byte[] bytes = contenidoArchivo.getBytes(StandardCharsets.US_ASCII); // Se obtienen los valores ASCII

        try (PrintWriter pw = new PrintWriter(getNombreSinExtension() + ".btp", "UTF-8")) {

            int cont = 0;

            String binWord = "";//Palabra de codigo por linea
            String bin = "";

            for (int i = 0; i < bytes.length; i++) {

                bin = Integer.toBinaryString(bytes[i]); // Se convierte a binario. Caracter por caracter.

                if (bin.length() < 8) {
                    for (int j = bin.length(); j < 8; j++) {
                        bin = "0" + bin;
                    }
                }

                if (cont == 16) {

                    resultado += binWord; //Agrego la palabra de datos
                    resultado += binWord.split("1").length % 2; //Palabra de codigo. Bit de paridad. 

                    resultado += "\n";
                    cont = 0;
                    binWord = "";
                }
                cont++;

                binWord += bin;//Agrego los caracteres binarios a la palabra de datos final.
            }
            
            pw.print(resultado);
            this.codeWord = resultado; //Lo agrego para un mejor manejo del codeWord enviado
            return resultado;

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex.toString());
            error = "El archivo no fue encontrado";
        }

        return "";
    }

    /**
     * 'Envia' la informacion por un medio fisico para poder transportarla hasta
     * otro lugar, el cual tendra un receptor que la analice. Se debe tener en
     * cuenta que al enviarla por este medio, cabe la posibilidad de que esta
     * pueda dañarse cuando se realize el transporte.
     */
    public void sendData() {

        for (String codeWord : this.getCodeWordAsVector()) {//Aca hago los cambios aleatorios.

            System.out.println("CodeWord: " + codeWord);

            if (this.randomNumber(0, 1) == 1) {//Si se cambia la palabra de codigo o no.
                int bitToChange = this.randomNumber(0, codeWord.length());//Posicion del bit a cambiar.
                String[] aux = codeWord.split("");//Separo el codeWord, para poder cambiar un bit.
                aux[bitToChange] = Integer.toString((Integer.parseInt(aux[bitToChange]) + 1) % 2);//Cambio el bit aleatorio.
                this.codeWordWithErrors += String.join("", aux) + "\n";
            } else { //Este else es en caso de que no se 'desee' cambiar un codeWord. Se debe recordar que todo es completamente aleatorio.
                this.codeWordWithErrors += codeWord + "\n";
            }
        }

        //Escribo el code word con los supuestos 'errores' de transporte en un 
        //archivo predefinido.
        try (PrintWriter pw = new PrintWriter("sentData.btp")) {
            pw.print(this.codeWordWithErrors);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            this.error = "Archivo no encontrado";
        }
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
        for (String i : this.getCodeWordWithErrorsAsVector()) {
            System.out.println(i);
            if (i.split("1").length % 2 == 1) {
                error = "Se ha(n) encontrado error(es) en el archivo con la palabra de codigos recibida.";
                return true;
            }
        }
        return false;
    }

    private int randomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
