package yuria.sul.ast;

import java.util.Locale;

public enum AssignOperation {
    ADD {
        @Override
        public Integer apply(Integer x, Integer y) {
            return x + y;
        }
    },
    SUB {
        @Override
        public Integer apply(Integer x, Integer y) {
            if (y > x) return y - x;
            return x - y;
        }
    },
    MULTI {
        @Override
        public Integer apply(Integer x, Integer y) {
            return x * y;
        }
    },
    DIV {
        @Override
        public Integer apply(Integer x, Integer y) {
            if (y == 0 || x == 0) throw new ArithmeticException("Cannot divide by zero");
            return x / y;
        }
    },
    EQUAL {
        @Override
        public Integer apply(Integer x, Integer y) {
            return x;
        }
    },
    POW {
        @Override
        public Integer apply(Integer x, Integer y) {
            return (int) Math.pow(x, y);
        }
    };

    public Integer apply(Integer x, Integer y) { return this.apply(x); };
    public Integer apply(Integer x) { return x; }

    public static AssignOperation from(String text)
    {
        return switch (text.toUpperCase(Locale.ROOT)) {
            case "OP_EQ", "->" -> EQUAL;
            case "OP_MULTI_EQ", "*=" -> MULTI;
            case "OP_PLUS_EQ", "+=" -> ADD;
            case "OP_MINUS_EQ", "-=" -> SUB;
            case "OP_DIV_EQ", "/=" -> DIV;
            case "OP_POW_EQ", "^=" -> POW;
            default -> throw new IllegalArgumentException("Unknown assignment operation: " + text);
        };
    }

    public static String toString(AssignOperation operation)
    {
        return switch (operation)
        {
            case EQUAL -> "->";
            case MULTI -> "*=";
            case ADD -> "+=";
            case SUB -> "-=";
            case DIV -> "/=";
            case POW -> "^=";
        };
    }
}
