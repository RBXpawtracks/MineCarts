package me.thomasvt.minecarts;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


 class MineScare {
	Minecarts minecarts;

	MineScare(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	public void scare(CommandSender sender, String arg) {
		minecarts.getLogger().info(sender.getName() + " did scare:" + arg);
		if (arg.startsWith("a")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playEffect(loc, Effect.GHAST_SHRIEK, 0);
			}
			return;
		}
		else if (arg.startsWith("b")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playEffect(loc, Effect.DOOR_TOGGLE, 0);
			}
			return;
		}
		else if (arg.startsWith("c")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playEffect(loc, Effect.ZOMBIE_DESTROY_DOOR, 0);
			}
			return;
		}
		else if (arg.startsWith("d")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playEffect(loc, Effect.ZOMBIE_CHEW_IRON_DOOR, 0);
			}
			return;
		}
		else if (arg.startsWith("e")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playEffect(loc, Effect.SMOKE, 0);
				p.playEffect(loc, Effect.EXTINGUISH, 0);
			}
			return;
		}
		else if (arg.startsWith("f")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playEffect(loc, Effect.GHAST_SHOOT, 0);
			}
			return;
		}
		else if (arg.startsWith("g")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playSound(loc, Sound.CAT_HIT, 5, 1);
			}
			return;
		}
		else if (arg.startsWith("h")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playSound(loc, Sound.BURP, 5, 1);
			}
			return;
		}
		else if (arg.startsWith("i")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playSound(loc, Sound.AMBIENCE_THUNDER, 5, 1);
			}
			return;
		}
		else if (arg.startsWith("j")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playSound(loc, Sound.AMBIENCE_CAVE, 5, 1);
			}
			return;
		}
		else if (arg.startsWith("k")) {
			sender.sendMessage(ChatColor.GREEN + "You did: "
					+ ChatColor.LIGHT_PURPLE + "Scare");
			for (Player p : minecarts.getServer().getOnlinePlayers()) {
				Location loc = p.getLocation();
				p.playSound(loc, Sound.ARROW_HIT, 5, 1);
			}
			return;
		} else {
			sender.sendMessage(ChatColor.DARK_PURPLE + "I dont know :(");
		}
	}
}
