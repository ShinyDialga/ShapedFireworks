package com.shinycraft.shapedfireworks;

import com.shinycraft.shapedfireworks.commands.ShapedFireworkCommand;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by ShinyDialga45 on 12/27/2015.
 */
public class ShapedFireworks extends JavaPlugin {

    private static ShapedFireworks instance;

    public void onEnable() {
        instance = this;
        registerCommands();
    }

    public static ShapedFireworks getInstance() {
        return instance;
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) {
            return (WorldEditPlugin) p;
        }
        return null;
    }

    private static CommandsManager<CommandSender> commands;
    private static CommandsManagerRegistration cmdRegister;

    public static void registerCommands() {
        commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        cmdRegister = new CommandsManagerRegistration(ShapedFireworks.getInstance(), commands);
        cmdRegister.register(ShapedFireworkCommand.class);
    }

    public static void unregisterCommands() {
        commands = null;
        cmdRegister.unregisterCommands();
        cmdRegister = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(org.bukkit.ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(org.bukkit.ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(org.bukkit.ChatColor.RED + e.getMessage());
            sender.sendMessage(org.bukkit.ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(org.bukkit.ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(org.bukkit.ChatColor.RED + "An error has occurred.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(org.bukkit.ChatColor.RED + e.getMessage());
        }

        return true;
    }

    public enum FireworkWoolColor {

        WHITE(DyeColor.WHITE, 16777215),
        ORANGE(DyeColor.ORANGE, 16737792),
        MAGENTA(DyeColor.MAGENTA, 16711935),
        LIGHT_BLUE(DyeColor.LIGHT_BLUE, 6737151),
        YELLOW(DyeColor.YELLOW, 16776960),
        LIME(DyeColor.LIME, 10092339),
        PINK(DyeColor.PINK, 16738047),
        GRAY(DyeColor.GRAY, 6710886),
        LIGHT_GRAY(DyeColor.SILVER, 12763842),
        CYAN(DyeColor.CYAN, 39372),
        PURPLE(DyeColor.PURPLE, 10027263),
        BLUE(DyeColor.BLUE,  255),
        BROWN(DyeColor.BROWN, 7555840),
        GREEN(DyeColor.GREEN, 39168),
        RED(DyeColor.RED, 16711680),
        BLACK(DyeColor.BLACK, 0);

        public DyeColor color;
        public int fireworkColor;

        FireworkWoolColor(DyeColor color, int fireworkColor) {
            this.color = color;
            this.fireworkColor = fireworkColor;
        }

        public static FireworkWoolColor getFireworkColor(DyeColor dyeColor) {
            for (FireworkWoolColor color : values()) {
                if (color.color.equals(dyeColor)) {
                    return color;
                }
            }
            return WHITE;
        }

    }

}
