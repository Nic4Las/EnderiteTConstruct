package net.enderitemc.enderitetconstruct.init;

import net.enderitemc.enderitetconstruct.EnderiteTCAddon;
import net.enderitemc.enderitetconstruct.util.EnderiteModifier;
import net.enderitemc.enderitetconstruct.util.EnderiteSwordModifier;
import net.enderitemc.enderitetconstruct.util.VoidFloatingModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import slimeknights.tconstruct.library.modifiers.Modifier;

@Mod.EventBusSubscriber(modid = EnderiteTCAddon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModifierInit {
        public static final DeferredRegister<Modifier> MODIFIERS = DeferredRegister.create(Modifier.class,
                        EnderiteTCAddon.MOD_ID);

        public static void init() {
                MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<Modifier> ENDERITE = MODIFIERS.register("enderite", EnderiteModifier::new);
        public static final RegistryObject<Modifier> VOID_FLOATING = MODIFIERS.register("void_floating",
                        VoidFloatingModifier::new);
        public static final RegistryObject<Modifier> ENDERITE_SWORD = MODIFIERS.register("enderite_sword",
                        EnderiteSwordModifier::new);

}