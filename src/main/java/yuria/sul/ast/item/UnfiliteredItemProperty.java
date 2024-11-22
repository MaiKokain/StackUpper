package yuria.sul.ast.item;

import yuria.sul.ast.AssignOperation;
import yuria.sul.ast.ComparisonOperator;

public class UnfiliteredItemProperty {
    public final AssignOperation assignOperation;
    public final long assingBy;

    public UnfiliteredItemProperty(AssignOperation assignOperation, long assingBy)
    {
        this.assignOperation = assignOperation;
        this.assingBy = assingBy;
    }

    public static class ItemPropertySize extends UnfiliteredItemProperty {
        public final ComparisonOperator comparisonOperator;
        public final int compareBy;

        public ItemPropertySize(AssignOperation assignOperation, long assingBy, ComparisonOperator comparisonOperator, int compareBy) {
            super(assignOperation, assingBy);
            this.comparisonOperator = comparisonOperator;
            this.compareBy = compareBy;
        }
    }

    public static class ItemPropertyId extends UnfiliteredItemProperty {
        public final String toMatch;
        public ItemPropertyId(AssignOperation assignOperation, long assingBy, String toMatch) {
            super(assignOperation, assingBy);
            this.toMatch = toMatch;
        }
    }

    public static class TagItemProperty extends UnfiliteredItemProperty {
        public final String tag;
        public boolean isRegex = false;

        public TagItemProperty(AssignOperation assignOperation, long assingBy, String tag, boolean isRegex) {
            super(assignOperation, assingBy);
            this.tag = tag;
            this.isRegex = isRegex;
        }
        public TagItemProperty(AssignOperation assignOperation, long assingBy, String tag) {
            super(assignOperation, assingBy);
            this.tag = tag;
        }
    }
}
