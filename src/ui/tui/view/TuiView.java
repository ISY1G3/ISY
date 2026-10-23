package ui.tui.view;

public abstract class TuiView {

    // elke view moet zichzelf kunnen maken
    // renderer is niet meer nodig ivm v1
    // de views gaan zelf print statements gebruiken
    public abstract void draw();
}