package deteccioncorreccionerrores;

import controlador.Controlador;
import modelo.Modelo;
import vista.Vista;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(vista, modelo);
        controlador.iniciar();
        vista.setVisible(true);
    }
}
