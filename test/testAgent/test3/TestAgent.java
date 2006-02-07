/*
 * Created on 07-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * $Id$
 */


package testAgent.test3;

import jade.core.Agent;
import jade.core.behaviours.PriorityBasedCompositeBehaviour;

public class TestAgent extends Agent {

    public TestAgent() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void setup() {
        printExpected();
        PriorityBasedCompositeBehaviour pbcb = new PriorityBasedCompositeBehaviour(this);
        pbcb.addSubBehaviour(new PriorityBehaviourExample(1, 2));
        pbcb.addSubBehaviour(new PriorityBehaviourExample(2, 1));
        pbcb.addSubBehaviour(new PriorityBehaviourExample(3, 0));
        addBehaviour(pbcb);
    }
    
    private void printExpected() {
        System.out.println("EXPECTED: 3-3-3-2-1-2-2-1-1-");
        System.out.print("ACTUAL..: ");
    }
}
