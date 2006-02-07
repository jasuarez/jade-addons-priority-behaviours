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
import jade.util.leap.Collection;
import jade.util.leap.Iterator;

/**
 * Composite behaviour with priority based children scheduling. It is a
 * <code>CompositeBehaviour</code> that executes its children behaviours using
 * their priorities, ant it terminates when all children are done.
 * 
 * The priorities of the children are represented by a number greater or equal
 * than zero. The lesser is this number, the higher is the priority of the
 * behaviour. The priorities are incrememented during time to avoid its
 * starvation. The exception is when there is any behaviour with a priority 0,
 * in which case this increment is stopped.
 * 
 * When a behaviour either has priority 0 or reaches priority 0 is inserted in a
 * list of ready behaviours.
 * 
 * Roughly speaking, the scheduler runs as follows:
 *      (1) If there are no child behaviours then this
 *          <code>CompositeBehaviour<code> is done.
 *      (2) If there are not any runnable behaviour (i.e., all the
 *          children is blocked), then block this behaviour.
 *      (3) While there are behaviours in the ready behaviours list, picks the
 *          first one, and if it is runnable then execute it. Drop it from
 *          this list.
 *      (4) If the ready behaviours list is empty, then searches all
 *          behaviours, either blocked or not, with priority 0 and insert
 *          they in the ready behaviours list. Resets its current priority.
 *      (4) If the ready behaviours list is empty, and there is not any
 *          behaviour with priority 0, then renices all behaviours. That is,
 *          searches for the behaviour with the lesser priority number, and
 *          increment all the priorities by  this number (i.e., rests to the
 *          priority number this number). Go to step 4.
 *      (5) Go to the step 1.
 *
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Date$ $Revision$
 */
public class PriorityBasedCompositeBehaviour extends CompositeBehaviour {
    private static final long serialVersionUID = 4668737902684655038L;

    /**
     * The default priority for the newly created behaviours, if none is
     * specified.
     */
    public static final int DEFAULT_PRIORITY = 5;

    /**
     * The priority number of the behaviour with the highest priority.
     */
    protected int maxPriority = 0;

    /**
     * A list with all children. This list does not contain the done children.
     */
    protected PriorityBehaviourList allBehaviours = new PriorityBehaviourList();

    /**
     * A list with all children with a current priority number of 0.
     * The head of this list is the behaviour to be executed.
     */
    protected PriorityBehaviourList readyBehaviours = new PriorityBehaviourList();

    /**
     * A list with all children that are done. 
     */
    protected PriorityBehaviourList doneBehaviours = new PriorityBehaviourList();
    
    /**
     * Then number of children behaviours that are no runnable. 
     */
    protected int numBlockedBehaviours = 0;

    /**
     * Default constructor. Creates a <code>CompositeBehaviour</code> without
     * setting the owner agent, and executes its children in a priority-based
     * manner, and it terminates when all children are done.  
     */
    public PriorityBasedCompositeBehaviour() {
        super();
    }

    /**
     * Creates a <code>PriorityBasedCompositeBehaviour</code> setting the
     * owner agent.
     * @param a
     */
    public PriorityBasedCompositeBehaviour(Agent a) {
        super(a);
    }

    /**
     * Prepares the first child to execute.
     * @see jade.core.behaviours.CompositeBehaviour#scheduleFirst()
     */
    protected void scheduleFirst() {
        readyBehaviours.clear();
        // The first time we need to search the behaviour with the
        // highest priority
        PriorityBehaviour mpb = allBehaviours.searchMaxPriorityBehaviour();
        if (mpb != null)
            maxPriority = mpb.getCurrentPriority();
        prepareNextBehaviour();
    }

    /**
     * Prepares the next child to execute.
     * @see jade.core.behaviours.CompositeBehaviour#scheduleNext(boolean, int)
     */
    protected void scheduleNext(boolean currentDone, int currentResult) {
        // The first behaviour has been executed, so we removed it.
        readyBehaviours.removeElement(0);
        prepareNextBehaviour();
    }

    /**
     * Prepares the next behaviour to be executed. That is, renice all
     * behaviours until either there is some runnable behaviour with
     * priority 0, or all behaviours are blocked. 
     */
    private void prepareNextBehaviour() {
        if (readyBehaviours.isEmpty())
            renice();
        // If there are some runnable behaviour
        if (numBlockedBehaviours < allBehaviours.size()) {
            readyBehaviours.pruneNotRunnableBehaviours();
            // This happens when all behaviours with priority 0
            // are blocked.
            if ((readyBehaviours.isEmpty()) && (maxPriority == 0))
                maxPriority = allBehaviours.searchMaxPriorityBehaviour(1)
                        .getCurrentPriority();
            while (readyBehaviours.isEmpty()) {
                renice();
                readyBehaviours.pruneNotRunnableBehaviours();
            }
        }
    }

    /**
     * Returns true if this behaviour must be terminated.
     * @see jade.core.behaviours.CompositeBehaviour#checkTermination(boolean, int)
     */
    protected boolean checkTermination(boolean currentDone, int currentResult) {
        if (currentDone) {
            PriorityBehaviour current = readyBehaviours.getElement(0);
            allBehaviours.removeElement(current);
            current.setParent(null);
            doneBehaviours.addElement(current);
        }
        return allBehaviours.isEmpty();
    }

    /**
     * Get the current child to be executed.
     * @see jade.core.behaviours.CompositeBehaviour#getCurrent()
     */
    protected Behaviour getCurrent() {
        Behaviour b = null;
        try {
            b = readyBehaviours.getElement(0);
        } catch (IndexOutOfBoundsException ioobe) {
            // Just do nothing. Null will be returned
        }
        return b;
    }

    /**
     * Return a Collection view of the children of this behaviour.
     * @see jade.core.behaviours.CompositeBehaviour#getChildren()
     */
    public Collection getChildren() {
        return allBehaviours;
    }

    /**
     * Add a behaviour to this
     * <code>PriorityBasedCompositeBehaviour</code>.
     * @param pb The behaviour to be added.
     */
    public void addSubBehaviour(PriorityBehaviour pb) {
        pb.setParent(this);
        pb.setAgent(myAgent);
        allBehaviours.addElement(pb);
        if (pb.isRunnable()) {
            // If all previous children were blocked (this Priority-based
            // Behaviour
            // was blocked too), restart this PriorityBasedCompositeBehaviour
            // and notify upwards
            if (!isRunnable()) {
                myEvent.init(true, NOTIFY_UP);
                super.handle(myEvent);
                if (myAgent != null)
                    myAgent.notifyRestarted(this);
                // Also reset the currentExecuted flag so that a runnable
                // child will be scheduled for execution
                currentExecuted = true;
            }
        }
        else
                numBlockedBehaviours++;
    }
    
    /**
     * Remove a behaviour from this
     * <code>ParallelBehaviour</code>.
     * @param pb The behaviur to be removed.
     */
    public void removeSubBehaviour(PriorityBehaviour pb) {
        if (allBehaviours.removeElement(pb)) {
            pb.setParent(null);
            // Case when the behaviour to be removed has current priority 0 but it is
            // not the behaviour which calls this method. In this last case the behaviour
            // will be removed on the next schedule.
            if ((!readyBehaviours.isEmpty()) && (readyBehaviours.getElement(0) != pb))
                readyBehaviours.removeElement(pb);
            if (!pb.isRunnable())
                numBlockedBehaviours--;
            // Perhaps there are a new behaviour with the highest priority.
            if (pb.getCurrentPriority() == maxPriority) {
                PriorityBehaviour mpb = allBehaviours.searchMaxPriorityBehaviour();
                if (mpb != null)
                    maxPriority = mpb.getCurrentPriority();
            }
            // If some children still exist and they are all blocked,
            // block this PriorityBasedCompositeBehaviour and notify upwards
            if ((!allBehaviours.isEmpty()) && (numBlockedBehaviours == allBehaviours.size())) {
                myEvent.init(false, NOTIFY_UP);
                super.handle(myEvent);
            }
        }
    }

    /**
     * Restores this behaviour to its initial state.
     * @see jade.core.behaviours.Behaviour#reset()
     */
    public void reset() {
        numBlockedBehaviours = 0;
        readyBehaviours.clear();
        // First restores the done behaviours.
        while (!doneBehaviours.isEmpty()) {
            PriorityBehaviour pb = doneBehaviours.getElement(0);
            doneBehaviours.removeElement(0);
            pb.setParent(this);
            allBehaviours.addElement(pb);
        }
        // Now resets each child.
        Iterator iter = allBehaviours.iterator();
        while (iter.hasNext()) {
            PriorityBehaviour pb = (PriorityBehaviour) iter.next();
            pb.resetCurrentPriority();
        }
        super.reset();
    }
    
    /**
     * Handler to manage the block/restart events.
     * @see jade.core.behaviours.Behaviour#handle(jade.core.behaviours.Behaviour.RunnableChangedEvent)
     */
    protected void handle(RunnableChangedEvent rce) {
        // This method may be executed by an auxiliary thread posting
        // a message into the Agent's message queue --> it must be
        // synchronized with sub-behaviour additions/removal.
        synchronized (allBehaviours) {
            // Upwards notification
            if(rce.isUpwards()) {
                Behaviour b = rce.getSource();                
                // If the event is from this behaviour, set the new 
                // runnable state and notify upwards.
                if (b == this) {
                    super.handle(rce);
                }
                else {
                    // Is neccesary to cast from "b" to "PriorityBehaviour"? I am not sure.
                    if (allBehaviours.contains(b)) {
                        // If the event is from a child -->
                        if(rce.isRunnable()) {
                            // If this is a restart, remove the child from the
                            // blocked behaviours
                            numBlockedBehaviours--;
                            // Only if all children were blocked (this ParallelBehaviour was
                            // blocked too), restart this ParallelBehaviour and notify upwards
                            if (!isRunnable()) {
                                myEvent.init(true, NOTIFY_UP);
                                super.handle(myEvent);
                                // Also reset the currentExecuted flag so that a runnable
                                // child will be scheduled for execution
                                currentExecuted = true;
                            }
                        }
                        else {
                            // If this is a block, put the child in the list of
                            // blocked children
                            numBlockedBehaviours++;
                            // Only if, with the addition of this child all sub-behaviours 
                            // are now blocked, block this ParallelBehaviour and notify upwards
                            if (numBlockedBehaviours == allBehaviours.size()) {
                                myEvent.init(false, NOTIFY_UP);
                                super.handle(myEvent);
                            }
                        }
                    }
                } // END of upwards notification from children                
            } // END of upwards notification
            else {
                // Downwards notification (from parent)
                boolean r = rce.isRunnable();               
                // Set the new runnable state
                setRunnable(r);
                // Notify all children
                Iterator it = getChildren().iterator();
                while (it.hasNext()) {
                    Behaviour b = (Behaviour) it.next();
                    b.handle(rce);
                }
                // Clear or completely fill the list of blocked children 
                // according to whether this is a block or restart
                if (r)
                    numBlockedBehaviours = 0;
                else
                    numBlockedBehaviours = allBehaviours.size();
            }  // END of downwards notification
        }
    }
    
    /**
     * Renice all children behaviours. Increment the priorities
     * of the behaviours to avoid the starvation of this behaviours.
     * During this process all behaviours that reach the priority
     * 0 are inserted in the <code>readyBehaviours</code> list. 
     */
    private void renice() {
        int newMaxPriority = maxPriority;        
        Iterator iter = allBehaviours.iterator();
        while (iter.hasNext()) {
            PriorityBehaviour pb = (PriorityBehaviour) iter.next();
            pb.incCurrentPriority(maxPriority);
            // If the behaviour reaches the maximum priority, insert
            // it in the corresponding ready behaviours list.
            if (pb.getCurrentPriority() == 0) {
                readyBehaviours.addElement(pb);
                pb.resetCurrentPriority();
            }
            // Is this behaviour the one which highest priority?
            if (pb.getCurrentPriority() < maxPriority)
                newMaxPriority = pb.getCurrentPriority();
        }
        if (newMaxPriority < maxPriority)
            maxPriority = newMaxPriority;
    }
    
    /**
     * Changes the priority of a behaviour.
     * @param pb The behaviour which priority is to be changed.
     * @param newPriority The new priority of the behaviour. If it less than
     * 0 then uses 0.
     * @param changeNow True if the change must be reflected inmediately,
     * that is, the current priority is reset to the new priority, or False
     * if this change takes effect after the execution of the behaviour.
     */
    public void changePriority(PriorityBehaviour pb, int newPriority, boolean changeNow) {
        pb.setPriority(newPriority);
        if (changeNow) {
            // If this is not the highest priority behaviour, resets
            // its current priority.
            if (pb.getCurrentPriority() > maxPriority) {
                pb.resetCurrentPriority();
                // Checks if now this is the highest priority behaviuor.
                if (pb.getCurrentPriority() < maxPriority)
                    maxPriority = pb.getCurrentPriority();
            }
            else
            {
                // This is the highest priority behaviour, and we change its
                // priority. So perhaps there is a new highest priority behaviour.
                pb.resetCurrentPriority();
                if (pb.getCurrentPriority() < maxPriority)
                    maxPriority = pb.getCurrentPriority();
                else {
                    PriorityBehaviour mpb = allBehaviours.searchMaxPriorityBehaviour();
                    if (mpb != null)
                        maxPriority = mpb.getCurrentPriority();
                }
            }
            // If this change makes the behaviour reach priority 0, then it is
            // ready to be executed.
            if ((pb.getCurrentPriority() == 0) && (!(readyBehaviours.contains(pb))))
                readyBehaviours.addElement(pb);
        }
    }
}
