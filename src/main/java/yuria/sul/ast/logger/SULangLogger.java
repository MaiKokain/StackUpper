package yuria.sul.ast.logger;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import yuria.stackupper.StackUpper;

import java.util.List;

public class SULangLogger extends BaseErrorListener {
    public final List<String> errors;

    public SULangLogger(List<String> errors)
    {
        this.errors = errors;
    }

    @Override
    public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPos, String msg, RecognitionException e)
    {
        StackUpper.LOGGER.error("[StackUpperAST] line " + line + ":" + charPos + " " + msg);
        errors.add("line " + line + ":" + charPos + " " + msg);
    }
}
