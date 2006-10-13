/*
 * Created on 11-oct-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * $Id$
 */


package test.parallelprioritybehaviour.common;

import jade.core.Agent;
import jade.core.behaviours.ParallelPriorityBehaviour;

public class ParallelPriorityBehaviourForTest extends ParallelPriorityBehaviour {
    int[] currentRun = new int[9];
    int index=0;
    
    public ParallelPriorityBehaviourForTest(int endCondition) {
       super(endCondition);
    }

    public ParallelPriorityBehaviourForTest() {
        super();
    }

    public void createBehaviour(int id, int priority) {
        addSubBehaviour(new BehaviourExample(this, id), priority);
    }
    
    public void createBehaviourChange(int id, int priority, boolean changeNow) {
        addSubBehaviour(new BehaviourExampleChange(this,id,changeNow), priority);
    }
    
    public void ranBehaviour(int id) {
        currentRun[index++] = id;
    }

    public int[] getCurrentRun() {
        return currentRun;
    }
}