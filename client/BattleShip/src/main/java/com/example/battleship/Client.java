package com.example.battleship;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Client {
    Socket socket;
    InputStream bufferedReader;
     OutputStream bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = socket.getInputStream();
            this.bufferedWriter = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void sendMessageToServer(String message) {
        new Thread(() -> {
            byte[] messageBuf = message.getBytes(StandardCharsets.UTF_8);
            try {
                bufferedWriter.write(messageBuf);
                System.out.println("Message sent");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void closeAll(Socket socket, InputStream bufferedReader, OutputStream bufferedWriter) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
