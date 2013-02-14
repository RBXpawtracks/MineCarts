package me.thomasvt.minecarts;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

 class Shedule {

	Minecarts minecarts;

	Shedule(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	@SuppressWarnings("deprecation")
	public void noSpawnChunks(){
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
		      public void run() {
		        for (World world : minecarts.getServer().getWorlds())
		          if (world.getPlayers().isEmpty())
		            for (Chunk c : world.getLoadedChunks())
		              if (c.getWorld().getPlayers().isEmpty())
		                world.unloadChunk(c);
		      }
		    }
		    , 200L, 200L);
	}
	
	@SuppressWarnings("deprecation")
	private void payDay(){
		minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts, new Runnable() {
			public void run(){
		for (Player p : Bukkit.getOnlinePlayers())
			minecarts.publicvoid.depositMoney(p.getName(), 25);
			}
		}, 0, 6000);
	}
	
	@SuppressWarnings("deprecation")
	private void trollmode(){
		if (minecarts.getConfig().getBoolean("trollmode")) {
			minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts,
					new Runnable() {
						public void run() {
							minecarts.publicvoid.randomSound();
						}
					}, 0, 24000);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void gmCheck(){
		if (minecarts.getConfig().getBoolean("gamemodecheck")) {
			minecarts.getServer().getScheduler().scheduleAsyncRepeatingTask(minecarts,
					new Runnable() {
						public void run() {
							minecarts.publicvoid.creativeCheck();
						}
					}, 0, 100);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void noInvisible(){
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
		if (minecarts.getConfig().getBoolean("removeinvisible")) {
			minecarts.getServer().getScheduler().scheduleSyncRepeatingTask(minecarts,
					new Runnable() {
						public void run() {
							minecarts.publicvoid.removeInvisibility();
						}
					}, 0, 100);
				}	
			}
		}, 0);
	}

	 void scheduler() {
		// 2
		trollmode();
		// 3
		payDay();
		// 4
		gmCheck();
		// 5
		noInvisible();
		// 6
		noSpawnChunks();
	}
}