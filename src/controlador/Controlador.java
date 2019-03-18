package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Modelo;
import vista.Vista;

public class Controlador implements ActionListener {

    private Vista vista;
    private Modelo modelo;
    
    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.vista.btnAbrir.addActionListener(this);
    }
    
    public void iniciar() {
        vista.setTitle("Deteccion y correccion de errores");
        vista.setLocationRelativeTo(null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.btnAbrir) {
            
        }
    }
    
}
