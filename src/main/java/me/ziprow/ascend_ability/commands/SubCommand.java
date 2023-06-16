package me.ziprow.ascend_ability.commands;

import org.bukkit.entity.Player;

interface SubCommand
{

	void onCommand(Player p, String[] args);

	String getPermission();

}
