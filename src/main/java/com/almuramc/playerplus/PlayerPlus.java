/*
 * This file is part of PlayerPlus.
 *
 * Copyright (c) 2012, AlmuraDev <http://www.almuradev.com/>
 * PlayerPlus is licensed under the Almura Development License.
 *
 * PlayerPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlayerPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License. If not,
 * see <http://www.gnu.org/licenses/> for the GNU General Public License.
 */
package com.almuramc.playerplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.player.accessories.AccessoryType;
import org.mcstats.MetricsLite;

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
		config.addDefault("ForceDefaultCape", true);
		config.addDefault("DefaultCape", "http://www.almuramc.com/playerplus/capes/almuracape.png");
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
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
        }
	}

	public List<WebAccessory> getAvailable(AccessoryType type) {
		// Accessories
		List<WebAccessory> toRet = new ArrayList<WebAccessory>();		
		File adr = new File(getDataFolder(), type.toString().toLowerCase() + ".yml");
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);				
				if (type.toString().equalsIgnoreCase("bracelet")) {
					temp.addDefault("Green and Silver", "http://www.almuramc.com/playerplus/bracelet/pp-bc-4.png");
					temp.addDefault("Black Stripes", "http://www.almuramc.com/playerplus/bracelet/pp-bc-618.png");					
				}
				if (type.toString().equalsIgnoreCase("ears")) {
					temp.addDefault("Rainbow", "http://www.almuramc.com/playerplus/ears/pp-er-27.png");
					temp.addDefault("Blue White", "http://www.almuramc.com/playerplus/ears/pp-er-1667.png");					
				}
				if (type.toString().equalsIgnoreCase("notchhat")) {
					temp.addDefault("Black and Blue", "http://www.almuramc.com/playerplus/notchhat/pp-nh-48.png");
					temp.addDefault("Red Stripes", "http://www.almuramc.com/playerplus/notchhat/pp-nh-801.png");
				}
				if (type.toString().equalsIgnoreCase("sunglasses")) {
					temp.addDefault("Black", "http://www.almuramc.com/playerplus/sunglasses/sun_black_1.png");
					temp.addDefault("Red", "http://www.almuramc.com/playerplus/sunglasses/sun_red_1.png");
				}
				if (type.toString().equalsIgnoreCase("tail")) {
					temp.addDefault("Blue Tail", "http://www.almuramc.com/playerplus/tail/pp-tl-1083.png");
					temp.addDefault("Red Tails", "http://www.almuramc.com/playerplus/tail/pp-tl-1217.png");
				}
				if (type.toString().equalsIgnoreCase("tophat")) {
					temp.addDefault("Black Tophat", "http://www.almuramc.com/playerplus/tophat/pp-th-23.png");
					temp.addDefault("Red TopHat", "http://www.almuramc.com/playerplus/tophat/pp-th-1455.png");
				}
				if (type.toString().equalsIgnoreCase("wings")) {
					temp.addDefault("Fire Wings", "http://www.almuramc.com/playerplus/wings/pp-wg-30.png");
					temp.addDefault("White Wings", "http://www.almuramc.com/playerplus/wings/pp-wg-9.png");
				}
				temp.options().copyDefaults(true);
				temp.save(adr);
			} catch (IOException ex) {
				Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		YamlConfiguration ycf = YamlConfiguration.loadConfiguration(adr);
		for (String name : ycf.getKeys(false)) {
			toRet.add(new WebAccessory(name, ycf.getString(name)));
		}

		return toRet;
	}

	public List<WebAccessory> getCapes() {
		// Capes, to be used until Capes are moved into the AccessoryType Class
		List<WebAccessory> toRet = new ArrayList<WebAccessory>();		
		File adr = new File(getDataFolder(), "capes.yml");
		if (!adr.exists()) {
			try {
				adr.createNewFile();
				YamlConfiguration temp = YamlConfiguration.loadConfiguration(adr);				
				temp.addDefault("Spout Cape", "http://www.almuramc.com/playerplus/capes/spoutcape.png");
				temp.addDefault("Almura Cape", "http://www.almuramc.com/playerplus/capes/almuracape.png");								
				temp.options().copyDefaults(true);
				temp.save(adr);
			} catch (IOException ex) {
				Logger.getLogger(PlayerPlus.class.getName()).log(Level.SEVERE, null, ex);
			}
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
				if (url != null) {
					sPlayer.setCape(url);
				} else {
					if (PlayerPlus.getInstance().getConfig().getBoolean("ForceDefaultCape")) {
						sPlayer.setCape(PlayerPlus.getInstance().getConfig().getString("DefaultCape"));
					}
				}
			} else {
				sPlayer.resetCape();
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
