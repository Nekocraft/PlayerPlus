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
package com.almuramc.playerplus;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class InputHandler implements BindingExecutionDelegate {
	public void keyPressed(KeyBindingEvent keyBindingEvent) {
				
		//Only toggle state when not on the gamescreen
		if (!keyBindingEvent.getScreenType().equals(ScreenType.GAME_SCREEN)) {
			return;
		}
		
		SpoutPlayer sPlayer = keyBindingEvent.getPlayer();		
		if(sPlayer.hasPermission("playerplus.use")) {
			new TextureChooser(PlayerPlus.getInstance(), keyBindingEvent.getPlayer());
		}

	}

	public void keyReleased(KeyBindingEvent keyBindingEvent) {
		//Do nothing
	}
}
