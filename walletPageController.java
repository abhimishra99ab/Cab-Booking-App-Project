package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class walletPageController 
{
	@FXML
	private Button add_money;
	@FXML
	private Button show_available_bal;
	@FXML
	private TextField add_amt;
	@FXML
	private Label available_bal;
	public String temp_id;
	
	public void assign_data(String user_id)
	{
		temp_id = user_id;
	}
	public void showAvailableBalance(ActionEvent e) throws Exception
	{
		CustomerDatabase obj1 = new CustomerDatabase();
		available_bal.setText(Integer.toString(obj1.wallet_amt(temp_id)));
	}
	public void add_money(ActionEvent e) throws Exception
	{
		int amt = Integer.parseInt(add_amt.getText());
		CustomerDatabase obj1 = new CustomerDatabase();
		obj1.add_money(amt, temp_id);
		
		available_bal.setText(Integer.toString(obj1.wallet_amt(temp_id)));
	}
}
