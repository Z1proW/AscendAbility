package me.ziprow.ascend_ability.helpers;

import me.ziprow.ascend_ability.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public final class Utils
{

	public static final String LINE_SEPARATOR = StringUtils.repeat('-', 12);
	private static final Set<Material> TRANSPARENT = EnumSet.of(Material.AIR, Material.CARPET);
	private static final ChatColor INFORM_COLOR = ChatColor.YELLOW;
	private static final ChatColor WARN_COLOR = ChatColor.RED;

	public static String color(String s)
	{
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static void broadcastMessages(String... msgs)
	{
		Bukkit.broadcastMessage("");
		for(String msg : msgs) Bukkit.broadcastMessage(color(msg));
	}

	public static Location stringToLocation(String string)
	{
		String[] parts = string.split(",");
		return new Location(
				Bukkit.getWorld(parts[0].substring(15)),
				Double.parseDouble(parts[0].substring(2)),
				Double.parseDouble(parts[1].substring(2)),
				Double.parseDouble(parts[2].substring(2)),
				Float.parseFloat(parts[3].substring(6)),
				Float.parseFloat(parts[4].substring(4)
						.replace('}', ' '))
		);
	}

	public static void nameItem(ItemStack item, String name)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public static void loreItem(ItemStack item, String... lore)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
	}

	public static void setUnbreakable(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
	}

	public static void setGlowing(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.LUCK, 0, false);
		item.setItemMeta(meta);
	}

	public static void setDurability(ItemStack item, double durabilityPercent)
	{
		item.setDurability((short)(durabilityPercent * item.getType().getMaxDurability()));
	}

	public static String convertToInvisibleString(String s)
	{
		StringBuilder hidden = new StringBuilder();
		for(char c : s.toCharArray())
			hidden.append(ChatColor.COLOR_CHAR + "").append(c);
		return hidden.toString();
	}

	public static String convertToVisibleString(String s)
	{
		return s.replaceAll(ChatColor.COLOR_CHAR + "", "");
	}

	public static boolean isSimilar(ItemStack i, ItemStack i2)
	{
		if(i == i2) return true;

		ItemStack ii = i.clone();

		switch(ii.getType())
		{
			case WOOD:
			case WOOL:
			case CARPET:
			case INK_SACK:
			case DOUBLE_STONE_SLAB2:
			case STONE_SLAB2:
				break;

			default:
				ii.setDurability(i2.getDurability());
		}
		return ii == i2;
	}

	public static ItemStack createPotionItem(PotionEffect... effects)
	{
		ItemStack item = new ItemStack(Material.POTION, 1);
		PotionMeta meta = (PotionMeta)item.getItemMeta();
		for(PotionEffect effect : effects)
			meta.addCustomEffect(effect, true);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createEnchBook(Map<Enchantment, Integer> enchants)
	{
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
		for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet())
			meta.addStoredEnchant(entry.getKey(), entry.getValue(), false);
		book.setItemMeta(meta);
		return book;
	}

	public static ItemStack createEnchBook(Object... enchants)
	{
		return createEnchBook(map(enchants));
	}

	public static <K, V> Map<K, V> map(Object... inputs)
	{
		if((inputs.length & 1) != 0) // + null check
			throw new InternalError("length is odd");

		Map<K, V> map = new HashMap<>();

		for(int i = 0; i < inputs.length; i += 2)
		{
			@SuppressWarnings("unchecked")
			K k = Objects.requireNonNull((K)inputs[i]);
			@SuppressWarnings("unchecked")
			V v = Objects.requireNonNull((V)inputs[i+1]);
			map.put(k, v);
		}

		return map;
	}

	/* Player */

	public static void sendMessage(Player p, String msg)
	{
		p.sendMessage(Utils.color(msg));
	}

	public static void sendMessage(Player p, String... msgs)
	{
		for(String msg : msgs)
			sendMessage(p, msg);
	}

	public static void inform(Player p, String... msgs)
	{
		sendMessage(p, "", Main.PREFIX);
		for(String msg : msgs)
			sendMessage(p, INFORM_COLOR + msg);
	}

	public static void warn(Player p, String... msgs)
	{
		playSound(p, ActionSound.ERROR);
		sendMessage(p, "", Main.PREFIX);
		for(String msg : msgs)
			sendMessage(p, WARN_COLOR + "\u26A0 " + msg.toUpperCase() + " !");
	}

	public static Block getBlockLookingAt(Player p)
	{
		return getBlockLookingAt(p, Integer.MAX_VALUE);
	}

	public static Block getBlockLookingAt(Player p, int maxDistance)
	{
		return p.getTargetBlock(TRANSPARENT, maxDistance);
	}

	public static void playSound(Player p, ActionSound actionSound)
	{
		playSound(p, actionSound.getSound());
	}

	public static void playSound(Player p, Sound sound)
	{
		p.playSound(p.getLocation(), sound, 1f, 1f);
	}

	public static void clear(Player p)
	{
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);
		p.setFoodLevel(20);
		p.setTotalExperience(0);
		for(PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}

	public static void heal(Player p)
	{
		p.setHealth(p.getMaxHealth());
	}

	public static void sendActionBar(Player p, String message)
	{
		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoc);
	}

	public static void showScoreBoard(Player p, String title, String... lines)
	{
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("scoreboard", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(Utils.color(title));

		int i = lines.length - 1;
		for(String line : lines)
		{
			Score score = obj.getScore(Utils.color(line));
			score.setScore(i);
			i--;
		}

		p.setScoreboard(board);
	}

	public static void loadBook(Player p, String author, String title, String... pages)
	{
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();

		meta.setAuthor(author);
		meta.setTitle(title);
		for(int i = 0; i < pages.length; i++)
			pages[i] = Utils.color(pages[i]);
		meta.setPages(pages);
		book.setItemMeta(meta);
		((CraftPlayer)p).getHandle().openBook(CraftItemStack.asNMSCopy(book));
	}

	public static void setMetaData(Player p, String key, boolean value)
	{
		p.setMetadata(key, new FixedMetadataValue(Main.getInstance(), value));
	}

	public static boolean getMetaData(Player p, String key)
	{
		return p.hasMetadata(key) && p.getMetadata(key).get(0).asBoolean();
	}

}
