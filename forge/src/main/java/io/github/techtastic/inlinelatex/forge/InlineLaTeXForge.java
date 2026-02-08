package io.github.techtastic.inlinelatex.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import io.github.techtastic.inlinelatex.InlineLaTeX;

@Mod(InlineLaTeX.MOD_ID)
public final class InlineLaTeXForge {
    public InlineLaTeXForge(FMLJavaModLoadingContext context) {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(InlineLaTeX.MOD_ID, context.getModEventBus());

        // Run our common setup.
        InlineLaTeX.init();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> InlineLaTeX::initClient);
    }
}
