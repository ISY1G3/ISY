package ui.tui.view;

public class MainMenuView extends TuiView {

    @Override
    public void draw() {

        System.out.println("""
                ================================
                           MAIN MENU
                ================================

                [1] Play Local Game
                [2] Play Online Game
                [3] Settings
                [0] Exit
                """);
    }
}