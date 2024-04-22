package fr.k0bus.creativemanager2.type;

public enum ListType {
    BLACKLIST(true),
    WHITELIST(false);

    private final boolean blacklistMode;

    ListType(boolean value)
    {
        this.blacklistMode = value;
    }

    public static ListType fromString(String s)
    {
        if(s == null) return BLACKLIST;
        for(ListType type:ListType.values())
        {
            if(type.name().toLowerCase().equals(s.toLowerCase())) return type;
        }
        return BLACKLIST;
    }

    public boolean isBlacklistMode() {
        return blacklistMode;
    }
}
