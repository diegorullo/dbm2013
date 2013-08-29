package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import dblp.Author;
import dblp.Corpus;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoPaperWithSuchIDException;
import exceptions.NoSuchTechniqueException;

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
	
	// exploreDB - controls injection
	@FXML
	private TableView<String> exploreDBAuthorsTableView;
	@FXML
	private TableColumn<Author, String> colAuthors;
	@FXML
	private TableView<String> exploreDBPapersTableView;
	@FXML
	private TableColumn<Author, String> colPapers;

	// Results TextArea - controls injection
	@FXML
	private static TextArea resultsTextArea;
	
	@FXML
	private static GridPane	resultsGridPane;

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
		phase1Task1TitleLabel.setText("titolo del paper selezionato");

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
				try {
					if (paperid != null && !paperid.equals("")) {
						Corpus dblp = Main.getDblp();
						Paper paper = null;
						paper = dblp.getPaperByID(Integer.parseInt(paperid));
						String output = null;
						if (modello.equals("TF")) {
							TreeMap<String, Double> ks = null;
							ks = paper.getTFVector();
							output = "Keyword vector calculated with TF Model:\n"
									+ "-----------------------------------------------------\n"
									+ ks.toString();
						} else if (modello.equals("TFIDF")) {
							TreeMap<String, Double> ks = paper
									.getTFIDFVector(dblp);
							output = "Keyword vector calculated with TFIDF Model:\n"
									+ "-----------------------------------------------------\n"
									+ ks.toString();
						}
						resultsTextArea.setText(output);
					} else {
						resultsTextArea.setText("paperid non valido!");
					}
				} catch (NumberFormatException | NoPaperWithSuchIDException
						| IOException e) {
					// TODO Auto-generated catch block
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
						Paper paper = null;

						paper = dblp.getPaperByID(Integer.parseInt(paperid));
						String title = paper.getTitle();
						phase1Task1TitleLabel.setText(title);

					} else {
						resultsTextArea.setText("paperid non valido!");
					}
				} catch (NumberFormatException | NoPaperWithSuchIDException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// PHASE 1 - Task 2 - controls settings
		phase1Task2AuthorIDComboBox.setItems(authorsIDs);
		phase1Task2ModelComboBox.getItems().clear();
		phase1Task2ModelComboBox.getItems().addAll("TF", "TFIDF");
		phase1Task2ModelComboBox.getSelectionModel().selectFirst();
		phase1Task2TitleLabel.setText("nome autore selezionato");

		// PHASE 1 - Task 2 - EVENT HANDLER
		phase1Task2ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				resultsTextArea.clear();
				resultsTextArea.setText("Attendere prego!\n");
				String modello = phase1Task2ModelComboBox.getValue();
				String authorid = phase1Task2AuthorIDComboBox.getValue();
				try {
					if (authorid != null && !authorid.equals("")) {

						Corpus dblp = Main.getDblp();
						Author author = null;

						author = dblp.getAuthorByID(Integer.parseInt(authorid));

						String output = null;
						if (modello.equals("TF")) {
							TreeMap<String, Double> ks = null;

							ks = author.getWeightedTFVector();

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
				} catch (IOException | AuthorWithoutPapersException
						| NumberFormatException | NoAuthorsWithSuchIDException e1) {
					// TODO Auto-generated catch block
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
								resultsTextArea.setText("authorid non valido!");
							}
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

		// PHASE 1 - Task 3 - controls settings
		phase1Task3AuthorIDComboBox.setItems(authorsIDs);
		phase1Task3ModelComboBox.getItems().clear();
		phase1Task3ModelComboBox.getItems().addAll("TFIDF2", "PF");
		phase1Task3ModelComboBox.getSelectionModel().selectFirst();
		phase1Task3TitleLabel.setText("nome autore selezionato");

		// PHASE 1 - Task 3 - EVENT HANDLER
		phase1Task3ExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resultsTextArea.clear();
				resultsTextArea.setText("Attendere prego!\n");
				String modello = phase1Task3ModelComboBox.getValue();
				String authorid = phase1Task3AuthorIDComboBox.getValue();
				try {
					if (authorid != null && !authorid.equals("")) {
						Corpus dblp = Main.getDblp();
						Author author = null;
						author = dblp.getAuthorByID(Integer.parseInt(authorid));
						String output = null;
						if (modello.equals("TFIDF2")) {
							TreeMap<String, Double> ks = null;
							ks = author.getTFIDF2Vector(dblp);
							output = "Keyword vector calculated with TFIDF2 Model:\n"
									+ "-----------------------------------------------------\n"
									+ ks.toString();
						} else if (modello.equals("PF")) {
							TreeMap<String, Double> ks = null;
							ks = author.getPFVector(dblp);
							output = "Keyword vector calculated with PF Model:\n"
									+ "-----------------------------------------------------\n"
									+ ks.toString();
						}
						resultsTextArea.setText(output);
					} else {
						resultsTextArea.setText("authorid non valido!");
					}
				} catch (NoAuthorsWithSuchIDException
						| AuthorWithoutPapersException | NumberFormatException e1) {
					// TODO Auto-generated catch block
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
								resultsTextArea.setText("authorid non valido!");
							}
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

		// PHASE 2 - Task 1a - controls settings
		phase2Task1aAuthorIDComboBox.setItems(authorsIDs);
		phase2Task1aModelComboBox.getItems().clear();
		phase2Task1aModelComboBox.getItems().addAll("PCA", "SVD");
		phase2Task1aModelComboBox.getSelectionModel().selectFirst();
		phase2Task1aTitleLabel.setText("nome autore selezionato");

		// PHASE 2 - Task 1a - EVENT HANDLER
		phase2Task1aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resultsTextArea.clear();
				resultsTextArea.setText("Attendere prego!\n");
				String modello = phase2Task1aModelComboBox.getValue();
				String authorid = phase2Task1aAuthorIDComboBox.getValue();
				try {
					if (authorid != null && !authorid.equals("")) {
						Corpus dblp = Main.getDblp();
						Author author = null;
						author = dblp.getAuthorByID(Integer.parseInt(authorid));
						@SuppressWarnings("unused")
						String output = null;
						if (modello.equals("PCA")) {
							String fileName = author.getAuthorID().toString();
							ArrayList<ArrayList<Double>> scoreLatentMatrix = author
									.getPCA(dblp, fileName, 5);
							ArrayList<TreeMap<String, Double>> topNMatrix = author
									.getTopN(scoreLatentMatrix, 5);
							if (PRINT_ON_FILE) {
								IO.printDocumentTermMatrixOnFile(
										topNMatrix,
										"../data/PCA_Top5_"
												+ author.getAuthorID() + ".csv");
							}
							if (PRINT_ON_CONSOLE) {
								System.out.println("Matrice Score("
										+ author.getAuthorID() + "): ");
								Printer.printMatrix(scoreLatentMatrix);
								System.out.println("Matrice top 5 PCA ("
										+ author.getAuthorID() + "): "
										+ topNMatrix);
								System.out
										.println("----------------------------------------------------------------------\n");
							}
							output = "Top 5 PCA matrix:\n"
									+ "-----------------------------------------------------\n"
									+ topNMatrix.toString();
							gUIPrintMatrix(scoreLatentMatrix);

						} else if (modello.equals("SVD")) {
							String authorID = author.getAuthorID().toString();
							ArrayList<ArrayList<Double>> vMatrix = author
									.getSVD(dblp, authorID, 5);
							ArrayList<TreeMap<String, Double>> topNMatrix = author
									.getTopN(vMatrix, 5);
							if (PRINT_ON_FILE) {
								IO.printDocumentTermMatrixOnFile(
										topNMatrix,
										"../data/SVD_Top5_"
												+ author.getAuthorID() + ".csv");
							}
							if (PRINT_ON_CONSOLE) {
								System.out.println("Matrice V ("
										+ author.getAuthorID() + "):");
								Printer.printMatrix(vMatrix);
								System.out.println("Matrice top 5 SVD ("
										+ author.getAuthorID() + "): "
										+ topNMatrix);
								System.out
										.println("----------------------------------------------------------------------\n");

							}
							output = "Top 5 SVD:\n"
									+ "-----------------------------------------------------\n"
									+ topNMatrix;
							 gUIPrintMatrix(vMatrix);
						}
						// resultsTextArea.setText(output);

					} else {
						resultsTextArea.setText("authorid non valido!");
					}
				} catch (NoAuthorsWithSuchIDException
						| AuthorWithoutPapersException | NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MatlabConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MatlabInvocationException e) {
					// TODO Auto-generated catch block
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
								resultsTextArea.setText("authorid non valido!");
							}
						} catch (NumberFormatException
								| NoAuthorsWithSuchIDException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	
	
	// PHASE 2 - Task 1b - controls settings
			phase2Task1bAuthorIDComboBox.setItems(authorsIDs);
			phase2Task1bModelComboBox.getItems().clear();
			phase2Task1bModelComboBox.getItems().addAll("keyword vectors", "vettori di differenziazione (TFIDF2)", "vettori di differenziazione (PF)", "top-5 semantiche latenti (PCA)", "top-5 semantiche latenti (SVD)");
			phase2Task1bModelComboBox.getSelectionModel().selectFirst();
			phase2Task1bTitleLabel.setText("nome autore selezionato");

			// PHASE 2 - Task 1b - EVENT HANDLER
			phase2Task1bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsTextArea.clear();
					resultsTextArea.setText("Attendere prego!\n");
					String modello = phase2Task1bModelComboBox.getValue();
					String authorid = phase2Task1bAuthorIDComboBox.getValue();
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
							resultsTextArea.setText(output);
						} else {
							resultsTextArea.setText("authorid non valido!");
						}
						
					} catch (NoAuthorsWithSuchIDException
							| AuthorWithoutPapersException | NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MatlabConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MatlabInvocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchTechniqueException e) {
						// TODO Auto-generated catch block
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
									resultsTextArea.setText("authorid non valido!");
								}
							} catch (NumberFormatException
									| NoAuthorsWithSuchIDException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			
			// PHASE 2 - Task 1c - controls settings
			phase2Task1cAuthorIDComboBox.setItems(authorsIDs);
			phase2Task1cModelComboBox.getItems().clear();
			phase2Task1cModelComboBox.getItems().addAll("keyword vectors", "vettori di differenziazione (TFIDF2)", "vettori di differenziazione (PF)", "top-5 semantiche latenti (PCA)", "top-5 semantiche latenti (SVD)");
			phase2Task1cModelComboBox.getSelectionModel().selectFirst();
			phase2Task1cTitleLabel.setText("nome autore selezionato");

			// PHASE 2 - Task 1c - EVENT HANDLER
			phase2Task1cExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsTextArea.clear();
					resultsTextArea.setText("Attendere prego!\n");
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
							resultsTextArea.setText(output);
						} else {
							resultsTextArea.setText("authorid non valido!");
						}
						
					} catch (NoAuthorsWithSuchIDException
							| AuthorWithoutPapersException | NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MatlabConnectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MatlabInvocationException e) {
						// TODO Auto-generated catch block
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
									resultsTextArea.setText("authorid non valido!");
								}
							} catch (NumberFormatException
									| NoAuthorsWithSuchIDException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

			// PHASE 2 - Task 2a - EVENT HANDLER
			phase2Task2aExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsTextArea.clear();
					resultsTextArea.setText("Attendere prego!\n");
					Corpus dblp = Main.getDblp();
					
					ArrayList<ArrayList<Double>> top3SVDOut = new ArrayList<ArrayList<Double>>();
					String startingDirectory = System.getProperty("user.dir");
		            String path = startingDirectory + "/../data/";
		            String fileName = "SimilarityMatrixAuthor.csv";
					try {
						top3SVDOut = dblp.getTop3SVDAuthor(path, fileName);
					} catch (MatlabConnectionException
							| MatlabInvocationException
							| AuthorWithoutPapersException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String output = null;
					output = "Top 3 SVD Author:\n"
										+ "-----------------------------------------------------\n"
										+ top3SVDOut.toString();
							
					resultsTextArea.setText(output);														
				}
			});
			
			// PHASE 2 - Task 2b - EVENT HANDLER
			phase2Task2bExecuteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resultsTextArea.clear();
					resultsTextArea.setText("Attendere prego!\n");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoAuthorsWithSuchIDException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String output = null;
					output = "Top 3 SVD Author:\n"
										+ "-----------------------------------------------------\n"
										+ top3SVDCoAuthorOut.toString();
							
					resultsTextArea.setText(output);														
				}
			});
			
			//PHASE 2 - 2A
			//TODO: da Corpus: 177 (OK)
			//PHASE 2 - 2B
			//TODO: da Corpus: 245 (OK)
			//PHASE 2 - 3A
			//TODO: da Corpus: 260
			//PHASE 2 - 3B
			//TODO: da Corpus: 383	

		}


	public static void gUIPrintMatrix(ArrayList<ArrayList<Double>> matrix) {
		resultsTextArea.setText("Score Latent Matrix:\n");
		resultsTextArea.appendText("-----------------------------\n");
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
				resultsTextArea.appendText(cellContent);
				System.out.println("\n");
			}
			resultsTextArea.appendText("\n");
		}
		return;
	}

}