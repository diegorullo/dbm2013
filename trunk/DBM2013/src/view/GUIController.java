package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;


import com.google.common.collect.Table;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import utils.IO;
import utils.Printer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import dblp.Author;
import dblp.Corpus;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoPaperWithSuchIDException;
import exceptions.NoSuchTechniqueException;
import exceptions.WrongClusteringException;

public class GUIController implements Initializable {

	private final static boolean PRINT_ON_CONSOLE = true;
	private final static boolean PRINT_ON_FILE = !true;

	// PHASE 1 - Task 1 - controls injection
	@FXML
	private ComboBox<String> phase1Task1PaperIDComboBox;
	@FXML
	private ComboBox<String> phase1Task1ModelComboBox;
	@FXML
	private Button phase1Task1ExecuteButton;
	@FXML
	private Label phase1Task1TitleLabel;

	// PHASE 1 - Task 2 - controls injection
	@FXML
	private ComboBox<String> phase1Task2AuthorIDComboBox;
	@FXML
	private ComboBox<String> phase1Task2ModelComboBox;
	@FXML
	private Button phase1Task2ExecuteButton;
	@FXML
	private Label phase1Task2TitleLabel;

	// PHASE 1 - Task 3 - controls injection
	@FXML
	private Button phase1Task3ExecuteButton;
	@FXML
	private Label phase1Task3TitleLabel;
	@FXML
	private ComboBox<String> phase1Task3ModelComboBox;
	@FXML
	private ComboBox<String> phase1Task3AuthorIDComboBox;

	// PHASE 2 - Task 1a - controls injection
	@FXML
	private Button phase2Task1aExecuteButton;
	@FXML
	private Label phase2Task1aTitleLabel;
	@FXML
	private ComboBox<String> phase2Task1aModelComboBox;
	@FXML
	private ComboBox<String> phase2Task1aAuthorIDComboBox;

	// PHASE 2 - Task 1b - controls injection
	@FXML
	private Button phase2Task1bExecuteButton;
	@FXML
	private Label phase2Task1bTitleLabel;
	@FXML
	private ComboBox<String> phase2Task1bModelComboBox;
	@FXML
	private ComboBox<String> phase2Task1bAuthorIDComboBox;
	
	// PHASE 2 - Task 1c - controls injection
	@FXML
	private Button phase2Task1cExecuteButton;
	@FXML
	private Label phase2Task1cTitleLabel;
	@FXML
	private ComboBox<String> phase2Task1cModelComboBox;
	@FXML
	private ComboBox<String> phase2Task1cAuthorIDComboBox;
	
	// PHASE 2 - Task 2a - controls injection
	@FXML
	private Button phase2Task2aExecuteButton;
	
	// PHASE 2 - Task 2b - controls injection
	@FXML
	private Button phase2Task2bExecuteButton;
	
	// PHASE 2 - Task 3a - controls injection
	@FXML
	private Button phase2Task3aExecuteButton;
	
	// PHASE 2 - Task 3b - controls injection
	@FXML
	private Button phase2Task3bExecuteButton;
		
	// PHASE 3 - Task 1 - controls injection
	@FXML
	private Button phase3Task1ExecuteButton;
	
	// PHASE 3 - Task 2 - controls injection
	@FXML
	private Button phase3Task2ExecuteButton;
	
	// PHASE 3 - Task 3 - controls injection
	
	@FXML
	private TextField phase3Task3TextField;
	
	@FXML
	private ComboBox<String> phase3Task3ComboBox;
	
	@FXML
	private Button phase3Task3ExecuteButton;
	
	// PHASE 3 - Task 4 - controls injection
		
	@FXML
	private TextField phase3Task4TextField;
		
	@FXML
	private Button phase3Task4ExecuteButton;
	
	// PHASE 3 - Task 5 - controls injection
	
	@FXML
	private TextField phase3Task5TextField;
	
	@FXML
	private ComboBox<String> phase3Task5ComboBox;
	
	@FXML
	private Button phase3Task5ExecuteButton;
	
	// PHASE 3 - Task 6 - controls injection
	
	@FXML
	private TextField phase3Task6TextField;
	
	@FXML
	private ComboBox<String> phase3Task6ComboBox;
	
	@FXML
	private Button phase3Task6ExecuteButton;
	
	// PHASE 3 - Task 7 - controls injection
	@FXML
	private Button phase3Task7ExecuteButton;
	
	// exploreDB - controls injection
	@FXML
	private TableView<String> exploreDBAuthorsTableView;
	@FXML
	private TableColumn<Author, String> colAuthors;
	@FXML
	private TableView<String> exploreDBPapersTableView;
	@FXML
	private TableColumn<Author, String> colPapers;

	// Output - controls injection
	
	@FXML
	private static GridPane	resultsGridPane;
	
	@FXML
	private static ListView<String> resultsListView;
	
	@FXML
	private static Label resultsLabel;

	//FIXME: Warning soppressi, ma bisogna ancora parametrizzare i riferimenti ai tipi generici
	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	
	@Override
	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		Corpus dblp = Main.getDblp();
		ArrayList<Paper> papers = dblp.getPapers();
		ArrayList<Author> authors = dblp.getAuthors();

		ObservableList<String> papersIDs = FXCollections.observableArrayList();
		ObservableList<String> papersNames = FXCollections
				.observableArrayList();
		ObservableList<String> authorsIDs = FXCollections.observableArrayList();
		ObservableList<String> authorsNames = FXCollections
				.observableArrayList();

		// ExploreDB - control settings
		ObservableList<TableColumn<String, ?>> exploreDBPapersColumns = exploreDBPapersTableView
				.getColumns();
		exploreDBPapersColumns.clear();
		
		//FIXME: parametrizzare i riferimenti ai tipi generici
		TableColumn paperIDTc = new TableColumn("paperID");
		paperIDTc.setCellValueFactory(new PropertyValueFactory("paperID"));
		paperIDTc.setPrefWidth(120);
		
		TableColumn titleTc = new TableColumn("title");
		titleTc.setCellValueFactory(new PropertyValueFactory("title"));
		titleTc.setPrefWidth(220);

		exploreDBPapersColumns.add(0,paperIDTc);
		exploreDBPapersColumns.add(1,titleTc);		

		ObservableList<TableColumn<String, ?>> exploreDBAuthorsColumns = exploreDBAuthorsTableView
				.getColumns();
		exploreDBAuthorsColumns.clear();
		
		TableColumn authorIDTc = new TableColumn("authorID");
		authorIDTc.setCellValueFactory(new PropertyValueFactory("authorID"));
		authorIDTc.setPrefWidth(120);
		
		TableColumn nameTc = new TableColumn("name");
		nameTc.setCellValueFactory(new PropertyValueFactory("name"));
		nameTc.setPrefWidth(220);

		exploreDBAuthorsColumns.add(0,authorIDTc);
		exploreDBAuthorsColumns.add(1,nameTc);

		Integer paperID, authorID;
		String paperTitle, authorName;

		ObservableList papersOL = FXCollections.observableArrayList();
		for (Paper p : papers) {
			paperID = p.getPaperID();
			paperTitle = p.getTitle();
			papersIDs.add(paperID.toString());
			papersNames.add(paperTitle);
			papersOL.add(p);
		}
		exploreDBPapersTableView.setItems(papersOL);
			
		ObservableList authorsOL = FXCollections.observableArrayList();		
		for (Author a : authors) {
			authorID = a.getAuthorID();
			authorName = a.getName();
			authorsIDs.add(authorID.toString());
			authorsNames.add(authorName);
			authorsOL.add(a);
		}
		exploreDBAuthorsTableView.setItems(authorsOL);

		// PHASE 1 - Task 1 - controls settings
		assert phase1Task1PaperIDComboBox != null : "fx:id=\"phase1Task1PaperIDComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";
		assert phase1Task1ModelComboBox != null : "fx:id=\"phase1Task1ModelComboBox\" non iniettato: controlla il file FXML 'dbm2013gui.fxml'.";

		phase1Task1PaperIDComboBox.setItems(papersIDs);
		phase1Task1ModelComboBox.getItems().clear();
		phase1Task1ModelComboBox.getItems().addAll("TF", "TFIDF");
		phase1Task1ModelComboBox.getSelectionModel().selectFirst();
		phase1Task1TitleLabel.setText("Titolo del paper selezionato");

		// PHASE 1 - Task 1 - EVENT HANDLER
		phase1Task1ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// System.out.println("hai premuto submit (task 1)\n");
				String modello = phase1Task1ModelComboBox.getValue();
				String paperid = phase1Task1PaperIDComboBox.getValue();
				// resultsTextArea.appendText("hai premuto submit (task 1)\n"
				// + modello);
				try {
					if (paperid != null && !paperid.equals("")) {
						Corpus dblp = Main.getDblp();
						Paper paper = dblp.getPaperByID(Integer.parseInt(paperid));
						ObservableList<String> resultsObservableList;
						ArrayList<String> resultsArrayList = new ArrayList<String>();
						
						if (modello.equals("TF")) {
							Set<Entry<String, Double>> entrySet = paper.getTFVector().entrySet();
							for(Entry<String, Double> e : entrySet) {
								resultsArrayList.add(e.getKey() + ":\t\t" + e.getValue());
							}
							resultsLabel.setText("Keyword vector (TF model) for paper " + paperid);

						} else if (modello.equals("TFIDF")) {
							Set<Entry<String, Double>> entrySet = paper.getTFIDFVector(dblp).entrySet();
							for(Entry<String, Double> e : entrySet) {
								resultsArrayList.add(e.getKey() + ":\t\t" + e.getValue());
							}
							resultsLabel.setText("Keyword vector (TFIDF model) for paper " + paperid);
						}
						
						resultsObservableList = FXCollections.observableArrayList(resultsArrayList);
						resultsListView.setItems(resultsObservableList);
					} else {
						resultsLabel.setText("Paper ID non valido!");
					}
				} catch (NumberFormatException | NoPaperWithSuchIDException
						| IOException e) {
					e.printStackTrace();
				}
			}
		});

		phase1Task1PaperIDComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				String paperid = phase1Task1PaperIDComboBox.getValue();
				try {
					if (paperid != null && !paperid.equals("")) {

						Corpus dblp = Main.getDblp();
						Paper paper = dblp.getPaperByID(Integer.parseInt(paperid));
						String title = paper.getTitle();
						phase1Task1TitleLabel.setText(title);

					} else {
						resultsLabel.setText("Paper ID non valido!");
					}
				} catch (NumberFormatException | NoPaperWithSuchIDException e) {
					e.printStackTrace();
				}
			}
		});

		// PHASE 1 - Task 2 - controls settings
		phase1Task2AuthorIDComboBox.setItems(authorsIDs);
		phase1Task2ModelComboBox.getItems().clear();
		phase1Task2ModelComboBox.getItems().addAll("TF", "TFIDF");
		phase1Task2ModelComboBox.getSelectionModel().selectFirst();
		phase1Task2TitleLabel.setText("Nome dell'autore selezionato");

		// PHASE 1 - Task 2 - EVENT HANDLER
		phase1Task2ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				resultsLabel.setText("Attendere prego!\n");
				String modello = phase1Task2ModelComboBox.getValue();
				String authorid = phase1Task2AuthorIDComboBox.getValue();
				
				try {
					if (authorid != null && !authorid.equals("")) {

						Corpus dblp = Main.getDblp();
						Author author = dblp.getAuthorByID(Integer.parseInt(authorid));
						ObservableList<String> resultsObservableList;
						ArrayList<String> resultsArrayList = new ArrayList<String>();
						
						if (modello.equals("TF")) {
							Set<Entry<String, Double>> entrySet = author.getWeightedTFVector().entrySet();
							for(Entry<String, Double> e : entrySet) {
								resultsArrayList.add(e.getKey() + ":\t\t" + e.getValue());
							}
							resultsLabel.setText("Weighted keyword vector (TF model) for author " + authorid);

						} else if (modello.equals("TFIDF")) {
							Set<Entry<String, Double>> entrySet = author.getWeightedTFIDFVector(dblp).entrySet();
							for(Entry<String, Double> e : entrySet) {
								resultsArrayList.add(e.getKey() + ":\t\t" + e.getValue());
							}
							resultsLabel.setText("Weighted keyword vector (TFIDF model) for author " + authorid);
						}
						
						resultsObservableList = FXCollections.observableArrayList(resultsArrayList);
						resultsListView.setItems(resultsObservableList);
					} else {
						resultsLabel.setText("Author ID non valido!");
					}
				} catch (IOException | AuthorWithoutPapersException
						| NumberFormatException | NoAuthorsWithSuchIDException e1) {
					e1.printStackTrace();
				}
			}
		});

		phase1Task2AuthorIDComboBox
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String authorid = phase1Task2AuthorIDComboBox
									.getValue();
							if (authorid != null && !authorid.equals("")) {

								Corpus dblp = Main.getDblp();
								Author author = null;

								author = dblp.getAuthorByID(Integer
										.parseInt(authorid));

								String name = author.getName();
								phase1Task2TitleLabel.setText(name);

							} else {
								resultsLabel.setText("Author ID non valido!");
							}
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							e.printStackTrace();
						}

					}
				});

		// PHASE 1 - Task 3 - controls settings
		phase1Task3AuthorIDComboBox.setItems(authorsIDs);
		phase1Task3ModelComboBox.getItems().clear();
		phase1Task3ModelComboBox.getItems().addAll("TFIDF2", "PF");
		phase1Task3ModelComboBox.getSelectionModel().selectFirst();
		phase1Task3TitleLabel.setText("Nome dell'autore selezionato");

		// PHASE 1 - Task 3 - EVENT HANDLER
		phase1Task3ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resultsLabel.setText("Attendere prego!\n");
				String modello = phase1Task3ModelComboBox.getValue();
				String authorid = phase1Task3AuthorIDComboBox.getValue();			
				
				try {
					if (authorid != null && !authorid.equals("")) {

						Corpus dblp = Main.getDblp();
						Author author = dblp.getAuthorByID(Integer.parseInt(authorid));
						
						ObservableList<String> resultsObservableList;
						ArrayList<String> resultsArrayList = new ArrayList<String>();
						
						if (modello.equals("TFIDF2")) {
							Set<Entry<String, Double>> entrySet = author.getTFIDF2Vector(dblp).entrySet();
							for(Entry<String, Double> e : entrySet) {
								resultsArrayList.add(e.getKey() + ":\t\t" + e.getValue());
							}
							resultsLabel.setText("Keyword vector (TFIDF2 model) for author " + authorid);

						} else if (modello.equals("PF")) {
							Set<Entry<String, Double>> entrySet = author.getPFVector(dblp).entrySet();
							for(Entry<String, Double> e : entrySet) {
								resultsArrayList.add(e.getKey() + ":\t\t" + e.getValue());
							}
							resultsLabel.setText("Keyword vector (PF model) for author " + authorid);
						}
						
						resultsObservableList = FXCollections.observableArrayList(resultsArrayList);
						resultsListView.setItems(resultsObservableList);
					} else {
						resultsLabel.setText("Author ID non valido!");
					}
				} catch (NoAuthorsWithSuchIDException
						| AuthorWithoutPapersException | NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});

		phase1Task3AuthorIDComboBox
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String authorid = phase1Task3AuthorIDComboBox
									.getValue();
							if (authorid != null && !authorid.equals("")) {
								Corpus dblp = Main.getDblp();
								Author author = null;
								author = dblp.getAuthorByID(Integer
										.parseInt(authorid));
								String name = author.getName();
								phase1Task3TitleLabel.setText(name);
							} else {
								resultsLabel.setText("Author ID non valido!");
							}
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							e.printStackTrace();
						}
					}

				});

		// PHASE 2 - Task 1a - controls settings
		phase2Task1aAuthorIDComboBox.setItems(authorsIDs);
		phase2Task1aModelComboBox.getItems().clear();
		phase2Task1aModelComboBox.getItems().addAll("PCA", "SVD");
		phase2Task1aModelComboBox.getSelectionModel().selectFirst();
		phase2Task1aTitleLabel.setText("Nome dell'autore selezionato");

		
//		// PHASE 2 - Task 2a - EVENT HANDLER
//		phase2Task2aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//
//				Corpus dblp = Main.getDblp();
//				ArrayList<String> papersList = new ArrayList<String>();
//
//				for (Paper p : dblp.getPapers()) {
//					papersList.add("(" + p.getPaperID() + ") " + p.getTitle());
//				}
//
//				TextArea resultsTextArea = new TextArea();
//				resultsTextArea.clear();
//				resultsTextArea.setText(papersList.toString());
//
//				Stage stage = new Stage();
//				AnchorPane resultPane = new AnchorPane();
//				resultPane.getChildren().add(resultsTextArea);
//				Scene resultScene = new Scene(resultPane);
//				stage.setScene(resultScene);
//				stage.centerOnScreen();
//				stage.setTitle("Results");
//				stage.show();
//			}
//		});
		
		
		// PHASE 2 - Task 1a - EVENT HANDLER
		phase2Task1aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resultsLabel.setText("Attendere prego!\n");
				String modello = phase2Task1aModelComboBox.getValue();
				String authorid = phase2Task1aAuthorIDComboBox.getValue();
				
				TextArea resultsTextArea = new TextArea();
				
				try {
					if (authorid != null && !authorid.equals("")) {
						Corpus dblp = Main.getDblp();
						Author author = null;
						author = dblp.getAuthorByID(Integer.parseInt(authorid));
						
						if (modello.equals("PCA")) {
							String fileName = author.getAuthorID().toString();
							ArrayList<ArrayList<Double>> scoreLatentMatrix = author.getPCA(dblp, fileName, 5);
							ArrayList<TreeMap<String, Double>> topNMatrix = author.getTopN(scoreLatentMatrix, 5);
							if (PRINT_ON_FILE) {
								IO.printDocumentTermMatrixOnFile(topNMatrix,"../data/PCA_Top5_"	+ author.getAuthorID() + ".csv");
							}

							resultsTextArea.clear();
							
							resultsTextArea.appendText("\n\nTop-5 latent semantics (PCA) for " + author.getName() + " (" + author.getAuthorID() + "):\n");
							printDocumentTermMatrixOnTextArea(topNMatrix, author, resultsTextArea);
							resultsTextArea.appendText("----------------------------------------------------------------------\n");


						} else if (modello.equals("SVD")) {
							String authorID = author.getAuthorID().toString();
							ArrayList<ArrayList<Double>> vMatrix = author
									.getSVD(dblp, authorID, 5);
							ArrayList<TreeMap<String, Double>> topNMatrix = author
									.getTopN(vMatrix, 5);
							if (PRINT_ON_FILE) {
								IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/SVD_Top5_" + author.getAuthorID() + ".csv");
							}
							
							resultsTextArea.clear();
							
							resultsTextArea.appendText("\n\nTop-5 latent semantics (SVD) for " + author.getName() + " (" + author.getAuthorID() + "):\n");
							printDocumentTermMatrixOnTextArea(topNMatrix, author, resultsTextArea);
							resultsTextArea.appendText("----------------------------------------------------------------------\n");
						}
						
						Stage stage = new Stage();
						AnchorPane resultPane = new AnchorPane();
						resultPane.setPrefSize(800, 600);
						resultPane.getChildren().add(resultsTextArea);
						resultsTextArea.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
						Scene resultScene = new Scene(resultPane);
						stage.setScene(resultScene);
						stage.centerOnScreen();
						stage.setTitle("Top-5 latent semantics");
						stage.show();

					} else {
						resultsLabel.setText("Author ID non valido!");
					}
				} catch (NoAuthorsWithSuchIDException
						| AuthorWithoutPapersException | NumberFormatException e1) {
					e1.printStackTrace();
				} catch (MatlabConnectionException e) {
					e.printStackTrace();
				} catch (MatlabInvocationException e) {
					e.printStackTrace();
				}
			}
		});

		phase2Task1aAuthorIDComboBox
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String authorid = phase2Task1aAuthorIDComboBox
									.getValue();
							if (authorid != null && !authorid.equals("")) {
								Corpus dblp = Main.getDblp();
								Author author = null;
								author = dblp.getAuthorByID(Integer
										.parseInt(authorid));
								String name = author.getName();
								phase2Task1aTitleLabel.setText(name);
							} else {
								resultsLabel.setText("Author ID non valido!");
							}
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							e.printStackTrace();
						}
					}
				});

	
	
	// PHASE 2 - Task 1b - controls settings
			phase2Task1bAuthorIDComboBox.setItems(authorsIDs);
			phase2Task1bModelComboBox.getItems().clear();
			phase2Task1bModelComboBox.getItems().addAll("keyword vectors", "vettori di differenziazione (TFIDF2)", "vettori di differenziazione (PF)", "top-5 semantiche latenti (PCA)", "top-5 semantiche latenti (SVD)");
			phase2Task1bModelComboBox.getSelectionModel().selectFirst();
			phase2Task1bTitleLabel.setText("Nome dell'autore selezionato");

			// PHASE 2 - Task 1b - EVENT HANDLER
			phase2Task1bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					

					String modello = phase2Task1bModelComboBox.getValue();
					String authorid = phase2Task1bAuthorIDComboBox.getValue();
					ObservableList p2t1bOL = FXCollections.observableArrayList();
					try {
						if (authorid != null && !authorid.equals("")) {
							Corpus dblp = Main.getDblp();
							Author author = null;
							author = dblp.getAuthorByID(Integer.parseInt(authorid));
							String output = null;
							LinkedHashMap<String,Double> lhmOut = new LinkedHashMap<String,Double>(); 
							if (modello.equals("keyword vectors")) {
								lhmOut = author.getSimilarAuthorsRankedByKeywordVector(dblp);
								output = "Similar authors ranked by keyword vector:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
								resultsLabel.setText("Similar authors ranked by keyword vector:");
								ArrayList<String> similarAuthors = new ArrayList<String>();
								Set<Entry<String, Double>> lEntrySet = lhmOut.entrySet();
								for (Entry<String, Double> e: lEntrySet){
									p2t1bOL.add("[" + e.getValue() + "] " + e.getKey());
								}
								
								
								
							} else if (modello.equals("vettori di differenziazione (TFIDF2)")) {
								lhmOut = author.getSimilarAuthorsRankedByTFIDF2Vector(dblp);
								output = "Similar authors ranked by TFIDF2 vector:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("vettori di differenziazione (PF)")) {
								lhmOut= author.getSimilarAuthorsRankedByPFVector(dblp);
								output = "Similar authors ranked by PF vector:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("top-5 semantiche latenti (PCA)")) {
								lhmOut = author.getSimilarAuthorsRankedByPCA(dblp);
								output = "Similar authors ranked by PCA:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("top-5 semantiche latenti (SVD)")) {
								lhmOut = author.getSimilarAuthorsRankedBySVD(dblp);
								output = "Similar authors ranked by SVD:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							}
							resultsListView.setItems(p2t1bOL);
						} else {
							resultsLabel.setText("Author ID non valido!");
						}
						
					} catch (NoAuthorsWithSuchIDException
							| AuthorWithoutPapersException | NumberFormatException e1) {
						e1.printStackTrace();
					} catch (MatlabConnectionException e) {
						e.printStackTrace();
					} catch (MatlabInvocationException e) {
						e.printStackTrace();
					} catch (NoSuchTechniqueException e) {
						e.printStackTrace();
					}
				}
			});

			phase2Task1bAuthorIDComboBox
					.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								String authorid = phase2Task1bAuthorIDComboBox
										.getValue();
								if (authorid != null && !authorid.equals("")) {
									Corpus dblp = Main.getDblp();
									Author author = null;
									author = dblp.getAuthorByID(Integer
											.parseInt(authorid));
									String name = author.getName();
									phase2Task1bTitleLabel.setText(name);
								} else {
									resultsLabel.setText("Author ID non valido!");
								}
							} catch (NumberFormatException
									| NoAuthorsWithSuchIDException e) {
								e.printStackTrace();
							}
						}
					});
			
			// PHASE 2 - Task 1c - controls settings
			phase2Task1cAuthorIDComboBox.setItems(authorsIDs);
			phase2Task1cModelComboBox.getItems().clear();
			phase2Task1cModelComboBox.getItems().addAll("keyword vectors", "vettori di differenziazione (TFIDF2)", "vettori di differenziazione (PF)", "top-5 semantiche latenti (PCA)", "top-5 semantiche latenti (SVD)");
			phase2Task1cModelComboBox.getSelectionModel().selectFirst();
			phase2Task1cTitleLabel.setText("Nome dell'autore selezionato");

			// PHASE 2 - Task 1c - EVENT HANDLER
			phase2Task1cExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					String modello = phase2Task1cModelComboBox.getValue();
					String authorid = phase2Task1cAuthorIDComboBox.getValue();
					try {
						if (authorid != null && !authorid.equals("")) {
							Corpus dblp = Main.getDblp();
							Author author = null;
							author = dblp.getAuthorByID(Integer.parseInt(authorid));
							String output = null;
							LinkedHashMap<String,Double> lhmOut = new LinkedHashMap<String,Double>(); 
							if (modello.equals("keyword vectors")) {
								lhmOut = author.getRelevantPapersRankedByKeywordVector(dblp);
								output = "Relevant papers ranked by keyword vector:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("vettori di differenziazione (TFIDF2)")) {
								lhmOut = author.getRelevantPapersRankedByTFIDF2Vector(dblp);
								output = "Relevant papers ranked by TFIDF2 vector:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("vettori di differenziazione (PF)")) {
								lhmOut= author.getRelevantPapersRankedByPFVector(dblp);
								output = "Relevant papers ranked by PF vector:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("top-5 semantiche latenti (PCA)")) {
								lhmOut = author.getRelevantPapersRankedByPCA(dblp);
								output = "Relevant papers ranked by PCA:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							} else if (modello.equals("top-5 semantiche latenti (SVD)")) {
								lhmOut = author.getRelevantPapersRankedBySVD(dblp);
								output = "Relevant papers ranked by SVD:\n"
										+ "-----------------------------------------------------\n"
										+ lhmOut.toString();
							}
//						FIXME!!!	resultsTextArea.setText(output);
						} else {
							resultsLabel.setText("authorid non valido!");
						}
						
					} catch (NoAuthorsWithSuchIDException
							| AuthorWithoutPapersException | NumberFormatException e1) {
						e1.printStackTrace();
					} catch (MatlabConnectionException e) {
						e.printStackTrace();
					} catch (MatlabInvocationException e) {
						e.printStackTrace();
					}
				}
			});

			phase2Task1cAuthorIDComboBox
					.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								String authorid = phase2Task1cAuthorIDComboBox
										.getValue();
								if (authorid != null && !authorid.equals("")) {
									Corpus dblp = Main.getDblp();
									Author author = null;
									author = dblp.getAuthorByID(Integer
											.parseInt(authorid));
									String name = author.getName();
									phase2Task1cTitleLabel.setText(name);
								} else {
									resultsLabel.setText("Author ID non valido!");
								}
							} catch (NumberFormatException
									| NoAuthorsWithSuchIDException e) {
								e.printStackTrace();
							}
						}
					});

			// PHASE 2 - Task 2a - EVENT HANDLER
			phase2Task2aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
	
					Corpus dblp = Main.getDblp();
					ArrayList<String> papersList = new ArrayList<String>();
	
					for (Paper p : dblp.getPapers()) {
						papersList.add("(" + p.getPaperID() + ") " + p.getTitle());
					}
	
					TextArea resultsTextArea = new TextArea();
					resultsTextArea.clear();
					resultsTextArea.setText(papersList.toString());
	
					Stage stage = new Stage();
					AnchorPane resultPane = new AnchorPane();
					resultPane.getChildren().add(resultsTextArea);
					Scene resultScene = new Scene(resultPane);
					stage.setScene(resultScene);
					stage.centerOnScreen();
					stage.setTitle("Results");
					stage.show();
				}
			});
			
			// PHASE 2 - Task 2b - EVENT HANDLER
			phase2Task2bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					ArrayList<ArrayList<Double>> top3SVDCoAuthorOut = new ArrayList<ArrayList<Double>>();
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixCoAuthor.csv";
					try {
						top3SVDCoAuthorOut = dblp.getTop3SVDCoAuthor(path, fileName);
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException e) {
						e.printStackTrace();
					} catch (NoAuthorsWithSuchIDException e) {
						e.printStackTrace();
					}
					String output = null;
					output = "Top 3 SVD CoAuthor:\n"
										+ "-----------------------------------------------------\n"
										+ top3SVDCoAuthorOut.toString();
							
//					FIXME!!!resultsTextArea.setText(output);														
				}
			});
			
			// PHASE 2 - Task 3a - EVENT HANDLER
			phase2Task3aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					TreeMap<Integer, ArrayList<Author>> clustersBasedOnConcepts = new TreeMap<Integer, ArrayList<Author>>();
					
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixAuthorForClusters.csv";
					
					try {
						clustersBasedOnConcepts = dblp.getClustersBasedOnConcepts(path, fileName);
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException
							| NoAuthorsWithSuchIDException
							| WrongClusteringException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Gruppi di autori, clusterizzati in base al \n grado di appartenenza alle 3 semantiche \n ottenute dalla matrice di similarita' author-author");
					System.out.println("Gruppo autori caratterizzati dalla semantica 1:");
					Printer.printAuthorsList(clustersBasedOnConcepts.get(0));
					System.out.println("Gruppo autori caratterizzati dalla semantica 2:");
					Printer.printAuthorsList(clustersBasedOnConcepts.get(1));
					System.out.println("Gruppo autori caratterizzati dalla semantica 3:");
					Printer.printAuthorsList(clustersBasedOnConcepts.get(2));
					
					String output = null;
					output = "Gruppi di autori, clusterizzati in base al \n grado di appartenenza alle 3 semantiche \n ottenute dalla matrice di similarita' author-author:\n"
										+ "-----------------------------------------------------\n"
										+ "Gruppo autori caratterizzati dalla semantica 1:\n"
										+ clustersBasedOnConcepts.get(0).toString() + "\n"
										+ "-----------------------------------------------------\n"
										+ "Gruppo autori caratterizzati dalla semantica 2:\n"
										+ clustersBasedOnConcepts.get(1).toString() + "\n"
										+ "-----------------------------------------------------\n"
										+ "Gruppo autori caratterizzati dalla semantica 3:\n"
										+ clustersBasedOnConcepts.get(2).toString() + "\n";
							 
//					FIXME!!!! resultsTextArea.setText(output);														
				}
			});
			
			// PHASE 2 - Task 3b - EVENT HANDLER
			phase2Task3bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					Table<Integer, String, Double> conceptsKeywordVectors = null;
					
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixAuthorForConceptsVectors.csv";		            
		            try {
						conceptsKeywordVectors = dblp.getConceptsKeywordVectors(path, fileName);
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException
							| NoAuthorsWithSuchIDException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            System.out.println("Matrice Concetti Keyword");
		            System.out.println("-----------------------------------------------");
		            Printer.printConceptsKeywordsTableWithCaptions(conceptsKeywordVectors);
					
					String output = null;
					output = "Matrice Concetti Keyword\n"
										+ "-----------------------------------------------------\n"
										+ conceptsKeywordVectors.toString() + "\n";							 
//					FIXME:::: resultsTextArea.setText(output);														
				}
			});

		}


	public static void gUIPrintMatrix(ArrayList<ArrayList<Double>> matrix) {
		resultsLabel.setText("Score Latent Matrix:\n");
		int cellSpanSize = 30;
		for (ArrayList<Double> riga : matrix) {
			for (Double cella : riga) {
				String cellContent = Double.toString(cella);
				int lenCellContent = cellContent.length();
				String spaceCellContent = "";
				int i;
				for (i = 0; i < (cellSpanSize - lenCellContent); i++) {
					spaceCellContent = spaceCellContent + " ";			
				}
				cellContent = cellContent + spaceCellContent;
//			FIXME	resultsTextArea.appendText(cellContent);
				System.out.println("\n");
			}
//			FIXME	resultsTextArea.appendText("\n");
		}
		return;
	}
	
	public void printMatrixOnTextArea(ArrayList<ArrayList<Double>> matrix, TextArea textArea) {
		for (ArrayList<Double> riga : matrix) {
			for (Double cella : riga) {
				textArea.appendText(String.format("\t%.7f", cella));
				textArea.appendText(",");
			}
			textArea.appendText("\n");
		}

		return;
	}
	
	public void printDocumentTermMatrixOnTextArea(ArrayList<TreeMap<String, Double>> documentTermMatrix, Author author, TextArea textArea) throws AuthorWithoutPapersException {

		for (String s : author.getKeywordSet()) {
			textArea.appendText(s + ",\t\t");
		}
		textArea.appendText("\n");
		for (TreeMap<String, Double> riga : documentTermMatrix) {
			for (Map.Entry<String, Double> cella : riga.entrySet()) {
				textArea.appendText(String.format("%.4f", cella.getValue()));
				textArea.appendText(",\t\t");
			}
			textArea.appendText("\n");
		}

		return;
	}

}