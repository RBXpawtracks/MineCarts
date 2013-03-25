package me.thomasvt.minecarts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

 class Shedule {

	Minecarts minecarts;

	Shedule(Minecarts minecarts) {
		this.minecarts = minecarts;
	}

	@SuppressWarnings("deprecation")
	 void donateReminder(){
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
		      public void run() {
		    		Bukkit.broadcastMessage(ChatColor.AQUA+"$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$");
		    		Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"MineCarts offers donation perks");
		    		Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"The donations help to keep MineCarts online");
		    		Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"All perks on: minecarts.nl/pages/donate");
		    		Bukkit.broadcastMessage(ChatColor.AQUA+"$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$-$");
		    	  }
		}, 12000, 12000);
	}
	
	private void noSpawnChunks(){
		minecarts.getServer().getScheduler().scheduleSyncRepeatingTask(minecarts, new Runnable() {
		      public void run() {
		        for (World world : minecarts.getServer().getWorlds())
		          if (world.getPlayers().isEmpty())
		            for (Chunk c : world.getLoadedChunks())
		              if (c.getWorld().getPlayers().isEmpty())
		                world.unloadChunk(c);
		      }
		    }
		    , 0, 200);
	}
	
	@SuppressWarnings("deprecation")
	private void clearList(){
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
			public void run(){
		minecarts.cooldownguess.clear();
		minecarts.listeners.dispensecooldown.clear();
		minecarts.eventvoid.chatCooldown.clear();
		minecarts.eventvoid.commandCooldown.clear();
		minecarts.eventvoid.fireworkCooldown.clear();
			}
		}, 6000, 6000);
	}
	
	@SuppressWarnings("deprecation")
	private void payDay(){
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
			public void run(){
		for (Player p : Bukkit.getOnlinePlayers())
			minecarts.publicvoid.depositMoney(p.getName(), 25);
			}
		}, 6000, 6000);
	}
	
	@SuppressWarnings("deprecation")
	private void gmCheck() {
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
					public void run() {
						minecarts.publicvoid.creativeCheck();
						minecarts.publicvoid.removeInvisibility();
					}
				}, 100, 100);
	}
	
	 void scheduler() {
		payDay();
		gmCheck();
		noSpawnChunks();
		donateReminder();
		clearList();
	}
}