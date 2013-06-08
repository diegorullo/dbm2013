package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import dblp.Corpus;
import dblp.Factory;
import dblp.Paper;
import exceptions.NoPaperWithSuchIDException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

;

public class GUIController implements Initializable {

	@FXML
	// fx:id="task1PaperIDTextField"
	private TextField task1PaperIDTextField; // Valore iniettato attraverso
												// FXMLLoader

	@FXML
	// fx:id="task1ModelloComboBox"
	private ComboBox<String> task1ModelloComboBox; // Valore iniettato
													// attraverso FXMLLoader

	@FXML
	private Button task1EseguiButton;

	@FXML
	private TextArea resultsTextArea;

	@Override
	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		assert task1PaperIDTextField != null : "fx:id=\"task1PaperIDTextField\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";
		assert task1ModelloComboBox != null : "fx:id=\"task1ModelloComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";

		task1ModelloComboBox.getItems().clear();
		task1ModelloComboBox.getItems().addAll("TF", "TFIDF");
		task1ModelloComboBox.getSelectionModel().selectFirst();
		// task1PaperIDTextField.selectAll();

		task1EseguiButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Factory f;
				try {
					System.out.println("hai premuto submit (task 1)\n");
					resultsTextArea.clear();
					resultsTextArea.setText("Attendere prego!\n");
					String modello = task1ModelloComboBox.getValue();
					String paperid = task1PaperIDTextField.getText();
					resultsTextArea.appendText("hai premuto submit (task 1)\n"
							+ modello);

					if (paperid != null && !paperid.equals("")) {
						f = new Factory();
						Corpus dblp = f.getCorpus();
						Paper paper = dblp.getPaperByID(Integer.parseInt(paperid));
						String output = null;
						if (modello.equals("TF")) {
							TreeMap<String, Double> ks = paper.getTFVector();
							output = "Keyword vector calculate with TF Model:\n"
									+ "-----------------------------------------------------\n"
									+ ks.toString();
						} else if (modello.equals("TFIDF")) {
							TreeMap<String, Double> ks = paper.getTFIDFVector(dblp);
							output = "Keyword vector calculate with TFIDF Model:\n"
									+ "-----------------------------------------------------\n"
									+ ks.toString();
						}

						resultsTextArea.setText(output);

					}
					else {
						resultsTextArea.setText("paperid non valido!");
					}
					

				} catch (SQLException | MatlabConnectionException
						| MatlabInvocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoPaperWithSuchIDException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

}
