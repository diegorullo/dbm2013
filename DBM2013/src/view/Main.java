package view;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import dblp.Corpus;
import dblp.Factory;

public class Main extends Application {

	private static Corpus dblp;
	
	public static Corpus getDblp() {
		return dblp;
	}

	public static void main(String[] args) {
		System.out.println("Launching the graphical interface...");
		Application.launch(Main.class, (java.lang.String[]) null);
	}


	@Override
	public void start(Stage primaryStage) {
		try {
			Factory f = new Factory();
			dblp = f.getCorpus();

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
