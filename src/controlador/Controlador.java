package controlador;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Modelo;
import vista.VistaPrincipal;
import vista.VistaDeteccion;
import vista.Vistas;

public class Controlador implements ActionListener {

    private final Vistas vistas;
    private final Modelo modelo;

    public Controlador(Vistas vistas, Modelo modelo) {
        this.vistas = vistas;
        this.modelo = modelo;
        vistas.vistaDeteccion.btnUbicar.setEnabled(false);
        vistas.vistaPrincipal.btnDeteccion.addActionListener(this);
        vistas.vistaDeteccion.btnEnviar.addActionListener(this);
        vistas.vistaDeteccion.btnReceptar.addActionListener(this);
        vistas.vistaDeteccion.btnUbicar.addActionListener(this);
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
                vistas.vistaDeteccion.txtNombreArchivo.setText(modelo.modeloDBP.getNombreConExtension());
                String infoArchivo = modelo.modeloDBP.getInfoArchivo(true);
                if (infoArchivo.compareTo("") == 0) {
                    JOptionPane.showMessageDialog(null, "Frase invalida, por favo corriga la frase.");
                } else {
                    vistas.vistaDeteccion.areaEntrada.setText(infoArchivo);
                    modelo.modeloDBP.str2Bin();
                    vistas.vistaDeteccion.btnUbicar.setEnabled(true);
                    vistas.vistaDeteccion.areaSalida.setText(modelo.modeloDBP.getCodeWord());
                }
            }
        }
        
        if (vistas.vistaDeteccion.btnUbicar == e.getSource()) {
            String ubicacion = modelo.modeloDBP.getUbicacionCarpeta(modelo.modeloDBP.getArchivo());
            System.out.println(ubicacion);
            try {
                Desktop.getDesktop().open(new File(ubicacion));
            } catch (IOException ex) {
                System.out.println("No se pudo abrir en el explorador la carpeta en esta ubicación.");
            }
        }

        if (vistas.vistaDeteccion.btnReceptar == e.getSource()) {
            File archivo = this.abrirChooser("btp");
            if (archivo != null) {
                modelo.modeloDBP.setArchivo(archivo);
                vistas.vistaDeteccion.txtNombreArchivo.setText(modelo.modeloDBP.getNombreConExtension());
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
                        try (PrintWriter pw = new PrintWriter(modelo.modeloDBP.getNombreSinExtension() + ".txt")) {
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
        

    }

    /**
     * Menu que se encarga de seleccionar un archivo y retornarlo.
     * @param extension
     * @return File
     */
    public File abrirChooser(String extension) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Escoja el archivo");
        FileFilter filtro_txt = new FileNameExtensionFilter("Solo archivos ." + extension, extension);
        chooser.setFileFilter(filtro_txt);
        return chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

}
