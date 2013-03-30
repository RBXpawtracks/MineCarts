package me.thomasvt.minecarts;
 
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
 
public class DoubleJump implements Listener {
 
	Minecarts minecarts;

	DoubleJump(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
 
    ArrayList<String> jumpcooldown = new ArrayList<String>();
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent e){
    	doubleJumpWorld(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerJoinEvent(PlayerJoinEvent e){
    	Player p = e.getPlayer();
    	if (p.isInsideVehicle())
    		p.eject();
    	doubleJumpWorld(p);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerToggleFlightEvent(PlayerToggleFlightEvent e){
    	setFlyOnJump(e);
    }
    
    /*
    private void old_setFlyOnJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        String pl = player.getName();
        Vector jump = player.getLocation().getDirection().multiply(0.4).setY(1.2);
        String w = player.getWorld().getName();
        
        if(event.isFlying() && player.getGameMode() != GameMode.CREATIVE) {
        	if (w.matches("world") || w.matches("lobby")){
        	event.setCancelled(true);
            if(!jumpcooldown.contains(pl)) {
                player.setVelocity(player.getVelocity().add(jump));
                startDoubleJump(player);
				}
			}
		}
	}
    */
    private void setFlyOnJump(PlayerToggleFlightEvent event) {
    	Player p = event.getPlayer();
    	if (p.getGameMode().getValue() == 1)
    		return;
        String w = p.getWorld().getName();
        if (w.matches("lobby") || w.matches("world")){
        	lobbyOrWorld(event);
        	event.setCancelled(true);
        	return;
        }
        if (w.matches("tf2")){
        	tf2(event);
        	event.setCancelled(true);
        	return;
        }
	}
    
    private void tf2(PlayerToggleFlightEvent event){
        Player p = event.getPlayer();
        String pl = p.getName();
        Vector jump = p.getLocation().getDirection().multiply(0.4).setY(1.2);
        event.setCancelled(true);
    	if (!p.hasPotionEffect(PotionEffectType.SPEED))
    		return;
    	if (p.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE){
    		if(!jumpcooldown.contains(pl)) {
            	p.setVelocity(p.getVelocity().add(jump));
            	startDoubleJump(p);
    		}
    	}
    }
    
    private void lobbyOrWorld(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();
        String pl = player.getName();
        Vector jump = player.getLocation().getDirection().multiply(0.4).setY(1.2);
        event.setCancelled(true);
        if(!jumpcooldown.contains(pl)) {
        	player.setVelocity(player.getVelocity().add(jump));
        	startDoubleJump(player);
		}
    }
    
	private void startDoubleJump(final Player p){
		minecarts.getServer().getScheduler().scheduleSyncDelayedTask(minecarts, new Runnable() {
					public void run() {
						if (p == null)
							return;
						String pl = p.getName();
						Location loc = p.getLocation();
						p.setFallDistance(-10);
						p.setExp(0);
						p.setFlying(false);
						p.setAllowFlight(false);
						p.playSound(loc, Sound.GHAST_FIREBALL, 1F, 1F);
						jumpcooldown.add(pl);
						warmUp(pl);
					}
				}, 0);
	}

	private void doubleJumpWorld(final Player p){
		minecarts.getServer().getScheduler().scheduleSyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				if (p == null)
					return;
				if (p.getGameMode().getValue() != 0)
					return;
				String w = p.getWorld().getName();
				if (w.matches("world") || w.matches("lobby")){
					p.setAllowFlight(true);
					p.sendMessage(ChatColor.DARK_AQUA+"Enjoy Double-Jumping in " + w + "!");
				} else {
					if (w.matches("pvp") || w.matches("pvp_nether"))
						p.setAllowFlight(false);
				}
			}
		}, 40);
	}
	
	private void warmUp(final String p){
		minecarts.getServer().getScheduler().scheduleSyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				jumpcooldown.remove(p);
				Player pl = Bukkit.getPlayerExact(p);
				if (pl == null)
					return;
				String w = pl.getWorld().getName();
				if (w.matches("world") || w.matches("lobby")){
					pl.setAllowFlight(true);
					pl.setExp(1);
					pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1F, 1F);
				} else {
					if (w.matches("pvp") || w.matches("pvp_nether"))
						pl.setAllowFlight(false);
				}
			}
		}, 50);
    }
}