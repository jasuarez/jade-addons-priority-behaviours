/*****************************************************************
 JADE - Java Agent DEvelopment Framework is a framework to develop multi-agent
 systems in compliance with the FIPA specifications.
 Copyright (C) 2000 CSELT S.p.A. 
 
 GNU Lesser General Public License
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

/*****************************************************************
 Laboratory for Research and Development in Artificial Intelligence (LIDIA)
 Computer Science Department
 University of A Coruña, Spain
 
 $Id$
 *****************************************************************/

package jade.core.behaviours;

import examples.*;
import junit.framework.TestCase;

public class PriorityBehaviourTest extends TestCase {

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour()'
     * Tests that assigning default priority works well.
     */
    public void testPriorityBehaviour() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample();
        assertEquals(PriorityBehaviour.DEFAULT_PRIORITY, pbe.getPriority());
        assertEquals(PriorityBehaviour.DEFAULT_PRIORITY, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(int)'
     * Tests that assigning a priority greater than 0 works well.
     */
    public void testPriorityBehaviourIntPos() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(3);
        assertEquals(3, pbe.getPriority());
        assertEquals(3, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(int)'
     * Tests that assigning a priority 0 works well.
     */
    public void testPriorityBehaviourIntZero() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(0);
        assertEquals(0, pbe.getPriority());
        assertEquals(0, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(int)'
     * Tests that assigning a priority lesser than 0 works well.
     */
    public void testPriorityBehaviourIntNeg() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(-2);
        assertEquals(0, pbe.getPriority());
        assertEquals(0, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(Agent)'
     * Tests that assigning an Agent and a default priority works well.
     */
    public void testPriorityBehaviourAgent() {
        AgentExample ae = new AgentExample();
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(ae);
        assertEquals(PriorityBehaviour.DEFAULT_PRIORITY, pbe.getPriority());
        assertEquals(PriorityBehaviour.DEFAULT_PRIORITY, pbe.getCurrentPriority());
        assertEquals(ae, pbe.myAgent);
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(Agent)'
     * Tests that assigning an Agent and a priority greater than 0 works well.
     */
    public void testPriorityBehaviourAgentIntPos() {
        AgentExample ae = new AgentExample();
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(ae, 5);
        assertEquals(5, pbe.getPriority());
        assertEquals(5, pbe.getCurrentPriority());
        assertEquals(ae, pbe.myAgent);
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(Agent)'
     * Tests that assigning an Agent and a priority 0 works well.
     */
    public void testPriorityBehaviourAgentZero() {
        AgentExample ae = new AgentExample();
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(ae, 0);
        assertEquals(0, pbe.getPriority());
        assertEquals(0, pbe.getCurrentPriority());
        assertEquals(ae, pbe.myAgent);
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.PriorityBehaviour(Agent)'
     * Tests that assigning an Agent and a a priority lesser than 0 works well.
     */
    public void testPriorityBehaviourAgentNeg() {
        AgentExample ae = new AgentExample();
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(ae, -7);
        assertEquals(0, pbe.getPriority());
        assertEquals(0, pbe.getCurrentPriority());
        assertEquals(ae, pbe.myAgent);
    }



    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.setPriority(int)'
     * Tests that we can change the priority.
     */
    public void testSetPriority() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(4);
        pbe.setPriority(10);
        assertEquals(10, pbe.getPriority());
        assertEquals(4, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.setPriority(int)'
     * Tests that we can change the priority using a negative one.
     */
    public void testSetPriorityNeg() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(4);
        pbe.setPriority(-5);
        assertEquals(0, pbe.getPriority());
        assertEquals(4, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.getCurrentPriority()'
     */
    public void testGetCurrentPriority() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(4);
        assertEquals(4, pbe.getCurrentPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.incCurrentPriority(int)'
     */
    public void testIncCurrentPriority() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(4);
        pbe.incCurrentPriority(3);
        assertEquals(1, pbe.getCurrentPriority());
        pbe.incCurrentPriority(3);
        assertEquals(0, pbe.getCurrentPriority());
        assertEquals(4, pbe.getPriority());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourExample.resetCurrentPriority()'
     */
    public void testResetCurrentPriority() {
        PriorityBehaviourExample pbe = new PriorityBehaviourExample(4);
        pbe.incCurrentPriority(2);
        pbe.resetCurrentPriority();
        assertEquals(4, pbe.getCurrentPriority());
        pbe.setPriority(5);
        pbe.resetCurrentPriority();
        assertEquals(5, pbe.getCurrentPriority());
    }

}
