package io.github.techtastic.inlinelatex.fabric;

import net.fabricmc.api.ModInitializer;

import io.github.techtastic.inlinelatex.InlineLaTeX;

public final class InlineLaTeXFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        InlineLaTeX.init();
    }
}
