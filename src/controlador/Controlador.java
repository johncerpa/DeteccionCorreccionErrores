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

    public Controlador(Vista vista, Modelo modelo, VistaDeteccion vistaDeteccion) {
        
        this.vista = vista;
        this.modelo = modelo;
        this.vistaDeteccion = vistaDeteccion;
        this.vista.btnDeteccion.addActionListener(this);
        this.vistaDeteccion.btnAbrir.addActionListener(this);
        this.vistaDeteccion.btnEnviar.addActionListener(this);
        this.vistaDeteccion.btnReceptar.addActionListener(this);
    }

    public void iniciar() {
        
        vista.setTitle("Deteccion y correccion de errores");
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
            
            File archivo = this.abrirChooser();
            
            if (archivo != null) {
                
                modelo.setArchivo(archivo);
                vistaDeteccion.txtNombreArchivo.setText(modelo.getNombreConExtension());
                
                if (modelo.getInfoArchivo().compareTo("") == 0) {
                    
                    JOptionPane.showMessageDialog(null, "Frase invalida, por favo corriga la frase.");
                } else {
            
                    vistaDeteccion.areaEntrada.setText(modelo.getInfoArchivo());
                    
                    modelo.str2Bin();
                    vistaDeteccion.areaSalida.setText(modelo.getCodeWord());
                }
            }
        }

        if (this.vistaDeteccion.btnEnviar == e.getSource()) {
            System.out.println("hola mundo 11111111");
            if (modelo.archivoValido) {
                
                modelo.sendData();
            } else {
                
                JOptionPane.showMessageDialog(null, modelo.error + ". Por favor, seleccione el archivo a enviar.");
            }
        }
        
        if(this.vistaDeteccion.btnReceptar == e.getSource()){
            
            if(modelo.Sindrome()){
                
                JOptionPane.showMessageDialog(null, modelo.error);
            }else{
                
                modelo.setDataWord(modelo.codeWordToDataWord());
                
                try(PrintWriter pw = new PrintWriter("sendedData.txt")) {
                    
                    //pw.print(modelo.getCodeWord());
                    //pw.print(modelo.bin2Str());
                    
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        }

    }
    
    /**
     * Menu que se encarga de seleccionar un archivo y retornarlo.
     */
    public File abrirChooser() {
        
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Escoja el archivo");

        FileFilter filtro_txt = new FileNameExtensionFilter("Solo archivos .txt", "txt");
        chooser.setFileFilter(filtro_txt);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        
            return chooser.getSelectedFile();
        }
        
        return null;
    }
    
    

}
