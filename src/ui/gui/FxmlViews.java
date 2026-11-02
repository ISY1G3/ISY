package ui.gui;

import javafx.fxml.FXMLLoader;

import java.net.URL;

/** Maakt FXML-loaders voor resources die zowel in Maven als in de jar bestaan. */
public final class FxmlViews {
    private static final String RESOURCE_DIRECTORY = "/ui/gui/fxml/";

    private FxmlViews() {
    }

    public static FXMLLoader loader(String fileName) {
        URL resource = FxmlViews.class.getResource(RESOURCE_DIRECTORY + fileName);
        if (resource == null) {
            throw new IllegalArgumentException("FXML-resource niet gevonden: " + fileName);
        }
        return new FXMLLoader(resource);
    }
}
