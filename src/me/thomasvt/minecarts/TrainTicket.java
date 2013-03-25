package me.thomasvt.minecarts;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TrainTicket {
	
	Minecarts minecarts;

	TrainTicket(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	private ArrayList<Player> ticketholders = new ArrayList<Player>();
	
	void interactHandler(PlayerInteractEvent event){
		useTicket(event);
		buyTicket(event);
	}
	
	private void useTicket(PlayerInteractEvent e){
		 Player p = e.getPlayer();
		 if (p.getItemInHand().getTypeId() != 339)
			 return;
		 int groundBlock = p.getLocation().getBlock().getTypeId();
		 if (groundBlock != 66)
			 return;
		 if (!ticketholders.contains(p)){
			 p.sendMessage(ChatColor.RED+"You dont have a ticket to spawn a train!");
			 return;
		 }
		 if (!p.hasPermission("minecarts.train.use")){
			 p.sendMessage(ChatColor.RED+"You dont have permissions to spawn a train!");
			 return;
		 }
		 if (ticketholders.contains(p) && p.hasPermission("minecarts.train.use")){
			 ticketholders.remove(p);
			 Minecart minecart = p.getWorld().spawn(p.getLocation(), Minecart.class);
			 p.setItemInHand(null);
			 p.sendMessage(ChatColor.LIGHT_PURPLE+"Your train is departing in 10 seconds. hurry, get in!");
			 startTrain(minecart, p.getLocation().getDirection(), p);
			 minecart.setMaxSpeed(0);
			 minecart.setSlowWhenEmpty(false);
		 }
	 }
	
	private void startTrain(final Minecart m, final Vector v, final Player p){
		minecarts.getServer().getScheduler().scheduleSyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				m.setMaxSpeed(0.4);
				m.setVelocity(v);
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F);
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
				p.sendMessage(ChatColor.LIGHT_PURPLE+"Train is departing!");
				continueTrain(m);
			}
		}, 210);
	}
	
	@SuppressWarnings("deprecation")
	private void continueTrain(final Minecart m){
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				if (m == null)
					return;
				if (m.isEmpty()){
					m.remove();
					return;
				}
				m.setVelocity(m.getVelocity().multiply(2));
				continueTrain(m);
			}
		}, 100);
	}

	 public void onSignChange(SignChangeEvent event){
	    Player player = event.getPlayer();
	    if (!event.getLine(0).equalsIgnoreCase("[ticket]"))
	      return;
	    if (!player.hasPermission("minecarts.train.make")) {
	      event.setLine(0, ChatColor.RED+"Hello :3");
	      return;
	    }
	    if (event.getLine(1).isEmpty()) {
	      player.sendMessage(ChatColor.DARK_RED + "Please fill in the missing value! (cost)");
	      return;
	    }
	    event.setLine(0, ChatColor.BOLD + event.getLine(0));
	  }
	
	 private void buyTicket(PlayerInteractEvent event){
	    Action action = event.getAction();
	    if (!action.equals(Action.RIGHT_CLICK_BLOCK))
	      return;
	    Player player = event.getPlayer();
	    if (!player.hasPermission("minecarts.train.buy"))
	      return;
	    Block block = event.getClickedBlock();
	    if ((!block.getType().equals(Material.SIGN_POST)) && (!block.getType().equals(Material.WALL_SIGN)))
	      return;
	    Sign sign = (Sign)block.getState();
	    if (!sign.getLine(0).equalsIgnoreCase(ChatColor.BOLD + "[ticket]"))
	      return;
	    if (sign.getLine(1).isEmpty()) {
	      player.sendMessage(ChatColor.RED + "This ticket shop is out of order!");
	      return;
	    }
	    buyTicket(Double.valueOf(Double.parseDouble(sign.getLine(1))), player);
	  }
	
	 @SuppressWarnings("deprecation")
	private void buyTicket(double cost, Player p){
		String pl = p.getName();
		if (minecarts.publicvoid.hasEnough(pl, cost)){
			p.sendMessage(ChatColor.RED + "You can't afford to buy a ticket here!");
			return;
		}
		p.sendMessage(ChatColor.AQUA+"You boughd a ticket for "+ cost +" " +minecarts.moneyname);
		p.sendMessage(ChatColor.GREEN+"Thank you and goodbye!");
		p.sendMessage(ChatColor.GRAY+"To use your ticket stand on the rail and rightclick");
		minecarts.econ.withdrawPlayer(pl, cost);
		p.getInventory().addItem(new ItemStack(Material.PAPER, 1));
		p.updateInventory();
		ticketholders.add(p);
	}
}