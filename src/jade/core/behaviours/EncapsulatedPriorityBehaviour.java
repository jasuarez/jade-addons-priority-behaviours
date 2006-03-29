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

import jade.core.Agent;
import jade.util.leap.Serializable;

/**
 * A special class which acts as a wrapper for a <code>Behaviour</code> in
 * order to add priorities to it.
 * 
 * The priority of a behaviour is represented by a number greater or equal
 * than 0. The lesser this number is, the higher is the priority of the
 * behaviour. So a behaviour with a priority 1 has more priority than a
 * behaviour with priority 2; and the last has twice priority than a behaviour
 * with priority 4.
 * 
 * There are two priorities: static priority and dynamic priority. The former
 * is the priority that the programmer assigns to the <code>Behaviour</code>.
 * The dynamic priority is a priority that is managed dynamically by the system,
 * with a value between 0 and the static priority.
 * 
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Revision$
 */
class EncapsulatedPriorityBehaviour implements Serializable {
    private static final long serialVersionUID = 548619304232112354L;

    /**
     * The encapsulated behaviour. 
     */
    private Behaviour encapsulatedBehaviour;

    /**
     * The static priority of the behaviour.
     */
    private int staticPriority;

    /**
     * The dynamic priority of the behaviour. This priority is initialized to
     * <code>staticPriority</code> and is incremented in order to avoid the
     * starvation of the current behaviour.
     */
    private int dynamicPriority;

    /**
     * Encapsulates a behaviour. It does not set the agent owning this behaviour
     * object.
     * 
     * @param b
     *            The behaviour to be encapsulated.
     * @param priority
     *            The priority of this behaviour object.
     */
    public EncapsulatedPriorityBehaviour(Behaviour b, int priority) {
        super();
        if (priority < 0)
            staticPriority = 0;
        else
            staticPriority = priority;
        dynamicPriority = staticPriority;
        encapsulatedBehaviour = b;
    }

    /**
     * Set the agent owning this behaviour.
     * 
     * @see jade.core.behaviours.Behaviour#setAgent(jade.core.Agent)
     */
    public void setAgent(Agent a) {
        encapsulatedBehaviour.setAgent(a);
    }

    /**
     * Returns the encapsulated behaviour.
     * @return
     */
    public Behaviour getBehaviour() {
        return encapsulatedBehaviour;
    }

    /**
     * Returns the static priority of this behaviour object.
     * 
     * @return The priority of this behaviour object.
     */
    public int getStaticPriority() {
        return staticPriority;
    }

    /**
     * Establish a new priority for this behaviour object. Note that the dynamic
     * priority of the behaviour is not changed at all.
     * 
     * @param newPriority
     *            The new priority for this behaviour object.
     * @see ParallelPriorityBehaviour#changePriority
     */
    protected void setStaticPriority(int newPriority) {
        if (newPriority < 0)
            staticPriority = 0;
        else
            staticPriority = newPriority;
    }

    /**
     * Returns the dynamic priority of this behaviour object.
     * 
     * @return The dynamic priority of this behaviour object.
     */
    public int getDynamicPriority() {
        return dynamicPriority;
    }

    /**
     * Increments the dynamic priority of this behaviour object.
     * 
     * @param increment
     *            The increment of the dynamic priority, so the new current
     *            priority is (old dynamic priority - increment).
     */
    protected void incDynamicPriority(int increment) {
        dynamicPriority -= increment;
        if (dynamicPriority < 0)
            dynamicPriority = 0;
    }

    /**
     * Establish the dynamic priority of this behaviour object to its static
     * priority.
     */
    protected void resetDynamicPriority() {
        dynamicPriority = staticPriority;
    }
}
