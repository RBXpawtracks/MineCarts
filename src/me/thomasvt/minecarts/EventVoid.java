package me.thomasvt.minecarts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

 class EventVoid {

	Minecarts minecarts;

	EventVoid(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	void handlePlayerDeath(PlayerDeathEvent event){
			Player player = event.getEntity();
			
			LivingEntity zombie = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
			zombie.setRemoveWhenFarAway(true);
			minecarts.zombiedeath.handleZombie(zombie, player.getName(), player.getInventory());
			
			if ((player.getKiller() == null) || !(player.getKiller() instanceof Player))
				player.getKiller().playSound(player.getKiller().getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
	}
	
	 void blockBow(PlayerInteractEvent e){
		if (e.getMaterial() == Material.BOW && !e.getPlayer().hasPermission("minecarts.shootbow"))
			e.setCancelled(true);
	}
	
	 void enderDisabler(PlayerInteractEvent e) {
		Material m = e.getMaterial();
		if (m == Material.ENDER_PEARL || m == Material.EYE_OF_ENDER || m == Material.EXP_BOTTLE)
			e.setCancelled(true);
	}
	 ArrayList<Player> cooldownplayers = new ArrayList<Player>();

	void fireWorkSlowDown(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() != Material.FIREWORK)
			return;
		if (p.hasPermission("minecarts.fastfirework"))
			return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (this.cooldownplayers.contains(p)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED
					+ "You need to wait before launching another firework!");
			return;
		}
		this.cooldownplayers.add(p);
		removeFromList(p);
	}
	
	@SuppressWarnings("deprecation")
	public void removeFromList(final Player p){
	    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
		      public void run() {
		        cooldownplayers.remove(p);
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