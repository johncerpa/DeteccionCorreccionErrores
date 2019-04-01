package deteccioncorreccionerrores;

import controlador.Controlador;
import modelo.Modelo;
import vista.Vistas;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vistas vistas = new Vistas();
        Controlador controlador = new Controlador(vistas, modelo);
        controlador.iniciar();
        vistas.vistaPrincipal.setVisible(true);
    }
}
