package io.github.techtastic.patchematical.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import io.github.techtastic.patchematical.Patchematical;

@Mod(Patchematical.MOD_ID)
public final class PatchematicalForge {
    public PatchematicalForge(FMLJavaModLoadingContext context) {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Patchematical.MOD_ID, context.getModEventBus());

        // Run our common setup.
        Patchematical.init();
    }
}
