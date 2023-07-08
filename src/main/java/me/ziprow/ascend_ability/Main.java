package me.ziprow.ascend_ability;

import me.ziprow.ascend_ability.commands.MainCommand;
import me.ziprow.ascend_ability.commands.TabComplete;
import me.ziprow.ascend_ability.helpers.Utils;
import me.ziprow.ascend_ability.listeners.MainListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin
{

	public static final String MAIN_COLOR = "&c";
	public static final String PREFIX = Utils.color("&8[" + MAIN_COLOR + "Ascend&8] &r");

	private static Main main;
	private static final Map<String, String> PHRASES = new HashMap<>();

	private static final Listener[] LISTENERS = new Listener[] {
		new MainListener()
	};

	@Override
	public void onEnable()
	{
		main = this;

		loadConfig();
		loadLang();

		registerCommands();
		registerEvents();
	}

	public void loadConfig()
	{
		saveDefaultConfig();
	}

	public void loadLang()
	{
		saveResource("english.yml", true);

		String lang = getConfig().getString("language") + ".yml";
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), lang));

		for(String phrase : langConfig.getKeys(false))
			PHRASES.put(phrase, langConfig.getString(phrase));
	}

	private void registerCommands()
	{
		getCommand("ascend").setExecutor(new MainCommand());
		getCommand("ascend").setTabCompleter(new TabComplete());
	}

	private void registerEvents()
	{
		for(Listener listener : LISTENERS)
			Bukkit.getPluginManager().registerEvents(listener, this);
	}

	public static void reload()
	{
		main.reloadConfig();
		main.loadConfig();
		main.loadLang();

		Bukkit.getLogger().info(ChatColor.stripColor(PREFIX) + "Settings Reloaded");
	}

	public static Main getInstance()
	{
		return main;
	}

	public static String getPhrase(String key)
	{
		return PHRASES.get(key);
	}

}
