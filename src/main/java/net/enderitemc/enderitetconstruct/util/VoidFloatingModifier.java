package net.enderitemc.enderitetconstruct.util;

import java.util.Iterator;

import net.enderitemc.enderitetconstruct.init.ModifierInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.tinkerstation.ValidatedResult;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ToolCore;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

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
        if ((!persistentData.getBoolean(EnderiteModifier.ADDED_VOID_FLOATING))
                && volatileData.getBoolean(EnderiteModifier.ENDERITE)) {
            new_level += 1;
        }
        volatileData.putInt(VOID_FLOATING, new_level);
    }

    // @Override
    // public void onInventoryTick(IModifierToolStack tool, int level, World world,
    // LivingEntity holder, int itemSlot,
    // boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
    // if (tool.getItem().is(
    // ItemTags.getAllTags().getTagOrEmpty(new
    // ResourceLocation("enderitetconstruct", "tconstruct_swords")))
    // && tool.getModifierLevel(ModifierInit.ENDERITE_SWORD.get()) == 0) {
    // ToolStack ts = ToolStack.copyFrom(stack);
    // ts.addModifier(ModifierInit.ENDERITE_SWORD.get(), 1);
    // Iterator<ModifierEntry> it = ts.getModifierList().iterator();
    // while (it.hasNext()) {
    // if (it.next().getModifier().getId().getPath() == "enderite_sword") {
    // ts.getPersistentData().putInt(ModifierInit.ENDERITE_SWORD.get().getId(), 0);
    // }
    // }
    // ts.updateStack(stack);

    // }
    // }

    @Override
    public ValidatedResult validate(ToolStack tool, int level) {
        if (tool.getItem().is(
                ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("enderitetconstruct", "tconstruct_swords")))
                && tool.getModifierLevel(ModifierInit.ENDERITE_SWORD.get()) == 0) {
            tool.addModifier(ModifierInit.ENDERITE_SWORD.get(), 1);
            Iterator<ModifierEntry> it = tool.getModifierList().iterator();
            while (it.hasNext()) {
                if (it.next().getModifier().getId().getPath() == "enderite_sword") {
                    tool.getPersistentData().putInt(ModifierInit.ENDERITE_SWORD.get().getId(), 0);
                }
            }
        }
        return ValidatedResult.PASS;
    }

    @Override
    public ITextComponent getDisplayName(IModifierToolStack tool, int level) {
        // Add Enderite Modifier
        int new_level = level;
        if (!tool.getPersistentData().getBoolean(EnderiteModifier.ADDED_VOID_FLOATING)) {
            new_level += tool.getModifierLevel(ModifierInit.ENDERITE.get());// > 3 ? 3 : level;
        }
        // Cap level
        new_level = new_level > 3 ? 3 : new_level;
        return getDisplayName(new_level);
    }

}
