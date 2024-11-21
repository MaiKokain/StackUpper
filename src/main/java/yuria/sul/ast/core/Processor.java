package yuria.sul.ast.core;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import yuria.stackupper.StackUpper;
import yuria.sul.StackUpperLexer;
import yuria.sul.StackUpperParser;
import yuria.sul.ast.SULangListener;
import yuria.sul.ast.logger.SULangLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Processor {
    private static final ArrayList<Path> filesToRead = new ArrayList<>();
    public static SULangListener listener = null;

    public static void processFileToArray(File folder)
    {
        if (!filesToRead.isEmpty()) filesToRead.clear();
        if (!folder.isDirectory()) throw new RuntimeException(String.format("%s is expected to be a folder!", folder.getAbsolutePath()));
        for (File file : Objects.requireNonNull(folder.listFiles((t, d) -> d.endsWith(".su") || d.endsWith(".stackupper")))) {
            filesToRead.add(Path.of(file.getAbsolutePath()));
        }
    }

    public Processor(File folder)
    {
        processFileToArray(folder);
    }

    public void compile(String input)
    {
        var lexer = new StackUpperLexer(CharStreams.fromString(input));
        var tokens = new CommonTokenStream(lexer);
        var parser = new StackUpperParser(tokens);
        var ctx = parser.start();
        var listener = new SULangListener();

        ParseTreeWalker.DEFAULT.walk(listener, ctx);
    }

    public void compile(Path path)
    {
        try {
            StackUpper.LOGGER.info("parsing {}", path.getFileName());
            var lexer = new StackUpperLexer(CharStreams.fromPath(path));
            var tokens = new CommonTokenStream(lexer);
            var parser = new StackUpperParser(tokens);
            listener = new SULangListener();

            lexer.removeErrorListeners();
            parser.removeErrorListeners();

            List<String> errorList = new ArrayList<>();
            SULangLogger errorListener = new SULangLogger(errorList);

            lexer.addErrorListener(errorListener);
            parser.addErrorListener(errorListener);

            var ctx = parser.start();

            ParseTreeWalker.DEFAULT.walk(listener, ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        for (Path file : filesToRead) {
            compile(file);
        }
    }
}
