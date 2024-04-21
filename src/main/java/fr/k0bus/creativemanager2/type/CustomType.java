package fr.k0bus.creativemanager2.type;

public enum CustomType {
    COMMANDS("commands"),
    PLACE("place"),
    BREAK("break"),
    ITEMUSE("itemuse"),
    BLOCKUSE("blockuse"),
    STOREITEM("storeitem"),
    NBT("nbt");

    private String id;

    CustomType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
