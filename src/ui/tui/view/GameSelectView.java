package ui.tui.view;

public class GameSelectView extends TuiView {
    @Override
    public void draw(){

        System.out.println("""
                ================================
                          GAME SELECT
                ================================

                [1] Tic-Tac-Toe
                [0] Back
                """);
    }
}
