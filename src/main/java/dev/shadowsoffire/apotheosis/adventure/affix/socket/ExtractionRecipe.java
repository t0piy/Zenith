package dev.shadowsoffire.apotheosis.adventure.affix.socket;

import com.google.gson.JsonObject;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.adventure.Adventure.Items;
import dev.shadowsoffire.apotheosis.adventure.AdventureModule.ApothSmithingRecipe;
import dev.shadowsoffire.apotheosis.adventure.affix.socket.gem.GemItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ExtractionRecipe extends ApothSmithingRecipe implements ReactiveSmithingRecipe {

    private static final ResourceLocation ID = Apotheosis.loc("extraction");

    public ExtractionRecipe() {
        super(ID, Ingredient.EMPTY, Ingredient.of(Items.VIAL_OF_EXTRACTION), ItemStack.EMPTY);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(Container pInv, Level pLevel) {
        List<ItemStack> sockets = SocketHelper.getGems(pInv.getItem(BASE));
        return pInv.getItem(ADDITION).getItem() == Items.VIAL_OF_EXTRACTION && !sockets.isEmpty() && !sockets.get(0).isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack assemble(Container pInv, RegistryAccess regs) {
        ItemStack base = pInv.getItem(BASE);
        ItemStack out = SocketHelper.getGems(base).get(0);
        out.removeTagKey(GemItem.UUID_ARRAY);
        return out;
    }

    @Override
    public void onCraft(Container inv, Player player, ItemStack output) {
        ItemStack base = inv.getItem(BASE);
        List<ItemStack> gems = SocketHelper.getGems(base);
        for (int i = 1; i < gems.size(); i++) {
            ItemStack stack = gems.get(i);
            if (!stack.isEmpty()) {
                stack.removeTagKey(GemItem.UUID_ARRAY);
                if (!player.addItem(stack)) Block.popResource(player.level(), player.blockPosition(), stack);
            }
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<ExtractionRecipe> {

        public static Serializer INSTANCE = new Serializer();

        @Override
        public ExtractionRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            return new ExtractionRecipe();
        }

        @Override
        public ExtractionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new ExtractionRecipe();
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ExtractionRecipe pRecipe) {

        }
    }

}
