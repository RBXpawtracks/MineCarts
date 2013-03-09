package me.thomasvt.minecarts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

 class EventVoid {

	Minecarts minecarts;

	EventVoid(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	ArrayList<Player> fireworkCooldown = new ArrayList<Player>();
	ArrayList<Player> commandCooldown = new ArrayList<Player>();
	ArrayList<Player> chatCooldown = new ArrayList<Player>();
	
	 void blockBow(PlayerInteractEvent e){
		if (e.getMaterial() == Material.BOW && !e.getPlayer().hasPermission("minecarts.shootbow"))
			e.setCancelled(true);
	}
	
	 void enderDisabler(PlayerInteractEvent e) {
		Material m = e.getMaterial();
		if (m == Material.EYE_OF_ENDER && e.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME){
			return;
		}
		if (m == Material.ENDER_PEARL || m == Material.EYE_OF_ENDER || m == Material.EXP_BOTTLE)
			e.setCancelled(true);
	}
	 
	 void noPlugins(PlayerCommandPreprocessEvent e){
			String cmd = e.getMessage();
			if (cmd.matches("/pl") || cmd.matches("/plugins")){
					if (e.getPlayer().hasPermission("minecarts.plugins"))
						return;
					else
						e.setCancelled(true);
			}
	 }
	 
	 @SuppressWarnings("deprecation")
	void commandCooldown(PlayerCommandPreprocessEvent e){
		 final Player p = e.getPlayer();
		 if (commandCooldown.contains(p)){
			 e.setCancelled(true);
			 p.sendMessage(ChatColor.RED+"Please wait before typing a new command");
			 return;
		 }
		 commandCooldown.add(p);
		    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			      public void run() {
			    	  commandCooldown.remove(p);
			      }
			    }, 40);
	 }
	 
	 @SuppressWarnings("deprecation")
	void chatCooldown(AsyncPlayerChatEvent e){
		 final Player p = e.getPlayer();
		 if (chatCooldown.contains(p)){
			 e.setCancelled(true);
			 p.sendMessage(ChatColor.RED+"Please wait before typing a new message");
			 return;
		 }
		 chatCooldown.add(p);
		    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			      public void run() {
			    	  chatCooldown.remove(p);
			      }
			    }, 10);
	 }
	 
	@SuppressWarnings("deprecation")
	void fireWorkSlowDown(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.getItemInHand().getType() != Material.FIREWORK)
			return;
		if (p.hasPermission("minecarts.fastfirework"))
			return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (fireworkCooldown.contains(p)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED
					+ "You need to wait before launching another firework!");
			return;
		}
		fireworkCooldown.add(p);
	    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
		      public void run() {
		    	  fireworkCooldown.remove(p);
		      }
		    }, 60);
	}
	
	 void noSnowball(PlayerInteractEvent e) {
		if (e.getMaterial() == Material.SNOW_BALL && !e.getPlayer().hasPermission("minecarts.snowball"))
			e.setCancelled(true);
	}
	
	 void potionDisabler(PlayerInteractEvent e) {
		if (e.getMaterial() == Material.POTION && !e.getPlayer().hasPermission("minecarts.potion"))
			e.setCancelled(true);
	}
	 
	 void buildProtect(BlockBreakEvent e){
		 String w = e.getBlock().getWorld().getName();
		 Player p = e.getPlayer();
		 if (!p.hasPermission("minecarts.build."+w)){
			 e.setCancelled(true);
			 e.getPlayer().sendMessage(ChatColor.RED+"You don't have permissions to build in this world");
		 }
	 }
	 
	 void buildProtect(BlockPlaceEvent e){
		 String w = e.getBlock().getWorld().getName();
		 Player p = e.getPlayer();
		 if (!p.hasPermission("minecarts.build."+w)){
			 e.setCancelled(true);
			 e.getPlayer().sendMessage(ChatColor.RED+"You don't have permissions to build in this world");
		 }
	 }
	
	   void lavaBlocker(BlockPlaceEvent e) {
		if (e.getPlayer().hasPermission("minecarts.lava"))
			return;
	    if ((e.getBlockPlaced().getType() == Material.LAVA) || (e.getBlockPlaced().getType() == Material.STATIONARY_LAVA)) {
	    	minecarts.getLogger().info("Prevented " + e.getPlayer().getName() + " from placing lava");
	        e.setCancelled(true);
	    }
	  }
	  
		 void blockGameMode(BlockPlaceEvent e) {
			if (!minecarts.getConfig().getBoolean("gamemodecheck"))
				return;
			if (!e.getPlayer().hasPermission("minecarts.c") && e.getPlayer().getGameMode().getValue() == 1) {
				e.setCancelled(true);
				minecarts.publicvoid.warnCreative(e.getPlayer());
			}
		}
		
		 void chatNoCapital(AsyncPlayerChatEvent e) {
			 if (e.getMessage().contains(":") && e.getMessage().length() <3)
				 return;
			if (!e.getPlayer().hasPermission("minecarts.caps"))
				e.setMessage(e.getMessage().toLowerCase());
		}
		
		private String lastmessage;
		public void chatNoRepeat(AsyncPlayerChatEvent e) {
			String msg = e.getMessage().toLowerCase();
			if (msg.equals(lastmessage))
				e.setCancelled(true);
			lastmessage = msg;
	}

		 void chatContainsName(AsyncPlayerChatEvent e) {
			if(e.isCancelled())
				return;
			minecarts.publicvoid.chatContainsName(e.getMessage(), e.getRecipients());
	}
}