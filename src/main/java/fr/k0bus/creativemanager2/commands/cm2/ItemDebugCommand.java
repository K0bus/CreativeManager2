package fr.k0bus.creativemanager2.commands.cm2;

import de.tr7zw.nbtapi.NBT;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.SubCommands;
import fr.k0bus.creativemanager2.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

public class ItemDebugCommand extends SubCommands {
    public ItemDebugCommand() {
        super("itemdebug", "cm2.admin.itemdebug", Player.class);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        execute(sender);
    }

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().equals(Material.AIR)) return;

        addEnchant(itemStack);
        addNBT(itemStack);
        addNMS(itemStack);
        addPotionEffect(itemStack);
        addItemFlag(itemStack);

        MessageUtils.sendRawMessage(sender, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        MessageUtils.sendRawMessage(sender, " &6You've added debug infos on your items !");
        MessageUtils.sendRawMessage(sender, "   &7> &f&lEnchantment");
        MessageUtils.sendRawMessage(sender, "   &7> &f&lNBT");
        MessageUtils.sendRawMessage(sender, "   &7> &f&lNMS");
        MessageUtils.sendRawMessage(sender, "   &7> &f&lPotionEffect");
        MessageUtils.sendRawMessage(sender, "   &7> &f&lItemFlag");
        MessageUtils.sendRawMessage(sender, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    private void addEnchant(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.MENDING, Enchantment.MENDING.getMaxLevel());
        itemStack.addUnsafeEnchantment(Enchantment.UNBREAKING, Enchantment.UNBREAKING.getMaxLevel() * 2);
    }

    private void addItemFlag(ItemStack itemStack) {
        for (ItemFlag itemFlag : ItemFlag.values()) itemStack.addItemFlags(itemFlag);
    }

    private void addNBT(ItemStack itemStack) {
        NBT.modify(itemStack, nbt -> {
            nbt.setBoolean("test-boolean", true);
            nbt.setDouble("test-double", 2.0);
            nbt.setString("test-string", "test");
            nbt.setInteger("test-int", 2);
        });
    }

    private void addPotionEffect(ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof PotionMeta potionMeta) {
            potionMeta.addCustomEffect(PotionEffectType.ABSORPTION.createEffect(10, 100), true);
            potionMeta.addCustomEffect(PotionEffectType.REGENERATION.createEffect(10, 100), true);
        }
    }

    private void addNMS(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(CreativeManager2.getAPI().getInstance(), "test-nms-string"), PersistentDataType.STRING, "Test");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(CreativeManager2.getAPI().getInstance(), "test-nms-int"), PersistentDataType.INTEGER, 2);
        itemStack.setItemMeta(itemMeta);
    }
}
