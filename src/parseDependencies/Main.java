package parseDependencies;
import java.io.IOException;

import dependencies.Program;
import parseDependencies.Parser;
import parseDependencies.Scanner;
import source.ErrorHandler;
import source.Errors;
import source.Source;

/** 
 * class Main - processes the command line arguments and then 
 * parses the program and analyses it.
 */
public class Main {

    /** Print usage information */
    public static void usage() {
        System.out.println(
            "Usage: java parser.Main [-dhp] <filename>\n"+
            "  -d  =  debug parse\n" +
            "  -h  =  output this usage information\n" +
            "  -p  =  parse only - no analysis\n" +
            " <filename> is parsed and if no errors the graph" +
            " is analysed unless -p is given." );
    }
    public static String SourceSuffix = ".simp";

    /** Main procedure */
    public static void main( String args[] ) throws java.lang.Exception {
        /** Name of the input source program file. */
        String srcFile = null;
        /** Input source stream */
        Source src;
        /** Error handler for reporting error messages */
        Errors errors;
        /** Abstract structure returned by parser */
        Program parsedProgram;
        /** Perform analysis */
        boolean analyse = true;
         /** Debug mode for parser - quite verbose */
        boolean debugParse = false;

        /* Process command line arguments */
        for( String arg : args ) {
            if( arg.charAt(0) == '-' ) { /* Option */
                switch( arg.charAt(1) ) {
                case 'd': /* Debug parse */
                    debugParse = true;
                    break;
                case 'p': /* Parse only */
                    analyse = false;
                    break;
                case 'h': /* Help */
                default:
                    usage();
                    System.exit(0);
                    break;
                }
            } else { /* ( arg.charAt(0) != '-' ) Not Option */
                srcFile = arg;
            }
        }
        try {
            /* Set up the input source stream for the source file */
            if( srcFile == null ) {
                System.out.println( "No source file specified" );
                System.exit( 1 );
            }
            src = new Source( srcFile );
            /* Set up the error handler reference */
            errors = new ErrorHandler( System.out, src );
            parsedProgram = parse( src, debugParse );
            if( analyse && parsedProgram != null ) {
                /* Perform the analysis */
                parsedProgram.buildProgram();
                parsedProgram.analyse();
                parsedProgram.printDependencies( System.out );
                errors.flush();
                System.out.println( "Analysis complete" );
            }
            errors.errorSummary();
        } catch( IOException e ) {
            System.out.println( "IOException: " + e + "... Aborting" );
            System.exit(1);
        }
    }

    /** Parse the program
     * @param src program source
     * @param errors handler for errors
     * @param staticCheck do the static checking
     * @param debugParse debugging messages during parsing 
     * @return generated program representation
     */
    private static Program parse( Source src, boolean debugParse ) 
                    throws IOException, Exception
    {
        Program parsedProgram;
        System.out.println( "Parsing " + src.getFileName() );
        try {
            /* Parse the program */
            /* Set up the lexical analyzer using the source program stream */
            Scanner lex = new Scanner( src );
            /** Recursive descent parser.
             * Set up the parser with the lexical analyzer. */
            Parser parser = new Parser( lex, debugParse );
            parsedProgram = parser.parse();
        } catch (IOException e) {
            parsedProgram = null;
            System.out.println( "Exception: " + e + "... Aborting" );
        }
        System.out.println( "Parsing complete" );
        return parsedProgram;
    }
}
