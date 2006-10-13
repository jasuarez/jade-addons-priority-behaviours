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
 University of A Coruna, Spain
 
 $Id$
 *****************************************************************/

package jade.core.behaviours;

import examples.AgentExample;
import examples.BehaviourExample;
import junit.framework.TestCase;

/**
 * Unit tests for the <code>EncapsulatedPriorityBehaviur</code> class.
 * 
 * @author Juan A. Suarez Romero - University of A Coruna
 * @version $Date$ $Revision$
 */
public class EncapsulatedPriorityBehaviourTest extends TestCase {

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.EncapsulatedPriorityBehaviour(Behaviour,
     * int)' Tests that assigning a priority greater than 0 works well.
     */
    public void testEncapsulatedPriorityBehaviourIntPos() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 3);
        assertEquals(3, epb.getStaticPriority());
        assertEquals(3, epb.getDynamicPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.EncapsulatedPriorityBehaviour(Behaviour,
     * int)' Tests that assigning a priority 0 works well.
     */
    public void testEncapsulatedPriorityBehaviourIntZero() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 0);
        assertEquals(0, epb.getStaticPriority());
        assertEquals(0, epb.getDynamicPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.EncapsulatedPriorityBehaviour(Behaviour,
     * int)' Tests that assigning a priority lesser than 0 works well.
     */
    public void testEncapsulatedPriorityBehaviourIntNeg() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), -2);
        assertEquals(0, epb.getStaticPriority());
        assertEquals(0, epb.getDynamicPriority());

    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.getBehaviour()' Tests
     * that return the correct encapsulated behaviour.
     */
    public void testGetBehaviour() {
        BehaviourExample be = new BehaviourExample();
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                be, 2);
        assertSame(be, epb.getBehaviour());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.getCurrentPriority()'
     */
    public void testGetCurrentPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        assertEquals(4, epb.getDynamicPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.getPriority()'
     */
    public void testGetPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        assertEquals(4, epb.getStaticPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.incCurrentPriority(int)'
     */
    public void testIncCurrentPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.incDynamicPriority(3);
        assertEquals(1, epb.getDynamicPriority());
        epb.incDynamicPriority(3);
        assertEquals(0, epb.getDynamicPriority());
        assertEquals(4, epb.getStaticPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.resetCurrentPriority()'
     */
    public void testResetCurrentPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.incDynamicPriority(2);
        epb.resetDynamicPriority();
        assertEquals(4, epb.getDynamicPriority());
        epb.setStaticPriority(5);
        epb.resetDynamicPriority();
        assertEquals(5, epb.getDynamicPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.setAgent(Agent)'
     */
    public void testSetAgent() {
        AgentExample ae = new AgentExample();
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.setAgent(ae);
        assertSame(ae, epb.getBehaviour().myAgent);
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.setPriority(int)'
     * Tests that we can change the priority, and rules well even if we used a
     * negative priority.
     */
    public void testSetPriorityNeg() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.setStaticPriority(-5);
        assertEquals(0, epb.getStaticPriority());
        assertEquals(4, epb.getDynamicPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.setPriority(int)'
     * Tests that we can change the priority.
     */
    public void testSetPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.setStaticPriority(10);
        assertEquals(10, epb.getStaticPriority());
        assertEquals(4, epb.getDynamicPriority());
    }
}
