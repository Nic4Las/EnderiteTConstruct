package net.enderitemc.enderitetconstruct.util;

import net.enderitemc.enderitetconstruct.init.ModifierInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ToolCore;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;

public class VoidFloatingModifier extends Modifier {
    public static final ResourceLocation VOID_FLOATING = Util.getResource("void_floating");

    public VoidFloatingModifier() {
        super(0x306850);

    }

    @Override
    public void addVolatileData(ToolDefinition toolDefinition, StatsNBT baseStats, IModDataReadOnly persistentData,
            int level, ModDataNBT volatileData) {
        // Apply indestructible entity
        volatileData.putBoolean(ToolCore.INDESTRUCTIBLE_ENTITY, true);

        int new_level = level;
        // Adjust level if not added Void Floating but Enderite Modifier is there
        if (volatileData.getBoolean(EnderiteModifier.ENDERITE)) {
            new_level += 1;
        }
        volatileData.putInt(VOID_FLOATING, new_level);
    }

    @Override
    public void onInventoryTick(IModifierToolStack tool, int level, World world, LivingEntity holder, int itemSlot,
            boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isSelected
                && tool.getItem()
                        .is(ItemTags.getAllTags()
                                .getTagOrEmpty(new ResourceLocation("enderitetconstruct", "tconstruct_swords")))
                && tool.getModifierLevel(ModifierInit.ENDERITE_SWORD.get()) == 0 && getHeldTool(holder) != null) {
            getHeldTool(holder).addModifier(ModifierInit.ENDERITE_SWORD.get(), 1);
            tool.getPersistentData().putInt(ModifierInit.ENDERITE_SWORD.get().getId(), 0);
        }
    }

    @Override
    public ITextComponent getDisplayName(IModifierToolStack tool, int level) {
        // Add Enderite Modifier
        int new_level = level;
        if (tool.getModifierLevel(ModifierInit.ENDERITE.get()) > 0) {
            new_level += 1;// > 3 ? 3 : level;
        }
        // Cap level
        new_level = new_level > 3 ? 3 : new_level;
        return getDisplayName(new_level);
    }

}
