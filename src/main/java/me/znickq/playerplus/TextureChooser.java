/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.znickq.playerplus;

import java.util.ArrayList;
import java.util.List;
import me.znickq.playerplus.widgets.*;

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

/**
 *
 * @author ZNickq
 */
public class TextureChooser extends GenericPopup {

	private PlayerPlus instance;
	private SpoutPlayer player;
	private ListWidget lw;
	private MyComboBox cb;
	private GenericTexture gt;
	private AccessoryType current;
	private List<WebAccessory> list;

	public TextureChooser(PlayerPlus instance, SpoutPlayer player) {
		this.instance = instance;
		this.player = player;
		current = AccessoryType.BRACELET;
		
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
		cb.setItems(getAvailableAccessories(player));

		lw = new MyListWidget(this);
		lw.setAnchor(WidgetAnchor.CENTER_CENTER);
		lw.setHeight(150).setWidth(150);		
		lw.shiftXPos(-190).shiftYPos(-60);		
		updateList();

		gt = new GenericTexture();
		gt.setAnchor(WidgetAnchor.CENTER_CENTER);
		gt.setHeight(150).setWidth(150);
		gt.shiftXPos(10).shiftYPos(-90);		

		GenericButton pre = new ActionButton("<", this, -1);
		pre.setAnchor(WidgetAnchor.CENTER_CENTER);
		pre.setHeight(20).setWidth(20);
		pre.shiftXPos(10).shiftYPos(65);

		GenericButton select = new ActionButton("Select", this, 0);
		select.setAnchor(WidgetAnchor.CENTER_CENTER);
		select.setHeight(20).setWidth(50);
		select.shiftXPos(55).shiftYPos(65);

		GenericButton next = new ActionButton(">", this, 1);
		next.setAnchor(WidgetAnchor.CENTER_CENTER);
		next.setHeight(20).setWidth(20);
		next.shiftXPos(138).shiftYPos(65);
		
		CloseButton close = new CloseButton(instance);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setHeight(20).setWidth(50);
		close.shiftXPos(150).shiftYPos(95);
		updateSelection();
		
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

	private List<String> getAvailableAccessories(SpoutPlayer player) {
		//TODO Dockter, good place to put permision check in ^^
		List<String> available = new ArrayList<String>();
		for (AccessoryType type : AccessoryType.values()) {
			available.add(type.name().toLowerCase());
		}
		return available;
	}

	public void onSelected(int item) {
		if (lw != null && item != -1) {
			current = AccessoryType.values()[item];
			updateList();
			updateSelection();
			updateTexture();
		}
	}

	public void onListSelected(int item) {
		updateTexture();
	}

	public void onActionClick(int id) {
		if (id == 0) {
			if (lw.getSelectedRow() > 0) {
				player.addAccessory(current, list.get(lw.getSelectedRow() - 1).getUrl());
			} else {				
				player.removeAccessory(current);
			}
			player.sendNotification("Accessory Applied", current.name().toLowerCase(), Material.GOLD_CHESTPLATE);
			player.sendMessage(ChatColor.GOLD + "[PlayerPlus]" + ChatColor.WHITE + " - Accessory Applied!");
			instance.save(player, current);
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
		String url = player.getAccessoryURL(current);
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
