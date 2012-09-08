/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almuramc.playerplus.widgets;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

import com.almuramc.playerplus.PlayerPlus;

public class CloseButton extends GenericButton {

	private PlayerPlus i;

	public CloseButton(PlayerPlus i) {
		super("Close");
		this.i = i;
	}

	public CloseButton(boolean openMain) {
		super("Close");
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		event.getPlayer().getMainScreen().closePopup();
	}
}