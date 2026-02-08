package io.github.techtastic.inlinelatex;

import com.samsthenerd.inline.api.InlineAPI;
import com.samsthenerd.inline.api.client.InlineClientAPI;
import com.samsthenerd.inline.api.data.SpriteInlineData;
import com.samsthenerd.inline.api.matching.*;
import com.samsthenerd.inline.utils.URLSprite;
import net.minecraft.resources.ResourceLocation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class InlineLaTeX {
    public static final String MOD_ID = "inlinelatex";
    private static final Pattern LATEX_PATTERN = Pattern.compile("\\[(?:tex|latex|formula):([^]]+)]");
    private static final String LATEX_API_URL = "https://latex.codecogs.com/png.image?\\dpi{200}";
    private static final RegexMatcher MATCHER = new RegexMatcher.Simple(
            LATEX_PATTERN,
            of("latex"),
            matchResult -> new InlineMatch.DataMatch(new SpriteInlineData(new URLSprite(URLEncoder.encode(LATEX_API_URL + matchResult.group(1), StandardCharsets.UTF_8), of("latex")))),
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
}
