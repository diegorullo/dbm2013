package view;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import dblp.Author;
import dblp.Corpus;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoPaperWithSuchIDException;

public class GUIController implements Initializable {

	@FXML
	// fx:id="task1PaperIDTextField"
	private ComboBox<String> phase1Task1PaperIDComboBox; // Valore iniettato
															// attraverso
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
	private ComboBox<String> phase1Task2AuthorIDComboBox;

	@FXML
	// fx:id="task1ModelloComboBox"
	private ComboBox<String> phase1Task2ModelComboBox; // Valore iniettato
														// attraverso FXMLLoader

	@FXML
	private Button phase1Task2ExecuteButton;

	@FXML
	private Label phase1Task2TitleLabel;
	
	@FXML
	private Button phase1Task3ExecuteButton;

	@FXML
	private Label phase1Task3TitleLabel;

	@FXML
	private ComboBox<String> phase1Task3ModelComboBox;

	@FXML
	private ComboBox<String> phase1Task3AuthorIDComboBox;

	@FXML
	private TableView<String> exploreDBAuthorsTableView;
	
    @FXML
    private TableColumn<Author, String> colAuthors;

	@FXML
	private TableView<String> exploreDBPapersTableView;
	
    @FXML
    private TableColumn<Author, String> colPapers;
	
	@FXML
	private TextArea resultsTextArea;


	@Override
	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		Corpus dblp = Main.getDblp();
		ArrayList<Paper> papers = dblp.getPapers();
		ArrayList<Author> authors = dblp.getAuthors();
		
		ObservableList<String> papersIDs = FXCollections.observableArrayList();
		ObservableList<String> papersNames = FXCollections.observableArrayList();
		ObservableList<String> authorsIDs = FXCollections.observableArrayList();
		ObservableList<String> authorsNames = FXCollections.observableArrayList();
		
		// ExploreDB - control settings
		ObservableList<TableColumn<String, ?>> exploreDBAuthorsColumns = exploreDBAuthorsTableView.getColumns();
		exploreDBAuthorsColumns.clear();
		exploreDBAuthorsColumns.add(0, new TableColumn<String, String>("ID"));
		exploreDBAuthorsColumns.add(1, new TableColumn<String, String>("Name"));
		
		ObservableList<TableColumn<String, ?>> exploreDBPapersColumns = exploreDBAuthorsTableView.getColumns();
		exploreDBPapersColumns.clear();
		exploreDBPapersColumns.add(0, new TableColumn<String, String>("ID"));
		exploreDBPapersColumns.add(1, new TableColumn<String, String>("Title"));
		
		Integer paperID, authorID;
		String paperTitle, authorName;
		
		for (Paper p : papers) {
			paperID = p.getPaperID();
			paperTitle = p.getTitle();
			papersIDs.add(paperID.toString());
			papersNames.add(paperTitle);
		}		
		
		for (Author a : authors) {
			authorID = a.getAuthorID();
			authorName = a.getName();
			authorsIDs.add(authorID.toString());
			authorsNames.add(authorName);
		}
		
		// PHASE 1 - Task 2 - controls settings
		assert phase1Task1PaperIDComboBox != null : "fx:id=\"phase1Task1PaperIDComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";
		assert phase1Task1ModelComboBox != null : "fx:id=\"phase1Task1ModelComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";

		phase1Task1PaperIDComboBox.setItems(papersIDs);
		phase1Task1ModelComboBox.getItems().clear();
		phase1Task1ModelComboBox.getItems().addAll("TF", "TFIDF");
		phase1Task1ModelComboBox.getSelectionModel().selectFirst();
		phase1Task1TitleLabel.setText("titolo del paper selezionato");

		// PHASE 1 - Task 2 - controls settings
		phase1Task2AuthorIDComboBox.setItems(authorsIDs);
		phase1Task2ModelComboBox.getItems().clear();
		phase1Task2ModelComboBox.getItems().addAll("TF", "TFIDF");
		phase1Task2ModelComboBox.getSelectionModel().selectFirst();
		phase1Task2TitleLabel.setText("nome autore selezionato");
		
		// PHASE 1 - Task 1 - EVENT HANDLER
		phase1Task1ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// System.out.println("hai premuto submit (task 1)\n");
				resultsTextArea.clear();
				resultsTextArea.setText("Attendere prego!\n");
				String modello = phase1Task1ModelComboBox.getValue();
				String paperid = phase1Task1PaperIDComboBox.getValue();
				// resultsTextArea.appendText("hai premuto submit (task 1)\n"
				// + modello);

				if (paperid != null && !paperid.equals("")) {

					Corpus dblp = Main.getDblp();
					Paper paper = null;
					try {
						paper = dblp.getPaperByID(Integer.parseInt(paperid));
					} catch (NumberFormatException | NoPaperWithSuchIDException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String output = null;
					if (modello.equals("TF")) {
						TreeMap<String, Double> ks = null;
						try {
							ks = paper.getTFVector();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						output = "Keyword vector calculated with TF Model:\n"
								+ "-----------------------------------------------------\n"
								+ ks.toString();
					} else if (modello.equals("TFIDF")) {
						TreeMap<String, Double> ks = paper.getTFIDFVector(dblp);
						output = "Keyword vector calculated with TFIDF Model:\n"
								+ "-----------------------------------------------------\n"
								+ ks.toString();
					}

					resultsTextArea.setText(output);

				} else {
					resultsTextArea.setText("paperid non valido!");
				}

			}
		});

		phase1Task1PaperIDComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				String paperid = phase1Task1PaperIDComboBox.getValue();
				if (paperid != null && !paperid.equals("")) {

					Corpus dblp = Main.getDblp();
					Paper paper = null;
					try {
						paper = dblp.getPaperByID(Integer.parseInt(paperid));
						String title = paper.getTitle();
						phase1Task1TitleLabel.setText(title);

					} catch (NumberFormatException | NoPaperWithSuchIDException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					resultsTextArea.setText("paperid non valido!");
				}

			}
		});

		// PHASE 1 - Task 2 - EVENT HANDLER
		phase1Task2ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				resultsTextArea.clear();
				resultsTextArea.setText("Attendere prego!\n");
				String modello = phase1Task2ModelComboBox.getValue();
				String authorid = phase1Task2AuthorIDComboBox.getValue();

				if (authorid != null && !authorid.equals("")) {

					Corpus dblp = Main.getDblp();
					Author author = null;
					try {
						author = dblp.getAuthorByID(Integer.parseInt(authorid));
					} catch (NumberFormatException
							| NoAuthorsWithSuchIDException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					String output = null;
					if (modello.equals("TF")) {
						TreeMap<String, Double> ks = null;
						try {
							ks = author.getWeightedTFVector();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (AuthorWithoutPapersException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						output = "Weighted keyword vector calculated with TF Model:\n"
								+ "-----------------------------------------------------\n"
								+ ks.toString();
					} else if (modello.equals("TFIDF")) {
						TreeMap<String, Double> ks = author
								.getWeightedTFIDFVector(dblp);
						output = "Weighted keyword vector calculated with TFIDF Model:\n"
								+ "-----------------------------------------------------\n"
								+ ks.toString();
					}

					resultsTextArea.setText(output);

				} else {
					resultsTextArea.setText("authorid non valido!");
				}

			}
		});

		phase1Task2AuthorIDComboBox
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						String authorid = phase1Task2AuthorIDComboBox
								.getValue();
						if (authorid != null && !authorid.equals("")) {

							Corpus dblp = Main.getDblp();
							Author author = null;

							try {
								author = dblp.getAuthorByID(Integer
										.parseInt(authorid));
							} catch (NumberFormatException
									| NoAuthorsWithSuchIDException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String name = author.getName();
							phase1Task2TitleLabel.setText(name);

						} else {
							resultsTextArea.setText("authorid non valido!");
						}

					}
				});
		
		

	}

}