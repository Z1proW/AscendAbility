package me.ziprow.ascend_ability.listeners;

import me.ziprow.ascend_ability.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.TimerTask;

public class MainListener implements Listener
{

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event)
	{
		Player player = event.getPlayer();

		if(!player.isSneaking()) return;

		Location location = player.getLocation();
		int y = player.getLocation().getBlockY() + 2;

		while(y <= location.getBlockY() + 6)
		{
			if(player.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType() != Material.AIR)
			{
				player.setVelocity(new Vector().setY(1));

				final int lowestY = y;

				while(player.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType() != Material.AIR)
					y++;

				final int highestY = y;

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if(player.getLocation().getBlockY() >= highestY) cancel();

						if(player.getLocation().getY() + 1.82 >= lowestY)
						{
							player.teleport(player.getLocation().add(0, 0.2, 0)); // TODO: smoother
						}
					}
				}.runTaskTimerAsynchronously(Main.getInstance(), 0, 1);

				break;
			}
			y++;
		}
	}

}
