/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se3309a.library;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Rzan
 */
public class AlertController implements Initializable {

    @FXML
    public Label error;
    protected BorderPane mainPane;
    protected Node newNode;
    protected Node hostingNode;

    public void init(BorderPane mainPane, Node newNode, Node hostingNode) {
        this.mainPane = mainPane;
        this.newNode = newNode;
        this.hostingNode = hostingNode;
    }

    public void setAlertText(String text) {
        // set text from another class
        error.setText(text);
    }

    public void exit() {
        mainPane.getChildren().remove(newNode);
        mainPane.setCenter(hostingNode);
        
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}
