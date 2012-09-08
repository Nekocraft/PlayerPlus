/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almuramc.playerplus.widgets;

import org.getspout.spoutapi.gui.GenericListWidget;

import com.almuramc.playerplus.TextureChooser;

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
