package net.enderitemc.enderitetconstruct.util;

import java.util.Iterator;

import net.enderitemc.enderitetconstruct.init.ModifierInit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.tinkerstation.ValidatedResult;
import slimeknights.tconstruct.library.tools.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.modifiers.free.WorldboundModifier;

public class EnderiteModifier extends WorldboundModifier {
    public static final ResourceLocation ADDED_VOID_FLOATING = Util.getResource("added_void_floating");
    public static final ResourceLocation ENDERITE = Util.getResource("enderite");

    public EnderiteModifier() {
        super(0x306850);
    }

    @Override
    public void addToolStats(ToolDefinition toolDefinition, StatsNBT baseStats, IModDataReadOnly persistentData,
            IModDataReadOnly volatileData, int level, ModifierStatsBuilder builder) {
        builder.multiplyDurability(1.25f);
        builder.multiplyAttackSpeed(1.25f);
        builder.multiplyMiningSpeed(1.25f);
        builder.setHarvestLevel(5);
    }

    @Override
    public void addVolatileData(ToolDefinition toolDefinition, StatsNBT baseStats, IModDataReadOnly persistentData,
            int level, ModDataNBT volatileData) {
        super.addVolatileData(toolDefinition, baseStats, persistentData, level, volatileData);
        volatileData.putBoolean(ENDERITE, true);
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        return getDisplayName();
    }

    // @Override
    // public ITextComponent getDisplayName(IModifierToolStack tool, int level) {
    // // Search for void floating
    // Iterator<ModifierEntry> it = tool.getModifierList().iterator();
    // while (it.hasNext()) {
    // if (it.next().getModifier().getId().getPath() == "void_floating") {
    // return getDisplayName(level);
    // }
    // }

    // // If there is no void floating, rename to "Enderite + Void Floating"
    // TranslationTextComponent t = new
    // TranslationTextComponent("modifier.enderitetconstruct.void_floating");
    // return getDisplayName(level).copy().append(" + ").append(t);
    // // .withStyle(style -> style.withColor(Color.fromRgb(getColor())));
    // }

    @Override
    public ValidatedResult validate(ToolStack tool, int level) {
        if (tool.getModifierLevel(ModifierInit.VOID_FLOATING.get()) == 0) {
            tool.getPersistentData().putBoolean(ADDED_VOID_FLOATING, true);
            tool.addModifier(ModifierInit.VOID_FLOATING.get(), 1);
        }
        return ValidatedResult.PASS;
    }
}
