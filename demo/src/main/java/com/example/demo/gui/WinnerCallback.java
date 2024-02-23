package com.example.demo.gui;

/**
 * Interface used to inform SPController that the winner box has been closed.
 */
public interface WinnerCallback {
    void onWinnerBoxClosed(boolean okButtonClicked);
}
