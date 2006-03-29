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
import examples.AgentExample;
import examples.BehaviourExample;
import junit.framework.TestCase;

/**
 * Unit tests for the <code>SequentialPriorityBehaviour</code> class, when we do not
 * skip the blocked behaviours (default).
 * 
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Date$ $Revision$
 */
public class SequentialPriorityBehaviourNoSkipTest extends TestCase {
    private BehaviourExample[] be;
    private SequentialPriorityBehaviour spb;
    
    /**
     * Checks the expected values for the sizes of lists.
     * 
     * @param expectedAll Expected size for <code>allBehaviours</code> list.
     * @param expectedDone Expected size for <code>doneBehaviours</code> list.
     * @param expectedReady Expected size for <code>readyBehaviours</code> list.
     * @param expectedBlocked Expected value for <code>numBlockedBehaviours</code>.
     * 
     */
    private void checkLists(int expectedAll, int expectedDone, int expectedReady, int expectedBlocked) {
        assertEquals(expectedAll, spb.getAllBehaviours().size());
        assertEquals(expectedDone, spb.getDoneBehaviours().size());
        assertEquals(expectedReady, spb.readyBehaviours.size());
        assertEquals(expectedBlocked, spb.getNumBlockedBehaviours());
        Iterator it = spb.getAllBehaviours().iterator();
        while (it.hasNext()) {
            assertTrue(spb.getAsListChildren().contains(((EncapsulatedPriorityBehaviour)it.next()).getBehaviour()));
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
        spb = new SequentialPriorityBehaviour(new AgentExample(), false);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.addSubBehaviour(Behaviour)'
     */
    public void testAddSubBehaviourBehaviour() {
        spb.addSubBehaviour(be[0]);
        assertSame(spb, be[0].getParent());
        spb.addSubBehaviour(be[1]);
        spb.addSubBehaviour(be[2]);
        spb.addSubBehaviour(be[3]);
        checkLists(4,0,0,0);
        be[4].block();
        spb.addSubBehaviour(be[4]);
        checkLists(5,0,0,1);
        spb.addSubBehaviour(be[5]);
        checkLists(6,0,0,1);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.addSubBehaviour(Behaviour, int)'
     * Case when we add a behaviour with a priority lesser than the dynamic.
     */
    public void testAddSubBehaviourBehaviourIntLesser() {
        spb.addSubBehaviour(be[0], 3);
        spb.addSubBehaviour(be[1], 3);
        spb.addSubBehaviour(be[2], 3);
        checkLists(3,0,0,0);
        spb.scheduleFirst();
        checkLists(3,0,3,0);
        spb.addSubBehaviour(be[3], 6);
        spb.scheduleNext(false, 1);
        checkLists(4,0,3,0);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.addSubBehaviour(Behaviour, int)'
     * Case when we add a behaviour and the CompositeBehaviour is blocked.
     */
    public void testAddSubBehaviourBehaviourIntAllBlocked() {
        be[0].block();
        spb.addSubBehaviour(be[0], 2);
        be[1].block();
        spb.addSubBehaviour(be[1], 1);
        be[2].block();
        spb.addSubBehaviour(be[2], 2);
        spb.action();
        checkLists(3,0,1,3);
        assertFalse(spb.isRunnable());
        assertSame(spb.getCurrent(), be[1]);
        spb.addSubBehaviour(be[3], 3);
        spb.action();
        checkLists(4,0,1,3);
        assertFalse(spb.isRunnable());
        assertSame(spb.getCurrent(), be[1]);
        spb.addSubBehaviour(be[4], 0);
        spb.action();
        checkLists(5,0,1,3);
        assertTrue(spb.isRunnable());
        assertSame(spb.getCurrent(), be[4]);
    }
    
    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.addSubBehaviour(Behaviour, int)'
     */
    public void testAddSubBehaviourBehaviourIntGreater() {
        spb.addSubBehaviour(be[0], 3);
        spb.addSubBehaviour(be[1], 3);
        spb.addSubBehaviour(be[2], 3);
        checkLists(3,0,0,0);
        spb.scheduleFirst();
        checkLists(3,0,3,0);
        spb.addSubBehaviour(be[3], 2);
        spb.scheduleNext(false, 1);
        checkLists(4,0,1,0);
    }
 
    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.changePriority(Behaviour, int)'
     */
    public void testChangePriority() {
        spb.addSubBehaviour(be[0], 3);
        spb.addSubBehaviour(be[1], 4);
        spb.addSubBehaviour(be[2], 4);
        spb.action();
        assertSame(spb.getCurrent(), be[0]);
        spb.changePriority(be[0], 2);
        spb.action();
        assertSame(spb.getCurrent(), be[0]);
        spb.changePriority(be[1], 3);
        spb.action();
        assertSame(spb.getCurrent(), be[0]);
        spb.changePriority(be[2], 1);
        spb.action();
        assertSame(spb.getCurrent(), be[2]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.checkTermination(boolean, int)'
     */
    public void testCheckTermination() {
        spb.addSubBehaviour(be[0], 2);
        spb.addSubBehaviour(be[1], 1);
        spb.addSubBehaviour(be[2], 4);
        for (int i = 0; i < 5*3-1; i++) {
            spb.action();
            //assertFalse(spb.checkTermination(spb.getCurrent().done(), spb.getCurrent().onEnd()));
        }
        assertFalse(spb.checkTermination(spb.getCurrent().done(), spb.getCurrent().onEnd()));
        spb.scheduleNext(false, 1);
        spb.getCurrent().action();
        assertTrue(spb.checkTermination(spb.getCurrent().done(), spb.getCurrent().onEnd()));
    }
    
    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.checkTermination(boolean, int)'
     * Case when there aren't children.
     */
    public void testCheckTerminationEmpty() {
        spb.action();
        assertTrue(spb.done());
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.getChildren()'
     */
    public void testGetChildren() {
        spb.addSubBehaviour(be[0]);
        spb.addSubBehaviour(be[1]);
        spb.addSubBehaviour(be[2]);
        be[3].block();   
        spb.addSubBehaviour(be[3]);
        Collection c = spb.getChildren();
        assertEquals(4, c.size());
        Iterator it = c.iterator();
        for (int i = 0; i<4; i++)
            assertSame(be[i], it.next());
        assertFalse(it.hasNext());
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.getCurrent()'
     */
    public void testGetCurrent() {
        spb.addSubBehaviour(be[0], 3);
        spb.addSubBehaviour(be[1], 1);
        spb.addSubBehaviour(be[2], 4);
        spb.action();
        assertSame(spb.getCurrent(), be[1]);
        spb.action();
        spb.action();
        spb.action();
        spb.action();
        spb.action();
        assertSame(spb.getCurrent(), be[0]);
        spb.action();
        spb.action();
        spb.action();
        spb.action();
        spb.action();
        assertSame(spb.getCurrent(), be[2]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.checkTermination(boolean, int)'
     * Case when there aren only one runnable behaviour.
     */
    public void testCheckTerminationOneRunnable() {
        be[0].block();
        spb.addSubBehaviour(be[0], 2);
        spb.addSubBehaviour(be[1], 1);
        be[2].block();
        spb.addSubBehaviour(be[2], 2);
        spb.action();
        checkLists(3,0,1,2);
        assertSame(spb.getCurrent(), be[1]);
        spb.action();
        spb.action();
        spb.action();
        spb.scheduleNext(false,1);
        spb.getCurrent().action();
        assertFalse(spb.checkTermination(spb.getCurrent().done(), spb.getCurrent().onEnd()));
        assertFalse(spb.isRunnable());
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.removeSubBehaviour(Behaviour)'
     * Case when we remove a behaviour that is not the current.
     */
    public void testRemoveSubBehaviourOther() {
        be[0].block();
        spb.addSubBehaviour(be[0], 2);
        spb.addSubBehaviour(be[1], 1);
        spb.addSubBehaviour(be[2], 2);
        spb.addSubBehaviour(be[3], 3);
        spb.action();
        checkLists(4,0,1,1);
        assertSame(spb.getCurrent(), be[1]);
        spb.removeSubBehaviour(be[3]);
        spb.action();
        this.checkLists(3,0,1,1);
        assertSame(spb.getCurrent(), be[1]);
        spb.removeSubBehaviour(be[0]);
        spb.action();
        checkLists(2,0,1,0);
        assertSame(spb.getCurrent(), be[1]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.removeSubBehaviour(Behaviour)'
     * Case when we remove the current executing behaviour.
     */
    public void testRemoveSubBehaviourCurrent() {
        be[0].block();
        spb.addSubBehaviour(be[0], 2);
        spb.addSubBehaviour(be[1], 1);
        spb.addSubBehaviour(be[2], 2);
        spb.addSubBehaviour(be[3], 3);
        spb.action();
        checkLists(4,0,1,1);
        assertSame(spb.getCurrent(), be[1]);
        spb.removeSubBehaviour(be[1]);
        spb.action();
        this.checkLists(3,0,2,1);
        assertSame(spb.getCurrent(), be[0]);
        spb.removeSubBehaviour(be[0]);
        spb.action();
        checkLists(2,0,1,0);
        assertSame(spb.getCurrent(), be[2]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.removeSubBehaviour(Behaviour)'
     * Case when we remove a behaviour and there is only one runnable behaviour.
     */
    public void testRemoveSubBehaviourOneRunnable() {
        be[0].block();
        spb.addSubBehaviour(be[0], 2);
        be[1].block();
        spb.addSubBehaviour(be[1], 1);
        be[2].block();
        spb.addSubBehaviour(be[2], 2);
        spb.addSubBehaviour(be[3], 3);
        spb.action();
        checkLists(4,0,1,3);
        assertSame(spb.getCurrent(), be[1]);
        assertFalse(spb.isRunnable());
        spb.removeSubBehaviour(be[3]);
        spb.action();
        this.checkLists(3,0,1,3);
        assertFalse(spb.isRunnable());
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.reset()'
     */
    public void testReset() {
        spb.addSubBehaviour(be[0], 2);
        spb.addSubBehaviour(be[1], 4);
        spb.addSubBehaviour(be[2], 4);
        for (int i = 0;  i < 8; i++)
            spb.action();
        checkLists(2, 1, 2, 0);
        assertSame(spb.getCurrent(), be[1]);
        spb.reset();
        spb.action();
        checkLists(3, 0, 1, 0);
        assertSame(spb.getCurrent(), be[0]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.scheduleFirst()'
     * Case when there aren't children.
     */
    public void testScheduleFirstEmpty() {
        spb.scheduleFirst();
        checkLists(0,0,0,0);
        assertNull(spb.getCurrent());
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.scheduleFirst()'
     * Case when the behaviour to be executed is blocked.
     */
    public void testScheduleFirstIsBlocked() {
        be[0].block();
        spb.addSubBehaviour(be[0], 3);
        spb.addSubBehaviour(be[1], 4);
        be[3].block();
        spb.addSubBehaviour(be[3], 4);
        spb.scheduleFirst();
        checkLists(3,0,1,2);
        assertSame(spb.getCurrent(),be[0]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.scheduleFirst()'
     * Case when the behaviour to be executed is runnable.
     */
    public void testScheduleFirstIsRunnable() {
        be[0].block();
        spb.addSubBehaviour(be[0], 3);
        spb.addSubBehaviour(be[1], 4);
        be[3].block();
        spb.addSubBehaviour(be[3], 4);
        spb.addSubBehaviour(be[4], 1);
        spb.scheduleFirst();
        checkLists(4,0,1,2);
        assertSame(spb.getCurrent(),be[4]);
        assertTrue(spb.getCurrent().isRunnable());
    }
    
    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.scheduleNext(boolean, int)'
     */
    public void testScheduleNext() {
        spb.addSubBehaviour(be[0], 2);
        spb.addSubBehaviour(be[1], 4);
        be[2].block();
        spb.addSubBehaviour(be[2], 2);
        spb.addSubBehaviour(be[3], 3);
        spb.addSubBehaviour(be[4], 3);
        spb.scheduleFirst();
        spb.scheduleNext(false,1);
        spb.action();
        checkLists(5,0,2,1);
        assertSame(spb.getCurrent(), be[0]);
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        checkLists(4,1,1,1);
        spb.action();
        checkLists(4,1,1,1);
        assertSame(spb.getCurrent(), be[2]);
        spb.scheduleNext(true, 1);
        be[2].reset();
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        assertSame(spb.getCurrent(), be[2]);
        spb.scheduleNext(false,1);
        spb.action();
        spb.scheduleNext(false,1);
        spb.action();
        assertSame(spb.getCurrent(), be[3]);
        be[3].block();
        spb.action();
        assertSame(spb.getCurrent(), be[3]);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.SequentialPriorityBehaviour()'
     */
    public void testSequentialPriorityBehaviour() {
        SequentialPriorityBehaviour beh = new SequentialPriorityBehaviour();
        assertNull(beh.myAgent);
        assertFalse(beh.skipBlocked);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.SequentialPriorityBehaviour(Agent)'
     */
    public void testSequentialPriorityBehaviourAgent() {
        AgentExample ae = new AgentExample();
        SequentialPriorityBehaviour beh = new SequentialPriorityBehaviour(ae);
        assertSame(beh.myAgent, ae);
        assertFalse(beh.skipBlocked);
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.SequentialPriorityBehaviour(Agent, boolean)'
     */
    public void testSequentialPriorityBehaviourAgentBoolean() {
        AgentExample ae = new AgentExample();
        SequentialPriorityBehaviour beh = new SequentialPriorityBehaviour(ae, true);
        assertSame(beh.myAgent, ae);
        assertTrue(beh.skipBlocked);        
    }

    /*
     * Test method for 'jade.core.behaviours.SequentialPriorityBehaviour.SequentialPriorityBehaviour(boolean)'
     */
    public void testSequentialPriorityBehaviourBoolean() {
        SequentialPriorityBehaviour beh = new SequentialPriorityBehaviour(true);
        assertNull(beh.myAgent);
        assertTrue(beh.skipBlocked);
    }

}
