package parseDependencies;

/** enumeration Token - Defines the basic tokens returned by 
 * the lexical analyzer.
 */
public enum Token {
    END_OF_FILE( "End-of-file"),
    LPAREN( "(" ),
    RPAREN( ")" ),
    LCURLY( "{" ),
    RCURLY( "}" ),
    COMMA( "," ),
    SEMICOLON( ";" ),
    ASSIGN( "=" ),
    ALT( "|" ),
    
    KW_INPUTS( "inputs" ),
    KW_NULL( "null" ),
    KW_REPEAT( "repeat" ),
    KW_SELECT( "select" ),
    
    IDENTIFIER( "identifier" ),
    NUMBER( "number" ),
    ILLEGAL( "illegal" );
    
    /** The name of the token */
    String name;
    
    private Token( String name ) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
