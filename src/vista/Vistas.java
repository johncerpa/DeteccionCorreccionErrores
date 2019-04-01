package vista;

public class Vistas {
    
    public VistaPrincipal vistaPrincipal;
    public VistaDeteccion vistaDeteccion;
    public VistaCorreccion vistaCorreccion;
    
    public Vistas() {
        vistaPrincipal = new VistaPrincipal();
        vistaDeteccion = new VistaDeteccion();
        vistaCorreccion = new VistaCorreccion();
    }
    
}
