package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
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
        } 
        
        
        if (e.getSource() == this.vistaDeteccion.btnAbrir) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Escoja el archivo");            
            
            FileFilter filtro_txt = new FileNameExtensionFilter("Solo archivos .txt", "txt");
            chooser.setFileFilter(filtro_txt);
                        
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
               
                try {
                    
                    File archivo = chooser.getSelectedFile();
                    BufferedReader br = new BufferedReader(new FileReader(archivo));
                    String linea = br.readLine();                    
                    
                } catch (FileNotFoundException ex1) {
                    System.out.println("No se encontro el archivo");
                } catch (IOException ex2) {
                    System.out.println("Ocurrio un problema con la lectura del archivo." + ex2.toString());
                }
                
            } else {
                System.out.println("Seleccione el archivo!");
            }
        }
    }
    
    
}
