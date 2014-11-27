package me.MnMaxon.CommandLog;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MainListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (!e.isCancelled()) {
			if (e.getPlayer().hasPermission("commandlog.bypass"))
				return;
			int x = 0;
			YamlConfiguration data = Config.Load(Main.dataFolder + "/Data.yml");
			while (data.get("Commands." + x) != null)
				x++;
			data.set("Commands." + x + ".Name", e.getPlayer().getName());
			data.set("Commands." + x + ".Command", e.getMessage());
			Config.Save(data, Main.dataFolder + "/Data.yml");
		}
	}
}
