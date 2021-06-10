package hapesh.raidlogs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
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
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("raidlog").setExecutor(this);
        try {
            fileLog = new FileHandler("raids.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @Override
    public void onDisable() {
        FileHandler fileLog = null;
        try {
            fileLog = new FileHandler("raids.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        getLogger(getFileHandler()).info("Player " + event.getPlayer().getDisplayName() + " started a raid in location X: " + event.getRaid().getLocation().getBlockX() + " Y: " + event.getRaid().getLocation().getBlockY() + " Z: " + event.getRaid().getLocation().getBlockZ()); }




    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        File raidlog = new File("raids.log");
        Scanner scanner = null;
        try {
            scanner = new Scanner(raidlog);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int lineNumber = 1;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            sender.sendMessage(line);
            lineNumber++;
        }
        return false;
    }
}
