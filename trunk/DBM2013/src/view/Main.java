package view;

import java.awt.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

   public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("dbm2013gui.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Progetto DBM 2013");
            primaryStage.show();
            

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
	@FXML
	// fx:id="task1PaperIDTextField"
	private TextField task1PaperIDTextField; // Valore iniettato attraverso FXMLLoader
 
	@FXML
	// fx:id="task1ModelloComboBox"
	private ComboBox<String> task1ModelloComboBox; // Valore iniettato attraverso FXMLLoader
 
	@FXML // Metodo chiamato attraverso FXMLLoader a inizializzazione completata
    void initialize() {    
        assert task1PaperIDTextField != null : "fx:id=\"task1PaperIDTextField\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";
        assert task1ModelloComboBox != null : "fx:id=\"task1ModelloComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";

        task1ModelloComboBox.getItems().clear();
        task1ModelloComboBox.getItems().addAll("TF","TFIDF");
        
        task1PaperIDTextField.setText("<inserire paperid>");
	}
}

