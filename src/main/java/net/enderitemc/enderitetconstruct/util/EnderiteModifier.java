package net.enderitemc.enderitetconstruct.util;

import net.enderitemc.enderitetconstruct.init.ModifierInit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants.NBT;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.tools.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
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
        // If no Void Floating Modifier, add void floating volatile data
        if (!volatileData.contains(VoidFloatingModifier.VOID_FLOATING, NBT.TAG_INT)
                || volatileData.getInt(VoidFloatingModifier.VOID_FLOATING) < 0) {
            volatileData.putInt(VoidFloatingModifier.VOID_FLOATING, 1);
        }
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        return getDisplayName();
    }

    @Override
    public ITextComponent getDisplayName(IModifierToolStack tool, int level) {
        // Search for void floating
        if (tool.getModifierLevel(ModifierInit.VOID_FLOATING.get()) > 0) {
            return getDisplayName(level);
        }

        // If there is no void floating, rename to "Enderite + Void Floating"
        TranslationTextComponent t = new TranslationTextComponent("modifier.enderitetconstruct.void_floating");
        return getDisplayName(level).copy().append(" + ").append(t);
        // .withStyle(style -> style.withColor(Color.fromRgb(getColor())));
    }
}
