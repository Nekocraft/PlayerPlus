/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.znickq.playerplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.player.accessories.AccessoryType;

/**
 *
 * @author ZNickq
 */
public class PlayerPlus extends JavaPlugin implements Listener {

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getDataFolder().mkdir();
		for (AccessoryType ttype : AccessoryType.values()) {
			List<WebAccessory> aval = getAvailable(ttype);
			for (WebAccessory wa : aval) {
				SpoutManager.getFileManager().addToCache(this, wa.getUrl());
			}
		}
	}

	public List<WebAccessory> getAvailable(AccessoryType type) {
		List<WebAccessory> toRet = new ArrayList<WebAccessory>();
		File adr = new File(getDataFolder(), type.toString().toLowerCase() + ".yml");
		try {
			adr.createNewFile();
			YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);
			temp.addDefault("Name", "URL");
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
	public void onKeyPressed(KeyPressedEvent event) {
		System.out.println("GG");
		if (event.getKey() == Keyboard.KEY_U) {
			new TextureChooser(this, event.getPlayer());
		}
	}
	
	@EventHandler
	public void onSpoutcraftAuth(SpoutCraftEnableEvent event) {
		for(AccessoryType ttype : AccessoryType.values()) {
			String url = get(event.getPlayer().getName(), ttype);
			if(url != null) {
				event.getPlayer().addAccessory(ttype, url);
			}
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
}
