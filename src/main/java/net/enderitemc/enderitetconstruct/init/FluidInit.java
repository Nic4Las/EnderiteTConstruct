package net.enderitemc.enderitetconstruct.init;

import net.enderitemc.enderitetconstruct.EnderiteTCAddon;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EnderiteTCAddon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FluidInit {

        // Fluids textures
        public static final ResourceLocation MOLTEN_ENDERITE_STILL = new ResourceLocation(EnderiteTCAddon.MOD_ID,
                        "blocks/fluids/molten_enderite_still");
        public static final ResourceLocation MOLTEN_ENDERITE_FLOW = new ResourceLocation(EnderiteTCAddon.MOD_ID,
                        "blocks/fluids/molten_enderite_flow");

        public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
                        EnderiteTCAddon.MOD_ID);

        public static void init() {
                FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        // Fluids
        public static final RegistryObject<ForgeFlowingFluid.Source> MOLTEN_ENDERITE = FLUIDS
                        .register("molten_enderite", () -> new ForgeFlowingFluid.Source(makeEnderiteProperties()));
        public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_MOLTEN_ENDERITE = FLUIDS.register(
                        "flowing_molten_enderite", () -> new ForgeFlowingFluid.Flowing(makeEnderiteProperties()));

        public static final RegistryObject<ForgeFlowingFluid.Source> MOLTEN_HARDENED_ENDERITE = FLUIDS.register(
                        "molten_hardened_enderite",
                        () -> new ForgeFlowingFluid.Source(makeHardenedEnderiteProperties()));
        public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_MOLTEN_HARDENED_ENDERITE = FLUIDS
                        .register("flowing_molten_hardened_enderite",
                                        () -> new ForgeFlowingFluid.Flowing(makeHardenedEnderiteProperties()));

        // Fluid materials
        private static ForgeFlowingFluid.Properties makeEnderiteProperties() {
                return new ForgeFlowingFluid.Properties(MOLTEN_ENDERITE, FLOWING_MOLTEN_ENDERITE,
                                FluidAttributes.builder(MOLTEN_ENDERITE_STILL, MOLTEN_ENDERITE_FLOW)
                                                .overlay(MOLTEN_ENDERITE_STILL).color(0xFF306850).luminosity(15)
                                                .density(3000).viscosity(6000).temperature(1200)
                                                .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA))
                                                                .bucket(ItemInit.MOLTEN_ENDERITE_BUCKET)
                                                                .block(BlockInit.MOLTEN_ENDERITE)
                                                                .explosionResistance(1000F).tickRate(32)
                                                                .levelDecreasePerBlock(3);
        }

        private static ForgeFlowingFluid.Properties makeHardenedEnderiteProperties() {
                return new ForgeFlowingFluid.Properties(MOLTEN_HARDENED_ENDERITE, FLOWING_MOLTEN_HARDENED_ENDERITE,
                                FluidAttributes.builder(MOLTEN_ENDERITE_STILL, MOLTEN_ENDERITE_FLOW)
                                                .overlay(MOLTEN_ENDERITE_STILL).color(0xFF1f4352).luminosity(15)
                                                .density(3000).viscosity(6000).temperature(1200)
                                                .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA))
                                                                .bucket(ItemInit.MOLTEN_HARDENED_ENDERITE_BUCKET)
                                                                .block(BlockInit.MOLTEN_HARDENED_ENDERITE)
                                                                .explosionResistance(1000F).tickRate(32)
                                                                .levelDecreasePerBlock(3);
        }
}
