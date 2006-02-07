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

import jade.util.leap.Collection;
import jade.util.leap.Iterator;
import examples.PriorityBehaviourExample;
import junit.framework.TestCase;

public class PriorityBasedCompositeBehaviourTest extends TestCase {
    PriorityBehaviourExample[] pbe = new PriorityBehaviourExample[6];
    PriorityBasedCompositeBehaviour pbcb;
    
    protected void setUp() throws Exception {
        super.setUp(); 
        pbe = new PriorityBehaviourExample[6];
        pbe[0] = new PriorityBehaviourExample();
        pbe[1] = new PriorityBehaviourExample(2);
        pbe[2] = new PriorityBehaviourExample(1);
        pbe[3] = new PriorityBehaviourExample(4);
        pbe[4] = new PriorityBehaviourExample(1);
        pbe[5] = new PriorityBehaviourExample(0);
        pbcb = new PriorityBasedCompositeBehaviour();
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.addSubBehaviour(PriorityBehaviour)'
     */
    public void testAddSubBehaviour() {
        pbcb.addSubBehaviour(pbe[0]);
        assertSame(pbcb, pbe[0].getParent());
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[3]);
        checkLists(4, 0, 0, 0);
        pbe[4].block();
        pbcb.addSubBehaviour(pbe[4]);
        checkLists(5,0,0,1);
        pbcb.addSubBehaviour(pbe[5]);
        checkLists(6,0,0,1);
    }

    /**
     * Checks the expected values for the sizes of lists.
     * @param expectedAll Expected size for <code>allBehaviours</code> list.
     * @param expectedDone Expected size for <code>doneBehaviours</code> list.
     * @param expectedReady Expected size for <code>readyBehaviours</code> list.
     * @param expectedBlocked Expected value for <code>numBlockedBehaviours</code>.
     * 
     */
    private void checkLists(int expectedAll, int expectedDone, int expectedReady, int expectedBlocked) {
        assertEquals(expectedAll, pbcb.allBehaviours.size());
        assertEquals(expectedDone, pbcb.doneBehaviours.size());
        assertEquals(expectedReady, pbcb.readyBehaviours.size());
        assertEquals(expectedBlocked, pbcb.numBlockedBehaviours);
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.changePriority(PriorityBehaviour, int, boolean)'
     * Changes the priority of a behaviour, but no takes effect until the behaviour is executed.
     */
    public void testChangePriorityNoEffect() {
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[3]);
        pbcb.scheduleFirst();
        pbcb.changePriority(pbe[1], 0, false);
        pbcb.scheduleNext(false,1);
        pbcb.scheduleNext(false,1);
        pbcb.scheduleNext(false,1);
        assertSame(pbe[1], pbcb.getCurrent());
        pbcb.scheduleNext(false,1);
        assertSame(pbe[1], pbcb.getCurrent());    
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.changePriority(PriorityBehaviour, int, boolean)'
     * Changes the priority of a behaviour, and reflects this change inmediately.
     */
    public void testChangePriorityWithEffect() {
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[3]);
        pbcb.scheduleFirst();
        pbcb.changePriority(pbe[3], 1, true);
        pbcb.scheduleNext(false,1);
        pbcb.scheduleNext(false,1);
        pbcb.scheduleNext(false,1);
        assertSame(pbe[3], pbcb.getCurrent());
        pbcb.scheduleNext(false,1);
        pbcb.scheduleNext(false,1);
        assertSame(pbe[3], pbcb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.changePriority(PriorityBehaviour, int, boolean)'
     * Changes the priority of a behaviour to be 0, and reflects this change inmediately.
     */
    public void testChangePriorityWithEffect0() {
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[3]);
        pbcb.scheduleFirst();
        pbcb.changePriority(pbe[3], 0, true);
        pbcb.scheduleNext(false,1);
        assertSame(pbe[3], pbcb.getCurrent());
        pbcb.scheduleNext(false,1);
        assertSame(pbe[3], pbcb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.getChildren()'
     */
    public void testGetChildren() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbe[3].block();
        pbcb.addSubBehaviour(pbe[3]);
        Collection c = pbcb.getChildren();
        assertEquals(4, c.size());
        Iterator it = c.iterator();
        for (int i = 0; i<4; i++)
            assertSame(pbe[i], it.next());
        assertFalse(it.hasNext());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.removeSubBehaviour(PriorityBehaviour)'
     */
    public void testRemoveSubBehaviour() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.removeSubBehaviour(pbe[3]);
        checkLists(3,0,0,0);
        pbcb.removeSubBehaviour(pbe[1]);
        checkLists(2,0,0,0);
        assertSame(pbe[0], pbcb.allBehaviours.getElement(0));
        assertSame(pbe[2], pbcb.allBehaviours.getElement(1));
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleFirst()'
     * Case when there aren't children.
     */
    public void testScheduleFirstEmpty() {
        pbcb.scheduleFirst();
        checkLists(0, 0, 0, 0);
        assertNull(pbcb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleFirst()'
     * Case when there is a child with priority 0 and is runnable.
     */
    public void testScheduleFirstSome0NonBlocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[5]);
        pbcb.scheduleFirst();
        assertEquals(5, pbe[0].getCurrentPriority());
        assertEquals(2, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(0, pbe[5].getCurrentPriority());
        checkLists(4, 0, 1, 0);
        assertSame(pbe[5], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[5], pbcb.getCurrent());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleFirst()'
     * Case when there is a child with priority 0 but is blocked.
     */
    public void testScheduleFirstSome0Blocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbe[2].block();
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[4]);
        pbe[5].block();
        pbcb.addSubBehaviour(pbe[5]);
        pbcb.scheduleFirst();
        assertEquals(4, pbe[0].getCurrentPriority());
        assertEquals(1, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        assertEquals(0, pbe[5].getCurrentPriority());
        checkLists(5, 0, 2, 2);
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[5], pbcb.readyBehaviours.getElement(1));
        assertSame(pbe[4], pbcb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleFirst()'
     * Case when there isn't a child with priority 0 and there is a runnable behaviour.
     */
    public void testScheduleFirstAny0NonBlocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[4]);
        pbcb.scheduleFirst();
        assertEquals(4, pbe[0].getCurrentPriority());
        assertEquals(1, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        checkLists(4, 0, 2, 0);
        assertSame(pbe[2], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(1));
        assertSame(pbe[2], pbcb.getCurrent());
     }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleFirst()'
     * Case when there isn't a child with priority 0, and the behaviours with the highest
     * priority are blocked.
     */
    public void testScheduleFirstAny0Blocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbe[2].block();
        pbcb.addSubBehaviour(pbe[2]);
        pbe[4].block();
        pbcb.addSubBehaviour(pbe[4]);
        pbcb.scheduleFirst();
        assertEquals(3, pbe[0].getCurrentPriority());
        assertEquals(2, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        checkLists(4, 0, 3, 2);
        assertSame(pbe[1], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[1], pbcb.getCurrent());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleFirst()'
     * Case when there isn't a child with priority 0, and all behaviours are blocked.
     */
    public void testScheduleFirstAny0AllBlocked() {
        pbe[0].block();
        pbcb.addSubBehaviour(pbe[0]);
        pbe[1].block();
        pbcb.addSubBehaviour(pbe[1]);
        pbe[2].block();
        pbcb.addSubBehaviour(pbe[2]);
        pbe[4].block();
        pbcb.addSubBehaviour(pbe[4]);
        pbcb.scheduleFirst();
        assertEquals(4, pbe[0].getCurrentPriority());
        assertEquals(1, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        checkLists(4, 0, 2, 4);
        assertSame(pbe[2], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(1));
        assertSame(pbe[2], pbcb.getCurrent());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleNext(boolean, int)'
     * Case when there is a child with priority 0 but is blocked.
     */
    public void testScheduleNextSome0NonBlocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[5]);
        pbcb.scheduleFirst();
        pbcb.scheduleNext(false, 1);
        assertEquals(5, pbe[0].getCurrentPriority());
        assertEquals(2, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(0, pbe[5].getCurrentPriority());
        checkLists(4, 0, 1, 0);
        assertSame(pbe[5], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[5], pbcb.getCurrent());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleNext(boolean, int)'
     * Case when there is a child with priority 0 but is blocked.
     */
    public void testScheduleNextSome0Blocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbe[2].block();
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[4]);
        pbe[5].block();
        pbcb.addSubBehaviour(pbe[5]);
        pbcb.scheduleFirst();
        pbcb.scheduleNext(false, 1);
        assertEquals(3, pbe[0].getCurrentPriority());
        assertEquals(2, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        assertEquals(0, pbe[5].getCurrentPriority());
        checkLists(5, 0, 4, 2);
        assertSame(pbe[1], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[2], pbcb.readyBehaviours.getElement(1));
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(2));
        assertSame(pbe[5], pbcb.readyBehaviours.getElement(3));
        assertSame(pbe[1], pbcb.getCurrent());
     }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleNext(boolean, int)'
     * Case when there isn't a child with priority 0 and there is a runnable behaviour.
     */
    public void testScheduleNextAny0NonBlocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        pbcb.addSubBehaviour(pbe[4]);
        pbcb.scheduleFirst();
        pbcb.scheduleNext(false,1);
        assertEquals(4, pbe[0].getCurrentPriority());
        assertEquals(1, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        checkLists(4, 0, 1, 0);
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[4], pbcb.getCurrent());        
        pbcb.scheduleNext(false,1);
        assertEquals(3, pbe[0].getCurrentPriority());
        assertEquals(2, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        checkLists(4, 0, 3, 0);
        assertSame(pbe[1], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[2], pbcb.readyBehaviours.getElement(1));
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(2));
        assertSame(pbe[1], pbcb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBasedCompositeBehaviour.scheduleNext(boolean, int)'
     * Case when there isn't a child with priority 0, and the behaviours with the highest
     * priority are blocked.
     */
    public void testScheduleNextAny0Blocked() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbe[2].block();
        pbcb.addSubBehaviour(pbe[2]);
        pbe[4].block();
        pbcb.addSubBehaviour(pbe[4]);
        pbcb.scheduleFirst();
        pbcb.scheduleNext(false,1);
        assertEquals(1, pbe[0].getCurrentPriority());
        assertEquals(2, pbe[1].getCurrentPriority());
        assertEquals(1, pbe[2].getCurrentPriority());
        assertEquals(1, pbe[4].getCurrentPriority());
        checkLists(4, 0, 3, 2);
        assertSame(pbe[1], pbcb.readyBehaviours.getElement(0));
        assertSame(pbe[2], pbcb.readyBehaviours.getElement(1));
        assertSame(pbe[4], pbcb.readyBehaviours.getElement(2));
        assertSame(pbe[1], pbcb.getCurrent());
    }

    /*
     * Tests that the Composite Behaviour become blocked where all children are blocked.
     */
    public void testBlock() {
        pbcb.addSubBehaviour(pbe[0]);
        pbcb.addSubBehaviour(pbe[1]);
        pbcb.addSubBehaviour(pbe[2]);
        assertTrue(pbcb.isRunnable());
        pbe[0].block();
        assertTrue(pbcb.isRunnable());
        pbe[1].block();
        assertTrue(pbcb.isRunnable());
        pbe[2].block();
        assertFalse(pbcb.isRunnable());
    }
}
