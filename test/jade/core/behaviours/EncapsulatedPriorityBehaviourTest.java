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

import examples.AgentExample;
import examples.BehaviourExample;
import junit.framework.TestCase;

/**
 * Unit tests for the <code>EncapsulatedPriorityBehaviur</code> class.
 * 
 * @author Juan A. Suárez Romero - University of A Coruña
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
        assertEquals(3, epb.getPriority());
        assertEquals(3, epb.getCurrentPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.EncapsulatedPriorityBehaviour(Behaviour,
     * int)' Tests that assigning a priority 0 works well.
     */
    public void testEncapsulatedPriorityBehaviourIntZero() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 0);
        assertEquals(0, epb.getPriority());
        assertEquals(0, epb.getCurrentPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.EncapsulatedPriorityBehaviour(Behaviour,
     * int)' Tests that assigning a priority lesser than 0 works well.
     */
    public void testEncapsulatedPriorityBehaviourIntNeg() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), -2);
        assertEquals(0, epb.getPriority());
        assertEquals(0, epb.getCurrentPriority());

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
        assertEquals(4, epb.getCurrentPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.getPriority()'
     */
    public void testGetPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        assertEquals(4, epb.getPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.incCurrentPriority(int)'
     */
    public void testIncCurrentPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.incCurrentPriority(3);
        assertEquals(1, epb.getCurrentPriority());
        epb.incCurrentPriority(3);
        assertEquals(0, epb.getCurrentPriority());
        assertEquals(4, epb.getPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.resetCurrentPriority()'
     */
    public void testResetCurrentPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.incCurrentPriority(2);
        epb.resetCurrentPriority();
        assertEquals(4, epb.getCurrentPriority());
        epb.setPriority(5);
        epb.resetCurrentPriority();
        assertEquals(5, epb.getCurrentPriority());
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
        epb.setPriority(-5);
        assertEquals(0, epb.getPriority());
        assertEquals(4, epb.getCurrentPriority());
    }

    /*
     * Test method for
     * 'jade.core.behaviours.EncapsulatedPriorityBehaviour.setPriority(int)'
     * Tests that we can change the priority.
     */
    public void testSetPriority() {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                new BehaviourExample(), 4);
        epb.setPriority(10);
        assertEquals(10, epb.getPriority());
        assertEquals(4, epb.getCurrentPriority());
    }
}
