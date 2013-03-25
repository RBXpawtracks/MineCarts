package me.thomasvt.minecarts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

 class Listeners implements Listener {

	Minecarts minecarts;

	Listeners(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	  public void onWorldInit(WorldInitEvent event) {
		event.getWorld().setKeepSpawnInMemory(false);
	  }
	
	ArrayList<Location> dispensecooldown = new ArrayList<Location>();
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void BlockDispenseEvent(BlockDispenseEvent event) {
		final Location l = event.getBlock().getLocation();
		if (dispensecooldown.contains(l)){
			event.setCancelled(true);
			return;
		}
		
		dispensecooldown.add(l);
		    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			      public void run() {
			    	  dispensecooldown.remove(l);
			      }
			    }, 40);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent  event){
		event.getEntity().setRemoveWhenFarAway(true);
		event.getEntity().setCanPickupItems(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void VehicleExitEvent(VehicleExitEvent event) {
		if (!minecarts.getConfig().getBoolean("exitvehicle"))
			return;
		Vehicle vehicle = event.getVehicle();
		if (vehicle.getType() == EntityType.MINECART){
			vehicle.remove();
			minecarts.getLogger().info("A minecart has been removed in: " + vehicle.getWorld().getName());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void EntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY))
				return;
			if (!p.getWorld().getName().matches("tf2"))
				return;
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void InventoryClickEvent(InventoryClickEvent event) {
		Player p = Bukkit.getPlayer(event.getWhoClicked().getName());
		if (p == null)
			return;
		if (p.getGameMode().getValue() == 1 && !p.hasPermission("minecarts.c")) {
			event.setCancelled(true);
			minecarts.publicvoid.warnCreative(p);
		}
	}
	

	@EventHandler(priority = EventPriority.NORMAL)
	public void BlockBurnEvent(BlockBurnEvent event) {
		if (event.getBlock().getWorld().getName().matches("pvp"))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void BlockIgniteEvent(BlockIgniteEvent event) {
		if (event.getBlock().getWorld().getName().matches("pvp"))
			return;
		event.setCancelled(true);
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerListPing(ServerListPingEvent event) {
		//String version = minecarts.getServer().getBukkitVersion().substring(0, 5);
		//event.setMotd("§2§l>> §b[§f§l§3M§f§3ine§f§a§f§aCarts§f§b] §e[§6" + version + "§e] §5[§d24/7§5] §2§l<<");
		event.setMotd("§2§l>> §b[§f§l§3M§f§3ine§f§a§f§aCarts§f§b] §e[§61.5.1§e] §5[§d24/7§5] §2§l<<");
	}

}
