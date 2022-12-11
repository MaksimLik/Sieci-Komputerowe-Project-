package com.example.battleship;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;


public class GameViewController implements Initializable {
    @FXML
    public Button readyBtn;
    @FXML
    private RadioButton horizontalRB;
    @FXML
    private RadioButton verticalRB;
    @FXML
    private Label sizeLabel;
    @FXML
    private GridPane boardContainer;

    private Game game;

    private final Button[][] buttons = new Button[10][10];

    private int indexOfShip = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameHolder holder = GameHolder.getInstance();
        game = holder.getGame();

        setBoard();
        setRadioButtons();
        readyBtn.setVisible(false);

        sizeLabel.setText("Rozmiar: " + game.getPlayer().sizeOfShips[indexOfShip]);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                buttons[i][j].setOnAction(e -> {
                    if (indexOfShip < 5)
                    {
                        String id = ((Control)e.getSource()).getId();
                        Field clickedField = game.getBoard().getFieldById(id);
                        char dir = horizontalRB.isSelected() ? Constant.HORIZONTAL : Constant.VERTICAL;
                        // ustaw statek jeżeli jest to możliwe
                        if (game.putShipOnBoard(clickedField, dir, game.getPlayer().sizeOfShips[indexOfShip]))
                        {
                            updateUI();
                            sizeLabel.setText("Rozmiar: " + game.getPlayer().sizeOfShips[indexOfShip]);
                            indexOfShip++;
                            if (indexOfShip ==  5) {
                                readyBtn.setVisible(true);
                            }
                        }
                    }
                });
            }
        }

        readyBtn.setOnAction(e -> {
            String mess = game.getBoard().makeBoardAsMessageToServer();
         game.getPlayer().getClient().sendMessageToServer(mess);
        });



    }

    private void setBoard() {
        final int size = 11 ;
        for (int i = 1; i < size; i++) {
            int letter = 'A';
            char label_text = (char)(letter + i-1);
            Label l_r = new Label(String.valueOf("  " +label_text));
            Label l_c = new Label(String.valueOf(i));
            boardContainer.add(l_r, i, 0);
            boardContainer.add(l_c, 0, i);
        }
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                Button button = new Button();
                button.setId(String.valueOf((char) ('A' + i-1) + "" + j ));
                button.setStyle(
                        "-fx-pref-width: 25px;"+
                                "-fx-pref-height: 25px;" +
                                "-fx-background-color: radial-gradient(focus-distance 0% , center 50% 50% , radius 55% , #545498, #00d4ff);"

                );
                boardContainer.add(button, i, j);
                buttons[j-1][i-1] = button;
            }
        }
    }

    private void setRadioButtons() {
        ToggleGroup direction = new ToggleGroup();
        horizontalRB.setToggleGroup(direction);
        verticalRB.setToggleGroup(direction);
        horizontalRB.setSelected(true);

    }

    private void updateUI() {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (this.game.getBoard().boardGame[i][j].getState() == Constant.SHIP_PART) {
                        buttons[i][j].setStyle(
                                "-fx-pref-width: 25px;"+
                                        "-fx-pref-height: 25px;" +
                                        "-fx-background-color: black;"
                        );
                    }

                }
            }
    }
}


// else { // tu bug do naprawienia
//
//         String mess = game.getBoard().makeBoardAsMessageToServer();
//         System.out.println(mess.length());
//         game.getPlayer().getClient().sendMessageToServer(mess);
//         System.out.println(mess);
//
//         new Thread(() -> {
//final byte[] byteArr = new byte[20];
//        System.out.println("GameView nowy wątek");
//        while (true) {
//        try {
//        int len = game.getPlayer().getClient().bufferedReader.read(byteArr);
//        String buf = new String(byteArr, 0, len, StandardCharsets.UTF_8);
//        System.out.println(len);
//        if (buf.equals(Constant.READY)) {
//        // odpalam gre
//        System.out.println("Odpalam gre, napie...");
//        // nowy layout
//        } else if (buf.equals(Constant.REPEAT)) {
//        game.getPlayer().getClient().sendMessageToServer(mess);
//        }
//
//        } catch (IOException ex){
//        ex.printStackTrace();
//        }
//        }
//        }).start();
//
//        }