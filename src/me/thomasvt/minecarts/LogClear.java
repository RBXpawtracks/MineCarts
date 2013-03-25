package me.thomasvt.minecarts;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class LogClear {
	
	Minecarts minecarts;
	
	LogClear(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	private void clearLog() {
		minecarts.getLogger().info("Starting Clearing of Log.");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("server.log"));
			out.write("");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			minecarts.getLogger().info("Something went wrong while clearing the log");
		}
	}
	 @SuppressWarnings("deprecation")
	void startShedule() {
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
			public void run() {
				clearLog();
			}
		}, 6000, 6000);
	}
}
