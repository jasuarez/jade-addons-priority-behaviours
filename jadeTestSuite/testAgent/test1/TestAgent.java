
package testAgent.test1;

import jade.core.Agent;
import jade.core.behaviours.ParallelPriorityBehaviour;

public class TestAgent extends Agent {

    private static final long serialVersionUID = -6262536113289737977L;

    public TestAgent() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void setup() {
        printExpected();
        ParallelPriorityBehaviour ppb = new ParallelPriorityBehaviour(this);
        ppb.addSubBehaviour(new BehaviourExample(1), 2);
        ppb.addSubBehaviour(new BehaviourExample(2), 2);
        ppb.addSubBehaviour(new BehaviourExample(3), 2);
        addBehaviour(ppb);
    }
    
    private void printExpected() {
        System.out.println("EXPECTED: 1-2-3-1-2-3-1-2-3-");
        System.out.print("ACTUAL..: ");
    }
}
