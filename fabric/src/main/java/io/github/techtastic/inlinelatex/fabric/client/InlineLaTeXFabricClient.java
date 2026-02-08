package io.github.techtastic.inlinelatex.fabric.client;

import io.github.techtastic.inlinelatex.InlineLaTeX;
import net.fabricmc.api.ClientModInitializer;

public final class InlineLaTeXFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InlineLaTeX.initClient();
    }
}
