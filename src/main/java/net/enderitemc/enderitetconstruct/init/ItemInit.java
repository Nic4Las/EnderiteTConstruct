package net.enderitemc.enderitetconstruct.init;

import net.enderitemc.enderitetconstruct.EnderiteTCAddon;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EnderiteTCAddon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            EnderiteTCAddon.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> HARDENED_ENDERITE_INGOT = ITEMS.register("hardened_enderite_ingot",
            () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC).fireResistant()));

    public static final RegistryObject<Item> MOLTEN_ENDERITE_BUCKET = ITEMS.register("molten_enderite_bucket",
            () -> new BucketItem(FluidInit.MOLTEN_ENDERITE,
                    new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> MOLTEN_HARDENED_ENDERITE_BUCKET = ITEMS
            .register("molten_hardened_enderite_bucket", () -> new BucketItem(FluidInit.MOLTEN_HARDENED_ENDERITE,
                    new BucketItem.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<BlockItem> UVAROVITE_ORE = ITEMS.register("hardened_enderite_block",
            () -> new BlockItem(BlockInit.HARDENED_ENDERITE_BLOCK.get(),
                    new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
}
