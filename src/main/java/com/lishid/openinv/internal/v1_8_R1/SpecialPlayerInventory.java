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

package com.lishid.openinv.internal.v1_8_R1;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;

import com.lishid.openinv.OpenInv;
import com.lishid.openinv.internal.ISpecialPlayerInventory;

//Volatile
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.*;
import org.bukkit.craftbukkit.v1_8_R1.inventory.*;

public class SpecialPlayerInventory extends PlayerInventory implements ISpecialPlayerInventory {
    private CraftInventory inventory = new CraftInventory(this);
    private ItemStack[] extra = new ItemStack[5];
    private CraftPlayer owner;
    private boolean playerOnline = false;

    public SpecialPlayerInventory(Player p, Boolean online) {
        super(((CraftPlayer) p).getHandle());
        this.owner = (CraftPlayer) p;
        this.items = player.inventory.items;
        this.armor = player.inventory.armor;
        this.playerOnline = online;
        OpenInv.inventories.put(owner.getName().toLowerCase(), this);
    }

    public Inventory getBukkitInventory() {
        return inventory;
    }

    public boolean inventoryRemovalCheck(boolean save) {
        boolean offline = transaction.isEmpty() && !playerOnline;
        if (offline && save) {
            owner.saveData();
        }
        return offline;
    }

    private void linkInventory(PlayerInventory inventory) {
        inventory.items = this.items;
        inventory.armor = this.armor;
    }

    public void playerOnline(Player player) {
        if (!playerOnline) {
            owner = (CraftPlayer) player;
            this.player = owner.getHandle();
            linkInventory(this.player.inventory);
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
        this.inventoryRemovalCheck(true);
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] C = new ItemStack[getSize()];
        System.arraycopy(items, 0, C, 0, items.length);
        System.arraycopy(armor, 0, C, items.length, armor.length);
        return C;
    }

    @Override
    public int getSize() {
        return super.getSize() + 5;
    }

    @Override
    public ItemStack getItem(int i) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        return is[i];
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        if (is[i] != null) {
            ItemStack itemstack;

            if (is[i].count <= j) {
                itemstack = is[i];
                is[i] = null;
                return itemstack;
            } else {
                itemstack = is[i].a(j);
                if (is[i].count == 0) {
                    is[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        if (is[i] != null) {
            ItemStack itemstack = is[i];

            is[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        // Effects
        if (is == this.extra) {
            owner.getHandle().drop(itemstack, true);
            itemstack = null;
        }

        is[i] = itemstack;

        owner.getHandle().defaultContainer.b();
    }

    private int getReversedItemSlotNum(int i) {
        if (i >= 27)
            return i - 27;
        else
            return i + 9;
    }

    private int getReversedArmorSlotNum(int i) {
        if (i == 0)
            return 3;
        if (i == 1)
            return 2;
        if (i == 2)
            return 1;
        if (i == 3)
            return 0;
        else
            return i;
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public void update() {
        super.update();
        player.inventory.update();
    }
}