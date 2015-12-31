package com.shinycraft.shapedfireworks.commands;

import com.shinycraft.shapedfireworks.ShapedFireworks;
import com.shinycraft.shapedfireworks.ShapedFireworks.FireworkWoolColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShinyDialga45 on 3/21/15.
 */
public class ShapedFireworkCommand {

    @Command(aliases = {"shapedfirework", "shapedfireworks"}, desc = "Make a shaped firework")
    public static void shapedfirework(final CommandContext args, CommandSender sender) throws Exception {
        Player player = (Player)sender;
        List<String> commands = new ArrayList<>();
        Selection s = ShapedFireworks.getWorldEdit().getSelection((Player)sender);
        try {
            for (BlockVector blockVector : s.getRegionSelector().getRegion()) {
                Block block = player.getWorld().getBlockAt(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
                if (block.getType().equals(Material.WOOL)) {
                    Wool wool = new Wool(block.getType(), block.getData());
                    DyeColor dyeColor = wool.getColor();
                    FireworkWoolColor fireworkColor = FireworkWoolColor.getFireworkColor(dyeColor);
                    commands.add("/summon FireworksRocketEntity " + block.getX() + " " + block.getY() + " " + block.getZ() + " {LifeTime:0,FireworksItem:{id:401,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:0,Trail:0,Colors:[" + fireworkColor.fireworkColor + "],FadeColors:[" + fireworkColor.fireworkColor + "]}]}}}}");
                    for (BlockFace face : BlockFace.values()) {
                        Block b = block.getRelative(face);
                        if (b.getType().equals(Material.AIR)) {
                            b.setType(Material.BARRIER);
                        }
                    }
                    block.setType(Material.BARRIER);
                }
            }
        } catch (NullPointerException e) {
            sender.sendMessage(ChatColor.RED + "Your WorldEdit selection cannot be empty.");
        }

        try {
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY() + 1;
            int z = player.getLocation().getBlockZ();
            for (String string : commands) {
                Block block = player.getWorld().getBlockAt(x, y, z);
                block.setType(Material.COMMAND);
                Block redstone = player.getWorld().getBlockAt(x, y + 1, z);
                redstone.setType(Material.REDSTONE_WIRE);
                CommandBlock commandBlock = (CommandBlock)block.getState();
                commandBlock.setCommand(string);
                commandBlock.update();
                x = x + 1;
                if (x >= player.getLocation().getBlockX() + 11) {
                    x = player.getLocation().getBlockX();
                    z = z + 1;
                }
            }
        } catch (Exception ignored) {

        }
    }

}
