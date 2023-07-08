package me.ziprow.ascend_ability.commands;

import me.ziprow.ascend_ability.Main;
import me.ziprow.ascend_ability.helpers.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

class HelpCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		Utils.sendMessage(p, Main.PREFIX + " Commands",
				ChatColor.DARK_PURPLE + Utils.LINE_SEPARATOR);

		for(Map.Entry<String, SubCommand> subCommand : MainCommand.SUB_COMMANDS.entrySet())
			Utils.sendMessage(p, ChatColor.DARK_PURPLE + "- " + Main.MAIN_COLOR + "/ascend " + ChatColor.GRAY + subCommand.getKey());

		Utils.sendMessage(p, ChatColor.DARK_PURPLE + Utils.LINE_SEPARATOR);
	}

	@Override
	public String getPermission()
	{
		return "ascend.user";
	}

}
