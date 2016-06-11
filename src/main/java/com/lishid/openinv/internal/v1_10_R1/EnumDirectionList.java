package com.lishid.openinv.internal.v1_10_R1;

import com.google.common.collect.Iterators;
import net.minecraft.server.v1_10_R1.EnumDirection;
import net.minecraft.server.v1_10_R1.EnumDirection.EnumDirectionLimit;

import java.util.Iterator;

public enum EnumDirectionList implements Iterable<EnumDirection> {
    HORIZONTAL(EnumDirectionLimit.HORIZONTAL),
    VERTICAL(EnumDirectionLimit.VERTICAL);

    private EnumDirectionLimit list;

    private EnumDirectionList(EnumDirectionLimit list) {
        this.list = list;
    }

    @Override
    public Iterator<EnumDirection> iterator() {
        return Iterators.forArray(list.a());
    }

}