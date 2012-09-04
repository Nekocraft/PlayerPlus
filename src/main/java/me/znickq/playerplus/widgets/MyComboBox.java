/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.znickq.playerplus.widgets;

import org.getspout.spoutapi.gui.GenericListWidget;
import me.znickq.playerplus.TextureChooser;
import org.getspout.spoutapi.gui.GenericComboBox;

/**
 *
 * @author ZNickq
 */
public class MyComboBox extends GenericComboBox{
	private TextureChooser base;
	
	public MyComboBox(TextureChooser base) {
		this.base = base;
	}

	@Override
	public void onSelectionChanged(int i, String text) {
		super.onSelectionChanged(i, text);
		base.onSelected(i);
	}
}
