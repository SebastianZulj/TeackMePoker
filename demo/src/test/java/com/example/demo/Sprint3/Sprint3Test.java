package com.example.demo.Sprint3;

import com.example.demo.aiClass.Ai;
import com.example.demo.controller.AIController;
import com.example.demo.controller.GameController;
import com.example.demo.controller.SPController;
import com.example.demo.controller.SettingsController;
import com.example.demo.gui.Sound;
import com.example.demo.gui.WinnerBox;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class Sprint3Test {


    /**
     * @author Fabian Kjellberg
     * This class tests test cases TF28 and displayWinner, aswell as well as testing methods for the class SPController
     */
    @BeforeAll
    static void setup() {
        // needed to start the javafx enviroment.
        Platform.startup(() -> {});
    }

    @Test
    void TF28(){

        //Creating necessary variables for the settings controller
        Sound sound = new Sound();
        CheckBox cbOn = new CheckBox();
        CheckBox cbOff = new CheckBox();
        ImageView ivSound = new ImageView();

        // creating and initializing the settings controller
        SettingsController settingsController = new SettingsController();
        settingsController.initMockController(sound, cbOn, cbOff, ivSound);

        //checking the initial state of the music
        settingsController.checkIfMuted();
        assertEquals("Stopped", sound.getSoundStatus(), "Initial sound status should be Stopped");

        //emulating clicking the mute button
        settingsController.soundSetting();

        //checking after clicking mute button
        assertEquals("Playing", sound.getSoundStatus(), "Sound should be Playing after unmuting");

        //emulating clicking the mute button
        settingsController.soundSetting();

        //checking again after clicking the mute button twice
        assertEquals("Stopped", sound.getSoundStatus(), "Sound should be Stopped after muting");
    }

    @Test
    void testAllCallOrFold_allFold(){
        SPController spController = new SPController();

        Ai ai1 = new Ai(5000,"ai1");
        Ai ai2 = new Ai(5000,"ai2");
        Ai ai3 = new Ai(5000,"ai3");

        ai1.setDecision("fold");
        ai2.setDecision("fold");
        ai3.setDecision("fold");

        List<Ai> aiPlayers= new ArrayList<>();

        aiPlayers.add(ai1);
        aiPlayers.add(ai2);
        aiPlayers.add(ai3);

        assertTrue(spController.allCallorFold(aiPlayers, "fold"));
    }

    @Test
    void testAllCallOrFold_allCall(){
        SPController spController = new SPController();

        Ai ai1 = new Ai(5000,"ai1");
        Ai ai2 = new Ai(5000,"ai2");
        Ai ai3 = new Ai(5000,"ai3");

        ai1.setDecision("call");
        ai2.setDecision("call");
        ai3.setDecision("call");

        List<Ai> aiPlayers= new ArrayList<>();

        aiPlayers.add(ai1);
        aiPlayers.add(ai2);
        aiPlayers.add(ai3);

        assertTrue(spController.allCallorFold(aiPlayers, "call"));
    }

    @Test
    void testAllCallOrFold_oneLive(){
        SPController spController = new SPController();

        Ai ai1 = new Ai(5000,"ai1");
        Ai ai2 = new Ai(5000,"ai2");
        Ai ai3 = new Ai(5000,"ai3");

        ai1.setDecision("live");
        ai2.setDecision("call");
        ai3.setDecision("call");

        List<Ai> aiPlayers= new ArrayList<>();

        aiPlayers.add(ai1);
        aiPlayers.add(ai2);
        aiPlayers.add(ai3);

        assertFalse(spController.allCallorFold(aiPlayers, "call"));
    }

    @Test
    void testAllCallOrFold_allLive(){
        SPController spController = new SPController();

        Ai ai1 = new Ai(5000,"ai1");
        Ai ai2 = new Ai(5000,"ai2");
        Ai ai3 = new Ai(5000,"ai3");

        ai1.setDecision("live");
        ai2.setDecision("live");
        ai3.setDecision("live");

        List<Ai> aiPlayers= new ArrayList<>();

        aiPlayers.add(ai1);
        aiPlayers.add(ai2);
        aiPlayers.add(ai3);

        assertFalse(spController.allCallorFold(aiPlayers, "live"));
    }

    //Test fail, no easy way to initiate testing and cannot create the needed variables to test the variable.
    @Test
    void testCheckWinner_playerWin(){
        GameController gameController = new GameController();
        SPController spController = new SPController();
        //gameController.setSPController(spController);
        gameController.initTestCase("hej");
        gameController.setUsername("player");

        Ai ai1 = new Ai(5000,"ai1");
        Ai ai2 = new Ai(5000,"ai2");
        Ai ai3 = new Ai(5000,"ai3");

        ai1.setDecision("live");
        ai2.setDecision("live");
        ai3.setDecision("live");

        List<Ai> aiPlayers= new ArrayList<>();

        aiPlayers.add(ai1);
        aiPlayers.add(ai2);
        aiPlayers.add(ai3);

        spController.checkWinner(gameController, aiPlayers);
    }

    @Test
    void test_askForPlayerDecision(){
        SPController spController = new SPController();
        GameController gController = new GameController();

        Ai ai1 = new Ai(5000,"ai1");
        Ai ai2 = new Ai(5000,"ai2");
        Ai ai3 = new Ai(5000,"ai3");

        ai1.setDecision("live");
        ai2.setDecision("live");
        ai3.setDecision("live");

        LinkedList<Ai> aiPlayers= new LinkedList<>();

        aiPlayers.add(ai1);
        aiPlayers.add(ai2);
        aiPlayers.add(ai3);

        gController.initTestCase("live");
        gController.setSPController(spController);
        gController.setAiPlayers(aiPlayers, true, 3);
        gController.askForPlayerDecision();
    }

    @Test
    void testtesttest(){
        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                WinnerBox winnerBox = new WinnerBox();
                System.out.println(winnerBox.displayWinner("hej", "hej", "hej", 2));
            } finally {
                latch.countDown();
            }
        });

        try {
            boolean await = latch.await(5, TimeUnit.SECONDS);
            assertTrue(await, "Timeout waiting for Platform.runLater()");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for Platform.runLater()", e);
        }

    }
}
