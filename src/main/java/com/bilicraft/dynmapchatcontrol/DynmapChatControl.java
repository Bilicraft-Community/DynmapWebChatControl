package com.bilicraft.dynmapchatcontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapWebChatEvent;

import java.util.HashSet;
import java.util.Set;

public final class DynmapChatControl extends JavaPlugin implements Listener {
    private DynmapAPI api;
    private final Set<String> blockedSource = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
        api = (DynmapAPI) Bukkit.getPluginManager().getPlugin("dynmap");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(ignoreCancelled = true)
    public void dynmapChat(DynmapWebChatEvent event){
        for (String s : blockedSource) {
            if(event.getName().equals(s) || event.getName().contains(s) || event.getName().startsWith(s) || event.getName().matches(s)){
                event.setCancelled(true);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("dwebchat.admin")){
            return false;
        }
        if(args.length != 2){
            sender.sendMessage("Usage: /dwebchat <mute/unmute> <source>");
            return true;
        }
        switch (args[0]){
            case "mute":
                this.blockedSource.add(args[1]);
                sender.sendMessage("操作成功，禁言对象将持续到下次服务器重启");
                break;
            case "unmute":
                this.blockedSource.remove(args[1]);
                sender.sendMessage("操作成功");
                break;
        }
       return true;

    }
}
