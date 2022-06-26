package de.bwdesign.print;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage) {
		Label label = new Label("Print Test");
		StackPane  stackpane = new StackPane();
		stackpane.getChildren().add(label);
		Scene scene = new Scene(stackpane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				File reportdir = new File("res","reports");
				File report = new File(reportdir,"test.rptdesign");
				try {
					File tmp = File.createTempFile("Test_", ".pdf");
					Print print = new Print();
					print.runReport(report, tmp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
	}

}
