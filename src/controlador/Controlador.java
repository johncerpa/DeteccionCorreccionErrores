package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Modelo;
import vista.Vista;
import vista.VistaDeteccion;

public class Controlador implements ActionListener {

    private Vista vista;
    private VistaDeteccion vistaDeteccion;
    private Modelo modelo;

    public Controlador(Vista vista, Modelo modelo, VistaDeteccion vistaDeteccion) {
        this.vista = vista;
        this.modelo = modelo;
        this.vistaDeteccion = vistaDeteccion;
        this.vista.btnDeteccion.addActionListener(this);
        this.vistaDeteccion.btnAbrir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Deteccion y correccion de errores");
        vista.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.vista.btnDeteccion) {
            vistaDeteccion.setTitle("Deteccion de errores");
            vistaDeteccion.setLocationRelativeTo(null);
            vistaDeteccion.setVisible(true);
            vistaDeteccion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if (e.getSource() == this.vistaDeteccion.btnAbrir) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Escoja el archivo");

            FileFilter filtro_txt = new FileNameExtensionFilter("Solo archivos .txt", "txt");
            chooser.setFileFilter(filtro_txt);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                modelo.setArchivo(chooser.getSelectedFile());
                vistaDeteccion.areaEntrada.setText(modelo.getContenido());
            } else {
                System.out.println("Por favor, seleccione el archivo.");
            }
        }
    }

}
