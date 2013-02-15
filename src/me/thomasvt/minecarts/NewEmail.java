package me.thomasvt.minecarts;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.thomasvt.chatlogger.ChatLogger;


 class NewEmail{
  Minecarts minecarts;

  NewEmail(Minecarts minecarts){
    this.minecarts = minecarts;
  }

  @SuppressWarnings("deprecation")
 void set(final String player, final String email){
	  minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				setEmail(player, email);
		}
	}, 0);
  }
  
  private void setEmail(String player, String email)
  {
    String database = "authme";
    PreparedStatement pst = null;
    try {
    	pst = ChatLogger.connection().prepareStatement("UPDATE  `mc20`.`"+database+"` SET  `email` =  '"+email+"' WHERE `username` = '"+player+"'");
    	pst.executeUpdate();
    }
    catch (SQLException ex) {
      System.out.print(ex);
    }
  }
}