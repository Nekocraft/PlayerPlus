/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.znickq.playerplus.widgets;

import me.znickq.playerplus.PlayerPlus;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

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