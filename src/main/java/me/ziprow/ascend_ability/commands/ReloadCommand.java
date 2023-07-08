package me.ziprow.ascend_ability.commands;

import me.ziprow.ascend_ability.Main;
import me.ziprow.ascend_ability.helpers.Utils;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		Main.reload();
		Utils.sendMessage(p, Main.PREFIX + "Settings Reloaded");
	}

	@Override
	public String getPermission()
	{
		return "ascend.admin";
	}

}
