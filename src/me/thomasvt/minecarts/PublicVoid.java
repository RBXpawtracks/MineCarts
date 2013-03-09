package me.thomasvt.minecarts;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

 class PublicVoid {
	
	Minecarts minecarts;

	PublicVoid(Minecarts minecarts) {
		this.minecarts = minecarts;
	}
	
	public void switchAttack() {
		if (minecarts.attack == false)
			minecarts.attack = true;
		else
			minecarts.attack = false;
	}
	
	void disabler() {
		//minecarts.getServer().getScheduler().cancelAllTasks();
		minecarts.getServer().getScheduler().cancelTasks(minecarts);
	}
	
	public int getGuessnumber(){
		if (guessnumber == -1)
			randomNumber();
		return guessnumber;
	}
	
	private int guessnumber = -1;
	
	 void randomNumber(){
		Random randomGenerator = new Random();
		int random = randomGenerator.nextInt(9);
		if (random > 9 || random < 0 || random == 0)
			randomNumber();
		else
			guessnumber = random;
		
	}
	
	@SuppressWarnings("deprecation")
	public void resendChunk(Player p){
		final Player pl = p;
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
		World world = pl.getWorld();
		Chunk chunk = world.getChunkAt(pl.getLocation());
		world.refreshChunk(chunk.getX(), chunk.getZ());
			}
		}, 0);
	}
	
	public int totalBuildersOnline(){
		int builders = 0;
		for (Player p : Bukkit.getOnlinePlayers()){
			if (p.hasPermission("minecarts.builder"))
				builders++;
		}
		return builders;
	}
	
	public int totalStaffOnline(){
		int staff = 0;
		for (Player p : Bukkit.getOnlinePlayers()){
			if (p.hasPermission("minecarts.staff"))
				staff++;
		}
		return staff;
	}
	
	public String totalOnline(){
		String respond = minecarts.getServer().getOnlinePlayers().length + "/" + minecarts.getServer().getMaxPlayers();
		return respond;
	}
	
	public int getInt(String s){
		return minecarts.getConfig().getInt(s);
	}
	
	public String getString(String s){
		return minecarts.getConfig().getString(s);
	}
	
	@SuppressWarnings("deprecation")
	 void welcome(Player pl){
		final Player p = pl;
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				p.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				p.sendMessage(ChatColor.GREEN + "Welcome to MineCarts!");
				p.sendMessage(ChatColor.GREEN + "We are a 24/7 NoLagg Creative & PVP server");
				p.sendMessage(ChatColor.GREEN + "Online players: " + totalOnline());
				p.sendMessage(ChatColor.GREEN + "Plugins: " + ChatColor.YELLOW + "Factions, PlotMe, PVPGuns. and much more!");
				p.sendMessage(ChatColor.GREEN + "Your balance: "+ minecarts.econ.getBalance(p.getName()));
				p.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			}
		}, 25);
	}
	
	@SuppressWarnings("deprecation")
	public void removeInvisibility(){
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers())
					p.removePotionEffect(PotionEffectType.INVISIBILITY);
					}
				}, 0);
	}
	
	@SuppressWarnings("deprecation")
	public void chatContainsName(final String m,final Set<Player> r) {
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
		ArrayList<String> rec = new ArrayList<String>();
		for (Player p : r)
			rec.add(p.getName());
		for (Player p : Bukkit.getOnlinePlayers())
			if (m.contains(p.getName()))
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.5F, 1.0F);
			}
		}, 0);
}
	
	@SuppressWarnings("deprecation")
	void pvpSound(final Entity damagerEn,final Entity victimEn) {
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				if (damagerEn instanceof Player && victimEn instanceof Player){
				      Player damager = (Player)damagerEn;
				      if (hasSword(damager) && hasArmour((Player)victimEn))
				        playPvpSound(damager);
				    }
					}
				}, 0);
	}
	
	@SuppressWarnings("deprecation")
	public void bowSound(final Entity damager,final Entity victim) {
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
				
			    if ((damager instanceof Arrow)) {
			        Arrow arrow = (Arrow)damager;

			        if (arrow.getShooter() instanceof Player && victim instanceof Player)
			        	((Player)arrow.getShooter()).playSound(damager.getLocation(), Sound.CAT_HIT, 1.0F, 1.0F);
						}
					}
				}, 0);
	}
	
	private int lastsound;
	private void playPvpSound(Player damager){
		if (lastsound > 2 || lastsound == 0)
			lastsound = 1;
		if (lastsound == 1)
			damager.playSound(damager.getLocation(), Sound.ANVIL_LAND, 1.0F, 2.0F);
		lastsound++;
	}
	
	  private boolean hasSword(Player p){
		if ((p.getItemInHand().getType().equals(Material.DIAMOND_SWORD))
				|| (p.getItemInHand().getType().equals(Material.GOLD_SWORD))
				|| (p.getItemInHand().getType().equals(Material.IRON_SWORD))
				|| (p.getItemInHand().getType().equals(Material.STONE_SWORD)))
			return true;
		else
			return false;
	  }

	  private boolean hasArmour(Player p){
		if ((p.getInventory().getChestplate() != null)
				|| (p.getInventory().getHelmet() != null)
				|| (p.getInventory().getLeggings() != null)
				|| (p.getInventory().getBoots() != null))
			return true;
		else
			return false;
	  }
	
	@SuppressWarnings("deprecation")
	void removeInactiveEssentials(final int days) {
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
		Bukkit.broadcastMessage("Starting to remove old player files..");
		long start = System.currentTimeMillis();
	    Long timeNow = Long.valueOf(System.currentTimeMillis());
	    Long dayToMs = Long.valueOf(86400000 * days);
	    String worldname = Bukkit.getServer().getWorlds().get(0).getName();
	    
	      int removeddatfiles = 0;
	      int removedymlfiles = 0;

	    File[] allyml = new File("plugins/Essentials/userdata").listFiles();

	    for (File yml : allyml) {
	      Long logout = null;
	      Long difference = null;
	      //YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(yml.getAbsolutePath()));
	      //logout = Long.valueOf(config.getLong("timestamps.logout"));
	      logout = Long.valueOf(yml.lastModified());

	      if ((logout != null) && (logout.longValue() != 0L)) {
	        difference = Long.valueOf(timeNow.longValue() - logout.longValue());
	        if (difference.longValue() >= dayToMs.longValue()) {
	          yml.delete();
	          removedymlfiles++;
					}
				}
			}
	    
	    File[] alldat = new File(worldname+"/players").listFiles();
	    
	    for (File dat : alldat) {
		      Long logout = null;
		      Long difference = null;
		      logout = Long.valueOf(dat.lastModified());

		      if ((logout != null) && (logout.longValue() != 0L)) {
		        difference = Long.valueOf(timeNow.longValue() - logout.longValue());
		        if (difference.longValue() >= dayToMs.longValue()) {
		          dat.delete();
		          removedymlfiles++;
						}
					}
				}
	    
	    long totalTime = System.currentTimeMillis() - start;
	    int totalDeleted = removeddatfiles + removedymlfiles;
	    Bukkit.broadcastMessage(ChatColor.GOLD + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
	    Bukkit.broadcastMessage(ChatColor.AQUA + "Removing old player files done, took: " + totalTime + " ms");
	    Bukkit.broadcastMessage(ChatColor.AQUA + "Total .dat files deleted: " + removeddatfiles );
	    Bukkit.broadcastMessage(ChatColor.AQUA + "Total .yml files deleted: " + removedymlfiles );
	    Bukkit.broadcastMessage(ChatColor.AQUA + "Total files deleted: " + totalDeleted );
	    Bukkit.broadcastMessage(ChatColor.GOLD + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
					}
				}, 0);
	}
	
	public void aSyncWithdraw(String p, double c){
		minecarts.econ.withdrawPlayer(p, c);
	}
	
	public void depositMoney(String p, double m){
		minecarts.econ.depositPlayer(p, m);
	}
	
	public boolean hasEnough (String p, double c){
		if (minecarts.econ.getBalance(p) < c)
			return true;
		else
			return false;
	}
	
	public void warnCreative(Player p){
		p.setGameMode(GameMode.SURVIVAL);
		p.sendMessage(ChatColor.BOLD + "Dont try to get illegal creative!");
	}
	
	public ItemStack setSkin(ItemStack item, String nick) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(nick);
        item.setItemMeta(meta);
        return item;
    }
	
	 void randomSound() {
		minecarts.getLogger().info("Did the Random sound.");
		for (Player p : minecarts.getServer().getOnlinePlayers())
			p.playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 5, 1);
	}
	
	void configCheck() {
		File file = new File(minecarts.getDataFolder() + File.separator + "config.yml");
		if (!file.exists()) {
			loadConfiguration();
			minecarts.reloadConfig();
		}
		if (minecarts.getConfig().getBoolean("updateconfig") == true) {
			loadConfiguration();
			minecarts.reloadConfig();
		}
	}
	
	void loadConfiguration() {
		minecarts.getConfig().options().copyDefaults(true);
		minecarts.saveConfig();
	}
	
	void creativeCheck() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p == null)
				return;
			if (p.getGameMode() == GameMode.CREATIVE && !p.hasPermission("minecarts.c")) {
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.BOLD + "Dont try to get illegal creative!");
							}
						}
	}

	@SuppressWarnings("deprecation")
	  void blindness(final Entity e) {
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			 public void run() {
				if (e instanceof Player)
					((Player)e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
					}
				}, 0);
	}
}
