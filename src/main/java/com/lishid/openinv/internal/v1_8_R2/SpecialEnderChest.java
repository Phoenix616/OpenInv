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

package com.lishid.openinv.internal.v1_8_R2;

import com.lishid.openinv.OpenInv;
import com.lishid.openinv.internal.ISpecialEnderChest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

//Volatile
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.*;
import org.bukkit.craftbukkit.v1_8_R2.inventory.*;

public class SpecialEnderChest extends InventorySubcontainer implements ISpecialEnderChest {
    private CraftInventory inventory = new CraftInventory(this);
    private InventoryEnderChest enderChest;
    private CraftPlayer owner;
    private boolean playerOnline = false;

    public SpecialEnderChest(Player p, Boolean online) {
        this(p, ((CraftPlayer) p).getHandle().getEnderChest(), online);
    }

    public SpecialEnderChest(Player p, InventoryEnderChest enderchest, boolean online) {
        super(enderchest.getName(), enderchest.hasCustomName(), enderchest.getSize());
        this.owner = (CraftPlayer) p;
        this.enderChest = enderchest;
        this.items = enderChest.getContents();
        this.playerOnline = online;
        OpenInv.enderChests.put(owner.getName().toLowerCase(), this);
    }

    public boolean inventoryRemovalCheck(boolean save) {
        boolean offline = transaction.isEmpty() && !playerOnline;
        if (offline && save) {
            owner.saveData();
        }
        return offline;
    }

    private void linkInventory(InventoryEnderChest inventory) {
        inventory.items = this.items;
    }

    public Inventory getBukkitInventory() {
        return inventory;
    }

    public void playerOnline(Player p) {
        if (!playerOnline) {
            owner = (CraftPlayer) p;
            linkInventory(((CraftPlayer) p).getHandle().getEnderChest());
            playerOnline = true;
        }
    }

    public boolean playerOffline() {
        playerOnline = false;
        return inventoryRemovalCheck(false);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        super.onClose(who);
        inventoryRemovalCheck(true);
    }

    @Override
    public InventoryHolder getOwner() {
        return this.owner;
    }

    @Override
    public void update() {
        super.update();
        enderChest.update();
    }
}
