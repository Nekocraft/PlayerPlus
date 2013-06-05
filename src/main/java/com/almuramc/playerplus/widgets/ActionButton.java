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

import com.almuramc.playerplus.TextureChooser;

/**
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
