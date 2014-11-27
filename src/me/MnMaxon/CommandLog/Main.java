package me.MnMaxon.CommandLog;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static String dataFolder;
	public static Main plugin;

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = this.getDataFolder().getAbsolutePath();
		Config.Load(dataFolder + "/Data.yml");
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		int page = 1;
		if (args.length > 0)
			if (args[0].equalsIgnoreCase("clear")) {
				if (s instanceof Player) {
					if (!((Player) s).hasPermission("commandlog.clear")) {
						s.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
						return false;
					}
				}
				YamlConfiguration data = Config.Load(dataFolder + "/Data.yml");
				data.set("Commands", null);
				s.sendMessage(ChatColor.DARK_GREEN + "Command log successfully cleared");
				Config.Save(data, dataFolder + "/Data.yml");
				return false;
			} else
				try {
					page = Integer.parseInt(args[0]);
				} catch (NumberFormatException ex) {
					s.sendMessage(ChatColor.DARK_RED + args[0] + " is not a number");
				}
		if (s instanceof Player) {
			if (!((Player) s).hasPermission("commandlog.clog")) {
				s.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				return false;
			}
		}
		YamlConfiguration data = Config.Load(dataFolder + "/Data.yml");
		int max = 0;
		while (data.get("Commands." + max) != null)
			max++;
		int maxPages = (int) Math.round((max + 5.0) / 10.0);
		if (page > maxPages)
			page = maxPages;
		s.sendMessage(ChatColor.DARK_BLUE + "== " + ChatColor.GOLD + "Page " + page + "/" + maxPages + ChatColor.BLUE
				+ " ==");
		for (int x = 0; x < 10; x++) {
			int num = max - ((page - 1) * 10) - x;
			if (data.get("Commands." + num) != null)
				s.sendMessage(ChatColor.RED + data.getString("Commands." + num + ".Name") + ": " + ChatColor.GREEN
						+ data.getString("Commands." + num + ".Command"));
		}
		return false;
	}
}