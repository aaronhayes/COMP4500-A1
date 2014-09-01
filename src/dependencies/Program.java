package dependencies;

import java.io.PrintStream;
import java.util.Set;

public class Program {
    private Set<String> inputs;
    private Statement body;
    private FlowGraph flowGraph;
    private Dependencies dependencies;

    
    public Program( Set<String> inputs, Statement body ) {
        super();
        this.inputs = inputs;
        this.body = body;
    }
    
    public Set<String> getInputs() {
        return inputs;
    }
    
    public Statement getBody() {
        return body;
    }
    
    public void buildProgram() {
        //System.out.println( body.toString() );
        flowGraph = new FlowGraph( body );
        //System.out.println( flowGraph );
    }
    
    public Dependencies analyse() {
        if( dependencies == null ) {
            Dependencies inDepends = new Dependencies();
            for( String var : inputs ) {
                inDepends.put( var, new DependSet( var ) );
            }
            dependencies = flowGraph.calculateDependencies( inDepends );
        }
        //System.out.println( flowGraph );
        return dependencies;
    }
    
    public void printDependencies( PrintStream out ) {
		if( dependencies == null ) {
			out.println( "No dependencies calculated" );
		} else {
			out.println( dependencies.toString() );
		}
	}
}
