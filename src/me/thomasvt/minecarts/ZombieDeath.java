package me.thomasvt.minecarts;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

class ZombieDeath {

	Minecarts minecarts;

	ZombieDeath(Minecarts minecarts) {
		this.minecarts = minecarts;
	}

	 void handleZombie(LivingEntity livingEntity, String playername,PlayerInventory playerInventory) {
		setHelmet(livingEntity, playername);
		equipZombie(livingEntity, playerInventory);
		addPotionEffects(livingEntity);
		livingEntity.setCanPickupItems(false);
	}

	private void addPotionEffects(LivingEntity livingEntity) {
		livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 3));
		livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 1));
		livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 1));
	}

	private void setHelmet(LivingEntity monster, String playername) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwner(playername);
		skull.setItemMeta(skullMeta);

		monster.getEquipment().setHelmet(skull);
	}
	
	private void equipZombie(LivingEntity monster,PlayerInventory playerInventory) {
		monster.getEquipment().setChestplate(playerInventory.getChestplate());
		monster.getEquipment().setLeggings(playerInventory.getLeggings());
		monster.getEquipment().setBoots(playerInventory.getBoots());
		monster.getEquipment().setItemInHand(playerInventory.getItemInHand());
	}
}