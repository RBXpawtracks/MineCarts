package me.thomasvt.minecarts;


import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

 class JoinQuitListeners implements Listener {

	Minecarts minecarts;

	JoinQuitListeners(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void MergedJoinListener(PlayerJoinEvent event) {
		minecarts.publicvoid.welcome(event.getPlayer());
		noJoinMessage(event);
		noOpOnJoin(event);
		noHungerGames(event);
	}
	
	void noHungerGames(PlayerJoinEvent event){
		String world = event.getPlayer().getWorld().getName();
		if (!world.matches("hg"))
			return;
		event.getPlayer().sendMessage("You have been killed becuise you logged in to the HungerGames world");
		event.getPlayer().setHealth(0);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerLoginEvent(PlayerLoginEvent event) {
		if (!minecarts.attack)
			return;
		File file = new File(Bukkit.getServer().getWorlds().get(0).getName() + "/players/" + event.getPlayer().getName() + ".dat");
		if (file.exists())
			event.allow();
		else
			event.disallow(Result.KICK_OTHER, ChatColor.AQUA + "Server is under attack, please wait.");
	}
	
	 void noJoinMessage(PlayerJoinEvent event) {
		if (!minecarts.getConfig().getBoolean("nojoinmessage"))
			return;
		event.setJoinMessage(null);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	 void LoginMessage(PlayerQuitEvent event) {
		if (!minecarts.getConfig().getBoolean("noquitmessage"))
			return;
		event.setQuitMessage(null);
	}
	
	
	 void noOpOnJoin(PlayerJoinEvent event) {
		if (!minecarts.getConfig().getBoolean("noop"))
			return;
		if (event.getPlayer().isOp()) {
			event.getPlayer().setOp(false);
			Bukkit.broadcastMessage(ChatColor.RED + "Watch out player: "
            + ChatColor.AQUA + event.getPlayer().getName() + ChatColor.RED + " had OP!");
		}
	}
}
