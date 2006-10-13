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

import examples.BehaviourExample;
import junit.framework.TestCase;

/**
 * Unit tests for the <code>EncapsulatedPriorityBehaviurList</code> class.
 * 
 * @author Juan A. Suarez Romero - University of A Coruna
 * @version $Date$ $Revision$
 */
public class EncapsulatedPriorityBehaviourListTest extends TestCase {

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.addElement(EncapsulatedPriorityBehaviour)'
     */
    public void testAddElement() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        epbl.addElement(epb1);
        assertEquals(1, epbl.size());
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        epbl.addElement(epb2);
        assertEquals(2, epbl.size());
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.getElement(int)'
     */
    public void testGetElement() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        assertSame(epb1, epbl.getElement(0));
        assertSame(epb2, epbl.getElement(1));
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when any of the behaviours in the list is runnable.
     */
    public void testPruneNotRunnableBehavioursAllNotRunnable() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        epbl.pruneNotRunnableBehaviours();
        assertTrue(epbl.isEmpty());
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.addElement(epb3);
        epb1.getBehaviour().block();
        epb2.getBehaviour().block();
        epb3.getBehaviour().block();
        epbl.pruneNotRunnableBehaviours();
        assertTrue(epbl.isEmpty());
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when all behaviours in the list are runnable.
     */
    public void testPruneNotRunnableBehavioursAllRunnable() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        epbl.pruneNotRunnableBehaviours();
        assertTrue(epbl.isEmpty());
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.addElement(epb3);
        epbl.pruneNotRunnableBehaviours();
        assertEquals(3, epbl.size());
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when the firsts behaviours in the list are not runnable, but the lasts are runnable.
     */
    public void testPruneNotRunnableBehavioursFirstNotRunnable() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        epbl.pruneNotRunnableBehaviours();
        assertTrue(epbl.isEmpty());
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.addElement(epb3);
        epb1.getBehaviour().block();
        epb3.getBehaviour().block();
        epbl.pruneNotRunnableBehaviours();
        assertEquals(2, epbl.size());
        assertSame(epb2, epbl.getElement(0));
        assertSame(epb3, epbl.getElement(1));
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when the firsts behaviours in the list are runnable.
     */
    public void testPruneNotRunnableBehavioursFirstRunnable() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        epbl.pruneNotRunnableBehaviours();
        assertTrue(epbl.isEmpty());
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.addElement(epb3);
        epb2.getBehaviour().block();
        epb3.getBehaviour().block();
        epbl.pruneNotRunnableBehaviours();
        assertEquals(3, epbl.size());    
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.removeElement(Behaviour)'
     */
    public void testRemoveElementBehaviour() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 0);
        BehaviourExample be = new BehaviourExample();
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.addElement(epb3);
        EncapsulatedPriorityBehaviour removed = epbl.removeElement(be);
        assertEquals(3, epbl.size());
        assertNull(removed);
        removed = epbl.removeElement(epb2.getBehaviour());
        assertEquals(2, epbl.size());
        assertSame(epb1, epbl.getElement(0));
        assertSame(epb3, epbl.getElement(1));
        assertSame(epb2, removed);
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.removeElement(EncapsulatedPriorityBehaviour)'
     */
    public void testRemoveElementEncapsulatedPriorityBehaviour() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 0);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.removeElement(epb3);
        assertEquals(2, epbl.size());
        epbl.removeElement(epb1);
        assertEquals(1, epbl.size());
        assertSame(epb2, epbl.getElement(0));
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.removeElement(int)'
     */
    public void testRemoveElementInt() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 0);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        epbl.addElement(epb3);
        epbl.removeElement(2);
        assertEquals(2, epbl.size());
        assertSame(epb1, epbl.getElement(0));
        assertSame(epb2, epbl.getElement(1));
        epbl.removeElement(0);
        assertEquals(1, epbl.size());
        assertSame(epb2, epbl.getElement(0));
        epbl.removeElement(0);
        assertTrue(epbl.isEmpty());
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.searchBehaviour(Behaviour)'
     */
    public void testSearchBehaviour() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 0);
        epbl.addElement(epb1);
        epbl.addElement(epb2);
        assertNull(epbl.searchBehaviour(epb3.getBehaviour()));
        assertSame(epb2, epbl.searchBehaviour(epb2.getBehaviour()));
    }

    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.searchMaxPriorityBehaviour()'
     */
    public void testSearchMaxPriorityBehaviour() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 3);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        EncapsulatedPriorityBehaviour epb4 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb5 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        epbl.addElement(epb1);
        assertEquals(epb1, epbl.searchMaxPriorityBehaviour());
        epbl.addElement(epb2);
        assertEquals(epb2, epbl.searchMaxPriorityBehaviour());
        epbl.addElement(epb3);
        assertEquals(epb3, epbl.searchMaxPriorityBehaviour());
        epbl.addElement(epb4);
        assertEquals(epb3, epbl.searchMaxPriorityBehaviour());
        epbl.addElement(epb5);
        assertEquals(epb3, epbl.searchMaxPriorityBehaviour());                
    }
    
    /*
     * Test method for 'jade.core.behaviours.EncapsulatedPriorityBehaviourList.searchMaxPriorityBehaviour(int)'
     */
    public void testSearchMaxPriorityBehaviourInt() {
        EncapsulatedPriorityBehaviourList epbl = new EncapsulatedPriorityBehaviourList();
        EncapsulatedPriorityBehaviour epb1 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 0);
        EncapsulatedPriorityBehaviour epb2 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 3);
        EncapsulatedPriorityBehaviour epb3 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 1);
        EncapsulatedPriorityBehaviour epb4 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 4);
        EncapsulatedPriorityBehaviour epb5 = new EncapsulatedPriorityBehaviour(new BehaviourExample(), 2);
        epbl.addElement(epb1);
        assertNull(epbl.searchMaxPriorityBehaviour(2));
        epbl.addElement(epb2);
        assertEquals(epb2, epbl.searchMaxPriorityBehaviour(2));
        epbl.addElement(epb3);
        assertEquals(epb2, epbl.searchMaxPriorityBehaviour(2));
        epbl.addElement(epb4);
        assertEquals(epb2, epbl.searchMaxPriorityBehaviour(2));
        epbl.addElement(epb5);
        assertEquals(epb5, epbl.searchMaxPriorityBehaviour(2));                
    }

}
