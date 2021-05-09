package net.enderitemc.enderitetconstruct.init;

import net.enderitemc.enderitetconstruct.EnderiteTCAddon;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EnderiteTCAddon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            EnderiteTCAddon.MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> HARDENED_ENDERITE_BLOCK = BLOCKS.register("hardened_enderite_block",
            () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                    .sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().strength(66.0F, 1200.0F)
                    .harvestTool(ToolType.PICKAXE).harvestLevel(4)));

    public static final RegistryObject<FlowingFluidBlock> MOLTEN_ENDERITE = BLOCKS.register("molten_enderite_block",
            () -> new FlowingFluidBlock(FluidInit.MOLTEN_ENDERITE,
                    Block.Properties.of(Material.LAVA).lightLevel((state) -> {
                        return 11;
                    }).randomTicks().strength(100.0F).noDrops()));

    public static final RegistryObject<FlowingFluidBlock> MOLTEN_HARDENED_ENDERITE = BLOCKS
            .register("molten_hardened_enderite_block", () -> new FlowingFluidBlock(FluidInit.MOLTEN_HARDENED_ENDERITE,
                    Block.Properties.of(Material.LAVA).lightLevel((state) -> {
                        return 11;
                    }).randomTicks().strength(100.0F).noDrops()));

}
