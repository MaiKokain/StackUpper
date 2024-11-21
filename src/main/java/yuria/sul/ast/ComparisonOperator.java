package yuria.sul.ast;

import java.util.Locale;

public enum ComparisonOperator {
    EQUAL {
        @Override
        public boolean test(int a, int b) {
            return a == b;
        }
    },
    NOT_EQUAL {
        @Override
        public boolean test(int a, int b) {
            return a != b;
        }
    },
    GREATER {
        @Override
        public boolean test(int a, int b) {
            return a > b;
        }
    },
    GREATER_EQUAL {
        @Override
        public boolean test(int a, int b) {
            return a >= b;
        }
    },
    LESSER {
        @Override
        public boolean test(int a, int b) {
            return a < b;
        }
    },
    LESSER_EQUAL {
        @Override
        public boolean test(int a, int b) {
            return a <= b;
        }
    };
    public boolean test(int a, int b) {return false;}

    public static ComparisonOperator from(String text)
    {
        return switch (text.toUpperCase(Locale.ROOT))
        {
            case "EQUAL", "=" -> EQUAL;
            case "OP_NE", "!=" -> NOT_EQUAL;
            case "OP_GT", ">" -> GREATER;
            case "OP_GT_EQ", ">=" -> GREATER_EQUAL;
            case "OP_LT", "<" -> LESSER;
            case "OP_LT_EQ", "<=" -> LESSER_EQUAL;
            default -> throw new IllegalArgumentException("What the hell is this comparison operation: " + text);
        };
    }

    public static String toString(ComparisonOperator operator)
    {
        return switch (operator)
        {
            case EQUAL -> "=";
            case GREATER -> ">";
            case GREATER_EQUAL -> ">=";
            case LESSER -> "<";
            case LESSER_EQUAL -> "<=";
            case NOT_EQUAL -> "!=";
        };
    }
}
