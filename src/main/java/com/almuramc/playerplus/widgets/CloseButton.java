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