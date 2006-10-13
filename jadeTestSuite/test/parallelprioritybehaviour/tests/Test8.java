/*
 * Created on 12-oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * $Id$
 */


package test.parallelprioritybehaviour.tests;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import test.common.Test;
import test.parallelprioritybehaviour.common.ParallelPriorityBehaviourForTest;

public class Test8 extends Test {
    class ValidateBehaviour extends OneShotBehaviour {
        ParallelPriorityBehaviourForTest toValidate;
        int[] expectedRun = {1,3,1,2,3,1,3};
        
        public ValidateBehaviour(ParallelPriorityBehaviourForTest ppbft) {
            this.toValidate = ppbft;
        }
        
        public void action() {
            int[] currentRun = toValidate.getCurrentRun();
            for (int i=0; i < expectedRun.length; i++)
                if (expectedRun[i]!=currentRun[i]) {
                    failed("Expected child "+expectedRun[i]+", but was "+currentRun[i]);
                    return;
                }
            passed("Test passed");
        }
    }
    
    public Behaviour load(Agent a) {
        SequentialBehaviour sb = new SequentialBehaviour(a);
        ParallelPriorityBehaviourForTest ppbft = new ParallelPriorityBehaviourForTest(2);
        ppbft.createBehaviour(1, 1);
        ppbft.createBehaviour(2, 2);
        ppbft.createBehaviour(3, 1);
        sb.addSubBehaviour(ppbft);
        sb.addSubBehaviour(new ValidateBehaviour(ppbft));
        return sb;
    }
}