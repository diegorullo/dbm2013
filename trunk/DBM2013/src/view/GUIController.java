package view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import dblp.Author;
import dblp.Corpus;
import dblp.Factory;
import dblp.Paper;
import exceptions.NoPaperWithSuchIDException;

public class GUIController implements Initializable {

	@FXML
	// fx:id="task1PaperIDTextField"
	private ComboBox<String> phase1Task1PaperIDComboBox; // Valore iniettato attraverso
												// FXMLLoader

	@FXML
	// fx:id="task1ModelloComboBox"
	private ComboBox<String> phase1Task1ModelComboBox; // Valore iniettato
													// attraverso FXMLLoader

	@FXML
	private Button phase1Task1ExecuteButton;
	
	@FXML
	private Label phase1Task1TitleLabel;

	@FXML
	private TextArea resultsTextArea;

	@Override
	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		Corpus dblp = Main.getDblp();
		ArrayList<Paper> papers = dblp.getPapers();
		ObservableList<String> papersIDs = FXCollections.observableArrayList();
		ObservableList<String> papersNames = FXCollections.observableArrayList();
		for (Paper p : papers) {
			papersIDs.add(p.getPaperID().toString());
			papersNames.add(p.getTitle().toString());
		}

		ArrayList<Author> authors = dblp.getAuthors();
		ObservableList<String> AuthorsIDs = FXCollections.observableArrayList();
		ObservableList<String> AuthorsNames = FXCollections.observableArrayList();
		for (Author a : authors) {
			papersIDs.add(a.getAuthorID().toString());
			papersNames.add(a.getName().toString());
		}


		
		assert phase1Task1PaperIDComboBox != null : "fx:id=\"phase1Task1PaperIDComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";
		assert phase1Task1ModelComboBox != null : "fx:id=\"phase1Task1ModelComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";

		phase1Task1PaperIDComboBox.setItems(papersIDs);
		phase1Task1ModelComboBox.getItems().clear();
		phase1Task1ModelComboBox.getItems().addAll("TF", "TFIDF");
		phase1Task1ModelComboBox.getSelectionModel().selectFirst();
		phase1Task1TitleLabel.setText("titolo del paper selezionato");
		//phase1Task1ModelComboBox.setItems(value);
		
		
		phase1Task1ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Factory f;
				try {
					//System.out.println("hai premuto submit (task 1)\n");
					resultsTextArea.clear();
					resultsTextArea.setText("Attendere prego!\n");
					String modello = phase1Task1ModelComboBox.getValue();
					String paperid = phase1Task1PaperIDComboBox.getValue();
					//resultsTextArea.appendText("hai premuto submit (task 1)\n"
					//		+ modello);

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
