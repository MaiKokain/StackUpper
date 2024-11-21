package yuria.sul.ast;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import yuria.stackupper.Constants;
import yuria.stackupper.StackUpper;
import yuria.stackupper.StackUpperCommand;
import yuria.sul.StackUpperBaseListener;
import yuria.sul.StackUpperParser;
import yuria.sul.ast.item.ItemHandler;
import yuria.sul.ast.item.ItemProperty;
import yuria.sul.ast.item.UnfiliteredItemProperty;

import java.util.ArrayList;

public class SULangListener extends StackUpperBaseListener {
    public final ArrayList<UnfiliteredItemProperty> toBeProcessed = new ArrayList<>();

    @Override
    public void exitStart(StackUpperParser.StartContext ctx)
    {
        if (ctx.getStop().getType() == Token.EOF) {
            ItemHandler.processSpecialData(toBeProcessed);
        }
    }

    @Override
    public void exitIdStatement(StackUpperParser.IdStatementContext ctx)
    {
        StackUpperParser.ProgramStatementsContext parent = (StackUpperParser.ProgramStatementsContext) ctx.getParent();
        TerminalNode keyId = ctx.getToken(StackUpperParser.STRING, 0);
        String cleanKeyId = keyId.getText().replaceAll( "\"", "");
        AssignOperation assignOperation = AssignOperation.from(parent.assignOp().getText());
        Integer opNumber = Integer.valueOf(parent.NUMBER().getText());
        if (ctx.TILDE() != null)
        {
            cleanKeyId = cleanKeyId.replaceAll("\\*", ".*");
            toBeProcessed.add(new UnfiliteredItemProperty.ItemPropertyId(assignOperation, opNumber, cleanKeyId));
            return;
        }

        if (ResourceLocation.tryParse(cleanKeyId) == null && ctx.TILDE() == null)
        {
            StackUpper.LOGGER.error("{} failed to pass, if you're doing RegEx, use '~='", keyId);
            return;
        }

        StackUpper.itemCollection.put(
                new ItemProperty(
                        BuiltInRegistries.ITEM.get(ResourceLocation.parse(cleanKeyId)),
                        assignOperation,
                        opNumber
                )
        );
    }

    @Override
    public void exitTagStatement(StackUpperParser.TagStatementContext ctx)
    {
        StackUpperParser.ProgramStatementsContext programStatementsContext = (StackUpperParser.ProgramStatementsContext) ctx.getParent();
        String tagInput = ctx.STRING().getText().replaceAll("[\"#]", "");
        AssignOperation assignOperation = AssignOperation.from(programStatementsContext.assignOp().getText());
        Integer doOpBy = Integer.valueOf(programStatementsContext.NUMBER().getText());

        if (ctx.TILDE() != null) {
            tagInput = tagInput.replaceAll("\\*", ".*");
            toBeProcessed.add(new UnfiliteredItemProperty.TagItemProperty(assignOperation, doOpBy, tagInput, true));
            return;
        }

        if (ResourceLocation.tryParse(tagInput) == null) throw new IllegalArgumentException("tag: " + tagInput + " did not pass ResourceLocation parse! if you're using regex use '~='");


        toBeProcessed.add(new UnfiliteredItemProperty.TagItemProperty(assignOperation, doOpBy, tagInput));
    }

    @Override
    public void exitSizeStatement(StackUpperParser.SizeStatementContext ctx)
    {
        StackUpperParser.ProgramStatementsContext parent = (StackUpperParser.ProgramStatementsContext) ctx.getParent();
        AssignOperation assignOperation = AssignOperation.from(parent.assignOp().getText());
        ComparisonOperator comparisonOperation = ComparisonOperator.from(ctx.compareOp().getText());
        int compareBy = Integer.parseInt(ctx.NUMBER().getText());
        int assignBy = Integer.parseInt(parent.NUMBER().getText());

        StackUpper.LOGGER.info("[sizeStatement] {} {} {}", assignOperation.toString(), compareBy, comparisonOperation.toString());

        toBeProcessed.add(
                new UnfiliteredItemProperty.ItemPropertySize(
                        assignOperation,
                        assignBy,
                        comparisonOperation,
                        compareBy
                )
        );
    }
}
