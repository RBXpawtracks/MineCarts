package me.thomasvt.minecarts;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

 class PlayerListener implements Listener {

	Minecarts minecarts;

	PlayerListener(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	  public void MergedBlockPlace(BlockPlaceEvent event) {
		minecarts.eventvoid.lavaBlocker(event);
		minecarts.eventvoid.buildProtect(event);
	  }
	
	@EventHandler(priority = EventPriority.NORMAL)
	  public void MergedBlockBreak(BlockBreakEvent event) {
		minecarts.eventvoid.buildProtect(event);
	  }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void MergedInteract(PlayerInteractEvent event) {
		minecarts.eventvoid.blockBow(event);
		minecarts.eventvoid.enderDisabler(event);
		minecarts.eventvoid.potionDisabler(event);
		minecarts.eventvoid.noSnowball(event);
		minecarts.eventvoid.fireWorkSlowDown(event);

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void DisablePluginsCommand(PlayerCommandPreprocessEvent event) {
		minecarts.eventvoid.commandCooldown(event);
		minecarts.eventvoid.noPlugins(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerDropItemEvent(PlayerDropItemEvent event) {
		if (event.getPlayer().hasPermission("minecarts.drop"))
			return;
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
		if (!minecarts.getConfig().getBoolean("bukkitperms"))
			return;
		if (event.getPlayer().hasPermission("minecarts.flow"))
			return;
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent  event){
		event.getEntity().setRemoveWhenFarAway(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void MergedAsyncPlayerChat(AsyncPlayerChatEvent event) {
		minecarts.eventvoid.chatNoCapital(event);
		minecarts.eventvoid.chatNoRepeat(event);
		minecarts.eventvoid.chatCooldown(event);
		//minecarts.eventvoid.chatContainsName(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	  public void PvpSound(EntityDamageByEntityEvent e){
		if (!e.isCancelled())
			minecarts.publicvoid.pvpSound(e.getDamager(), e.getEntity());
		if (!e.isCancelled())
			minecarts.publicvoid.bowSound(e.getDamager(), e.getEntity());
		if (!e.isCancelled())
			minecarts.publicvoid.blindness(e.getEntity());
	  }
}