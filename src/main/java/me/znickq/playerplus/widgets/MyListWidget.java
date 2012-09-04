/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.znickq.playerplus.widgets;

import me.znickq.playerplus.TextureChooser;
import org.getspout.spoutapi.gui.GenericListWidget;

/**
 *
 * @author ZNickq
 */
public class MyListWidget extends GenericListWidget{
	TextureChooser base;
	
	public MyListWidget(TextureChooser base) {
		this.base = base;
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		super.onSelected(item, doubleClick);
		base.onListSelected(item);
	}
	
}
