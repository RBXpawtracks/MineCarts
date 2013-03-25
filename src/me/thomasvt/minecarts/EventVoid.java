package me.thomasvt.minecarts;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		if (m == Material.EYE_OF_ENDER || m == Material.EXP_BOTTLE|| m == Material.ENDER_PEARL){
		e.setCancelled(true);
		if (m == Material.ENDER_PEARL) {
			if (e.getPlayer().getWorld().getName().matches("pvp"))
				e.setCancelled(false);
			}
		}
	}
	 
	 void noPlugins(PlayerCommandPreprocessEvent e){
			String cmd = e.getMessage();
			if (cmd.matches("/?") || cmd.matches("/pl") || cmd.matches("/plugins")){
					if (e.getPlayer().hasPermission("minecarts.plugins"))
						return;
					else
						e.setCancelled(true);
			}
	 }
	 
	 @SuppressWarnings("deprecation")
	void commandCooldown(PlayerCommandPreprocessEvent e){
		 final Player p = e.getPlayer();
		 if (p.hasPermission("minecarts.fastcommand"))
			 return;
		 if (commandCooldown.contains(p)){
			 e.setCancelled(true);
			 p.sendMessage(ChatColor.RED+"Please wait before typing a new command");
			 commandCooldown.remove(p);
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
		 if (p.hasPermission("minecarts.fastchat"))
			 return;
		 if (chatCooldown.contains(p)){
			 e.setCancelled(true);
			 p.sendMessage(ChatColor.RED+"Please wait before typing a new message");
			 chatCooldown.remove(p);
		 }
		 chatCooldown.add(p);
		    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			      public void run() {
			    	  chatCooldown.remove(p);
			      }
			    }, 20);
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
		if (e.getMaterial() != Material.SNOW_BALL)
			return;
		Player p = e.getPlayer();
		if (!p.hasPermission("minecarts.snowball"))
			e.setCancelled(true);
	}
	
	 void potionDisabler(PlayerInteractEvent e) {
		if (e.getMaterial() != Material.POTION)
			return;
		Player p = e.getPlayer();
		if (!p.hasPermission("minecarts.potion"))
			e.setCancelled(true);
	}
	 
	 void buildProtect(BlockBreakEvent e){
		 Player p = e.getPlayer();
		 if (!p.hasPermission("minecarts.build."+e.getBlock().getWorld().getName())){
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
		 void chatNoRepeat(AsyncPlayerChatEvent e) {
			String msg = e.getMessage().toLowerCase();
			if (msg.equals(lastmessage))
				e.setCancelled(true);
			lastmessage = msg;
	}

		 void chatdontSay(AsyncPlayerChatEvent e) {
			if (e.getMessage().contains("lagg")){
				e.setMessage(e.getMessage().replaceAll("lagg", ""));
				return;
			}
			else if (e.getMessage().contains("lag"))
				e.setMessage(e.getMessage().replaceAll("lag", ""));
		}

		 void spyClock(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			if (e.getMaterial().getId() == 347){
				if (p.getWorld().getName().matches("tf2"))
					if (p.hasPermission("tf2.button.donator")){
						if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
							p.sendMessage(ChatColor.ITALIC+"Your are already vanished!");
							return;
						}
						p.sendMessage(ChatColor.ITALIC+"Your are now vanished!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,400, 1));
						minecarts.publicvoid.spyClock(p, 15);
					}
			}
		}

		 void tf2NoLoss(PlayerCommandPreprocessEvent e) {
			String w = e.getPlayer().getWorld().getName();
			if (!e.getMessage().contains("/tf2"))
				return;
			if (w.matches("pvp") || w.matches("pvp_nether")){
					e.getPlayer().sendMessage(ChatColor.DARK_AQUA+"Doing this in pvp world will wipe your inventory!");
					e.setCancelled(true);
			}
		}

		 void pvpChest(PlayerInteractEvent e) {
			Block block = e.getClickedBlock();
			if (block == null || block.getTypeId() != 54)
				return;
			if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
				return;
			if (!block.getWorld().getName().matches("pvp"))
				return;
			else {
				e.setCancelled(false);
				e.getPlayer().sendMessage(ChatColor.ITALIC + "You successfully made the chest open!");
			}
			
		}
}