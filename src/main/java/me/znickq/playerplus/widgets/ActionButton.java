/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.znickq.playerplus.widgets;

import me.znickq.playerplus.TextureChooser;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

/**
 *
 * @author ZNickq
 */
public class ActionButton extends GenericButton {

	private int id;
	private TextureChooser base;

	public ActionButton(String name, TextureChooser tx, int id) {
		super(name);
		this.base = tx;
		this.id = id;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		super.onButtonClick(event);
		base.onActionClick(id);
	}
}
