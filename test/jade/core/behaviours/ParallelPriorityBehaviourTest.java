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
import junit.framework.TestCase;
import examples.*;

/**
 * Unit tests for the <code>ParallelPriorityBehaviour</code> class.
 * 
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Date$ $Revision$
 */
public class ParallelPriorityBehaviourTest extends TestCase {
    private BehaviourExample[] be;
    private ParallelPriorityBehaviour ppb;

    /**
     * Checks the expected values for the sizes of lists.
     * @param expectedAll Expected size for <code>allBehaviours</code> list.
     * @param expectedDone Expected size for <code>doneBehaviours</code> list.
     * @param expectedReady Expected size for <code>readyBehaviours</code> list.
     * @param expectedBlocked Expected value for <code>numBlockedBehaviours</code>.
     * 
     */
    private void checkLists(int expectedAll, int expectedDone, int expectedReady, int expectedBlocked) {
        assertEquals(expectedAll, ppb.getAllBehaviours().size());
        assertEquals(expectedDone, ppb.getDoneBehaviours().size());
        assertEquals(expectedReady, ppb.readyBehaviours.size());
        assertEquals(expectedBlocked, ppb.getNumBlockedBehaviours());
        Iterator it = ppb.getAllBehaviours().iterator();
        while (it.hasNext()) {
            assertTrue(ppb.getAsListChildren().contains(((EncapsulatedPriorityBehaviour)it.next()).getBehaviour()));
        }
    }

    protected void setUp() throws Exception {
        super.setUp(); 
        be = new BehaviourExample[6];
        be[0] = new BehaviourExample();
        be[1] = new BehaviourExample();
        be[2] = new BehaviourExample();
        be[3] = new BehaviourExample();
        be[4] = new BehaviourExample();
        be[5] = new BehaviourExample();
        be[0].setBehaviourName("be[0]");
        be[1].setBehaviourName("be[1]");
        be[2].setBehaviourName("be[2]");
        be[3].setBehaviourName("be[3]");
        be[4].setBehaviourName("be[4]");
        be[5].setBehaviourName("be[5]");
        ppb = new ParallelPriorityBehaviour(new AgentExample());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.addSubBehaviour(Behaviour)'
     */
    public void testAddSubBehaviourBehaviour() {
        ppb.addSubBehaviour(be[0]);
        assertSame(ppb, be[0].getParent());
        ppb.addSubBehaviour(be[1]);
        ppb.addSubBehaviour(be[2]);
        ppb.addSubBehaviour(be[3]);
        checkLists(4,0,0,0);
        be[4].block();
        ppb.addSubBehaviour(be[4]);
        checkLists(5,0,0,1);
        ppb.addSubBehaviour(be[5]);
        checkLists(6,0,0,1);
    }
        
    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.addSubBehaviour(Behaviour, int)'
     */
    public void testAddSubBehaviourBehaviourInt() {
        ppb.addSubBehaviour(be[0], 0);
        assertSame(ppb, be[0].getParent());
        assertEquals(0, ppb.getAllBehaviours().getElement(0).getPriority());
        ppb.addSubBehaviour(be[1], -2);
        assertEquals(0, ppb.getAllBehaviours().getElement(1).getPriority());
        ppb.addSubBehaviour(be[2], 4);
        assertEquals(4, ppb.getAllBehaviours().getElement(2).getPriority());
        ppb.addSubBehaviour(be[3], 1);
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getPriority());
        //ppb.scheduleFirst();
        ppb.action();
        checkLists(4,0,2,0);
        be[4].block();
        ppb.addSubBehaviour(be[4], 1);
        assertEquals(1, ppb.getAllBehaviours().getElement(4).getPriority());
        checkLists(5,0,2,1);
        ppb.addSubBehaviour(be[5], 10);
        assertEquals(10, ppb.getAllBehaviours().getElement(5).getPriority());
        checkLists(6,0,2,1);
        while (ppb.isRunnable() && !ppb.done())
            ppb.action();
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.addSubBehaviour(Behaviour, int)'
     * Case when the subbehaviours are blocked, but not the last.
     */
    public void testAddSubBehaviourBehaviourIntBlocked() {
        be[0].block();
        ppb.addSubBehaviour(be[0], 0);
        be[1].block();
        ppb.addSubBehaviour(be[1], 1);
        be[2].block();
        ppb.addSubBehaviour(be[2], 1);
        ppb.action();
        assertFalse(ppb.isRunnable());
        ppb.addSubBehaviour(be[3], 1);
        assertTrue(ppb.isRunnable());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.changePriority(Behaviour, int, boolean)'
     * Case when we change the highest priority behaviour and becomes even higher.
     */
    public void testChangePriorityHighestGoHigher() {
        ppb.addSubBehaviour(be[0], 3);
        ppb.addSubBehaviour(be[1], 7);
        ppb.addSubBehaviour(be[2], 12);
        ppb.action();
        ppb.changePriority(be[0], 1, true);
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        ppb.action();
        assertSame(be[1], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.changePriority(Behaviour, int, boolean)'
     * Case when we change the highest priority behaviour and another behaviour becomes the highest.
     */
    public void testChangePriorityHighestGoLesser() {
        ppb.addSubBehaviour(be[0], 3);
        ppb.addSubBehaviour(be[1], 7);
        ppb.addSubBehaviour(be[2], 12);
        ppb.action();
        ppb.changePriority(be[0], 6, true);
        ppb.action();
        assertSame(be[1], ppb.getCurrent());
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        ppb.action();
        assertSame(be[2], ppb.getCurrent());
        ppb.action();
        assertSame(be[1], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.changePriority(Behaviour, int, boolean)'
     * Changes the priority of a behaviour, but no takes effect until the behaviour is executed.
     */
    public void testChangePriorityNoEffect() {
        ppb.addSubBehaviour(be[0], 2);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 4);
        //ppb.scheduleFirst();
        ppb.action();
        ppb.changePriority(be[0], 0, false);
        //ppb.scheduleNext(false,1);
        ppb.action();
        //ppb.scheduleNext(false,1);
        ppb.action();
        //ppb.scheduleNext(false,1);
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        //ppb.scheduleNext(false,1);
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.changePriority(Behaviour, int, boolean)'
     * Changes the priority of a behaviour, and reflects this change inmediately.
     */
    public void testChangePriorityWithEffect() {
        ppb.addSubBehaviour(be[0], 2);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 4);
        //ppb.scheduleFirst();
        ppb.action();
        ppb.changePriority(be[2], 1, true);
        //ppb.scheduleNext(false, 1);
        ppb.action();
        //ppb.scheduleNext(false, 1);
        ppb.action();
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[2], ppb.getCurrent());
        //ppb.scheduleNext(false, 1);
        ppb.action();
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[2], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.changePriority(Behaviour, int, boolean)'
     * Changes the priority of a behaviour to be 0, and reflects this change inmediately.
     */
    public void testChangePriorityWithEffect0() {
        ppb.addSubBehaviour(be[0], 2);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 4);
        //ppb.scheduleFirst();
        ppb.action();
        ppb.changePriority(be[2], 0, true);
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[2], ppb.getCurrent());
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[2], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.checkTermination(boolean, int)'
     */
    public void testCheckTermination() {
        ppb.addSubBehaviour(be[0], 2);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 4);
        /*ppb.scheduleFirst();
        for (int i = 0; i < (5*3 - 1); i++) {
            ppb.getCurrent().action();
            assertFalse(ppb.checkTermination(ppb.getCurrent().done(), ppb.getCurrent().onEnd()));
            ppb.scheduleNext(ppb.getCurrent().done(), ppb.getCurrent().onEnd());
        }
        ppb.getCurrent().action();
        assertTrue(ppb.checkTermination(ppb.getCurrent().done(), ppb.getCurrent().onEnd()));*/
        for (int i = 0; i < (5*3 - 1); i++) {
            ppb.action();
            assertFalse(ppb.checkTermination(ppb.getCurrent().done(), ppb.getCurrent().onEnd()));
        }
        ppb.action();
        assertTrue(ppb.checkTermination(ppb.getCurrent().done(), ppb.getCurrent().onEnd()));
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.checkTermination(boolean, int)'
     * Case when there are not children
     */
    public void testCheckTerminationEmpty() {
        ppb.action();
        assertTrue(ppb.done());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.getChildren()'
     */
    public void testGetChildren() {
        ppb.addSubBehaviour(be[0]);
        ppb.addSubBehaviour(be[1]);
        ppb.addSubBehaviour(be[2]);
        be[3].block();   
        ppb.addSubBehaviour(be[3]);
        Collection c = ppb.getChildren();
        assertEquals(4, c.size());
        Iterator it = c.iterator();
        for (int i = 0; i<4; i++)
            assertSame(be[i], it.next());
        assertFalse(it.hasNext());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.getCurrent()'
     */
    public void testGetCurrent() {
        ppb.addSubBehaviour(be[0]);
        ppb.addSubBehaviour(be[1]);
        ppb.addSubBehaviour(be[2]);
        //ppb.scheduleFirst();
        ppb.action();
        assertSame(be[0], ppb.getCurrent());
        ppb.addSubBehaviour(be[3], 1);
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[1], ppb.getCurrent());
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[2], ppb.getCurrent());
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[3], ppb.getCurrent());
        //ppb.scheduleNext(false, 1);
        ppb.action();
        assertSame(be[3], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.ParallelPriority()'
     */
    public void testParallelPriorityBehaviour() {
        ParallelPriorityBehaviour beh = new ParallelPriorityBehaviour();
        assertNull(beh.myAgent);
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.ParallelPriority(Agent, int)'
     * Checks that the Composite Behaviour terminates when all children terminate.
     */
    public void testParallelPriorityBehaviourAgentIntAll() {
        AgentExample ae = new AgentExample();
        ParallelPriorityBehaviour beh = new ParallelPriorityBehaviour(ae, ParallelPriorityBehaviour.WHEN_ALL);
        beh.addSubBehaviour(be[0], 1);
        beh.addSubBehaviour(be[1], 2);
        beh.addSubBehaviour(be[2], 1);
        for (int i = 0; i < 14; i++) 
            beh.action();
        assertFalse(beh.done());
        beh.action();
        assertTrue(beh.done());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.ParallelPriority(Agent, int)'
     * Checks that the Composite Behaviour terminates when one child terminate
     */
    public void testParallelPriorityBehaviourAgentIntAny() {
        AgentExample ae = new AgentExample();
        ParallelPriorityBehaviour beh = new ParallelPriorityBehaviour(ae, ParallelPriorityBehaviour.WHEN_ANY);
        beh.addSubBehaviour(be[0], 1);
        beh.addSubBehaviour(be[1], 2);
        beh.addSubBehaviour(be[2], 1);
        for (int i = 0; i < 10; i++) 
            beh.action();
        assertFalse(beh.done());
        beh.action();
        assertTrue(beh.done());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.ParallelPriority(Agent, int)'
     * Checks that the Composite Behaviour terminates when a number of children terminate.
     */
    public void testParallelPriorityBehaviourAgentIntN() {
        AgentExample ae = new AgentExample();
        ParallelPriorityBehaviour beh = new ParallelPriorityBehaviour(ae, 2);
        beh.addSubBehaviour(be[0], 1);
        beh.addSubBehaviour(be[1], 2);
        beh.addSubBehaviour(be[2], 1);
        for (int i = 0; i < 11; i++) 
            beh.action();
        assertFalse(beh.done());
        beh.action();
        assertTrue(beh.done());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.removeSubBehaviour(Behaviour)'
     */
    public void testRemoveSubBehaviour() {
        ppb.addSubBehaviour(be[0], 3);
        //ppb.scheduleFirst();
        ppb.action();
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 2);
        ppb.removeSubBehaviour(be[3]);
        checkLists(3, 0, 1, 0);
        ppb.removeSubBehaviour(be[1]);
        checkLists(2, 0, 1, 0);
        assertSame(be[0], ppb.getAllBehaviours().getElement(0).getBehaviour());
        assertSame(be[2], ppb.getAllBehaviours().getElement(1).getBehaviour());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.removeSubBehaviour(Behaviour)'
     * Case when there are a mix of blocked and runnable behaviours, and we remove a blocked one.
     */
    public void testRemoveSubBehaviourMixed() {
        ppb.addSubBehaviour(be[0], 3);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 2);
        ppb.addSubBehaviour(be[3], 1);
        be[1].block();
        be[2].block();
        ppb.action();
        checkLists(4, 0, 1, 2);
        assertSame(ppb.getCurrent(), be[3]);
        ppb.removeSubBehaviour(be[1]);
        ppb.action();
        checkLists(3, 0, 1, 1);
        assertSame(ppb.getCurrent(), be[3]);
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.removeSubBehaviour(Behaviour)'
     * Case where there is only one runnable behaviour, and we remove it.
     */
    public void testRemoveSubBehaviourOneRunnable() {
        ppb.addSubBehaviour(be[0], 3);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 2);
        ppb.addSubBehaviour(be[3], 1);
        be[1].block();
        be[2].block();
        be[3].block();
        ppb.action();
        checkLists(4, 0, 3, 3);
        assertSame(ppb.getCurrent(), be[0]);
        ppb.removeSubBehaviour(be[0]);
        ppb.action();
        checkLists(3, 0, 2, 3);
        assertFalse(ppb.isRunnable());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.reset()'
     */
    public void testReset() {
        ppb.addSubBehaviour(be[0], 2);
        ppb.addSubBehaviour(be[1], 1);
        ppb.addSubBehaviour(be[2], 0);
        /*ppb.scheduleFirst();
        for (int i = 0; i < 5; i++) {
            ppb.getCurrent().action();
            ppb.checkTermination(ppb.getCurrent().done(), ppb.getCurrent().onEnd());
            ppb.scheduleNext(ppb.getCurrent().done(), ppb.getCurrent().onEnd());
        }
        ppb.getCurrent().action();
        ppb.scheduleNext(ppb.getCurrent().done(), ppb.getCurrent().onEnd());*/
        for (int i = 0; i < 5; i++)
            ppb.action();
        ppb.action();
        ppb.reset();
        checkLists(3, 0, 0, 0);
        assertEquals(2, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(0).getPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(1).getPriority());
        assertEquals(0, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(0, ppb.getAllBehaviours().getElement(2).getPriority());        
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleFirst()'
     * Case when there isn't a child with priority 0, and all behaviours are blocked.
     */
    public void testScheduleFirstAny0AllBlocked() {
        be[0].block();
        ppb.addSubBehaviour(be[0], 5);
        be[1].block();
        ppb.addSubBehaviour(be[1], 2);
        be[2].block();
        ppb.addSubBehaviour(be[2], 1);
        be[3].block();
        ppb.addSubBehaviour(be[3], 1);
        ppb.scheduleFirst();
        assertEquals(4, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4,0,2,4);
        assertSame(be[2], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[3], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[2], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleFirst()'
     * Case when there isn't a child with priority 0, and the behaviours with the highest
     * priority are blocked.
     */
    public void testScheduleFirstAny0Blocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        be[2].block();
        ppb.addSubBehaviour(be[2], 1);
        be[3].block();
        ppb.addSubBehaviour(be[3], 1);
        ppb.scheduleFirst();
        assertEquals(3, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4,0,3,2);
        assertSame(be[1], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[2], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[3], ppb.getReadyBehaviours().getElement(2).getBehaviour());
        assertSame(be[1], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleFirst()'
     * Case when there isn't a child with priority 0 and there is a runnable behaviour.
     */
    public void testScheduleFirstAny0NonBlocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        ppb.addSubBehaviour(be[2], 1);
        ppb.addSubBehaviour(be[3], 1);
        ppb.scheduleFirst();
        assertEquals(4, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4,0,2,0);
        assertSame(be[2], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[3], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[2], ppb.getCurrent());
    }
    
    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleFirst()'
     * Case when there aren't children.
     */
    public void testScheduleFirstEmpty() {
        ppb.scheduleFirst();
        checkLists(0, 0, 0, 0);
        assertNull(ppb.getCurrent());
    
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleFirst()'
     * Case when there is a child with priority 0 but is blocked.
     */
    public void testScheduleFirstSome0Blocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        be[2].block();
        ppb.addSubBehaviour(be[2], 1);
        ppb.addSubBehaviour(be[3], 1);
        be[4].block();
        ppb.addSubBehaviour(be[4], 0);
        ppb.scheduleFirst();
        assertEquals(4, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        assertEquals(0, ppb.getAllBehaviours().getElement(4).getCurrentPriority());
        checkLists(5, 0, 2, 2);
        assertSame(be[3], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[4], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[3], ppb.getCurrent());
    }
     
    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleFirst()'
     * Case when there is a child with priority 0 and is runnable.
     */
    public void testScheduleFirstSome0NonBlocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        ppb.addSubBehaviour(be[2], 1);
        ppb.addSubBehaviour(be[3], 0);
        ppb.scheduleFirst();
        assertEquals(5, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(0, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4, 0, 1, 0);
        assertSame(be[3], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[3], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleNext(boolean, int)'
     * Case when there isn't a child with priority 0, and the behaviours with the highest
     * priority are blocked.
     */
    public void testScheduleNextAny0Blocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        be[2].block();
        ppb.addSubBehaviour(be[2], 1);
        be[3].block();
        ppb.addSubBehaviour(be[3], 1);
        ppb.scheduleFirst();
        ppb.scheduleNext(false,1);
        assertEquals(1, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4, 0, 3, 2);
        assertSame(be[1], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[2], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[3], ppb.getReadyBehaviours().getElement(2).getBehaviour());
        assertSame(be[1], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleNext(boolean, int)'
     * Case when there isn't a child with priority 0 and there is a runnable behaviour.
     */
    public void testScheduleNextAny0NonBlocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        ppb.addSubBehaviour(be[2], 1);
        ppb.addSubBehaviour(be[3], 1);
        ppb.scheduleFirst();
        ppb.scheduleNext(false,1);
        assertEquals(4, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4, 0, 1, 0);
        assertSame(be[3], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[3], ppb.getCurrent());
        ppb.scheduleNext(false, 1);
        assertEquals(3, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4, 0, 3, 0);
        assertSame(be[1], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[2], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[3], ppb.getReadyBehaviours().getElement(2).getBehaviour());
        assertSame(be[1], ppb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleNext(boolean, int)'
     * Case when there is a child with priority 0 but is blocked.
     */
    public void testScheduleNextSome0Blocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        be[2].block();
        ppb.addSubBehaviour(be[2], 1);
        ppb.addSubBehaviour(be[3], 1);
        be[4].block();
        ppb.addSubBehaviour(be[4], 0);
        ppb.scheduleFirst();
        ppb.scheduleNext(false, 1);
        assertEquals(3, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        assertEquals(0, ppb.getAllBehaviours().getElement(4).getCurrentPriority());
        checkLists(5, 0, 4, 2);
        assertSame(be[1], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[2], ppb.getReadyBehaviours().getElement(1).getBehaviour());
        assertSame(be[3], ppb.getReadyBehaviours().getElement(2).getBehaviour());
        assertSame(be[4], ppb.getReadyBehaviours().getElement(3).getBehaviour());
        assertSame(be[1], ppb.getCurrent());
     }

    /*
     * Test method for 'jade.core.behaviours.ParallelPriorityBehaviour.scheduleNext(boolean, int)'
     * Case when there is a child with priority 0 but it is not blocked.
     */
    public void testScheduleNextSome0NonBlocked() {
        ppb.addSubBehaviour(be[0], 5);
        ppb.addSubBehaviour(be[1], 2);
        ppb.addSubBehaviour(be[2], 1);
        ppb.addSubBehaviour(be[3], 0);
        ppb.scheduleFirst();
        ppb.scheduleNext(false, 1);
        assertEquals(5, ppb.getAllBehaviours().getElement(0).getCurrentPriority());
        assertEquals(2, ppb.getAllBehaviours().getElement(1).getCurrentPriority());
        assertEquals(1, ppb.getAllBehaviours().getElement(2).getCurrentPriority());
        assertEquals(0, ppb.getAllBehaviours().getElement(3).getCurrentPriority());
        checkLists(4, 0, 1, 0);
        assertSame(be[3], ppb.getReadyBehaviours().getElement(0).getBehaviour());
        assertSame(be[3], ppb.getCurrent());
    }
}

