package controlador;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Modelo;
import vista.Vistas;

public class Controlador implements ActionListener {

    private final Vistas vistas;
    private final Modelo modelo;
    
    /**
     * Me permite saber que metodo se ha seleccionado para codificar.
     * Me hace mas facil el proceso de identificacion cuando se vaya a guardar la informacion que
     * ha sido cambiada --> se le han agregado errores.
     * Si es 'true', entonces es hamming, de lo contrario es agregado el bit de paridad.
     */
    private boolean hammingOrOther = true;

    public Controlador(Vistas vistas, Modelo modelo) {
        this.vistas = vistas;
        this.modelo = modelo;
        // --- BIND Vista Principal --- //
        vistas.vistaPrincipal.btnDeteccion.addActionListener(this);
        vistas.vistaPrincipal.btnCorreccion.addActionListener(this);
        // --- BIND Vista Deteccion --- //
        vistas.vistaDeteccion.btnUbicar.setEnabled(false);
        vistas.vistaDeteccion.btnEnviar.addActionListener(this);
        vistas.vistaDeteccion.btnReceptar.addActionListener(this);
        vistas.vistaDeteccion.btnUbicar.addActionListener(this);
        // --- BIND Vista Correccion --- //
        vistas.vistaCorreccion.btnUbicar.setEnabled(false);
        vistas.vistaCorreccion.btnEnviar.addActionListener(this);
        vistas.vistaCorreccion.btnReceptar.addActionListener(this);
        vistas.vistaCorreccion.btnUbicar.addActionListener(this);
        // --- BIND Vista Cambio de bits --- //
        vistas.vistaCambioBits.finishProcess.addActionListener(this);
    }

    public void iniciar() {
        vistas.vistaPrincipal.setTitle("Deteccion y correccion de errores - UniCode");
        vistas.vistaPrincipal.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // --- INTERFAZ PRINCIPAL --- //
        if (vistas.vistaPrincipal.btnDeteccion == e.getSource()) {
            vistas.vistaDeteccion.setTitle("Deteccion de errores con bit de paridad");
            vistas.vistaDeteccion.setLocationRelativeTo(null);
            vistas.vistaDeteccion.setVisible(true);
            vistas.vistaDeteccion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if (vistas.vistaPrincipal.btnCorreccion == e.getSource()) {
            vistas.vistaCorreccion.setTitle("Correccion de errores con Hamming");
            vistas.vistaCorreccion.setLocationRelativeTo(null);
            vistas.vistaCorreccion.setVisible(true);
            vistas.vistaCorreccion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        // --- INTERFAZ DETECCCION --- //
        if (vistas.vistaDeteccion.btnEnviar == e.getSource()) {
            
            File archivo = this.abrirChooser("txt");
            
            if (archivo != null) {
                
                modelo.modeloDBP.setArchivo(archivo);
                vistas.vistaDeteccion.txtNombreArchivo.setText(modelo.getNombreConExtension(modelo.modeloDBP.getArchivo()));
                String infoArchivo = modelo.modeloDBP.getInfoArchivo(true);
                
                if (infoArchivo.compareTo("") == 0) {
                
                    JOptionPane.showMessageDialog(null, "Frase invalida, por favor corriga la frase.");
                } else {
                    
                    vistas.vistaDeteccion.areaEntrada.setText(infoArchivo);
                    modelo.modeloDBP.str2Bin();
                    vistas.vistaDeteccion.btnUbicar.setEnabled(true);
                    vistas.vistaDeteccion.areaSalida.setText(modelo.modeloDBP.getCodeWord());
                    
                    hammingOrOther = false;
                    generarVistaCambio(modelo.modeloDBP.getCodeWordAsVector(), hammingOrOther);
                }
            }
        }

        if (vistas.vistaDeteccion.btnUbicar == e.getSource()) {
            String ubicacion = modelo.getUbicacionCarpeta(modelo.modeloDBP.getArchivo());
            try {
                Desktop.getDesktop().open(new File(ubicacion));
            } catch (IOException ex) {
                System.out.println("No se pudo abrir en el explorador la carpeta en esta ubicación.");
            }
        }

        if (vistas.vistaDeteccion.btnReceptar == e.getSource()) {
            File archivo = abrirChooser("btp");
            if (archivo != null) {
                modelo.modeloDBP.setArchivo(archivo);
                vistas.vistaDeteccion.txtNombreArchivo.setText(modelo.getNombreConExtension(modelo.modeloDBP.getArchivo()));
                String infoArchivo = modelo.modeloDBP.getInfoArchivo(false);
                if (infoArchivo.compareTo("") == 0) {
                    JOptionPane.showMessageDialog(null, "Archivo vacio");
                } else {
                    modelo.modeloDBP.setCodeWordWithErrors(infoArchivo);
                    if (modelo.modeloDBP.Sindrome()) {
                        JOptionPane.showMessageDialog(null, modelo.modeloDBP.error);
                    } else {
                        JOptionPane.showMessageDialog(null, "La información enviada se recibió de manera correcta.");
                        modelo.modeloDBP.setDataWord(modelo.modeloDBP.codeWordToDataWord());
                        try (PrintWriter pw = new PrintWriter(Modelo.getNombreSinExtension(modelo.modeloDBP.getArchivo()) + ".txt")) {
                            pw.print(modelo.modeloDBP.getDataWord());
                            pw.print(modelo.modeloDBP.bin2Str());
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }
                    }
                }
            }
            
        }

        // --- INTERFAZ CORRECCION --- //
        if (vistas.vistaCorreccion.btnEnviar == e.getSource()) {
            File archivo = abrirChooser("txt");
            if (archivo != null) {
                modelo.modeloHamming.setArchivo(archivo);
                // Mostrar archivo en IU
                vistas.vistaCorreccion.txtNombreArchivo.setText(modelo.getNombreConExtension(archivo));
                // Se guardan las palabras de datos en una lista
                String infoArchivo = modelo.modeloHamming.getInfoArchivo(archivo, 16);
                if (infoArchivo.compareTo("") == 0 || !modelo.modeloHamming.archivoValido) {
                    JOptionPane.showMessageDialog(null, modelo.modeloHamming.error);
                } else {
                    vistas.vistaCorreccion.areaEntrada.setText(infoArchivo);
                    // Procesar entrada (datos -> codigos y guardar en .ham) y mostrar en salida
                    modelo.modeloHamming.enviar(); // Convertir datos -> codigos
                    String salida = modelo.modeloHamming.listaParaImprimirPorLineas(modelo.modeloHamming.getPalabrasDeCodigo());
                    vistas.vistaCorreccion.areaSalida.setText(salida);
                    vistas.vistaCorreccion.btnUbicar.setEnabled(true);
                    
                    hammingOrOther = true;
                    ArrayList<String> listaCodeWords = modelo.modeloHamming.getPalabrasDeCodigo(); 
                    String[] v = listaCodeWords.toArray(new String[listaCodeWords.size()]);
                    generarVistaCambio(v, hammingOrOther);
                }
            }
        }

        if (vistas.vistaCorreccion.btnReceptar == e.getSource()) {
            File archivo = abrirChooser("ham");
            if (archivo != null) {
                modelo.modeloHamming.setArchivo(archivo);
                String infoArchivo = modelo.modeloHamming.getInfoArchivo(archivo, 21);
                vistas.vistaCorreccion.txtNombreArchivo.setText(modelo.getNombreConExtension(archivo));                
                if (infoArchivo.compareTo("") == 0 || !modelo.modeloHamming.archivoValido) {
                    JOptionPane.showMessageDialog(null, modelo.modeloHamming.error);
                } else {
                    vistas.vistaCorreccion.areaEntrada.setText(infoArchivo);
                    modelo.modeloHamming.receptar(modelo.modeloHamming.getPalabrasDeCodigo());
                    String salida = modelo.modeloHamming.listaParaImprimirPorLineas(modelo.modeloHamming.getPalabrasDeDato(), modelo.modeloHamming.getErrores());
                    vistas.vistaCorreccion.areaSalida.setText(salida);
                    vistas.vistaCorreccion.btnUbicar.setEnabled(true);
                }

            }
        }

        if (vistas.vistaCorreccion.btnUbicar == e.getSource()) {
            String ubicacion = modelo.getUbicacionCarpeta(modelo.modeloHamming.getArchivo());
            try {
                Desktop.getDesktop().open(new File(ubicacion));
            } catch (IOException ex) {
                System.out.println("No se pudo abrir en el explorador la carpeta en esta ubicación.");
            }
        }

        
        // --- INTERFAZ CAMBIO DE BITS PARA EL ENVIO DE LA INFORMACION --- //        
        
        if(vistas.vistaCambioBits.finishProcess == e.getSource()){
            
            String result = CodeWordWithErrors();
            
            if(hammingOrOther){//hamming
                
                try (PrintWriter pw = new PrintWriter(modelo.getNombreSinExtension(modelo.modeloHamming.getArchivo()) + ".ham", "UTF-8")) {
                
                    pw.write(result);
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
            }else{//Bit de paridad
                
                try (PrintWriter pw = new PrintWriter(modelo.getNombreSinExtension(modelo.modeloDBP.getArchivo()) + ".btp", "UTF-8")) {
                
                    pw.write(result);
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }
            }
            
            vistas.vistaCambioBits.setVisible(false);
            vistas.vistaCambioBits.dispose();
        }
    }

    
    public String CodeWordWithErrors(){
        
        int x = vistas.vistaCambioBits.getXLabel();
        int y = vistas.vistaCambioBits.getYLabel();
        String result = "";
        
        for(int i = 0; i < x; i++){
            
            for(int j=0; j < y; j++){
                
                result += vistas.vistaCambioBits.codeWord[i][j].getText();
            }
            
            result += "\n";
        }
        
        return result;
    }
    
    /**
     * Permite generar la vista en la cual se podra visualizar, y realizar, los cambios
     * de bits que se realicen al 'enviar' la informacion codificada por el medio
     * fisico.
     * @param codeWord --> Informacion que ha sido codificada por el medoto de 
     * hamming o simplemente por el bit de paridad. Este debe ser un vector para 
     * un manejo mucho mas facil.
     * @param hamming --> me indica cuando se ha aplicado el metodo de hamming para la 
     * codificacion, para asi saber de cuanto debe ser la matriz para mostrar la informacion
     * y su manejo.
     */
    public void generarVistaCambio(String[] codeWord, boolean hamming){
        
        if(hamming){
            
            System.out.println("Se ha elegido HAMMING");
            vistas.vistaCambioBits.setXY(codeWord.length, 22); // Genera la vista a mostrar
        }else{
            
            vistas.vistaCambioBits.setXY(codeWord.length, 129); // Genera la vista a mostrar
        }
        
        for(int i = 0; i < codeWord.length; i++){
            
            String info = codeWord[i];
            
            for(int j=0; j < info.length(); j++){
                
                vistas.vistaCambioBits.codeWord[i][j].setText( String.valueOf( info.charAt( j ) ) ); //Llenando la vista de informacion.
            }
        }
        
        vistas.vistaCambioBits.setVisible(true);
    }
    
    public File abrirChooser(String extension) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Escoja el archivo");
        FileFilter filtro_txt = new FileNameExtensionFilter("Solo archivos ." + extension, extension);
        chooser.setFileFilter(filtro_txt);
        return chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

}
