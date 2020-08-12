package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Node;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

public class walletPageLoginController {
	@FXML
	private TextField user_id;
	@FXML
	private PasswordField wallet_pin;
	@FXML
	private Button continuer;
	@FXML
	private Label wrong_input;

	// Event Listener on Button[#continuer].onAction
	@FXML
	public void continues(ActionEvent event) throws Exception
	{
		CustomerDatabase obj = new CustomerDatabase();
		String wallet_passwd = obj.return_wallet_pin(user_id.getText());
		if(wallet_passwd.equals(wallet_pin.getText()))
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("walletPage.fxml"));
			Parent root = loader.load();
			walletPageController controller = loader.getController();
			controller.assign_data(user_id.getText());
			((Node)event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			primaryStage.setTitle("Wallet Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		else
		{
			wrong_input.setText("Wrong UserID/Pin");
		}
	}
}
