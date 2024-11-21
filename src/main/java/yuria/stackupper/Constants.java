package yuria.stackupper;

import net.neoforged.fml.loading.FMLPaths;
import yuria.sul.ast.core.Processor;
import yuria.sul.ast.item.ItemCollection;

import java.io.File;
import java.text.DecimalFormat;

public class Constants {
    public static final int BILLION = 1_000_000_000;
    public static final int MILLION = 1_000_000;
    public static final int THOUSAND = 1_000;
    public static final DecimalFormat    TOOLTIP_NUMBER_FORMAT                      = new DecimalFormat("###,###,###,###,###,###");


    public static final String MODID = "stackupper";
    public static final ItemCollection itemCollection = new ItemCollection();
    public static final File StackUpperLangFolder = new File(FMLPaths.CONFIGDIR.get().toFile().getAbsolutePath(), "stackupper");
    public static final Processor astProccessor = new Processor(StackUpperLangFolder);
}
