package app.prismarine.server;

import com.google.common.collect.Multimap;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class PrismarineUnsafeValues implements UnsafeValues {

    @Override
    public Material toLegacy(Material material) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Material fromLegacy(Material material) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Material getMaterial(String material, int version) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public int getDataVersion() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        // todo check something with the plugin api version
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        return clazz; // todo wtf else am i meant to do?
    }

    /**
     * Load an advancement represented by the specified string into the server.
     * The advancement format is governed by Minecraft and has no specified
     * layout.
     * <br>
     * It is currently a JSON object, as described by the <a href="https://minecraft.wiki/w/Advancements">Minecraft wiki</a>.
     * <br>
     * Loaded advancements will be stored and persisted across server restarts
     * and reloads.
     * <br>
     * Callers should be prepared for {@link Exception} to be thrown.
     *
     * @param key         the unique advancement key
     * @param advancement representation of the advancement
     * @return the loaded advancement or null if an error occurred
     */
    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Delete an advancement which was loaded and saved by
     * {@link #loadAdvancement(NamespacedKey, String)}.
     * <br>
     * This method will only remove advancement from persistent storage. It
     * should be accompanied by a call to {@link Server#reloadData()} in order
     * to fully remove it from the running instance.
     *
     * @param key the unique advancement key
     * @return true if a file matching this key was found and deleted
     */
    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot slot) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public CreativeCategory getCreativeCategory(Material material) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String getBlockTranslationKey(Material material) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String getItemTranslationKey(Material material) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String getTranslationKey(EntityType entityType) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String getTranslationKey(Attribute attribute) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public @Nullable FeatureFlag getFeatureFlag(@NotNull NamespacedKey key) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Do not use, method will get removed, and the plugin won't run
     *
     * @param key of the potion type
     * @return an internal potion data
     */
    @Override
    public PotionType.InternalPotionData getInternalPotionData(NamespacedKey key) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public @Nullable DamageEffect getDamageEffect(@NotNull String key) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Create a new {@link DamageSource.Builder}.
     *
     * @param damageType the {@link DamageType} to use
     * @return a {@link DamageSource.Builder}
     */
    @Override
    public @NotNull DamageSource.Builder createDamageSourceBuilder(@NotNull DamageType damageType) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String get(Class<?> aClass, String value) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public <B extends Keyed> B get(Registry<B> registry, NamespacedKey key) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
