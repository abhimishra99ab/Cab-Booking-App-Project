package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class CustomerDatabase 
{
	
	 public Connection getConnection() throws Exception
	 {
		 try
	{
	   String driver = "com.mysql.cj.jdbc.Driver";
	   String url = "jdbc:mysql://localhost:3306/customerdb";
	   String username = "root";
	   String password = "root";
	   Class.forName(driver);
	   
	   Connection conn = DriverManager.getConnection(url,username,password);
	   return conn;
	  } catch(Exception e)
		 {
		  	System.out.println(e);
		 }
	  return null;
	 }
	 public void new_user_entry(String name, String id, String email, String phone_no, String passwd, String wallet_pwd) throws Exception
	 {
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("INSERT INTO customer_list (username, user_id, phone_nos, email_id, password, wallet_pin) "
		 		+ "VALUES ('"+name+"','"+id+"', '"+phone_no+"', '"+email+"','"+passwd+"','"+wallet_pwd+"')");
		 qry1.executeUpdate();
		 conn.close();
	 }
	 public String return_passwd(String user_id) throws Exception
	 {
		 String pass = "";
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("SELECT password FROM customer_list WHERE user_id=?");
		 qry1.setString(1, user_id);
		 ResultSet res = qry1.executeQuery();
		 while(res.next())
		 {
			 pass = res.getString("password");
		 }
		 conn.close();
		 return pass;
	 }
	 public boolean is_logged_in(String user_id) throws Exception
	 {
		 int log = 0;
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("SELECT login FROM customer_list WHERE user_id=?");
		 qry1.setString(1, user_id);
		 ResultSet res = qry1.executeQuery();
		 while(res.next())
		 {
			 log = res.getInt("login");
		 }
		 conn.close();
		 return (log==0) ? false : true;
	 }
	 public void update_log_in(String user_id) throws Exception
	 {
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("UPDATE customer_list SET login="+1+" WHERE user_id=?");
		 qry1.setString(1, user_id);
		 qry1.executeUpdate();
		 conn.close();
	 }
	 public void reset_all_logins_and_ridings() throws Exception
	 {
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("UPDATE customer_list SET login=?");
		 qry1.setInt(1, 0);
		 qry1.executeUpdate();
		 PreparedStatement qry2 = conn.prepareStatement("UPDATE customer_list SET riding=?");
		 qry2.setInt(1, 0);
		 qry2.executeUpdate();
		 conn.close();
	 }
	 
	 
	 public void add_money(int amt, String user_id) throws Exception
	 {
		 Connection conn = getConnection();
		 int new_amt = amt + wallet_amt(user_id);
		 PreparedStatement qry2 = conn.prepareStatement("UPDATE customer_list SET wallet=? WHERE user_id=?");
		 qry2.setInt(1, new_amt);
		 qry2.setString(2, user_id);
		 qry2.executeUpdate();
		 conn.close();
	 }
	 public int wallet_amt(String user_id) throws Exception
	 {
		 int existing_amt = 0;
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("SELECT wallet FROM customer_list WHERE user_id=?");
		 qry1.setString(1, user_id);
		 ResultSet res = qry1.executeQuery();
		 while(res.next())
		 {
			 existing_amt = res.getInt("wallet");
		 }
		 conn.close();
		 return existing_amt;
	 }
	 public String return_wallet_pin(String user_id) throws Exception
	 {
		 String pass = "";
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("SELECT wallet_pin FROM customer_list WHERE user_id=?");
		 qry1.setString(1, user_id);
		 ResultSet res = qry1.executeQuery();
		 while(res.next())
		 {
			 pass = res.getString("wallet_pin");
		 }
		 conn.close();
		 return pass;
	 }
	 public void assign_driver_details(int driver_id, String driver_location, double driver_rating, int rides) throws Exception
	 {
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("UPDATE driver_list SET curr_location=? WHERE driver_id=?");
		 qry1.setString(1, driver_location);
		 qry1.setInt(2, driver_id);
		 qry1.executeUpdate();
		 
		 PreparedStatement qry2 = conn.prepareStatement("UPDATE driver_list SET rating=? WHERE driver_id=?");
		 qry2.setDouble(1, driver_rating);
		 qry2.setInt(2, driver_id);
		 qry2.executeUpdate();
		 
		 PreparedStatement qry3 = conn.prepareStatement("UPDATE driver_list SET busy_ness=? WHERE driver_id=?");
		 qry3.setInt(1, 0);
		 qry3.setInt(2, driver_id);
		 qry3.executeUpdate();
		 
		 qry3 = conn.prepareStatement("UPDATE driver_list SET nos_rides=? WHERE driver_id=?");
		 qry3.setInt(1, rides);
		 qry3.setInt(2, driver_id);
		 qry3.executeUpdate();
		 conn.close();
	 }
	 public int[]  closest_driver(String pick_up_point) throws Exception
	 {
		 int closestDr = 0;
		 Connection conn = getConnection();
		 travel obj = new travel();
		 PreparedStatement qry1 = conn.prepareStatement("SELECT curr_location, busy_ness FROM driver_list");
		 ResultSet res = qry1.executeQuery();
		 int i = 1;
		 int min_dist = 100000;
		 while(res.next())
		 {
			// System.out.println("HI");
			 String dr_loc = res.getString("curr_location");
			 int busy = res.getInt("busy_ness");
			 int dist = obj.dist_bw_loc(dr_loc, pick_up_point);
			 if(dist <= min_dist && busy==0)
			 {
				 if(dist == min_dist)
				 {
					 double curr_rat = 0;
					 double old_rat = 0;
					 PreparedStatement qry2 = conn.prepareStatement("SELECT rating FROM driver_list WHERE driver_id=?");
					 qry2.setInt(1, i);
					 ResultSet res1 = qry2.executeQuery();
					 while(res1.next())
					 {
						 curr_rat = res1.getDouble("rating");
					 }
					 
					 PreparedStatement qry3 = conn.prepareStatement("SELECT rating FROM driver_list WHERE driver_id=?");
					 qry3.setInt(1, closestDr);
					 ResultSet res2 = qry3.executeQuery();
					 while(res2.next())
					 {
						 old_rat = res2.getDouble("rating");
					 }
					 
					 if(curr_rat > old_rat)
					 {
						 min_dist = dist;
						 closestDr = i;
					 }
				 }
				 else
				 {
					 min_dist = dist;
					 closestDr = i;
				 }
			 }
			 i++;
		 }
		 int[] some_name = {closestDr, min_dist};
		 conn.close();
		 return some_name;
	 }
	 public String[] get_driver_details(int driver_id) throws Exception
	 {
		 String[] details = new String[3];
		 Connection conn = getConnection();
		 PreparedStatement qry1 = conn.prepareStatement("SELECT * FROM driver_list WHERE driver_id=?");
		 qry1.setInt(1, driver_id);
		 ResultSet res = qry1.executeQuery();
		 while(res.next())
		 {
			 details[0] = res.getString("name");
			 details[1] = res.getString("phone_nos");
			 details[2] = Double.toString(res.getDouble("rating"));
		 }
		 conn.close();
		 return details;
	 }
	 public void driver_busy_ness(int driver_id, int busy) throws Exception
	 {
		 Connection conn = getConnection();
		 PreparedStatement qry3 = conn.prepareStatement("UPDATE driver_list SET busy_ness=? WHERE driver_id=?");
		 qry3.setInt(1, busy);
		 qry3.setInt(2, driver_id);
		 qry3.executeUpdate();
		 conn.close();
	 }
	 public void driver_relocate(int driver_id, String new_driver_loc) throws Exception
	 {
		 Connection conn = getConnection();
		 PreparedStatement qry3 = conn.prepareStatement("UPDATE driver_list SET curr_location=? WHERE driver_id=?");
		 qry3.setString(1, new_driver_loc);
		 qry3.setInt(2, driver_id);
		 qry3.executeUpdate();
		 conn.close();
	 }
	public void deduct_user_money(String user_id, int deduct_amt) throws Exception
	{
		 int existing_amt = wallet_amt(user_id);
		 Connection conn = getConnection();
		 PreparedStatement qry3 = conn.prepareStatement("UPDATE customer_list SET wallet=? WHERE user_id=?");
		 qry3.setInt(1, existing_amt - deduct_amt);
		 qry3.setString(2, user_id);
		 qry3.executeUpdate();
		 conn.close();
	}
	public boolean is_riding(String user_id) throws Exception
	{
		 int riding = 0;
		 Connection conn = getConnection();
		 PreparedStatement qry3 = conn.prepareStatement("SELECT riding FROM customer_list WHERE user_id=?");
		 qry3.setString(1, user_id);
		 ResultSet res = qry3.executeQuery();
		 while(res.next())
		 {
			 riding = res.getInt("riding");
		 }
		 conn.close();
		 if(riding == 1)
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	}
	public void set_riding(String user_id, int ride) throws Exception
	{
		 Connection conn = getConnection();
		 PreparedStatement qry3 = conn.prepareStatement("UPDATE customer_list SET riding=? WHERE user_id=?");
		 qry3.setInt(1, ride);
		 qry3.setString(2, user_id);
		 qry3.executeUpdate();
		 conn.close();
	}
	public void update_driver_rating(int driver_id, double rating) throws Exception
	{
		Connection conn = getConnection();
		double old_rating = 0;
		int nos_rides = 0;
		PreparedStatement qry3 = conn.prepareStatement("SELECT rating, nos_rides FROM driver_list WHERE driver_id=?");
		qry3.setInt(1, driver_id);
		ResultSet res = qry3.executeQuery();
		while(res.next())
		{
			old_rating = res.getDouble("rating");
			nos_rides = res.getInt("nos_rides");
		}
		double new_rating = (old_rating * nos_rides + rating)/(nos_rides + 1);
		qry3 = conn.prepareStatement("UPDATE driver_list SET rating=? WHERE driver_id=?");
		qry3.setDouble(1, new_rating);
		qry3.setInt(2, driver_id);
		qry3.executeUpdate();
		
		qry3 = conn.prepareStatement("UPDATE driver_list SET nos_rides=? WHERE driver_id=?");
		qry3.setDouble(1, nos_rides + 1);
		qry3.setInt(2, driver_id);
		qry3.executeUpdate();
		conn.close();		
	}
}
