/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almuramc.playerplus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ListWidget;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.player.accessories.AccessoryType;

import com.almuramc.playerplus.widgets.*;

public class TextureChooser extends GenericPopup {

	private PlayerPlus instance;
	private SpoutPlayer player;
	private ListWidget lw;
	private GenericButton select;
	private MyComboBox cb;
	private GenericTexture gt;
	private AccessoryType current;
	private List<WebAccessory> list;
	private String currentAddon;
	private int addonType = 1;

	public TextureChooser(PlayerPlus instance, SpoutPlayer player) {
		this.instance = instance;
		this.player = player;
		current = AccessoryType.BRACELET;
		currentAddon = null;

		GenericTexture border = new GenericTexture(PlayerPlus.getInstance().getConfig().getString("GUITexture"));
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(420).setHeight(345);
		border.shiftXPos(-205).shiftYPos(-120);

		GenericLabel label = new GenericLabel();
		label.setText(PlayerPlus.getInstance().getConfig().getString("PromptTitle"));
		label.setAnchor(WidgetAnchor.CENTER_CENTER);
		label.shiftXPos(-50).shiftYPos(-112);	
		label.setScale(1.2F).setWidth(-1).setHeight(-1);

		GenericLabel label1 = new GenericLabel();
		label1.setText("Images represent the map of the image, not actual appearence.");
		label1.setAnchor(WidgetAnchor.CENTER_CENTER);
		label1.shiftXPos(-190).shiftYPos(100);	
		label1.setScale(1.0F).setWidth(-1).setHeight(-1);

		cb = new MyComboBox(this);
		cb.setText("Accessories");
		cb.setAnchor(WidgetAnchor.CENTER_CENTER);
		cb.shiftXPos(-190).shiftYPos(-90);
		cb.setHeight(20).setWidth(150);
		cb.setSelection(0);		
		updateDropdown();		

		lw = new MyListWidget(this);
		lw.setAnchor(WidgetAnchor.CENTER_CENTER);
		lw.setHeight(150).setWidth(150);		
		lw.shiftXPos(-190).shiftYPos(-60);		

		gt = new GenericTexture();
		gt.setAnchor(WidgetAnchor.CENTER_CENTER);
		gt.setHeight(150).setWidth(150);
		gt.shiftXPos(10).shiftYPos(-90);		

		GenericButton pre = new ActionButton("<", this, -1);
		pre.setAnchor(WidgetAnchor.CENTER_CENTER);
		pre.setHeight(20).setWidth(20);
		pre.shiftXPos(10).shiftYPos(65);

		select = new ActionButton("Select", this, 0);
		select.setAnchor(WidgetAnchor.CENTER_CENTER);
		select.setHeight(20).setWidth(50);
		select.shiftXPos(55).shiftYPos(65);
		select.setEnabled(false);

		GenericButton next = new ActionButton(">", this, 1);
		next.setAnchor(WidgetAnchor.CENTER_CENTER);
		next.setHeight(20).setWidth(20);
		next.shiftXPos(138).shiftYPos(65);

		CloseButton close = new CloseButton(instance);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setHeight(20).setWidth(50);
		close.shiftXPos(150).shiftYPos(95);
	
		attachWidgets(instance, border, label, label1, lw, cb, gt, pre, select, next, close);
		player.getMainScreen().attachPopupScreen(this);
		updateTexture();
	}

	public void updateTexture() {
		int sel = lw.getSelectedRow();
		if (sel < 1) {
			gt.setUrl("");
		} else {
			gt.setUrl(list.get(sel - 1).getUrl());
		}		
		gt.setDirty(true);
	}

	public void updateList() {
		lw.clear();
		list = instance.getAvailable(current);
		lw.addItem(new ListWidgetItem("None", ""));
		for (WebAccessory toAdd : list) {
			lw.addItem(new ListWidgetItem(toAdd.getName(), "", toAdd.getUrl()));
		}
		lw.setDirty(true);
	}
	
	public void updateCapesList() {
		lw.clear();
		list = instance.getCapes();
		lw.addItem(new ListWidgetItem("None", ""));
		for (WebAccessory toAdd : list) {
			lw.addItem(new ListWidgetItem(toAdd.getName(), "", toAdd.getUrl()));
		}
		lw.setDirty(true);
	}

	private void updateDropdown() {
		List<String> available = new ArrayList<String>();
		for (AccessoryType type : AccessoryType.values()) {
			available.add(type.name().toLowerCase());			
		}
		available.add("capes");		
		Collections.sort(available, String.CASE_INSENSITIVE_ORDER);
		cb.setItems(available);
		cb.setDirty(true);
	}

	private List<String> getAvailableAccessories(SpoutPlayer player) {
		List<String> available = new ArrayList<String>();
		for (AccessoryType type : AccessoryType.values()) {
			available.add(type.name().toLowerCase());
			Collections.sort(available, String.CASE_INSENSITIVE_ORDER);
		}
		return available;
	}

	public void onSelected(String item) {
		if (lw != null && item != null) {			
			if(item.equalsIgnoreCase("bracelet")  || item.equalsIgnoreCase("ears")  || item.equalsIgnoreCase("notchhat")  || item.equalsIgnoreCase("sunglasses")  || item.equalsIgnoreCase("tail")   || item.equalsIgnoreCase("tophat")  ||  item.equalsIgnoreCase("wings")) { 
				current = AccessoryType.valueOf((item.toUpperCase()));			
				addonType = 1;
				updateList();
				updateSelection();
				updateTexture();
				if (player.hasPermission("PlayerPlus.use." + current)) {
					select.setEnabled(true);
				} else {
					select.setEnabled(false);
				}
			}
			if (item.equalsIgnoreCase("capes")) {
				currentAddon = item;
				addonType = 2;
				updateCapesList();
				updateSelection();
				updateTexture();
				if (player.hasPermission("PlayerPlus.use." + item)) {
					select.setEnabled(true);
				} else {
					select.setEnabled(false);
				}
			}
			
		}
	}

	public void onListSelected(int item) {
		updateTexture();
	}

	public void onActionClick(int id) {
		if (id == 0) {
			if (lw.getSelectedRow() > 0) {
				if (addonType == 2) {
					player.setCape(list.get(lw.getSelectedRow() - 1).getUrl());
					instance.saveCape(player);
					player.sendNotification("Accessory Applied", "Cape", Material.GOLD_CHESTPLATE);
				} else {
					player.addAccessory(current, list.get(lw.getSelectedRow() - 1).getUrl());
					instance.save(player, current);
					player.sendNotification("Accessory Applied", current.name().toLowerCase(), Material.GOLD_CHESTPLATE);
				}
				player.sendMessage(ChatColor.GOLD + "[PlayerPlus]" + ChatColor.WHITE + " - Accessory Applied!");	
			} else {				
				if (addonType == 2) {
					player.resetCape();
				} else {
					player.removeAccessory(current);
					instance.save(player, current);
				}
			}			
			return;
		}
		int cuRow = lw.getSelectedRow();
		cuRow += id;
		if (cuRow == -1) {
			cuRow = lw.getItems().length - 1;
		}
		if (cuRow == lw.getItems().length) {
			cuRow = 0;
		}
		lw.setSelection(cuRow);
		lw.setDirty(true);
		updateTexture();
	}

	
	private void updateSelection() {
		int which = 0;
		String url = null;
		
		url = player.getAccessoryURL(current);
		
		if(url == null) {
			lw.setSelection(which);
			return;
		}
		for(int i=0;i<list.size();i++) {
			WebAccessory wa = list.get(i);
			if(url.equals(wa.getUrl())) {
				which = i + 1;
				break;
			}
		}
		lw.setSelection(which);
	}
}
