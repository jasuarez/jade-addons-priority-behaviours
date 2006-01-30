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

import jade.util.leap.Iterator;
import jade.util.leap.LinkedList;
import jade.util.leap.Serializable;

/**
 * A list of behaviours with priority.
 * 
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Date$ $Revision$
 */
class PriorityBehaviourList extends LinkedList implements Serializable {
    private static final long serialVersionUID = 1288302737310286387L;

    /**
     * Appends the specified behaviuor to the end of this list.
     * @param pb Behaviour to be appended to this list.
     */
    public void addElement (PriorityBehaviour pb) {
        add(pb);
    }
    
    /**
     * Removes the behaviour at the specified position in this list. Shifts any subsequent
     * elements to the left (subtracts one from their indices). 
     * @param index  The index of the behaviour to be removed.
     * @throws IndexOutOfBoundsException If the specified index is out of range
     * (index < 0 || index >= size()).
     */
    public void removeElement(int index) throws IndexOutOfBoundsException {
        remove(index);
    }
    
    /**
     * Removes the first occurrence of the specified behaviour in this list. If the list
     * does not contain the behaviour, it is unchanged.
     * @param pb Behaviour to be removed from this list, if present.
     * @return True if the list contained the specified behaviour.
     */
    public boolean removeElement(PriorityBehaviour pb) {
        return remove(pb);
    }
    
    /**
     * Returns the behaviour at the specified position in this list.
     * 
     * @param index Index of behaviour to return.
     * @return The behaviour at the specified position in this list.
     * @throws IndexOutOfBoundsException If the specified index is out of range
     * (index < 0 || index >= size()).
     */
    public PriorityBehaviour getElement(int index) throws IndexOutOfBoundsException {
        return (PriorityBehaviour) get(index);
    }
    
    /**
     * Removes all behaviours from the list until the first behaviour is runnable.
     * If there is not any runnable behaviour then the list is clear.
     */
    public void pruneNotRunnableBehaviours() {
        boolean runnableFound = false;
        
        while (!(runnableFound  || isEmpty())) {
            if (!getElement(0).isRunnable())
                remove(0);
            else
                runnableFound = true;
        }
    }
    
    /**
     * Searches this list for the behaviour with the current highest priority,
     * but considering only the behaviours which its current priority number is
     * greater or equal than a limit.
     * @param limit The limit for the priority.
     * @return The behaviour found, or null if none is found.
     */
    public PriorityBehaviour searchMaxPriorityBehaviour (int limit) {
        PriorityBehaviour maxBehaviour = null;
        Iterator iter = this.iterator();
        
        while (iter.hasNext()) {
            PriorityBehaviour pb = (PriorityBehaviour) iter.next();
            if (pb.getCurrentPriority() >= limit)
                if ((maxBehaviour == null) || (pb.getCurrentPriority() < maxBehaviour.getCurrentPriority()))
                    maxBehaviour = pb;
        }
        return maxBehaviour;
    }
    
    /**
     * Searches this list for the behaviour with the current highest priority.
     * @return The behaviour found, or null if this list is empty.
     */
    public PriorityBehaviour searchMaxPriorityBehaviour () {
        PriorityBehaviour maxBehaviour = null;
        Iterator iter = this.iterator();
        
        while (iter.hasNext()) {
            PriorityBehaviour pb = (PriorityBehaviour) iter.next();
            if ((maxBehaviour == null) || (pb.getCurrentPriority() < maxBehaviour.getCurrentPriority()))
                maxBehaviour = pb;
        }
        return maxBehaviour;
    }    
}
