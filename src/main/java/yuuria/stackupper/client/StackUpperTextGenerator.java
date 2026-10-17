package yuuria.stackupper.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.MathHelper;
import yuuria.stackupper.StackUpper;

import java.util.Locale;

public final class StackUpperTextGenerator {
    private StackUpperTextGenerator() {}

    public static final class AbbreviationResult {
        private final String text;
        private final float scaleFactor;
        private final boolean fits;
        private final boolean abbreviated;

        public AbbreviationResult(String text, float scaleFactor, boolean fits, boolean abbreviated) {
            this.text = text;
            this.scaleFactor = scaleFactor;
            this.fits = fits;
            this.abbreviated = abbreviated;
        }

        public String getText()          { return text; }
        public float getScaleFactor()    { return scaleFactor; }
        public boolean isFits()          { return fits; }
        public boolean isAbbreviated()   { return abbreviated; }

        private AbbreviationResult withPrefix(String prefix) {
            if (prefix.isEmpty()) return this;
            return new AbbreviationResult(prefix + text, scaleFactor, fits, abbreviated);
        }
    }

    public static AbbreviationResult abbreviate(FontRenderer fr, String count, int maxWidth, boolean justCheckAbbreviation) {
        if (count == null || count.isEmpty()) {
            return new AbbreviationResult("", 1f, true, false);
        }

        int i = 0;
        while (i + 1 < count.length() && count.charAt(i) == '\u00A7') {
            i += 2;
        }
        String prefix = count.substring(0, i);
        String body = count.substring(i);

        AbbreviationResult result = abbreviateInner(fr, body, maxWidth, justCheckAbbreviation);
        return result.withPrefix(prefix);
    }

    public static int getStringLenWithoutFmtCodes(String count) {
        if (count == null) return 0;
        int i = 0;
        while (i + 1 < count.length() && count.charAt(i) == '\u00A7') {
            i += 2;
        }
        return count.length() - i;
    }

    private static AbbreviationResult abbreviateInner(FontRenderer fr, String count, int maxWidth, boolean justCheckAbbreviation) {
        int countI;
        try {
            countI = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            countI = -1;
        }

        // Don't handle negative or invalid numbers.
        if (countI < 0) {
            return tryFitString(fr, maxWidth, count, count, false);
        }

        int maxScale = StackUpper.proxy.getCurrentScaleFactor();
        String padded = Integer.toString(paddedFloor(countI));

        AbbreviationResult result = tryFitString(fr, maxWidth, padded, count, false);
        if (result.isFits()) return result;

        String longPadded = abbreviateLong(paddedFloor(countI));
        String longCount  = abbreviateLong(countI);
        result = tryFitString(fr, maxWidth, longPadded, longCount, true);
        if (result.isFits()) return result;

        String shortPadded = abbreviateShort(paddedFloor(countI));
        String shortCount  = abbreviateShort(countI);
        return tryFitString(fr, maxWidth, shortPadded, shortCount, true);
    }

    private static int paddedFloor(int value) {
        if (value == 0) return 0;
        int pow10 = 1;
        int tmp = value;
        while (tmp >= 10) {
            tmp /= 10;
            pow10 *= 10;
        }
        return tmp * pow10;
    }

    private static AbbreviationResult tryFitString(FontRenderer fr, int maxWidth, String comparedText, String text, boolean abbreviated) {
        int strWidth = fr.getStringWidth(text);
        if (!comparedText.equals(text)) {
            strWidth = Math.max(strWidth, fr.getStringWidth(comparedText));
        }

        if (StackUpper.CONFIG.isScaleTextLinearly()) {
            return fitLinear(maxWidth, strWidth, text, abbreviated);
        }
        return fitDiscrete(maxWidth, strWidth, text, abbreviated);
    }

    private static AbbreviationResult fitLinear(int maxWidth, int strWidth, String text, boolean abbreviated) {
        float scale = (float) maxWidth / strWidth;
        boolean fits = true;

        if (scale > StackUpper.CONFIG.getHighestScaleDown()) {
            scale = StackUpper.CONFIG.getHighestScaleDown();
        } else if (scale < StackUpper.CONFIG.getLowestScaleDown()) {
            scale = StackUpper.CONFIG.getLowestScaleDown();
            fits = false;
        }
        return new AbbreviationResult(text, scale, fits, abbreviated);
    }

    private static AbbreviationResult fitDiscrete(int maxWidth, int strWidth, String text, boolean abbreviated) {
        int maxScale = StackUpper.proxy.getCurrentScaleFactor();
        int minStep = MathHelper.clamp(Math.round(StackUpper.CONFIG.getLowestScaleDown()  * maxScale), 1, maxScale);
        int maxStep = MathHelper.clamp(Math.round(StackUpper.CONFIG.getHighestScaleDown() * maxScale), 1, maxScale);

        for (int step = maxStep; step >= minStep; step--) {
            float scale = (float) step / maxScale;
            if (scale * strWidth <= maxWidth) {
                return new AbbreviationResult(text, scale, true, abbreviated);
            }
        }

        float scale = (float) minStep / maxScale;
        return new AbbreviationResult(text, scale, scale * strWidth <= maxWidth, abbreviated);
    }

    private static String abbreviateShort(int count) {
        if (count < 1000)        return Integer.toString(count);
        if (count < 100_000)     return (count / 1_000) + "K";
        if (count < 1_000_000)   return "." + (count / 100_000) + "M";
        if (count < 100_000_000) return (count / 1_000_000) + "M";
        if (count < 1_000_000_000) return "." + (count / 100_000_000) + "B";
        return (count / 1_000_000_000) + "B";
    }

    private static String abbreviateLong(int count) {
        if (count < 100_000)          return Integer.toString(count);
        if (count < 1_000_000)        return (count / 1_000) + "K";
        if (count < 10_000_000)       return fixed((count / 10_000), 2) + "M";
        if (count < 100_000_000)      return fixed((count / 100_000), 1) + "M";
        if (count < 1_000_000_000)    return (count / 1_000_000) + "M";
        return fixed((count / 10_000_000), 2) + "B";
    }

    private static String fixed(int scaled, int decimals) {
        int divisor = (int) Math.pow(10, decimals);
        int whole = scaled / divisor;
        int frac  = scaled % divisor;
        if (decimals == 1) return whole + "." + frac;
        return whole + "." + String.format(Locale.ROOT, "%0" + decimals + "d", frac);
    }
}