package se3309a.library;

import com.jpro.webapi.JProApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibraryApplication extends JProApplication {

    @Override
    public void start(Stage stage) {
        // load user interface as FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/se3309a/library/fxml/library-view.fxml"));
        Scene scene = null;
        try {
            Parent root = loader.load();
            LibraryController controller = loader.getController();
            controller.init(this);

            // create JavaFX scene
            scene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Library");
        stage.setScene(scene);

        // open JavaFX window
        stage.show();
    }

    /**
     * Application entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
