package view;


import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {

   public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }
   

    @Override
    public void start(Stage primaryStage) {
        try {
        	FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("dbm2013gui.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Progetto DBM 2013");
            primaryStage.show();
            
        

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

 

}

