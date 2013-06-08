package view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;;

public class GUIController
    implements Initializable {


    
	@FXML // fx:id="task1PaperIDTextField"
	private TextField task1PaperIDTextField; // Valore iniettato attraverso FXMLLoader

	@FXML // fx:id="task1ModelloComboBox"
	private ComboBox<String> task1ModelloComboBox; // Valore iniettato attraverso FXMLLoader

	@FXML 
	private Button task1EseguiButton;
	
	@FXML
	private TextArea resultsTextArea;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert task1PaperIDTextField != null : "fx:id=\"task1PaperIDTextField\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";
        assert task1ModelloComboBox != null : "fx:id=\"task1ModelloComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";

        task1ModelloComboBox.getItems().clear();
        task1ModelloComboBox.getItems().addAll("TF","TFIDF");
        
//        task1PaperIDTextField.selectAll();
        
        task1EseguiButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("hai premuto submit (task 1)");
                resultsTextArea.appendText("hai premuto submit (task 1)");
            }
        });



    }


}



