package me.thomasvt.minecarts;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

 class CostumItems {

	Minecarts minecarts;

	CostumItems(Minecarts minecarts) {
		this.minecarts = minecarts;
	}

	void addRecipes() {
		addValentineRose();
	}
	 
	 private void addValentineRose(){
		ShapelessRecipe roseRecipe = new ShapelessRecipe(valentineRose(1));
		roseRecipe.addIngredient(1, Material.RED_ROSE);
		roseRecipe.addIngredient(1, Material.GOLD_INGOT);
		Bukkit.getServer().addRecipe(roseRecipe);
	 }

	 private ItemStack valentineRose(int amount) {
		ItemStack valentineRose = new ItemStack(Material.RED_ROSE, amount);
		ItemMeta roseMeta = valentineRose.getItemMeta();
		roseMeta.setDisplayName(ChatColor.RED + "Valentine Rose");
		List<String> roseLore = Arrays.asList(new String[] { ChatColor.GRAY + "Isn't it romantic...?"});
		roseMeta.setLore(roseLore);
		valentineRose.setItemMeta(roseMeta);
		return valentineRose;
	}
}
