package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Modelo;
import vista.Vista;
import vista.VistaDeteccion;

public class Controlador implements ActionListener {

    private final Vista vista;
    private final VistaDeteccion vistaDeteccion;
    private final Modelo modelo;

    public Controlador(Vista vista, VistaDeteccion vistaDeteccion, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.vistaDeteccion = vistaDeteccion;
        this.vista.btnDeteccion.addActionListener(this);
        this.vistaDeteccion.btnAbrir.addActionListener(this);
        this.vistaDeteccion.btnReceptar.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Deteccion y correccion de errores - UniCode");
        vista.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (this.vista.btnDeteccion == e.getSource()) {
            vistaDeteccion.setTitle("Deteccion de errores");
            vistaDeteccion.setLocationRelativeTo(null);
            vistaDeteccion.setVisible(true);
            vistaDeteccion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if (this.vistaDeteccion.btnAbrir == e.getSource()) {
            File archivo = this.abrirChooser("txt");
            if (archivo != null) {
                modelo.modeloDBP.setArchivo(archivo);
                vistaDeteccion.txtNombreArchivo.setText(modelo.modeloDBP.getNombreConExtension());
                String infoArchivo = modelo.modeloDBP.getInfoArchivo(true);
                if (infoArchivo.compareTo("") == 0) {
                    JOptionPane.showMessageDialog(null, "Frase invalida, por favo corriga la frase.");
                } else {
                    vistaDeteccion.areaEntrada.setText(infoArchivo);
                    modelo.modeloDBP.str2Bin();
                    vistaDeteccion.areaSalida.setText(modelo.modeloDBP.getCodeWord());
                }
            }
        }

        if (this.vistaDeteccion.btnReceptar == e.getSource()) {
            File archivo = this.abrirChooser("btp");
            if (archivo != null) {
                modelo.modeloDBP.setArchivo(archivo);
                vistaDeteccion.txtNombreArchivo.setText(modelo.modeloDBP.getNombreConExtension());
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
