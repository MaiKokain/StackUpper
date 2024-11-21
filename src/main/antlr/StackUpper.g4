grammar StackUpper;

start : programStatements* EOF;

// PROGRAM: functions
programStatements : (idStatement|sizeStatement|tagStatement)? assignOp NUMBER;
idStatement     : ID (TILDE)? EQUAL STRING;
sizeStatement   : SIZE compareOp (NUMBER|STRING);
tagStatement    : TAG (TILDE)? EQUAL STRING;

// PROGRAM: functions lexer
ID      : I D       ;
SIZE    : S I Z E   ;
TAG     : T A G     ;


// PROGRAM: assign size operator
// ->, *=, +=, -=, /=
assignOp    : OP_EQ         // ->
            | OP_MULTI_EQ   // *=
            | OP_PLUS_EQ    // +=
            | OP_MINUS_EQ   // -=
            | OP_DIV_EQ     // /=
            | OP_POW_EQ     // ^=
            ;

OP_EQ       :   '->';
OP_MULTI_EQ :   '*=';
OP_PLUS_EQ  :   '+=';
OP_MINUS_EQ :   '-=';
OP_DIV_EQ   :   '/=';
OP_POW_EQ   :   '^=';

// PROGRAM: comparison operator
compareOp   :   EQUAL
            |   OP_NE
            |   OP_GT
            |   OP_GT_EQ
            |   OP_LT
            |   OP_LT_EQ
            ;

OP_NE       : '!='  ;
OP_GT       : '>'   ;
OP_GT_EQ    : '>='  ;
OP_LT       : '<'   ;
OP_LT_EQ    : '<='  ;

IDENTIFIER      : [a-zA-Z_*][a-zA-Z0-9_*]* | '*';
NUMBER          : [0-9]+;
STRING          : ('"'|'\'') (~('"'|'\''))* ('"'|'\'');

WS              : [ \r\t\n]+ -> skip;

// SYMBOLS
COLON       : ':'   ;
EQUAL       : '='   ;
DOUBLE_EQUAL: '=='  ;
EXCLAMATION : '!'   ;
TILDE       : '~'   ;

fragment A  :('a' | 'A') ;
fragment B  :('b' | 'B') ;
fragment C  :('c' | 'C') ;
fragment D  :('d' | 'D') ;
fragment E  :('e' | 'E') ;
fragment F  :('f' | 'F') ;
fragment G  :('g' | 'G') ;
fragment H  :('h' | 'H') ;
fragment I  :('i' | 'I') ;
fragment J  :('j' | 'J') ;
fragment K  :('k' | 'K') ;
fragment L  :('l' | 'L') ;
fragment M  :('m' | 'M') ;
fragment N  :('n' | 'N') ;
fragment O  :('o' | 'O') ;
fragment P  :('p' | 'P') ;
fragment Q  :('q' | 'Q') ;
fragment R  :('r' | 'R') ;
fragment S  :('s' | 'S') ;
fragment T  :('t' | 'T') ;
fragment U  :('u' | 'U') ;
fragment V  :('v' | 'V') ;
fragment W  :('w' | 'W') ;
fragment X  :('x' | 'X') ;
fragment Y  :('y' | 'Y') ;
fragment Z  :('z' | 'Z') ;