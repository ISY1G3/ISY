package model;

public class LegacyPlayer {
    private char marker; //Slaat het teken van de speler op.

    public LegacyPlayer(char marker) { //Draait wanneer je speler aanmaakt en teken aangeeft.
        this.marker = marker; //Slaat de teken op in de speler.
    }

    //get en set wordt gebruikt voor encapsulation (zie README.md)
    public char getMarker() { 
        return marker;
    }
}
