package application;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainController {
	@FXML
	private Button prevUser;
	@FXML
	private Button newUser;
	@FXML
	private TextField user_name;
	@FXML
	public TextField user_id;
	@FXML
	private TextField phone_nos;
	@FXML
	private TextField email_id;
	@FXML
	private PasswordField passwd;
	@FXML
	private Button reg;
	@FXML
	private TextField login_user_id;
	@FXML
	private PasswordField login_passwd;
	@FXML
	private PasswordField wallet_passwd;
	@FXML
	private Button login;
	@FXML private Label logged_in;
	@FXML private Label unfilled;
	@FXML private Label wrong_usr_pwd;
	
	
			
		public void new_user(ActionEvent e) throws Exception
		{
			((Node)e.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("NewUserForm.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("New User Form");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		public void prev_user(ActionEvent e) throws Exception
		{
			((Node)e.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		public void register(ActionEvent e) throws Exception
		{
			if(imp_isfilled())
			{
				CustomerDatabase obj = new CustomerDatabase();
				obj.new_user_entry(user_name.getText(), user_id.getText(), email_id.getText(), phone_nos.getText(), passwd.getText(), wallet_passwd.getText());
				
				((Node)e.getSource()).getScene().getWindow().hide();
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setTitle("Login");
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		}
		public boolean imp_isfilled()
		{
			if(!(user_name.getText().length() > 0))
			{
				unfilled.setText("Fill User Name");
				return false;
			}
			if(!(user_id.getText().length() > 0))
			{
				unfilled.setText("Fill User ID");
				return false;
			}
			if(!(phone_nos.getText().length() > 0))
			{
				unfilled.setText("Fill Phone Number");
				return false;
			}
			if(!(passwd.getText().length() > 0))
			{
				unfilled.setText("Fill Password");
				return false;
			}
			if(!(wallet_passwd.getText().length() > 0))
			{
				unfilled.setText("Fill 4 Digit Wallet Pin");
				return false;
			}
			return true;
		}
		public void login(ActionEvent e) throws Exception
		{
			CustomerDatabase obj = new CustomerDatabase();
			String passwd = obj.return_passwd(login_user_id.getText());
			if(passwd.equals(login_passwd.getText()))
			{
				if(!obj.is_logged_in(login_user_id.getText()))
				{
					FXMLLoader loader = new FXMLLoader(getClass().getResource("startpage.fxml"));
					Parent root = loader.load();
					startpageController controller = loader.getController();
					controller.assign_data(login_user_id.getText());
					((Node)e.getSource()).getScene().getWindow().hide();
					Stage primaryStage = new Stage();
					
					/*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					    @Override
					    public void handle(WindowEvent t) {
					        Platform.exit();
					        //System.exit(0);
					    }
					});*/
					
					Scene scene = new Scene(root);
					primaryStage.setTitle("Start Page");
					primaryStage.setScene(scene);
					primaryStage.show();
					
					obj.update_log_in(login_user_id.getText());
				}
				else
				{
					logged_in.setText("Already Logged in");
				}
				
			}
			else
			{
				wrong_usr_pwd.setText("Wrong UserID/Password");
			}
		}
		
	}


