package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
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
            File archivo = vista.abrirChooser();
            if (archivo != null) {
                modelo.setArchivo(archivo);
                vistaDeteccion.txtNombreArchivo.setText(modelo.getNombreConExtension());
                if (modelo.getInfoArchivo().compareTo("") == 0) {
                    System.out.println("Frase invalida. Por favor, corriga la frase.");
                } else {
                    vistaDeteccion.areaEntrada.setText(modelo.getInfoArchivo());
                    String resultado = modelo.convertirFraseaBinario();
                    vistaDeteccion.areaSalida.setText(resultado);
                }
            }
        }
    }
}
