package me.thomasvt.minecarts;

import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Minecarts extends JavaPlugin implements Listener {
	
	public boolean attack = false;
	
	Economy econ = null;
	String moneyname = "MineCredits";
	
	ArrayList<Player> cooldownguess = new ArrayList<Player>();

	Shedule shedule = new Shedule(this);
	PublicVoid publicvoid = new PublicVoid(this);
	Listeners minecartslisteners = new Listeners(this);
	PlayerListener playerlistener = new PlayerListener(this);
	JoinQuitListeners joinquitlisteners = new JoinQuitListeners(this);
	TrainTicket trainticket = new TrainTicket(this);
	DoubleJump doublejump = new DoubleJump(this);
	CostumItems costumitems = new CostumItems(this);
	EventVoid eventvoid = new EventVoid(this);
	JoinCarts joincarts = new JoinCarts(this);
	Listeners listeners = new Listeners(this);
	MineScare minescare = new MineScare(this);
	LogClear logclear = new LogClear(this);
	NewEmail newemail = new NewEmail(this);
	
	public int getInt(String conf){
		int response = publicvoid.getInt(conf);
		return response;
	}

	 void enableListeners() {
		getServer().getPluginManager().registerEvents(minecartslisteners, this);
		getServer().getPluginManager().registerEvents(playerlistener, this);
		getServer().getPluginManager().registerEvents(joinquitlisteners, this);
		getServer().getPluginManager().registerEvents(doublejump, this);
		getServer().getPluginManager().registerEvents(joincarts, this);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onEnable() {
		enableListeners();
		publicvoid.configCheck();
		shedule.scheduler();
		setupEconomy();
		costumitems.addRecipes();
		logclear.startShedule();
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return econ != null;
    }

	public void onDisable() {
		publicvoid.disabler();
		getLogger().info("Disabling Minecarts.");
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("purgeessentials")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED + "/purgeessentials " + ChatColor.GOLD + "days");
				return true;
			}
			int days = Integer.parseInt(args[0]);
			if (days < 14){
				sender.sendMessage(ChatColor.RED + "No, dont use too less days!");
				return true;
			}
			publicvoid.removeInactiveEssentials(days);
		}
		
		else if (cmd.getName().equalsIgnoreCase("skull")) {
			Player player = (Player) sender;
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED + "/skull " + ChatColor.GOLD + "name");
				return true;
			}
			ItemStack skull = publicvoid.setSkin(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3), args[0]);
			player.getInventory().addItem(skull);
		}

		else if (cmd.getName().equalsIgnoreCase("scare")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED+ "/scare " + ChatColor.GOLD + "a-k");
				return true;
			}
			minescare.scare(sender,args[0]);
		}
		
		else if (cmd.getName().equalsIgnoreCase("attack")) {
			if (args.length > 0) {
				if (attack = true) {
					sender.sendMessage(ChatColor.RED + "Current attack mode: "+ attack);
					return false;
				}
				if (attack = false) {
					sender.sendMessage(ChatColor.GREEN+ "Current attack mode: " + attack);
					return false;
				}
			}
			publicvoid.switchAttack();
			if (attack == true) {
				Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "**Server is on Lockdown**");
				Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "*It wil no longer accept new players*");
				sender.sendMessage(ChatColor.GREEN + "The attack mode is: " + attack);
				return false;
			}
			if (attack == false) {
				sender.sendMessage(ChatColor.GREEN + "The attack mode is: " + attack);
				return false;
			}
		}
		
		else if (cmd.getName().equalsIgnoreCase("setmail")) {
			if (args.length == 0 || args.length > 1) {
				sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED+ "/setmail " + ChatColor.GOLD + "youremail@address.com");
				return true;
			}
			String email = args[0].toLowerCase();
			if (!email.contains("@")){
				sender.sendMessage("This isn't a email address");
				sender.sendMessage("Please note this is used for the forgot password feature");
				return true;
			}
			sender.sendMessage("Email address changed to: " + email);
			newemail.set(sender.getName(), email);
		}
		
		else if (cmd.getName().equalsIgnoreCase("tpt")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 0 || args.length > 1) {
					sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED + "/tpt " + ChatColor.GOLD + "targetname");
				} else if (player.hasPermission("minecarts.tpt")) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String playername = player.getDisplayName();
					if (targetPlayer == null) {
						sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
						return false;
					}
					String targetName = targetPlayer.getDisplayName();
					World tworld = targetPlayer.getWorld();
					String worldname = tworld.getName();
					getLogger().info(playername + " teleported to: " + targetName + "in world: " + worldname);
					Location targetPlayerLocation = targetPlayer.getLocation();
					player.teleport(targetPlayerLocation);
					sender.sendMessage(ChatColor.GOLD + "You teleported to " + targetPlayer.getDisplayName());
					if (!player.hasPermission("minecarts.tpt.silent"))
						targetPlayer.sendMessage(ChatColor.GOLD+ player.getDisplayName()+ " teleported to you");
				}
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("tph")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 0 || args.length > 1) {
					sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED + "/tph " + ChatColor.GOLD + "targetname");
				} else if (player.hasPermission("minecarts.tph")) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String playername = player.getDisplayName();
					if (targetPlayer == null) {
						sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
						return false;
					}
					String targetName = targetPlayer.getDisplayName();
					World tworld = targetPlayer.getWorld();
					String worldname = tworld.getName();
					getLogger().info(playername + " teleported to: " + targetName + "in world: " + worldname);
					Location playerLocation = player.getLocation();
					targetPlayer.teleport(playerLocation);
					sender.sendMessage(ChatColor.GOLD + "You teleported " + targetPlayer.getDisplayName() + " to you.");
					if (!player.hasPermission("minecarts.tph.silent"))
						targetPlayer.sendMessage(ChatColor.GOLD + player.getDisplayName() + " teleported you to him.");
				}
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("serverstat")) {
			if (sender instanceof Player) {
				sender.sendMessage(ChatColor.GREEN + "Server Name: " + Bukkit.getServerName());
				sender.sendMessage(ChatColor.GREEN + "Viewdistance: " + Bukkit.getViewDistance());
				sender.sendMessage(ChatColor.GREEN + "Motd: " + Bukkit.getMotd());
				sender.sendMessage(ChatColor.GREEN + "GameMode: " + Bukkit.getDefaultGameMode().name());
				sender.sendMessage(ChatColor.GREEN + "Max Animals: " + Bukkit.getAnimalSpawnLimit());
				sender.sendMessage(ChatColor.GREEN + "Bukkit Version: " + Bukkit.getBukkitVersion());
				sender.sendMessage(ChatColor.GREEN + "Max Players: " + Bukkit.getMaxPlayers());
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("chunk")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				double cost = publicvoid.getInt("chunk");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED + "You cannot afford the chunk sending! (" + cost + " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				sender.sendMessage(ChatColor.GREEN + "Chunk ReSended to you for " + cost + " " + moneyname + ".");
				World world = player.getWorld();
				Chunk chunk = world.getChunkAt(player.getLocation());
				world.refreshChunk(chunk.getX(), chunk.getZ());
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("fastfood")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				double cost = publicvoid.getInt("fastfood");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+"You can't afford the fastfood!("+cost+" "+ moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				sender.sendMessage(ChatColor.GREEN+ "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
				sender.sendMessage(ChatColor.GOLD + "You paid " + cost + " "+ moneyname + " for your Hamburger");
				sender.sendMessage(ChatColor.GOLD + "Here's your Hamburger, bon appetit!");
				sender.sendMessage(ChatColor.GREEN+ "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
				player.setFoodLevel(20);
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("site")) {
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
			sender.sendMessage(ChatColor.GREEN + "-Site: " + publicvoid.getString("site"));
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
		}

		else if (cmd.getName().equalsIgnoreCase("minecartsreload")) {
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
			sender.sendMessage(ChatColor.YELLOW + "Starting reload on MineCarts plugin Config.yml");
			reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "Finished reload on MineCarts plugin Config.yml");
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
		}
		
		else if (cmd.getName().equalsIgnoreCase("guess")) {
			if (sender instanceof Player) {
				// WILL DO WHEN PLAYER = PLAYER
				final Player p = (Player) sender;
				
				if (cooldownguess.contains(p)){
					sender.sendMessage(ChatColor.RED+"You need to wait to guess again!");
					return true;
				}
				
				if (args.length == 0 || args.length > 1) {
					sender.sendMessage(ChatColor.GOLD + "It's " + ChatColor.RED + "/guess " + ChatColor.GOLD + "0-9");
					return true;
				}
				Player player = (Player) sender;
				double cost = publicvoid.getInt("guess");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED + "You cannot afford to guess the number! (" + cost + " " + moneyname + ")");
					return false;
				}
				int guess;
			    try {
			        guess = Integer.parseInt(args[0]);
			    }
			    catch( Exception e ) {
			    	sender.sendMessage(ChatColor.RED+"This isn't a number");
			        return false;
			    }

			    if (guess > 9 || guess < 0 || guess == 0){
					sender.sendMessage(ChatColor.RED +"The number needs to be 1-9");
					return true;
				}
			    
			    cooldownguess.add(p);
				    Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
					      public void run() {
					    	  cooldownguess.remove(p);
					      }
					    }, 1200);
			    
				if (publicvoid.getGuessnumber() == guess){
					int win = (int)cost * 4;
					publicvoid.depositMoney(player.getName(), win);
					sender.sendMessage(ChatColor.GREEN+"Yay, that is the right number, you won "+win+ " " + moneyname);
					Bukkit.broadcastMessage(ChatColor.GREEN + "Yay, "
							+ sender.getName()
							+ " guessed the right number and won " + win + " "
							+ moneyname);
					publicvoid.randomNumber();
				}
				else {
					publicvoid.aSyncWithdraw(player.getName(), cost);
					sender.sendMessage(ChatColor.RED + "Awh, that isn't the right number, you lost "+cost+ " " + moneyname );
				}
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("pay4xp")) {
			if (sender instanceof Player) {
				// WILL DO WHEN PLAYER = PLAYER
				Player player = (Player) sender;
				double cost = publicvoid.getInt("pay4xp");
				int add = publicvoid.getInt("addxp");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED + "You cannot afford that XP! (" + cost + " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				int before = player.getLevel();
				int total = (int) before + add;
				player.setLevel(total);
				sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
				sender.sendMessage(ChatColor.GOLD + "-You paid " + cost + ". " + moneyname + ". for adding " + add + " XP levels.");
				sender.sendMessage(ChatColor.GOLD + "From " + ChatColor.RED + before + ChatColor.GOLD + " to " + ChatColor.AQUA + total);
				sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}
		
		else if (cmd.getName().equalsIgnoreCase("drugs")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				double cost = publicvoid.getInt("drugs");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+ "You cannot afford the Drugs! (" + cost + " "+ moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1800, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,150, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 1));
				sender.sendMessage("[" + ChatColor.RED + "Drugs"+ ChatColor.GREEN + "Dealer" + ChatColor.WHITE
						+ "] You boughd some drugs from the DrugsDealer for " + cost + " " + moneyname + ".");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("vision")) {
			Player player = (Player) sender;
			player.addPotionEffect(new PotionEffect(
			PotionEffectType.NIGHT_VISION, 12000, 1));
			sender.sendMessage(ChatColor.YELLOW + "You like carrots isnt it?");
		}

		else if (cmd.getName().equalsIgnoreCase("world")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				World world = player.getWorld();
				String worldname = world.getName();
				sender.sendMessage(ChatColor.BOLD+ "Your current world is: " + ChatColor.AQUA + worldname);
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("waterbreath")) {
			if (sender instanceof Player) {
				double cost = publicvoid.getInt("waterbreath");
				Player player = (Player) sender;
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+ "You cannot afford underwater breathing! ("+ cost + " " + moneyname + ")");
					return false;
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 12000, 1));
				publicvoid.aSyncWithdraw(player.getName(), cost);
				sender.sendMessage(ChatColor.YELLOW + "You paid " + cost + " "+ moneyname + " for the Water Breathing potion");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("hop")) {
			if (sender instanceof Player) {
				double cost = publicvoid.getInt("hop");
				Player player = (Player) sender;
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+ "You cannot afford to hop like a bunny! (" + cost + " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				sender.sendMessage(ChatColor.YELLOW + "You paid " + cost + " "+ moneyname + " for the Hopness");
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,12000, 1));
				sender.sendMessage(ChatColor.YELLOW + "Oh hey! you can hop like a bunny now!");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("fast")) {
			if (sender instanceof Player) {
				double cost = publicvoid.getInt("fast");
				Player player = (Player) sender;
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+ "You cannot afford to be faster! (" + cost + " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				sender.sendMessage(ChatColor.YELLOW + "You paid " + cost + " " + moneyname + " for the Fastness");
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12000, 1));
				sender.sendMessage(ChatColor.YELLOW + "Oh hey! you are faster now!");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("vote")) {
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
			sender.sendMessage(ChatColor.GREEN + "-Site: " + publicvoid.getString("vote"));
			sender.sendMessage(ChatColor.YELLOW + "-----------------------------------------");
		}

		else if (cmd.getName().equalsIgnoreCase("compass")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Try /compass target");
					return false;
				}
				Player player = (Player) sender;
				String playername = player.getName();
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				if (player == targetPlayer) {
					sender.sendMessage(ChatColor.RED + "You cant point the compass to you!");
					return false;
				}
				if (targetPlayer == null) {
					sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
					return false;
				}
				double cost = publicvoid.getInt("compass");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED + "You cannot afford the Compass pointing! (" + cost + " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				String targetName = targetPlayer.getName();
				sender.sendMessage(ChatColor.GREEN + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
				sender.sendMessage(ChatColor.GOLD + "Your compass is now pointed to: " + targetName);
				sender.sendMessage(ChatColor.GOLD + "Total cost: " + cost + " " + moneyname);
				sender.sendMessage(ChatColor.GREEN + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
				World world = player.getWorld();
				World targetWorld = targetPlayer.getWorld();
				if (world == targetWorld) {
					Location loc = targetPlayer.getLocation();
					player.setCompassTarget(loc);
					return true;
				} else {
					sender.sendMessage(ChatColor.GREEN + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
					sender.sendMessage(ChatColor.RED + "You are not in the same world.");
					sender.sendMessage(ChatColor.RED + "You got the " + cost + " " + moneyname + " back.");
					sender.sendMessage(ChatColor.GREEN + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
					publicvoid.depositMoney(playername, cost);
				}
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("c")) {
			Player player = (Player) sender;
			player.setGameMode(GameMode.CREATIVE);
			sender.sendMessage(ChatColor.GREEN + "You are now on: CREATIVE!");
			getLogger().info(player.getName() + " has changed to CREATIVE in world:"+ player.getWorld().getName());
		}

		else if (cmd.getName().equalsIgnoreCase("s")) {
			Player player = (Player) sender;
			player.setGameMode(GameMode.SURVIVAL);
			sender.sendMessage(ChatColor.GREEN + "You are now on: SURVIVAL!");
			getLogger().info(player.getName() + " has changed to SURVIVAL in world:"+ player.getWorld().getName());
		}

		else if (cmd.getName().equalsIgnoreCase("a")) {

			Player player = (Player) sender;
			player.setGameMode(GameMode.ADVENTURE);
			sender.sendMessage(ChatColor.GREEN + "You are now on: ADVENTURE!");
			getLogger().info(player.getName() + " has changed to ADVENTURE in world:"+ player.getWorld().getName());
		}

		else if (cmd.getName().equalsIgnoreCase("pay4day")) {
			if (sender instanceof Player) {
				// WILL DO WHEN PLAYER = PLAYER
				Player player = (Player) sender;
				float explosionPower = 0F; // TNT explosions are 4F by default
				double cost = publicvoid.getInt("pay4day");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+ "You cannot afford it to make it day! (" + cost + " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				player.getWorld().createExplosion(player.getLocation(),
						explosionPower);
				player.getPlayer().getLocation().getWorld().setTime(0L);
				getLogger().info(player.getPlayerListName() + " Did Pay4Day");
				sender.sendMessage(ChatColor.GOLD+"[Pay4Day]"+ChatColor.GREEN+" You paid " + cost + " "+ moneyname + " for the Day!");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
			}
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("pay4sun")) {
			if (sender instanceof Player) {
				// WILL DO WHEN PLAYER = PLAYER
				Player player = (Player) sender;
				float explosionPower = 0F; // TNT explosions are 4F by default
				double cost = publicvoid.getInt("pay4sun");
				if (publicvoid.hasEnough(sender.getName(), cost)) {
					sender.sendMessage(ChatColor.RED+ "You cannot afford it to make it sun! (" + cost+ " " + moneyname + ")");
					return false;
				}
				publicvoid.aSyncWithdraw(player.getName(), cost);
				player.getWorld().createExplosion(player.getLocation(),explosionPower);
				player.getPlayer().getLocation().getWorld().setStorm(false);
				player.getPlayer().getLocation().getWorld().setThundering(false);
				getLogger().info(player.getPlayerListName() + " Did Pay4Sun");
				sender.sendMessage(ChatColor.GOLD + "[Pay4Sun]"+ ChatColor.GREEN + " You paid "+cost+" "+moneyname+" for the Sun!");
			} else {
				// IF ITS NOT AN PLAYER
				sender.sendMessage("[MineCarts] This command can only run from player");
				return false;
			}
		}
		return true;
	}
}