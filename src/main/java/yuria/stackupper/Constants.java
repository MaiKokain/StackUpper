package yuria.stackupper;

import java.text.DecimalFormat;
import java.util.function.Supplier;

public class Constants {
    Constants() {}
    public static final int BILLION = 1_000_000_000;
    public static final int MILLION = 1_000_000;
    public static final int THOUSAND = 1_000;
    public static final DecimalFormat    TOOLTIP_NUMBER_FORMAT                      = new DecimalFormat("###,###,###,###,###,###");

    public static final Supplier<Integer> maxIntSupplier = () -> Integer.MAX_VALUE;
}
