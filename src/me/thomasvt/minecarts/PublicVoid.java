package me.thomasvt.minecarts;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
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
	
	public void resendChunk(Player p){
		final Player pl = p;
		minecarts.getServer().getScheduler().scheduleSyncDelayedTask(minecarts, new Runnable() {
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
	
	 void removeInvisibility(){
				for (Player p : Bukkit.getOnlinePlayers())
					if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
						if (!p.hasPermission("minecarts.invisible"))
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
					}
	}
	
	@SuppressWarnings("deprecation")
	void removeInactiveEssentials(final int days) {
		minecarts.getServer().getScheduler().scheduleAsyncDelayedTask(minecarts, new Runnable() {
			public void run() {
		long start = System.currentTimeMillis();
	    Long timeNow = Long.valueOf(System.currentTimeMillis());
	    Long dayToMs = Long.valueOf(86400000 * days);
	    String worldname = Bukkit.getServer().getWorlds().get(0).getName();
	    int removed = 0;
	      
	    ArrayList<File> files = new ArrayList<File>();
	    Bukkit.broadcastMessage(ChatColor.ITALIC+"Removing inactive player files (1/3)");
	    for (File f : new File("plugins/Essentials/userdata").listFiles())
	    	files.add(f);
	    for (File f : new File(worldname+"/players").listFiles())
	    	files.add(f);
	    
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/hg").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/flatplus").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/groups/plots").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/plots").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/bplots").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/hugeplots").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/uSkyBlock/players").listFiles())
	    	files.add(f);

	    
	    /*
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/pvp").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/worlds/world").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/groups/default").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/groups/pvp").listFiles())
	    	files.add(f);
	    for (File f : new File("plugins/Multiverse-Inventories/players").listFiles())
	    	files.add(f);
	    */
	    
	    Bukkit.broadcastMessage(ChatColor.ITALIC+"Removing inactive player files (2/3)");
	    for (File f : files) {
		      Long logout = null;
		      Long difference = null;
		      logout = Long.valueOf(f.lastModified());

		      if ((logout != null) && (logout.longValue() != 0L)) {
		        difference = Long.valueOf(timeNow.longValue() - logout.longValue());
		        if (difference.longValue() >= dayToMs.longValue()) {
		          f.delete();
		          removed++;
						}
					}
				}
	    Bukkit.broadcastMessage(ChatColor.ITALIC+"Removing inactive player files (3/3)");
	    long totalTime = System.currentTimeMillis() - start;
	    Bukkit.broadcastMessage(ChatColor.GOLD + "--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--");
	    Bukkit.broadcastMessage(ChatColor.AQUA + "Removing old player files done, took: " + totalTime + " ms");
	    Bukkit.broadcastMessage(ChatColor.AQUA + "Total files deleted: " + removed );
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
			if (p.getGameMode() == GameMode.CREATIVE){
				if (p.hasPermission("minecarts.c"))
					return;
				else
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.BOLD + "Dont try to get illegal creative!");
			}
		}
	}
	
	private void endAfterSpy(Player p){
		p.sendMessage(ChatColor.ITALIC+"You are no longer invisible!");
		p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		p.setLevel(0);
	}
	
	  void spyClock(final Player p, final int timer) {
		minecarts.getServer().getScheduler().scheduleSyncDelayedTask(minecarts, new Runnable() {
			 public void run() {
				 if (p == null)
					 return;
				 if (!p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
					 endAfterSpy(p);
					 return;
				 }
				 if (timer == 0){
					 endAfterSpy(p);
					 return;
				 }
				 int t = timer - 1;
				 p.setLevel(t);
				 spyClock(p, t);
			 }
		}, 20);
	}

	void blindness(final Entity e) {
		if (e instanceof Player)
			((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
	}
}
