package dev.shadowsoffire.apotheosis.adventure.affix.effect;

import com.mojang.serialization.Codec;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixType;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Reduces damage to durability.
 */
public class DurableAffix extends Affix {

    public static final Codec<DurableAffix> CODEC = Codec.unit(DurableAffix::new);

    public DurableAffix() {
        super(AffixType.DURABILITY);
    }

    @Override
    public boolean canApplyTo(ItemStack stack, LootCategory cat, LootRarity rarity) {
        return stack.isDamageableItem();
    }

    @Override
    public void addInformation(ItemStack stack, LootRarity rarity, float level, Consumer<Component> list) {
        super.addInformation(stack, rarity, level * 100, list);
    }

    @Override
    public float getDurabilityBonusPercentage(ItemStack stack, LootRarity rarity, float level, @Nullable ServerPlayer user) {
        return level;
    }

    @Override
    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }

    /**
     * A reduction that computes the diminishing return value of multiple durability bonuses.<br>
     * For this computation, the first bonus is applied in full, but further bonuses are only applied to the reduced value.
     *
     * @param result  The current result value.
     * @param element The next element.
     * @return The updated result, after applying the element.
     */
    public static double duraProd(double result, double element) {
        return result + (1 - result) * element;
    }

}
