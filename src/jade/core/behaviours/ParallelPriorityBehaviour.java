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
import jade.util.leap.LinkedList;

/**
 * Composite behaviour with priority based children scheduling. It is a
 * <code>CompositeBehaviour</code> that executes its children behaviours using
 * their priorities, and it terminates when all children are done, similary like
 * the <code>ParallelBehaviour</code> runs, but using priorities.
 * 
 * The priorities of the children are represented by a number greater or equal
 * than zero. The lesser is this number, the higher is the priority of the
 * behaviour. The priorities are incrememented during time to avoid its
 * starvation. The exception is when there is any behaviour with a priority 0,
 * in which in this case this increment is stopped.
 * 
 * When a behaviour either has or reaches priority 0 is inserted in a
 * list of ready behaviours.
 * 
 * Roughly speaking, the scheduler runs as follows: (1) If there are no child
 * behaviours then this <code>CompositeBehaviour<code> is done.
 *      (2) If there are not any runnable behaviour (i.e., all the
 *          children is blocked), then block this behaviour.
 *      (3) While there are behaviours in the ready behaviours list, picks the
 *          first one, and if it is runnable then execute it. Drop it from
 *          this list.
 *      (4) If the ready behaviours list is empty, then searches all
 *          behaviours, either blocked or not, with priority 0 and insert
 *          they in the ready behaviours list. Resets its priority.
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
public class ParallelPriorityBehaviour extends CompositeBehaviour {
    /**
     * Predefined constant to be used in the constructor to create 
     * a <code>ParallelPriorityBehaviour</code> that terminates when
     * all its children are done.
     */
    public static final int WHEN_ALL = 0;

    /** 
     * Predefined constant to be used in the constructor to create 
     * a <code>ParallelPriorityBehaviour</code> that terminates when
     * any of its child is done.
     */
    public static final int WHEN_ANY = 1;

    /**
     * The default priority for the newly created behaviours, if none is
     * specified.
     */
    public static final int DEFAULT_PRIORITY = 5;

    private static final long serialVersionUID = 4668737902684655038L;

    /**
     * A list with all children. This list does not contain the done children.
     */
    private EncapsulatedPriorityBehaviourList allBehaviours = new EncapsulatedPriorityBehaviourList();

    /**
     * This lists contains the same children that <code>allBehaviours</code>,
     * but without considering the priorities (that is, unencapsulated
     * behaviours).
     */
    private LinkedList children = new LinkedList();

    /**
     * A list with all children that are done.
     */
    private EncapsulatedPriorityBehaviourList doneBehaviours = new EncapsulatedPriorityBehaviourList();

    /**
     * The priority number of the behaviour with the highest priority.
     */
    private int maxPriority = 0;

    /**
     * Then number of children behaviours that are no runnable.
     */
    private int numBlockedBehaviours = 0;
    
    /**
     * The condition to terminate this behaviour. 
     */
    private int whenToStop;

    /**
     * A list with all children with a dynamic priority number of 0. The head of
     * this list is the behaviour to be executed.
     */
    protected EncapsulatedPriorityBehaviourList readyBehaviours = new EncapsulatedPriorityBehaviourList();

    /**
     * Default constructor. Creates a <code>CompositeBehaviour</code> without
     * setting the owner agent, and executes its children in a priority-based
     * manner, and it terminates when all children are done.
     */
    public ParallelPriorityBehaviour() {
        super();
        whenToStop = WHEN_ALL;
    }

    /**
     * Construct a <code>ParallelPriorityBehaviour</code> without setting the
     * owner agent.
     * 
     * @param endCondition This value defines the termination condition
     * for this <code>ParallelPriorityBehaviour</code>. Use
     * <ol>
     * <li>
     * <code>WHEN_ALL</code> to terminate this <code>ParallelPriorityBehaviour</code>
     * when all its children are done.
     * </li>
     * <li>
     * <code>WHEN_ANY</code> to terminate this <code>ParallelPriorityBehaviour</code>
     * when any of its child is done.
     * </li>
     * <li>
     * a positive <code>int</code> value n to terminate this
     * <code>ParallelPriorityBehaviour</code> when n of its children are done.
     * </li>
     * </ol>
     */
    public ParallelPriorityBehaviour(int endCondition) {
        super();
        whenToStop = endCondition;
    }

    /**
     * Creates a <code>ParallelPriorityBehaviour</code> setting the
     * owner agent. This behaviour terminates when all children are done.
     * 
     * @param a
     */
    public ParallelPriorityBehaviour(Agent a) {
        super(a);
        whenToStop = WHEN_ALL;
    }
    
    /**
     * Construct a <code>ParallelPriorityBehaviour</code> setting the
     * owner agent.
     * 
     * @param endCondition This value defines the termination condition
     * for this <code>ParallelPriorityBehaviour</code>. Use
     * <ol>
     * <li>
     * <code>WHEN_ALL</code> to terminate this <code>ParallelPriorityBehaviour</code>
     * when all its children are done.
     * </li>
     * <li>
     * <code>WHEN_ANY</code> to terminate this <code>ParallelPriorityBehaviour</code>
     * when any of its child is done.
     * </li>
     * <li>
     * a positive <code>int</code> value n to terminate this
     * <code>ParallelPriorityBehaviour</code> when n of its children are done.
     * </li>
     * </ol>
     */
    public ParallelPriorityBehaviour(Agent a, int endCondition) {
        super(a);
        whenToStop = endCondition;
    }

    /**
     * Add a priority behaviour to this
     * <code>ParallelPriorityBehaviour</code>, with a default priority.
     * 
     * @param b
     *            The behaviour to be added.
     */
    public void addSubBehaviour(Behaviour b) {
        addSubBehaviour(b, ParallelPriorityBehaviour.DEFAULT_PRIORITY);
    }

    /**
     * Add a priority behaviour to this
     * <code>ParallelPriorityBehaviour</code>.
     * 
     * @param b
     *            The behaviour to be added.
     * @param priority
     *            The priority of the behaviour. If it is less than 0 then it is
     *            changed to 0.
     */
    public void addSubBehaviour(Behaviour b, int priority) {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(
                b, priority);
        b.setParent(this);
        b.setAgent(myAgent);
        allBehaviours.addElement(epb);
        children.add(b);
        // Checks if this behaviour has the highest priority
        if (epb.getDynamicPriority() < maxPriority)
            maxPriority = epb.getDynamicPriority();
        //Checks if the behaviour is ready to run
        if (b.isRunnable()) {
            // If all previous children were blocked (this Priority-based
            // Behaviour
            // was blocked too), restart this ParallelPriorityBehaviour
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
        } else
            numBlockedBehaviours++;
    }

    /**
     * Changes the priority of a behaviour.
     * 
     * @param pb
     *            The behaviour which priority is to be changed.
     * @param newPriority
     *            The new priority of the behaviour. If it less than 0 then uses
     *            0.
     * @param changeNow
     *            True if the change must be reflected inmediately, that is, the
     *            dynamic priority is reset to the new static priority, or False if
     *            this change takes effect after the execution of the behaviour.
     */
    public void changePriority(Behaviour b, int newPriority, boolean changeNow) {
        EncapsulatedPriorityBehaviour epb = allBehaviours.searchBehaviour(b);
        // If we have this behaviour
        if (epb != null) {
            epb.setStaticPriority(newPriority);
            if (changeNow) {
                // If this is not the highest priority behaviour, resets
                // its dynamic priority.
                if (epb.getDynamicPriority() > maxPriority) {
                    epb.resetDynamicPriority();
                    // Checks if now this is the highest priority behaviuor.
                    if (epb.getDynamicPriority() < maxPriority)
                        maxPriority = epb.getDynamicPriority();
                } else {
                    // This is the highest priority behaviour, and we change its
                    // priority. So perhaps there is a new highest priority
                    // behaviour.
                    epb.resetDynamicPriority();
                    if (epb.getDynamicPriority() < maxPriority)
                        maxPriority = epb.getDynamicPriority();
                    else {
                        EncapsulatedPriorityBehaviour mpb = allBehaviours
                                .searchMaxPriorityBehaviour();
                        if (mpb != null)
                            maxPriority = mpb.getDynamicPriority();
                    }
                }
          }
        }
    }

    /**
     * Returns true if this behaviour must be terminated.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#checkTermination(boolean,
     *      int)
     */
    protected boolean checkTermination(boolean currentDone, int currentResult) {
        if (currentDone) {
            EncapsulatedPriorityBehaviour current = readyBehaviours
                    .getElement(0);
            allBehaviours.removeElement(current);
            children.remove(current.getBehaviour());
            current.getBehaviour().setParent(null);
            doneBehaviours.addElement(current);
        }
        if (!evalCondition()) {
            // The following check must be done regardless of the fact  
            // that the current child is done or not, but provided that 
            // this ParallelPriorityBehaviour is not terminated
            if (allBehaviours.size() == numBlockedBehaviours) {
                // If all children are blocked --> this 
                // ParallelPriorityBehaviour must block too and notify upwards
                myEvent.init(false, NOTIFY_UP);
                super.handle(myEvent);
            }
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Return a Collection view of the children of this behaviour.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#getChildren()
     */
    public Collection getChildren() {
        return children;
    }

    /**
     * Get the current child to be executed.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#getCurrent()
     */
    protected Behaviour getCurrent() {
        Behaviour b = null;
        try {
            b = readyBehaviours.getElement(0).getBehaviour();
        } catch (IndexOutOfBoundsException ioobe) {
            // Just do nothing. Null will be returned
        }
        return b;
    }

    /**
     * Handler to manage the block/restart events.
     * 
     * @see jade.core.behaviours.Behaviour#handle(jade.core.behaviours.Behaviour.RunnableChangedEvent)
     */
    protected void handle(RunnableChangedEvent rce) {
        // This method may be executed by an auxiliary thread posting
        // a message into the Agent's message queue --> it must be
        // synchronized with sub-behaviour additions/removal.
        synchronized (allBehaviours) {
            // Upwards notification
            if (rce.isUpwards()) {
                Behaviour b = rce.getSource();
                // If the event is from this behaviour, set the new
                // runnable state and notify upwards.
                if (b == this) {
                    super.handle(rce);
                } else {
                    EncapsulatedPriorityBehaviour epb = allBehaviours
                            .searchBehaviour(b);
                    if (epb != null) {
                        // If the event is from a child -->
                        if (rce.isRunnable()) {
                            // If this is a restart, remove the child from the
                            // blocked behaviours
                            numBlockedBehaviours--;
                            // Only if all children were blocked (this
                            // ParallelBehaviour was
                            // blocked too), restart this ParallelBehaviour and
                            // notify upwards
                            if (!isRunnable()) {
                                myEvent.init(true, NOTIFY_UP);
                                super.handle(myEvent);
                                // Also reset the currentExecuted flag so that a
                                // runnable
                                // child will be scheduled for execution
                                currentExecuted = true;
                            }
                        } else {
                            // If this is a block, put the child in the list of
                            // blocked children
                            numBlockedBehaviours++;
                            // Only if, with the addition of this child all
                            // sub-behaviours
                            // are now blocked, block this ParallelBehaviour and
                            // notify upwards
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
            } // END of downwards notification
        }
    }

    /**
     * Prepares the next behaviour to be executed. That is, renice all
     * behaviours until either there is some runnable behaviour with priority 0,
     * or all behaviours are blocked.
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
                        .getDynamicPriority();
            while (readyBehaviours.isEmpty()) {
                renice();
                readyBehaviours.pruneNotRunnableBehaviours();
            }
        }
    }

    /**
     * Remove a behaviour from this <code>ParallelBehaviour</code>.
     * 
     * @param pb
     *            The behaviour to be removed.
     */
    public void removeSubBehaviour(Behaviour b) {
        EncapsulatedPriorityBehaviour removed = allBehaviours.removeElement(b);
        if (removed != null) {
            children.remove(b);
            b.setParent(null);
            // Case when the behaviour to be removed has dynamic priority 0 but
            // it is
            // not the behaviour which calls this method. In this last case the
            // behaviour
            // will be removed on the next schedule.
            if ((!readyBehaviours.isEmpty())
                    && (readyBehaviours.getElement(0) != removed))
                readyBehaviours.removeElement(removed);
            if (!b.isRunnable())
                numBlockedBehaviours--;
            // Perhaps there are a new behaviour with the highest priority.
            if (removed.getDynamicPriority() == maxPriority) {
                EncapsulatedPriorityBehaviour mpb = allBehaviours
                        .searchMaxPriorityBehaviour();
                if (mpb != null)
                    maxPriority = mpb.getDynamicPriority();
            }
            // If some children still exist and they are all blocked,
            // block this ParallelPriorityBehaviour and notify upwards
            if ((!allBehaviours.isEmpty())
                    && (numBlockedBehaviours == allBehaviours.size())) {
                myEvent.init(false, NOTIFY_UP);
                super.handle(myEvent);
            }
        }
    }

    /**
     * Renice all children behaviours. Increment the priorities of the
     * behaviours to avoid the starvation of this behaviours. During this
     * process all behaviours that reach the priority 0 are inserted in the
     * <code>readyBehaviours</code> list.
     */
    private void renice() {
        int newMaxPriority = maxPriority;
        Iterator it = allBehaviours.iterator();
        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            epb.incDynamicPriority(maxPriority);
            // If the behaviour reaches the maximum priority, insert
            // it in the corresponding ready behaviours list.
            if (epb.getDynamicPriority() == 0) {
                readyBehaviours.addElement(epb);
                epb.resetDynamicPriority();
            }
            // Is this behaviour the one which highest priority?
            if (epb.getDynamicPriority() < maxPriority)
                newMaxPriority = epb.getDynamicPriority();
        }
        if (newMaxPriority < maxPriority)
            maxPriority = newMaxPriority;
    }

    /**
     * Restores this behaviour to its initial state.
     * 
     * @see jade.core.behaviours.Behaviour#reset()
     */
    public void reset() {
        numBlockedBehaviours = 0;
        readyBehaviours.clear();
        // First restores the done behaviours.
        Iterator it = doneBehaviours.iterator();
        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            it.remove();
            epb.getBehaviour().setParent(this);
            allBehaviours.addElement(epb);
            children.add(epb.getBehaviour());
        }
        // Now resets each child.
        it = allBehaviours.iterator();
        while (it.hasNext()) {
            EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it
                    .next();
            epb.resetDynamicPriority();
        }
        // Finally, super is in charge of reset the encapsulated behaviours.
        super.reset();
    }

    /**
     * Prepares the first child to execute.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#scheduleFirst()
     */
    protected void scheduleFirst() {
        readyBehaviours.clear();
        // The first time we need to search the behaviour with the
        // highest priority
        EncapsulatedPriorityBehaviour mpb = allBehaviours
                .searchMaxPriorityBehaviour();
        if (mpb != null)
            maxPriority = mpb.getDynamicPriority();
        prepareNextBehaviour();
    }

    /**
     * Prepares the next child to execute.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#scheduleNext(boolean, int)
     */
    protected void scheduleNext(boolean currentDone, int currentResult) {
        // The first behaviour has been executed, so we removed it.
        readyBehaviours.removeElement(0);
        prepareNextBehaviour();
    }
    
    private boolean evalCondition() {   
        boolean cond;
        switch(whenToStop) {
        case WHEN_ALL:
            cond = allBehaviours.isEmpty();
            break;
        case WHEN_ANY:
            cond = (doneBehaviours.size() > 0);
            break;
        default:
            cond = (doneBehaviours.size() >= whenToStop);
        break;
        }
        return cond;
    }

    
    //////////////////////////////////////////////////////////////////////////////
    // UNCONMENT THIS TO TEST THE CLASS
    
    protected EncapsulatedPriorityBehaviourList getAllBehaviours() {
        return allBehaviours;
    }

    protected EncapsulatedPriorityBehaviourList getDoneBehaviours() {
        return doneBehaviours;
    }

    protected int getNumBlockedBehaviours() {
        return numBlockedBehaviours;
    }

    protected EncapsulatedPriorityBehaviourList getReadyBehaviours() {
        return readyBehaviours;
    }
    
    protected LinkedList getAsListChildren() {
        return children;
    }
}
