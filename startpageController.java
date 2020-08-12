package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import org.controlsfx.control.Rating;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;




public class startpageController implements Initializable {
	
	@FXML
	private Button new_ride;
	@FXML
	private Button wallet;
	@FXML
	private ComboBox<String> pickup;
	@FXML
	private ComboBox<String> dropoff;
	@FXML
	private Button book_cab;
	@FXML
	private Label driver_arrival_time;
	@FXML
	private Label trip_duration;
	@FXML
	private Label trip_cost;
	@FXML
	private Label driver_name;
	@FXML
	private Label driver_phone_nos;
	@FXML
	private Label driver_rating;
	@FXML
	private Label time_out;
	@FXML
	private Label pennyless;
	@FXML
	private Label reached;
	private String user_id;
	@FXML
	private ProgressBar pBar;
	@FXML
	private Label driver_here;
	@FXML
    private Rating rate;
	@FXML
    private CheckBox rating_given;
	private int driver_id1;
	double start_time = 0;
	
	ObservableList<String> list= FXCollections.observableArrayList("Secunderabad", "BPHC" , "Banjara Hills", "Panjagutta", "Jubilee Hills", "Hi-Tech city", "Nampally", "Golconda Fort");
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{	
		pickup.setItems(list);
		dropoff.setItems(list);
		rating_settings();
		rating_given.setOpacity(0);
	}
	public void assign_data(String userID)
	{
		user_id = userID;
	}
	
	public void newRide(ActionEvent e) throws IOException 
	{
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void wallet_func(ActionEvent e) throws IOException
	{
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("walletPageLogin.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Wallet Page Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public void book_cab_func(ActionEvent e) throws Exception
	{
		rating_settings();
		rating_given.setOpacity(0);
		rating_given.setSelected(false);
		
		CustomerDatabase obj = new CustomerDatabase();
		travel ob = new travel();
		int[] some_stuff = obj.closest_driver(pickup.getValue());
		int driver_id = some_stuff[0];
		driver_id1 = driver_id;
		int driver_dist = some_stuff[1];
		int destination_dist = ob.dist_bw_loc(pickup.getValue(), dropoff.getValue());
		reached.setText("");
		
		pBar.setProgress(0);
		if(obj.wallet_amt(user_id) >= 300 && obj.wallet_amt(user_id) >= 4 * destination_dist)
		{
			if(!obj.is_riding(user_id))
			{
				obj.set_riding(user_id, 1);
				if(driver_id==0)
				{
					time_out.setText("Request Timed Out");
					reset_details();
				}
				else
				{
				    String[] details = obj.get_driver_details(driver_id);
					driver_arrival_time.setText(Integer.toString(2 * driver_dist));
					trip_duration.setText(Integer.toString(2 * destination_dist));
					trip_cost.setText(Integer.toString(4 * destination_dist));
					driver_name.setText(details[0]);
					driver_phone_nos.setText(details[1]);
					driver_rating.setText(details[2]);
					pennyless.setText("");
					obj.driver_busy_ness(driver_id, 1);
					long delay_time = (2 * destination_dist + 2 * driver_dist) * 1000;
					
					new java.util.Timer().schedule(new java.util.TimerTask()
					{
					            @Override
					            public void run()
					            {
					            	try
					            	{
					            		obj.driver_busy_ness(driver_id, 0);
					            		obj.driver_relocate(driver_id, dropoff.getValue());
					            		obj.deduct_user_money(user_id, 4 * destination_dist);
					            		obj.set_riding(user_id, 0);
					            		/*startpageController ob = new startpageController();
					            		ob.reset_details();*/
					            		
					            		Platform.runLater(new Runnable() {
					            		    @Override public void run() {
					            		        reset_details();
					            		        pennyless.setText("");
					            		        reached.setText("You have arrived at your destination " );
					            		        rate.setOpacity(1);
					            		        rating_given.setOpacity(1);
					            		    }
					            		});
					            		
					            	}
					            	catch(Exception e)
					            	{
					            		System.out.println(e);
					            	}
					            }
					}, delay_time);
					new java.util.Timer().schedule(new java.util.TimerTask() 
					{
					            @Override
					            public void run() 
					            {
					            	Platform.runLater(new Runnable() 
					            	{
				            		    @Override 
				            		    public void run() 
				            		    {
				            		    	driver_here.setText("Driver Arrived");
				            		    	start_time = System.currentTimeMillis() * 0.001d;
				            		    }
					            	});
					            	Timer t = new java.util.Timer();
					            	t.schedule(new java.util.TimerTask() 
					            	{
									            @Override
									            public void run() 
									            {
									            	Platform.runLater(new Runnable() 
									            	{
								            		    @Override 
								            		    public void run() 
								            		    {
								            		    	double elapsed_time = System.currentTimeMillis() * 0.001d - start_time;
								            		    	double delay = (2 * destination_dist);
								            		        pBar.setProgress(elapsed_time/delay);
								            		        if(elapsed_time/delay > 1)
								            		        {
								            		        	t.cancel();
								            		        	t.purge();
								            		        }
								            		    }
								            		});
									            }
									 },  0, 500);
					            }
					   }, (2 * driver_dist) *1000);
					remove_driver_arrived_tag((2 * driver_dist + 2) *1000);
				}
			}
			else
			{
				pennyless.setText("You are already on a ride");
				//reset_details();
			}
		}
		else
		{
			pennyless.setText("Error: Money Insufficient");
			reset_details();
		}
	}
	public void reset_details()
	{
		driver_arrival_time.setText("-");
		trip_duration.setText("-");
		trip_cost.setText("-");
		driver_name.setText("-");
		driver_phone_nos.setText("-");
		driver_rating.setText("-");
	}
	public void rating_settings()
	{
		rate.setPartialRating(true);
		rate.setRating(0);
		rate.setMax(5);
		rate.setOpacity(0);
	}
	public void remove_driver_arrived_tag(int time)
	{
		new java.util.Timer().schedule(new java.util.TimerTask() 
		{
		            @Override
		            public void run() 
		            {
		            	Platform.runLater(new Runnable() 
		            	{
	            		    @Override 
	            		    public void run() 
	            		    {
	            		    	driver_here.setText("");
	            		    }
		            	});
		            }
		}, time);
	}
	public void rating_given_action(ActionEvent e) throws Exception
	{
		CustomerDatabase obj = new CustomerDatabase();
		obj.update_driver_rating(driver_id1, rate.getRating());
	}
}
