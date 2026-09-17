package model;

public class Player {
    private char marker; //Slaat het teken van de speler op.

    public Player(char marker) { //Draait wanneer je speler aanmaakt en teken aangeeft.
        this.marker = marker; //Slaat de teken op in de speler.
    }

    //get en set wordt gebruikt voor encapsulation (zie README.md)
    public char getMarker() { 
        return marker;
    }
}
