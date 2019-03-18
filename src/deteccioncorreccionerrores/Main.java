package deteccioncorreccionerrores;

import controlador.Controlador;
import modelo.Modelo;
import vista.Vista;
import vista.VistaDeteccion;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        VistaDeteccion vistaDeteccion = new VistaDeteccion();
        Controlador controlador = new Controlador(vista, modelo, vistaDeteccion);
        controlador.iniciar();
        vista.setVisible(true);
    }
}
