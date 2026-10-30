package ui.gui;

import network.ResultType;

public interface OnlineGameView {
    void refresh();
    void showResult(ResultType type, String comment);
}