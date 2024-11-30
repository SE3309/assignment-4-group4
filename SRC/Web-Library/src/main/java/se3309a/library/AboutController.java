/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se3309a.library;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class AboutController {

    @FXML
    Button okBtn;

    protected BorderPane mainPane;
    protected Node node;

    public void init(BorderPane mainPane, Node node)
    {
        this.mainPane = mainPane;
        this.node = node;
    }

    public void exit() {
        mainPane.getChildren().remove(node);
    }


//    public void exit() {
//        Stage stage = (Stage) okBtn.getScene().getWindow();
//        stage.close();
//    }

}
