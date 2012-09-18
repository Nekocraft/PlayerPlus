/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almuramc.playerplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.player.accessories.AccessoryType;
import org.getspout.spout.Spout;
import org.getspout.spout.player.SpoutCraftPlayer;

/**
 *
 * @author ZNickq & Dockter
 */
public class PlayerPlus extends JavaPlugin implements Listener {

	private static PlayerPlus instance;
	public static String hotkeys = null;

	public static PlayerPlus getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {	
		instance = this;
		FileConfiguration config = this.getConfig();
		config.addDefault("PromptTitle", "Player Plus Accessories");
		config.addDefault("TitleX", 190);
		config.addDefault("Hot_Key", "KEY_U");
		config.addDefault("GUITexture", "http://www.pixentral.com/pics/1duZT49LzMnodP53SIPGIqZ8xdKS.png");
		config.options().copyDefaults(true);
		saveConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getDataFolder().mkdir();
		for (AccessoryType ttype : AccessoryType.values()) {
			List<WebAccessory> aval = getAvailable(ttype);
			for (WebAccessory wa : aval) {
				SpoutManager.getFileManager().addToPreLoginCache(this, wa.getUrl());
			}
		}
		hotkeys = config.getString("Hot_Key");
		SpoutManager.getKeyBindingManager().registerBinding("PlayerPlus", Keyboard.valueOf(PlayerPlus.hotkeys), "Opens Player Plus Accessories", new InputHandler(), PlayerPlus.getInstance());
	}

	public List<WebAccessory> getAvailable(AccessoryType type) {
		List<WebAccessory> toRet = new ArrayList<WebAccessory>();		
		File adr = new File(getDataFolder(), type.toString().toLowerCase() + ".yml");
		try {
			adr.createNewFile();
			YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
			// temp.addDefault("Name", "URL");
			temp.options().copyDefaults(true);
			temp.save(adr);
		} catch (IOException ex) {
			Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
		}

		YamlConfiguration ycf = YamlConfiguration.loadConfiguration(adr);
		for (String name : ycf.getKeys(false)) {
			toRet.add(new WebAccessory(name, ycf.getString(name)));
		}

		return toRet;
	}

	public List<WebAccessory> getCapes() {
		List<WebAccessory> toRet = new ArrayList<WebAccessory>();		
		File adr = new File(getDataFolder(), "capes.yml");
		try {
			adr.createNewFile();
			YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
			//temp.addDefault("Name", "URL");
			temp.options().copyDefaults(true);
			temp.save(adr);
		} catch (IOException ex) {
			Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
		}

		YamlConfiguration ycf = YamlConfiguration.loadConfiguration(adr);
		for (String name : ycf.getKeys(false)) {
			toRet.add(new WebAccessory(name, ycf.getString(name)));
		}

		return toRet;
	}

	@EventHandler
	public void onSpoutcraftAuth(SpoutCraftEnableEvent event) {
		SpoutPlayer sPlayer = event.getPlayer();

		if (!sPlayer.hasPermission("PlayerPlus.use")) {
			for(AccessoryType ttype : AccessoryType.values()) {
				String url = get(event.getPlayer().getName(), ttype);		
				sPlayer.removeAccessory(ttype);
			}
		}

		if (sPlayer.hasPermission("PlayerPlus.use")) {
			for(AccessoryType ttype : AccessoryType.values()) {
				String url = get(event.getPlayer().getName(), ttype);
				if (event.getPlayer().hasPermission("PlayerPlus.use." + ttype.toString())) {
					if(url != null) {
						event.getPlayer().addAccessory(ttype, url);				
					}	
				} else {
					sPlayer.removeAccessory(ttype);					
				}			
			}
			if (event.getPlayer().hasPermission("PlayerPlus.use.capes")) {				
				String url = getCape(event.getPlayer().getName(), "CAPES");
				if (url != null)
					sPlayer.setCape(url);				
			} else {
				 sPlayer.resetCape();
			}
			
		}
		
		if (sPlayer.hasPermission("PlayerPlus.title")) {
			if (sPlayer.hasPermission("Guest.title") && !sPlayer.hasPermission("Member.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.GRAY+"Guest");				
			}
			if (sPlayer.hasPermission("Member.title") && !sPlayer.hasPermission("SuperMember.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.YELLOW+"Member");				
			}
			if (sPlayer.hasPermission("SuperMember.title") && !sPlayer.hasPermission("UltraMember.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.DARK_GREEN+"SuperMember");				
			}
			if (sPlayer.hasPermission("Ultra.title") && !sPlayer.hasPermission("CreativeMember.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.GOLD+"UltraMember");				
			}
			if (sPlayer.hasPermission("CreativeMember.title") && !sPlayer.hasPermission("Mod.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.LIGHT_PURPLE+"CreativeMember");				
			}
			if (sPlayer.hasPermission("Dev.title") && !sPlayer.hasPermission("admin.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.DARK_BLUE+"Developer");				
			}
			if (sPlayer.hasPermission("mod.title") && !sPlayer.hasPermission("Admin.title")) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.DARK_BLUE+"Moderator");
			}
			if (sPlayer.hasPermission("admin.title") && !sPlayer.isOp()) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.DARK_RED+"Almura Admin");
			}
			if (sPlayer.hasPermission("admin.title") && sPlayer.isOp()) {
				sPlayer.setTitle(sPlayer.getDisplayName()+"\n"+ChatColor.DARK_RED+"Almura SuperAdmin");
			}
			SpoutCraftPlayer player = (SpoutCraftPlayer)SpoutCraftPlayer.getPlayer(event.getPlayer());			
			player.doPostPlayerChangeWorld();
			
		}
	}

	
	
	public String get(String player, AccessoryType type) {
		File saveFile = new File(getDataFolder(), "saved.yml");
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		YamlConfiguration yFile = YamlConfiguration.loadConfiguration(saveFile);
		return yFile.getString(player+"."+type.name());
	}
	
	public String getCape(String player, String type) {
		File saveFile = new File(getDataFolder(), "saved.yml");
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		YamlConfiguration yFile = YamlConfiguration.loadConfiguration(saveFile);
		return yFile.getString(player+"."+"CAPES");
	}

	public void save(SpoutPlayer player, AccessoryType type) {
		File saveFile = new File(getDataFolder(), "saved.yml");
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
			}
		 }
				
		YamlConfiguration yFile = YamlConfiguration.loadConfiguration(saveFile);
		yFile.set(player.getName()+"."+type.name()+"", player.getAccessoryURL(type));
		try {
			yFile.save(saveFile);
		} catch (IOException ex) {
			Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void saveCape(SpoutPlayer player) {
		File saveFile = new File(getDataFolder(), "saved.yml");
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
			}
		 }
		
		YamlConfiguration yFile = YamlConfiguration.loadConfiguration(saveFile);
		yFile.set(player.getName()+"."+"CAPES", player.getCape());
		try {
			yFile.save(saveFile);
		} catch (IOException ex) {
			Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
