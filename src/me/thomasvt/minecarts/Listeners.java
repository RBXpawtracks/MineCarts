package me.thomasvt.minecarts;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.WorldInitEvent;

 class Listeners implements Listener {

	Minecarts minecarts;

	Listeners(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		if (!minecarts.getConfig().getBoolean("bananachunk"))
			return;
		minecarts.publicvoid.resendChunk(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	  public void onWorldInit(WorldInitEvent event) {
		event.getWorld().setKeepSpawnInMemory(false);
	  }

	@EventHandler(priority = EventPriority.MONITOR)
	public void BlockDispenseEvent(BlockDispenseEvent event) {
		if (!minecarts.getConfig().getBoolean("disabledispenser"))
			return;
		String worldname = event.getBlock().getWorld().getName();
		if (worldname.matches("pvp") || worldname.matches("flatplus") || worldname.matches("world"))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
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
	public void InventoryClickEvent(InventoryClickEvent event) {
		if (!minecarts.getConfig().getBoolean("gamemodecheck"))
			return;
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
		if (!minecarts.getConfig().getBoolean("blockburn"))
			return;
		if (event.getBlock().getWorld().getName().matches("pvp"))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void BlockIgniteEvent(BlockIgniteEvent event) {
		if (!minecarts.getConfig().getBoolean("blockignite"))
			return;
		event.setCancelled(true);
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerListPing(ServerListPingEvent event) {
		if (!minecarts.getConfig().getBoolean("serverlist"))
			return;
		String version = minecarts.getServer().getBukkitVersion().substring(0, 5);
		event.setMotd("§2§l>> §b[§f§l§3M§f§3ine§f§a§f§aCarts§f§b] §e[§6" + version + "§e] §5[§d24/7§5] §2§l<<");
	}

}
