package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import utils.Clusterer;
import utils.IO;

import com.google.common.collect.Table;

import dblp.Author;
import dblp.Corpus;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoPaperWithSuchIDException;
import exceptions.NoSuchTechniqueException;
import exceptions.WrongClusteringException;
import exceptions.WrongFileTypeException;

public class GUIController implements Initializable {
	

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
	
	@FXML
	private ComboBox<String> phase3Task1PrintFormatComboBox;
	
	// PHASE 3 - Task 2 - controls injection
	@FXML
	private Button phase3Task2ExecuteButton;
	
	@FXML
	private ComboBox<String> phase3Task2PrintFormatComboBox;
	
	// PHASE 3 - Task 3 - controls injection
	
	@FXML
	private TextField phase3Task3NClustersTextField;
	
	@FXML
	private ComboBox<String> phase3Task3AlgorithmComboBox;
	
	@FXML
	private Button phase3Task3ExecuteButton;
	
	// PHASE 3 - Task 4 - controls injection
		
	@FXML
	private TextField phase3Task4NNodesTextField;
		
	@FXML
	private Button phase3Task4ExecuteButton;
	
	// PHASE 3 - Task 5 - controls injection
	
	@FXML
	private TextField phase3Task5NNodesTextField;
	
	@FXML
	private ComboBox<String> phase3Task5AuthorIDComboBox;
	
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
	
	@FXML
	private static ListView<String> cluster1ListView, cluster2ListView, cluster3ListView;
	

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

		phase1Task3AuthorIDComboBox.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String authorid = phase1Task3AuthorIDComboBox.getValue();
							if (authorid != null && !authorid.equals("")) {
								Corpus dblp = Main.getDblp();
								Author author = null;
								author = dblp.getAuthorByID(Integer.parseInt(authorid));
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
		
		// PHASE 2 - Task 1a - EVENT HANDLER
		phase2Task1aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resultsLabel.setText("Attendere prego!\n");
				String modello = phase2Task1aModelComboBox.getValue();
				String authorid = phase2Task1aAuthorIDComboBox.getValue();		
				TextArea resultsTextArea = new TextArea();
				resultsTextArea.clear();
				resultsTextArea.setPrefColumnCount(Integer.MAX_VALUE);
				
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

							resultsTextArea.appendText("Top-5 latent semantics (PCA) for " + author.getName() + " (" + author.getAuthorID() + "):\n\n");
							printDocumentTermMatrixOnTextArea(topNMatrix, author, resultsTextArea);
							
						} else if (modello.equals("SVD")) {
							String authorID = author.getAuthorID().toString();
							ArrayList<ArrayList<Double>> vMatrix = author.getSVD(dblp, authorID, 5);
							ArrayList<TreeMap<String, Double>> topNMatrix = author.getTopN(vMatrix, 5);
							if (PRINT_ON_FILE) {
								IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/SVD_Top5_" + author.getAuthorID() + ".csv");
							}
							
							resultsTextArea.appendText("Top-5 latent semantics (SVD) for " + author.getName() + " (" + author.getAuthorID() + "):\n\n");
							printDocumentTermMatrixOnTextArea(topNMatrix, author, resultsTextArea);
						}
						
						Stage stage = new Stage();
						ScrollPane resultPane = new ScrollPane();
						resultPane.setPrefSize(800,250);
						resultPane.setFitToWidth(true);
						resultPane.setFitToHeight(true);
						resultsTextArea.setPrefSize(800,250);
						resultPane.setContent(resultsTextArea);
						
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

		phase2Task1aAuthorIDComboBox.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							String authorid = phase2Task1aAuthorIDComboBox.getValue();
							if (authorid != null && !authorid.equals("")) {
								Corpus dblp = Main.getDblp();
								Author author = null;
								author = dblp.getAuthorByID(Integer.parseInt(authorid));
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
			phase2Task1bModelComboBox.getItems().addAll("Keyword vector", "TFIDF2 vector", "PF vector", "Top-5 latent semantics (PCA)", "Top-5 latent semantics (SVD)");
			phase2Task1bModelComboBox.getSelectionModel().selectFirst();
			phase2Task1bTitleLabel.setText("Nome dell'autore selezionato");

			// PHASE 2 - Task 1b - EVENT HANDLER
			phase2Task1bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String modello = phase2Task1bModelComboBox.getValue();
					String authorid = phase2Task1bAuthorIDComboBox.getValue();
					ObservableList resultsObservableList = FXCollections.observableArrayList();
					
					try {
						if (authorid != null && !authorid.equals("")) {
							Corpus dblp = Main.getDblp();
							Author author = null;
							author = dblp.getAuthorByID(Integer.parseInt(authorid));

							if (modello.equals("Keyword vector")) {								
								resultsLabel.setText("Similar authors ranked by keyword vector:");
								Set<Entry<String, Double>> entrySet = author.getSimilarAuthorsRankedByKeywordVector(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}		
								
							} else if (modello.equals("TFIDF2 vector")) {
								resultsLabel.setText("Similar authors ranked by TFIDF2 vector:");
								Set<Entry<String, Double>> entrySet = author.getSimilarAuthorsRankedByTFIDF2Vector(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							} else if (modello.equals("PF vector")) {
								resultsLabel.setText("Similar authors ranked by PF vector:");
								Set<Entry<String, Double>> entrySet = author.getSimilarAuthorsRankedByPFVector(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							} else if (modello.equals("Top-5 latent semantics (PCA)")) {								
								resultsLabel.setText("Similar authors ranked by Top-5 latent semantics (PCA):");
								Set<Entry<String, Double>> entrySet = author.getSimilarAuthorsRankedByPCA(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							} else if (modello.equals("Top-5 latent semantics (SVD)")) {
								resultsLabel.setText("Similar authors ranked by Top-5 latent semantics (SVD):");
								Set<Entry<String, Double>> entrySet = author.getSimilarAuthorsRankedBySVD(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							}
							resultsListView.setItems(resultsObservableList);
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

			phase2Task1bAuthorIDComboBox.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								String authorid = phase2Task1bAuthorIDComboBox.getValue();
								if (authorid != null && !authorid.equals("")) {
									Corpus dblp = Main.getDblp();
									Author author = null;
									author = dblp.getAuthorByID(Integer.parseInt(authorid));
									String name = author.getName();
									phase2Task1bTitleLabel.setText(name);
								} else {
									resultsLabel.setText("Author ID non valido!");
								}
							} catch (NumberFormatException | NoAuthorsWithSuchIDException e) {
								e.printStackTrace();
							}
						}
					});
			
			// PHASE 2 - Task 1c - controls settings
			phase2Task1cAuthorIDComboBox.setItems(authorsIDs);
			phase2Task1cModelComboBox.getItems().clear();
			phase2Task1cModelComboBox.getItems().addAll("Keyword vector", "TFIDF2 vector", "PF vector", "Top-5 latent semantics (PCA)", "Top-5 latent semantics (SVD)");
			phase2Task1cModelComboBox.getSelectionModel().selectFirst();
			phase2Task1cTitleLabel.setText("Nome dell'autore selezionato");

			// PHASE 2 - Task 1c - EVENT HANDLER
			phase2Task1cExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					String modello = phase2Task1cModelComboBox.getValue();
					String authorid = phase2Task1cAuthorIDComboBox.getValue();
					ObservableList resultsObservableList = FXCollections.observableArrayList();
					try {
						if (authorid != null && !authorid.equals("")) {
							Corpus dblp = Main.getDblp();
							Author author = dblp.getAuthorByID(Integer.parseInt(authorid)); 
							if (modello.equals("Keyword vector")) {								
								resultsLabel.setText("Relevant papers ranked by keyword vector:");
								Set<Entry<String, Double>> entrySet = author.getRelevantPapersRankedByKeywordVector(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							} else if (modello.equals("TFIDF2 vector")) {
								resultsLabel.setText("Relevant papers ranked by TFIDF2 vector:");
								Set<Entry<String, Double>> entrySet = author.getRelevantPapersRankedByTFIDF2Vector(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							} else if (modello.equals("PF vector")) {
								resultsLabel.setText("Relevant papers ranked by PF vector:");
								Set<Entry<String, Double>> entrySet = author.getRelevantPapersRankedByPFVector(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add("[" + e.getValue() + "] " + e.getKey());
								}
							} else if (modello.equals("Top-5 latent semantics (PCA)")) {
								resultsLabel.setText("Relevant papers ranked by top-5 latent semantics (PCA):");
								Set<Entry<String, Double>> entrySet = author.getRelevantPapersRankedByPCA(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add(e.getKey());
								}
							} else if (modello.equals("Top-5 latent semantics (SVD)")) {
								resultsLabel.setText("Relevant papers ranked by top-5 latent semantics (SVD):");
								Set<Entry<String, Double>> entrySet = author.getRelevantPapersRankedBySVD(dblp).entrySet();
								for (Entry<String, Double> e : entrySet){
									resultsObservableList.add(e.getKey());
								}
							}
							resultsListView.setItems(resultsObservableList);
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

			phase2Task1cAuthorIDComboBox.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								String authorid = phase2Task1cAuthorIDComboBox.getValue();
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
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					TextArea resultsTextArea = new TextArea();
					resultsTextArea.clear();
					
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixAuthor.csv";
					try {
						ArrayList<ArrayList<Double>> top3SVDAuthorMatrix = dblp.getTop3SVDAuthor(path, fileName);
						resultsTextArea.appendText("Top-3 SVD author-author similarity matrix:\n\n");
						printMatrixOnTextArea(top3SVDAuthorMatrix, resultsTextArea);
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException e) {
						e.printStackTrace();
					}
					
					Stage stage = new Stage();
					ScrollPane resultPane = new ScrollPane();
					resultPane.setPrefSize(800,250);
					resultPane.setFitToWidth(true);
					resultPane.setFitToHeight(true);
					resultsTextArea.setPrefSize(800,250);
					resultPane.setContent(resultsTextArea);
					
					Scene resultScene = new Scene(resultPane);
					stage.setScene(resultScene);
					stage.centerOnScreen();
					stage.setTitle("Top-3 SVD author-author similarity matrix");
					stage.show();
				}
			});
			
			// PHASE 2 - Task 2b - EVENT HANDLER
			phase2Task2bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					TextArea resultsTextArea = new TextArea();
					resultsTextArea.clear();
					
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixCoAuthor.csv";
					try {
						ArrayList<ArrayList<Double>> top3SVDCoAuthorMatrix = dblp.getTop3SVDAuthor(path, fileName);
						resultsTextArea.appendText("Top-3 SVD coauthor-couthor similarity matrix:\n\n");
						printMatrixOnTextArea(top3SVDCoAuthorMatrix, resultsTextArea);
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException e) {
						e.printStackTrace();
					}
					
					Stage stage = new Stage();
					ScrollPane resultPane = new ScrollPane();
					resultPane.setPrefSize(800,250);
					resultPane.setFitToWidth(true);
					resultPane.setFitToHeight(true);
					resultsTextArea.setPrefSize(800,250);
					resultPane.setContent(resultsTextArea);
					
					Scene resultScene = new Scene(resultPane);
					stage.setScene(resultScene);
					stage.centerOnScreen();
					stage.setTitle("Top-3 SVD coauthor-couthor similarity matrix");
					stage.show();													
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
//						Scene scene = new Scene((AnchorPane)fxmlLoader.load());
//						AnchorPane ap = (AnchorPane) FXMLLoader.load(Main.class.getResource("Phase2Task3Agui.fxml"));
//						Scene scene = new Scene(ap);
//						Stage stage = new Stage();
//						stage.setScene(scene);
//						stage.centerOnScreen();
//						stage.setTitle("Authors clustered according to the top-3 SVD similarity matrix");						
						
						clustersBasedOnConcepts = dblp.getClustersBasedOnConcepts(path, fileName);
						ObservableList<String> cluster1ObservableList = FXCollections.observableArrayList();
						ObservableList<String> cluster2ObservableList = FXCollections.observableArrayList();
						ObservableList<String> cluster3ObservableList = FXCollections.observableArrayList();
						
						cluster1ObservableList.add("############### CLUSTER 1 ###############");
						for(Author author1 : clustersBasedOnConcepts.get(0)) {
							cluster1ObservableList.add(author1.getName() + " (" + author1.getAuthorID() + ")");
						}
						cluster2ObservableList.add("############### CLUSTER 2 ###############");
						for(Author author2 : clustersBasedOnConcepts.get(1)) {
							cluster2ObservableList.add(author2.getName() + " (" + author2.getAuthorID() + ")");
						}
						cluster3ObservableList.add("############### CLUSTER 3 ###############");
						for(Author author3 : clustersBasedOnConcepts.get(2)) {
							cluster3ObservableList.add(author3.getName() + " (" + author3.getAuthorID() + ")");
						}
												
//						cluster1ListView.setItems(cluster1ObservableList);
//						cluster2ListView.setItems(cluster2ObservableList);
//						cluster3ListView.setItems(cluster3ObservableList);
						
						ObservableList<String> globalObservableList = FXCollections.observableArrayList();
						globalObservableList.addAll(cluster1ObservableList);
						globalObservableList.addAll(cluster2ObservableList);
						globalObservableList.addAll(cluster3ObservableList);
						
						resultsListView.setItems(globalObservableList);				

//						stage.show();
						
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException
							| NoAuthorsWithSuchIDException
							| WrongClusteringException e) {
						e.printStackTrace();
					}
					
					
//					System.out.println("Gruppi di autori, clusterizzati in base al \n grado di appartenenza alle 3 semantiche \n ottenute dalla matrice di similarita' author-author");
//					System.out.println("Gruppo autori caratterizzati dalla semantica 1:");
//					Printer.printAuthorsList(clustersBasedOnConcepts.get(0));
//					System.out.println("Gruppo autori caratterizzati dalla semantica 2:");
//					Printer.printAuthorsList(clustersBasedOnConcepts.get(1));
//					System.out.println("Gruppo autori caratterizzati dalla semantica 3:");
//					Printer.printAuthorsList(clustersBasedOnConcepts.get(2));
//					
//					String output = null;
//					output = "Gruppi di autori, clusterizzati in base al \n grado di appartenenza alle 3 semantiche \n ottenute dalla matrice di similarita' author-author:\n"
//										+ "-----------------------------------------------------\n"
//										+ "Gruppo autori caratterizzati dalla semantica 1:\n"
//										+ clustersBasedOnConcepts.get(0).toString() + "\n"
//										+ "-----------------------------------------------------\n"
//										+ "Gruppo autori caratterizzati dalla semantica 2:\n"
//										+ clustersBasedOnConcepts.get(1).toString() + "\n"
//										+ "-----------------------------------------------------\n"
//										+ "Gruppo autori caratterizzati dalla semantica 3:\n"
//										+ clustersBasedOnConcepts.get(2).toString() + "\n";

				}
			});
			
			// PHASE 2 - Task 3b - EVENT HANDLER
			phase2Task3bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixAuthorForConceptsVectors.csv";		            
		            try {
		            	Table<Integer, String, Double> conceptsKeywordVectors = dblp.getConceptsKeywordVectors(path, fileName);
		            	
		            	ObservableList<String> resultObservableList = FXCollections.observableArrayList();
						
		            	ArrayList<String> keywordsList = new ArrayList<String>();
		            	for(String keyword : conceptsKeywordVectors.columnKeySet()) {
		            		keywordsList.add(keyword);
		            	}
		            			            	
						for (int i : conceptsKeywordVectors.rowKeySet()) {
							int k = 0;
							resultObservableList.add("############### CONCEPT " + (i + 1) + " ###############");
							for (String j : conceptsKeywordVectors.columnKeySet()) {
								resultObservableList.add(String.format(keywordsList.get(k) + ": %.7f", conceptsKeywordVectors.get(i, j)));
								k++;
							}
						}
						
						resultsListView.setItems(resultObservableList);
		            	
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException
							| NoAuthorsWithSuchIDException e) {
						e.printStackTrace();
					}
		            
		            
//		            System.out.println("Concepts-Keywords matrix");
//		            System.out.println("-----------------------------------------------");
//		            Printer.printConceptsKeywordsTableWithCaptions(conceptsKeywordVectors);
//					
//					String output = null;
//					output = "Matrice Concetti Keyword\n"
//										+ "-----------------------------------------------------\n"
//										+ conceptsKeywordVectors.toString() + "\n";							 
				}
			});
		
		// PHASE 3 - Task 1 - controls settings
			phase3Task1PrintFormatComboBox.getItems().clear();
			phase3Task1PrintFormatComboBox.getItems().addAll("PDF", "GEXF");
			
		// PHASE 3 - Task 1 - EVENT HANDLER
			phase3Task1ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					String type = phase3Task1PrintFormatComboBox.getValue();
					if (type != null) {
						String fileName = "coAuthorsGraphBasedOnKeywordVectors";
						Graph graph = null;
						try {
							graph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
						} catch (NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							dblp.printGraphOnFile(graph, fileName, type);
						} catch (WrongFileTypeException
								| NoAuthorsWithSuchIDException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
					}
					else {
						resultsLabel.setText("Formato di stampa non valido o non selezionato!\n");						
					}
					
																			
				}
			});
			
			// PHASE 3 - Task 2 - controls settings
			phase3Task2PrintFormatComboBox.getItems().clear();
			phase3Task2PrintFormatComboBox.getItems().addAll("PDF", "GEXF");
		
			// PHASE 3 - Task 2 - EVENT HANDLER
			phase3Task2ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					String type = phase3Task2PrintFormatComboBox.getValue();
					if (type != null) {
						String fileName = "coAuthoredPapersGraphBasedOnKeywordVectors";
						Graph graph = null;
						try {
							graph = dblp.getCoAuthoredPapersGraphBasedOnKeywordVectors();
						} catch (NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							dblp.printGraphOnFile(graph, fileName, type);
						} catch (WrongFileTypeException
								| NoAuthorsWithSuchIDException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						resultsLabel.setText("Formato di stampa non valido o non selezionato!\n");	
					}
 					
																
				}
			});
			
			// PHASE 3 - Task 3 - controls settings
			phase3Task3AlgorithmComboBox.getItems().clear();
			phase3Task3AlgorithmComboBox.getItems().addAll("Simple K-Means", "Farthest First");
		
			// PHASE 3 - Task 3 - EVENT HANDLER
			phase3Task3ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Clusterer clusterer = new Clusterer();				
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					String algorithm = phase3Task3AlgorithmComboBox.getValue();
					int numClusters = Integer.parseInt(phase3Task3NClustersTextField.getText());
					
					TextArea resultsTextArea = new TextArea();
					resultsTextArea.clear();
					resultsTextArea.setPrefColumnCount(Integer.MAX_VALUE);
					
					if (algorithm == "Simple K-Means") {
						Map<Integer, Integer> clusters = null;
						
						try {
							clusters = clusterer.clusterAuthorsBySimpleKMeans(dblp, numClusters);
						} catch (IOException | AuthorWithoutPapersException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						resultsTextArea.appendText("Clustering degli autori tramite SimpleKMeans:\n");	
						
						System.out.println("Clustering degli autori tramite SimpleKMeans:\n");		
						resultsTextArea.setStyle("-fx-font-family: monospace");					
						
						Set<Entry<Integer, Integer>> clustersEntrySet = clusters.entrySet();
						for(Entry<Integer, Integer> clusteredAuthor: clustersEntrySet) {
							int authorID = clusteredAuthor.getKey();
							try {
								System.out.println(dblp.getAuthorByID(authorID).getName() + " (" + authorID + ") > cluster " + clusteredAuthor.getValue());
								resultsTextArea.appendText(stretchTo18(dblp.getAuthorByID(authorID).getName()) + " (" + authorID + ") > cluster " + clusteredAuthor.getValue());
								resultsTextArea.appendText("\n");
							} catch (NoAuthorsWithSuchIDException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}			
					} else if (algorithm == "Farthest First") {
						Map<Integer, Integer> clusters = null;
						try {
							clusters = clusterer.clusterAuthorsByFarthestFirst(dblp, numClusters);
						} catch (IOException | AuthorWithoutPapersException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						resultsTextArea.appendText("Clustering degli autori tramite SimpleKMeans:\n");
						
						System.out.println("Clustering degli autori tramite FarthestFirst:\n");
						resultsTextArea.setStyle("-fx-font-family: monospace");	
						
						Set<Entry<Integer, Integer>> clustersEntrySet = clusters.entrySet();
						for(Entry<Integer, Integer> clusteredAuthor: clustersEntrySet) {
							int authorID = clusteredAuthor.getKey();
							try {
								System.out.println(dblp.getAuthorByID(authorID).getName() + " (" + authorID + ") > cluster " + clusteredAuthor.getValue());
								resultsTextArea.appendText(stretchTo18(dblp.getAuthorByID(authorID).getName()) + " (" + authorID + ") > cluster " + clusteredAuthor.getValue());
								resultsTextArea.appendText("\n");
							} catch (NoAuthorsWithSuchIDException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}										

						
					}
					else {
						resultsLabel.setText("Algoritmo non valido o non selezionato!\n");	
					} 
					Stage stage = new Stage();
					ScrollPane resultPane = new ScrollPane();
					resultPane.setPrefSize(800,250);
					resultPane.setFitToWidth(true);
					resultPane.setFitToHeight(true);
					resultsTextArea.setPrefSize(800,250);
					resultPane.setContent(resultsTextArea);
					
					Scene resultScene = new Scene(resultPane);
					stage.setScene(resultScene);
					stage.centerOnScreen();
					stage.setTitle("Clustering degli autori");
					stage.show();
				}
			});
			
		
			// PHASE 3 - Task 4 - EVENT HANDLER
			phase3Task4ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
								
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();

					int numNodes = Integer.parseInt(phase3Task4NNodesTextField.getText());
					
					Node[] rankedNodes;
					Graph coAuthorsGraph = null;
					try {
						coAuthorsGraph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
					} catch (NoAuthorsWithSuchIDException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rankedNodes = dblp.pageRank(coAuthorsGraph, numNodes);
					
					System.out.println("I " + numNodes + " autori più dominanti sono:");
					resultsLabel.setText("I " + numNodes + " autori più dominanti sono:");
					
					ObservableList<String> resultsObservableList;
					ArrayList<String> resultsArrayList = new ArrayList<String>();
					
					for(int i = 0; i < numNodes; i++) {
						try {
							System.out.println("[" + (i+1) + "] " +  dblp.getAuthorByID(Integer.parseInt(rankedNodes[i].getNodeData().getLabel())).getName()  + " (" + rankedNodes[i].toString() + ")");
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							resultsArrayList.add("[" + (i+1) + "] " +  dblp.getAuthorByID(Integer.parseInt(rankedNodes[i].getNodeData().getLabel())).getName()  + " (" + rankedNodes[i].toString() + ")");
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			
					resultsObservableList = FXCollections.observableArrayList(resultsArrayList);
					resultsListView.setItems(resultsObservableList);			
					
				}
			});
			
			
			// PHASE 3 - Task 5 - controls settings
			phase3Task5AuthorIDComboBox.setItems(authorsIDs);
			// PHASE 3 - Task 5 - EVENT HANDLER
			phase3Task5ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
								
					resultsLabel.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();

					int numNodes = Integer.parseInt(phase3Task5NNodesTextField.getText());
					int authorid = Integer.parseInt(phase3Task5AuthorIDComboBox.getValue());
					
					Author author = null;
					try {
						author = dblp.getAuthorByID(authorid);
					} catch (NoAuthorsWithSuchIDException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Graph graph = null;
					try {
						graph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
					} catch (NoAuthorsWithSuchIDException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println(numNodes + " authors most similar to " + author.getName() + " (" + authorid + ")");
					resultsLabel.setText(numNodes + " authors most similar to " + author.getName() + " (" + authorid + ")");
					
					Map<String, Double> result = dblp.getKMostSimilarAuthors(graph, author, numNodes);				
					
					// Stampo gli autori piu' simili in ordine decrescente
					System.out.println("Found " + result.size() + " authors.");
					

					
					ObservableList<String> resultsObservableList;
					ArrayList<String> resultsArrayList = new ArrayList<String>();
					
					int j = numNodes;
					Set<Entry<String, Double>> resultEntrySet = result.entrySet();
					for(Entry<String, Double> similarAuthor : resultEntrySet) {
						try {
							System.out.println((numNodes - j + 1) + ") " + dblp.getAuthorByID(Integer.parseInt(similarAuthor.getKey())).getName() + " [" + similarAuthor.getKey() + "]: " +  similarAuthor.getValue());
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						j--;
						try {
							resultsArrayList.add((numNodes - j + 1) + ") " + dblp.getAuthorByID(Integer.parseInt(similarAuthor.getKey())).getName() + " [" + similarAuthor.getKey() + "]: " +  similarAuthor.getValue());
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}						
			
					resultsObservableList = FXCollections.observableArrayList(resultsArrayList);
					resultsListView.setItems(resultsObservableList);			
					
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
		
		textArea.setStyle("-fx-font-family: monospace");
		
		for (ArrayList<Double> riga : matrix) {
			for (Double cella : riga) {
				textArea.appendText(stretchTo18(String.format("\t%.7f,", cella)));
			}
			textArea.appendText("\n");
		}

		return;
	}
	
	public void printDocumentTermMatrixOnTextArea(ArrayList<TreeMap<String, Double>> documentTermMatrix, Author author, TextArea textArea) throws AuthorWithoutPapersException {

		textArea.setStyle("-fx-font-family: monospace");
		
		for (String s : author.getKeywordSet()) {
//			System.err.print(stretchTo18(s + ","));
			textArea.appendText(stretchTo18(s + ","));
		}
		
//		System.err.println();
		textArea.appendText("\n");

		for (TreeMap<String, Double> riga : documentTermMatrix) {
			for (Map.Entry<String, Double> cella : riga.entrySet()) {
//				System.err.print(stretchTo18(String.format("%.4f,", cella.getValue())));
				textArea.appendText(stretchTo18(String.format("%.4f,", cella.getValue())));
			}
//			System.err.println();
			textArea.appendText("\n");
		}

		return;
	}
	
	
	//FIXME: da testare
	public void printConceptsKeywordsTableWithCaptions(Table<Integer, String, Double> table, TextArea textArea) {
		
		textArea.setStyle("-fx-font-family: monospace");
		
		int columnsSize = table.columnKeySet().size();
		textArea.appendText(stretchTo18("KW"));
		for (String s : table.columnKeySet()) {
			textArea.appendText(stretchTo18(s));
		}
		textArea.appendText("\n");
		//Riga orizzontale
		for(int k = 0; k < 16 * (columnsSize + 3); k++) {
			textArea.appendText("-");
		}
		System.out.println();
		for (int i : table.rowKeySet()) {
			textArea.appendText(stretchTo18(i + "\t|"));
			for (String j : table.columnKeySet()) {
				textArea.appendText(stretchTo18(String.format("\t%.7f", table.get(i, j))));
			}
			textArea.appendText("\n");
		}
	}
	
	public String stretchTo18(String s) {
		while (s.length() < 18) {
			s = s + " ";
		}
		return s;
	}

}