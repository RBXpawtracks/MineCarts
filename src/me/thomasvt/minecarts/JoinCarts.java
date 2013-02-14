package me.thomasvt.minecarts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

 class JoinCarts implements Listener {
	
	Minecarts minecarts;

	JoinCarts(Minecarts minecarts) {
		this.minecarts = minecarts;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getPlayer().isBanned()) {
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, 
					ChatColor.RED +
					"You are banned appeal on Minecarts.nl\n" +
					ChatColor.GOLD +
					event.getKickMessage());
			return;
		}

		if (mustAlwaysBeKicked(event)) {
			return;
		}
		if (playerWithSameNameAlreadyOnline(event)) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, 
					ChatColor.RED
					+ "An user with your name is already online.");
			return;
		}

		if (mayJoinBasedOnFullnessAndRights(event)){
			event.allow();
		} else {
			event.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.RED +
					"The server is full\n"+
					ChatColor.GOLD +
					"Donators bypass full server");
		}
	}

	private boolean playerWithSameNameAlreadyOnline(PlayerLoginEvent event) {
		Player p = Bukkit.getPlayerExact(event.getPlayer().getName());
		if (p == null || !p.isOnline()){
			return false;
		}else{
			return true;
		}
	}

	private boolean mustAlwaysBeKicked(PlayerLoginEvent event) {
		switch (event.getResult()) {
		
		case KICK_BANNED:
			return true;
		case KICK_OTHER:
			return true;
		case KICK_WHITELIST:
			return true;
		case ALLOWED:
			return false;
		case KICK_FULL:
			return false;
		default:
			return false;
		}
	}
	
	private boolean mayJoinBasedOnFullnessAndRights(PlayerLoginEvent event) {
		int numberOfPlayers = Bukkit.getServer().getOnlinePlayers().length;
		int maxPlayers = Bukkit.getServer().getMaxPlayers();

		if (numberOfPlayers < maxPlayers) {
			return true;
		}

		Player player = event.getPlayer();
		if (numberOfPlayers < maxPlayers + slotIncrease(player)) {
			return true;
		}
		return false;
	}

	private int slotIncrease(Player player) {
		for (int i = 16; i > 0; i--) {
			if (player.hasPermission("cartjoin." + i)) {
				return i;
			}
		}
		return 0;
	}
}