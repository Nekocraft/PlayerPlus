/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almuramc.playerplus.widgets;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

import com.almuramc.playerplus.TextureChooser;

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
