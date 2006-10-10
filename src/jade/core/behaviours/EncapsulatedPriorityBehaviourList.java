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

import jade.util.leap.Iterator;
import jade.util.leap.LinkedList;
import jade.util.leap.Serializable;

/**
 * A list of behaviours with priority.
 * 
 * @author Juan A. Suarez Romero - University of A Coruna
 * @version $Date$ $Revision$
 */
class EncapsulatedPriorityBehaviourList extends LinkedList implements
        Serializable {
    private static final long serialVersionUID = 1288302737310286387L;

    /**
     * Appends the specified behaviuor to the end of this list.
     * 
     * @param epb
     *            the behaviour to be appended to this list
     */
    public void addElement(EncapsulatedPriorityBehaviour epb) {
        add(epb);
    }

    /**
     * Removes the behaviour at the specified position in this list. Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * 
     * @param index
     *            the index of the behaviour to be removed
     * @throws IndexOutOfBoundsException
     *             If the specified index is out of range (index < 0 || index >=
     *             size())
     */
    public void removeElement(int index) throws IndexOutOfBoundsException {
        remove(index);
    }

    /**
     * Removes the first occurrence of the specified behaviour in this list. If
     * the list does not contain the behaviour, it is unchanged.
     * 
     * @param epb
     *            the behaviour to be removed from this list, if present
     */
    public void removeElement(EncapsulatedPriorityBehaviour epb) {
        remove(epb);
    }

    /**
     * Removes the first occurrence of the element that encapsulates the
     * specified behaviour. Returns this element if the list contains it, or
     * <code>null</code> in other case.
     * 
     * @param b
     *            the encapsulated behaviour to be removed
     * @return the <code>EncapsulatedPriorityBehaviour</code> removed, or <code>null</code>
     *         if does not exist
     */
    public EncapsulatedPriorityBehaviour removeElement(Behaviour b) {
        Iterator it = this.iterator();
        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            if (epb.getBehaviour() == b) {
                it.remove();
                return epb;
            }
        }
        return null;
    }

    /**
     * Returns the behaviour at the specified position in this list.
     * 
     * @param index
     *            the index of behaviour to return
     * @return the behaviour at the specified position in this list
     * @throws IndexOutOfBoundsException
     *             If the specified index is out of range (index < 0 || index >=
     *             size())
     */
    public EncapsulatedPriorityBehaviour getElement(int index)
            throws IndexOutOfBoundsException {
        return (EncapsulatedPriorityBehaviour) get(index);
    }

    /**
     * Searches for the <code>EncapsulatedPriorityBehaviour</code> that
     * encapsulates the specified behaviour.
     * 
     * @param b
     *            the behaviour to be searched
     * @return the <code>EncapsulatedPriorityBehaviour</code> found,
     *          or <code>null</code> in other case
     */
    public EncapsulatedPriorityBehaviour searchBehaviour(Behaviour b) {
        Iterator it = this.iterator();

        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            if (epb.getBehaviour() == b)
                return epb;
        }
        return null;
    }

    /**
     * Removes all behaviours from the list until the first behaviour is
     * runnable. If there is not any runnable behaviour then the list is clear.
     */
    public void pruneNotRunnableBehaviours() {
        boolean runnableFound = false;

        while (!(runnableFound || isEmpty())) {
            if (!getElement(0).getBehaviour().isRunnable())
                remove(0);
            else
                runnableFound = true;
        }
    }

    /**
     * Searches this list for the behaviour with the dynamic highest priority,
     * but considering only the behaviours which its dynamic priority number is
     * as higher as the limit.
     * 
     * @param limit
     *            the limit for the priority
     * @return the behaviour found, or <code>null</code> if none is found
     */
    public EncapsulatedPriorityBehaviour searchMaxPriorityBehaviour(int limit) {
        EncapsulatedPriorityBehaviour maxBehaviour = null;
        Iterator it = this.iterator();

        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            if (epb.getDynamicPriority() >= limit)
                if ((maxBehaviour == null)
                        || (epb.getDynamicPriority() < maxBehaviour
                                .getDynamicPriority()))
                    maxBehaviour = epb;
        }
        return maxBehaviour;
    }

    /**
     * Searches this list for the behaviour with the dynamic highest priority.
     * 
     * @return the behaviour found, or <code>null</code> if this list is empty
     */
    public EncapsulatedPriorityBehaviour searchMaxPriorityBehaviour() {
        EncapsulatedPriorityBehaviour maxBehaviour = null;
        Iterator it = this.iterator();

        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            if ((maxBehaviour == null)
                    || (epb.getDynamicPriority() < maxBehaviour
                            .getDynamicPriority()))
                maxBehaviour = epb;
        }
        return maxBehaviour;
    }
}
