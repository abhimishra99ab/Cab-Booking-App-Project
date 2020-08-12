package application;
import java.util.Random;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
			Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Home Page");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	public static void main(String[] args) throws Exception 
	{
		CustomerDatabase obj = new CustomerDatabase();
		obj.reset_all_logins_and_ridings();
		random_driver_distribution();
		launch(args);
	}
	public static void random_driver_distribution() throws Exception 
	{
		travel ob = new travel();
		CustomerDatabase obj = new CustomerDatabase();
		Random rand = new Random();
		for(int i=1 ; i<=12 ; i++)
		{
			int random_loc_index = rand.nextInt(8);
			double random_rating = (rand.nextDouble()) * 5;
			String random_loc = ob.locations[random_loc_index];
			int random_nos_rides = rand.nextInt(100) + 10;
			obj.assign_driver_details(i, random_loc, random_rating, random_nos_rides);	
		}
	}
	
}
