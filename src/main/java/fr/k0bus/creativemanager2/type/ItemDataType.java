package fr.k0bus.creativemanager2.type;

import fr.k0bus.creativemanager2.protections.Protection;
import java.util.List;

public enum ItemDataType {
    ENCHANT,
    NBT,
    POTION_EFFECT,
    NAME_SPACED_KEY,
    ITEM_FLAG;

    public boolean isEnabled(Protection protection) {
        return protection.getConfig().getBoolean("type." + this.name() + ".enabled");
    }

    public ListType getListType(Protection protection) {
        return ListType.fromString(protection.getConfig().getString("type." + this.name() + ".remover.list-type"));
    }

    public List<String> getList(Protection protection) {
        return protection.getConfig().getStringList("type." + this.name() + ".remover.list");
    }
}
