package hapesh.raidlogs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
    FileConfiguration config = getConfig();
    FileHandler fileLog;
    @Override
    public void onEnable() {
        File raidlog = new File("raids.log");
        try {
            raidlog.createNewFile();
            System.out.println("File created");
        } catch (IOException e) {
            System.out.println("File is already created.");
        }
        config.options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("raidlog").setExecutor(this);
        getCommand("raidlog").setTabCompleter(this);
        try {
            fileLog = new FileHandler("raids.log", 0, 1, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable() {
        File shit = new File("raids.log.1");
        shit.deleteOnExit();
        File shitlck = new File("raids.log.lck");
        shitlck.deleteOnExit();
        try {
            fileLog = new FileHandler("raids.log", 0, 1, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger(fileLog).removeHandler(fileLog);
        fileLog.close();
        System.out.println("RaidLogs disabled, bye!");
    }
    public FileHandler getFileHandler() {
        return fileLog;
    }
    public Logger getLogger(FileHandler fileLog) {
        Logger logger = Logger.getLogger("raids.log");
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false);
        logger.addHandler(fileLog);
        return logger;
    }

    @EventHandler
    public void onRaid (RaidTriggerEvent event) {
        fileLog.setFormatter(new MyFormatter());
        if (getConfig().getString("loglanguage").equals("en")) {
            getLogger(getFileHandler()).info("Player " + event.getPlayer().getDisplayName() + " started a raid in location X: " + event.getRaid().getLocation().getBlockX() + " Y: " + event.getRaid().getLocation().getBlockY() + " Z: " + event.getRaid().getLocation().getBlockZ());
        }
        if (getConfig().getString("loglanguage").equals("ru")) {
            getLogger(getFileHandler()).info("?????????? " + event.getPlayer().getDisplayName() + " ?????????????? ???????? ???? ?????????????????????? X: " + event.getRaid().getLocation().getBlockX() + " Y: " + event.getRaid().getLocation().getBlockY() + " Z: " + event.getRaid().getLocation().getBlockZ());
        }
        if (getConfig().getString("loglanguage").equals("ua")) {
            getLogger(getFileHandler()).info("?????????????? " + event.getPlayer().getDisplayName() + " ?????????? ???????? ???? ?????????????????????? X: " + event.getRaid().getLocation().getBlockX() + " Y: " + event.getRaid().getLocation().getBlockY() + " Z: " + event.getRaid().getLocation().getBlockZ());
        }
    }

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        if (args.length < 1) {return false;}
        if (args[0].equals("reload")) {
            reloadConfig();
        }
        if (args[0].equals("view")){
            File raidlog = new File("raids.log");
            Scanner scanner = null;
            try {
                scanner = new Scanner(raidlog);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int lineNumber = 1;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sender.sendMessage(line);
                lineNumber++;
            }
        }
        return false;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        list.add("reload");list.add("view");
        return list;
    }
}
