package io.github.techtastic.inlinelatex;

import com.samsthenerd.inline.api.InlineAPI;
import com.samsthenerd.inline.api.client.InlineClientAPI;
import com.samsthenerd.inline.api.data.SpriteInlineData;
import com.samsthenerd.inline.api.matching.*;
import com.samsthenerd.inline.impl.InlineStyle;
import com.samsthenerd.inline.utils.SpriteUVRegion;
import com.samsthenerd.inline.utils.URLSprite;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class InlineLaTeX {
    public static final String MOD_ID = "inlinelatex";
    private static final Pattern LATEX_PATTERN = Pattern.compile("\\[(?:tex|latex|formula)([:;,!+])([^]]+)]");
    private static final String LATEX_API_URL = "https://latex.codecogs.com/png.latex?";
    private static final RegexMatcher MATCHER = new RegexMatcher.Simple(
            LATEX_PATTERN,
            of("latex"),
            matchResult -> new InlineMatch.DataMatch(
                    new SpriteInlineData(
                            new URLSprite(
                                    toURL(matchResult.group(2)),
                                    of("latex/" + matchResult.group(2).hashCode())
                            ), true
                    ), InlineAPI.INSTANCE.withSizeModifier(Style.EMPTY, getSize(matchResult.group(1)))
            ),
            MatcherInfo.fromId(of("latex"))
    );

    public static void init() {
        InlineAPI.INSTANCE.addChatMatcher(MATCHER);
    }

    public static void initClient() {
        InlineClientAPI.INSTANCE.addMatcher(MATCHER);
    }

    public static ResourceLocation of(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String toURL(String formula) {
        return LATEX_API_URL + URLEncoder.encode("\\dpi{300}\\fg{FFFFFF}\\\\%s".formatted(formula), StandardCharsets.UTF_8);
    }

    public static double getSize(String delimiter) {
        return switch (delimiter) {
            case "," -> 0.75;
            case "!" -> 1.5;
            case "+" -> 2.0;
            default -> 1.0;
        };
    }
}
