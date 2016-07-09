/*
 * Copyright (C) 2011-2014 lishid.  All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation,  version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.lishid.openinv.internal;

import com.lishid.openinv.OpenInv;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class AbstractPlayerDataManager {
    public Player loadPlayer(String name) {
        Player player = Bukkit.getServer().getPlayer(name);
        if (player != null && player.isOnline()) {
            return player;
        }
        if (OpenInv.inventories.containsKey(name.toLowerCase())) {
            return (Player) OpenInv.inventories.get(name.toLowerCase()).getOwner();
        }
        if (OpenInv.enderChests.containsKey(name.toLowerCase())) {
            return (Player) OpenInv.enderChests.get(name.toLowerCase()).getOwner();
        }
        return loadOfflinePlayer(name);
    }

    protected abstract Player loadOfflinePlayer(String name);
}
