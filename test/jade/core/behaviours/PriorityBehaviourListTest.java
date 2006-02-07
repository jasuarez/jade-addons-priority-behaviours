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

import examples.PriorityBehaviourExample;
import junit.framework.TestCase;

public class PriorityBehaviourListTest extends TestCase {
    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.addElement(PriorityBehaviour)'
     */
    public void testAddElement() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.addElement(new PriorityBehaviourExample());
        assertEquals(1, pbl.size());
        pbl.addElement(new PriorityBehaviourExample());
        assertEquals(2, pbl.size());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.removeElement(int)'
     */
    public void testRemoveElementInt() {
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb3 = new PriorityBehaviourExample();
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        pbl.addElement(pb2);
        pbl.addElement(pb3);
        pbl.removeElement(3);
        assertEquals(3, pbl.size());
        assertSame(pb0, pbl.getElement(0));
        assertSame(pb1, pbl.getElement(1));
        assertSame(pb2, pbl.getElement(2));
        pbl.removeElement(1);
        assertEquals(2, pbl.size());
        assertSame(pb0, pbl.getElement(0));
        assertSame(pb2, pbl.getElement(1));
        pbl.removeElement(0);
        assertEquals(1, pbl.size());
        assertSame(pb2, pbl.getElement(0));
        pbl.removeElement(0);
        assertTrue(pbl.isEmpty());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.removeElement(PriorityBehaviour)'
     */
    public void testRemoveElementPriorityBehaviour() {
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb3 = new PriorityBehaviourExample();
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        pbl.addElement(pb2);
        pbl.addElement(pb3);
        pbl.removeElement(pb3);
        assertEquals(3, pbl.size());
        assertSame(pb0, pbl.getElement(0));
        assertSame(pb1, pbl.getElement(1));
        assertSame(pb2, pbl.getElement(2));
        pbl.removeElement(pb1);
        assertEquals(2, pbl.size());
        assertSame(pb0, pbl.getElement(0));
        assertSame(pb2, pbl.getElement(1));
        pbl.removeElement(pb0);
        assertEquals(1, pbl.size());
        assertSame(pb2, pbl.getElement(0));
        pbl.removeElement(pb2);
        assertTrue(pbl.isEmpty());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.getElement(int)'
     */
    public void testGetElement() {
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        assertSame(pb0, pbl.getElement(0));
        assertSame(pb1, pbl.getElement(1));
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when all behaviours in the list are runnable.
     */
    public void testPruneNotRunnableBehavioursAllRunnable() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.pruneNotRunnableBehaviours();
        assertTrue(pbl.isEmpty());
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        pbl.addElement(pb2);
        pbl.pruneNotRunnableBehaviours();
        assertEquals(3, pbl.size());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when any of the behaviours in the list is runnable.
     */
    public void testPruneNotRunnableBehavioursAllNotRunnable() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.pruneNotRunnableBehaviours();
        assertTrue(pbl.isEmpty());
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        pbl.addElement(pb2);
        pb0.block();
        pb1.block();
        pb2.block();
        pbl.pruneNotRunnableBehaviours();
        assertTrue(pbl.isEmpty());
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when the firsts behaviours in the list are runnable.
     */
    public void testPruneNotRunnableBehavioursFirstRunnable() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.pruneNotRunnableBehaviours();
        assertTrue(pbl.isEmpty());
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        pbl.addElement(pb2);
        pb1.block();
        pb2.block();
        pbl.pruneNotRunnableBehaviours();
        assertEquals(3, pbl.size());
    }
    
    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.pruneNotRunnableBehaviours()'
     * Tests when the firsts behaviours in the list are not runnable, but the lasts are runnable.
     */
    public void testPruneNotRunnableBehavioursFirstNotRunnable() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        pbl.pruneNotRunnableBehaviours();
        assertTrue(pbl.isEmpty());
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample();
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample();
        pbl.addElement(pb0);
        pbl.addElement(pb1);
        pbl.addElement(pb2);
        pb0.block();
        pb2.block();
        pbl.pruneNotRunnableBehaviours();
        assertEquals(2, pbl.size());
        assertSame(pb1, pbl.getElement(0));
        assertSame(pb2, pbl.getElement(1));
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.searchMaxPriorityBehaviour(int)'
     */
    public void testSearchMaxPriorityBehaviourInt() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        assertNull(pbl.searchMaxPriorityBehaviour(2));
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample(0);
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample(3);
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample(1);
        PriorityBehaviourExample pb3 = new PriorityBehaviourExample(4);
        PriorityBehaviourExample pb4 = new PriorityBehaviourExample(2);
        pbl.addElement(pb0);
        assertNull(pbl.searchMaxPriorityBehaviour(2));
        pbl.addElement(pb1);
        assertEquals(pb1, pbl.searchMaxPriorityBehaviour(2));
        pbl.addElement(pb2);
        assertEquals(pb1, pbl.searchMaxPriorityBehaviour(2));
        pbl.addElement(pb3);
        assertEquals(pb1, pbl.searchMaxPriorityBehaviour(2));
        pbl.addElement(pb4);
        assertEquals(pb4, pbl.searchMaxPriorityBehaviour(2));
    }

    /*
     * Test method for 'jade.core.behaviours.PriorityBehaviourList.searchMaxPriorityBehaviour()'
     */
    public void testSearchMaxPriorityBehaviour() {
        PriorityBehaviourList pbl = new PriorityBehaviourList();
        assertNull(pbl.searchMaxPriorityBehaviour());
        PriorityBehaviourExample pb0 = new PriorityBehaviourExample(3);
        PriorityBehaviourExample pb1 = new PriorityBehaviourExample(2);
        PriorityBehaviourExample pb2 = new PriorityBehaviourExample(1);
        PriorityBehaviourExample pb3 = new PriorityBehaviourExample(4);
        PriorityBehaviourExample pb4 = new PriorityBehaviourExample(1);
        pbl.addElement(pb0);
        assertEquals(pb0, pbl.searchMaxPriorityBehaviour());
        pbl.addElement(pb1);
        assertEquals(pb1, pbl.searchMaxPriorityBehaviour());
        pbl.addElement(pb2);
        assertEquals(pb2, pbl.searchMaxPriorityBehaviour());
        pbl.addElement(pb3);
        assertEquals(pb2, pbl.searchMaxPriorityBehaviour());
        pbl.addElement(pb4);
        assertEquals(pb2, pbl.searchMaxPriorityBehaviour());
    }
}
