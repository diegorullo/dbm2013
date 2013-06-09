package view;


import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import dblp.Corpus;
import dblp.Factory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	Factory f;
	Corpus dblp;

   public static void main(String[] args) {	
 
        Application.launch(Main.class, (java.lang.String[])null);
    }
   

    @Override
    public void start(Stage primaryStage) {
        try {
        	
//    	    try {
    			f = new Factory();
    			dblp = f.getCorpus();
//    		} catch (IOException | SQLException | MatlabConnectionException
//    				| MatlabInvocationException e) {
//    			// TODO Auto-generated catch block
//    			System.out.println("Errore durante l'avvio dell'applicazione");
//    			e.printStackTrace();
//    		}
        	
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

