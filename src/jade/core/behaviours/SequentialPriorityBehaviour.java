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
 * Composite behaviour with priority-based children scheduling. It is a
 * <code>CompositeBehaviour</code> that executes its children behaviours in sequential form, using
 * first their priorities to establish the sequence and, if two behaviours have the same priority,
 * the order in which they have been added.
 * 
 * Also this <code>CompositeBehaviour</code> allows two variants: skip the blocked behaviours and
 * without skipping them (default behaviour).
 * 
 * Roughly speaking, the scheduler runs as follows: 
 *      (1) If there are no children behaviours then this <code>CompositeBehaviour<code> is done.
 *      (2) Always pick the behaviour with the highest priority, that is, the behaviour who
 *          priority is the lesser. If two or more behaviours have the highest priority, then
 *          select the one that has been created first.
 *      (3) If the previously selected  behaviour is blocked, and this <code>CompositeBehaviour</code>
 *          is configured to skip the blocked behaviours, then skip this behaviour and go again to
 *          step 2.
 *      (4) If there are not any runnable behaviour (i.e., all the
 *          children is blocked), then block this behaviour.
 *      (5) If the selected behaviour is blocked and this <code>CompositeBehaviour</code> does not
 *          skip the blocked behaviours, then block this composite behaviour. 
 *      (6) In other case execute this behaviour and go to step 1.
 *
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Date$ $Revision$
 */
public class SequentialPriorityBehaviour extends SerialBehaviour {
    /**
     * The default priority for the newly created behaviours, if none is
     * specified.
     */
    public static final int DEFAULT_PRIORITY = 5;
    
    //private static final long serialVersionUID = -8081100734240269128L;
    
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
     * A flag to indicate that the current behaviour has been already removed from the
     * <code>readyBehaviours</code>.
     */
    private boolean currentWasRemoved = false;

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
     * A list with all children with a current priority number of 0. The head of
     * this list is the behaviour to be executed.
     */
    protected EncapsulatedPriorityBehaviourList readyBehaviours = new EncapsulatedPriorityBehaviourList();
    
    /**
     * A flag to indicate that is necessary to obtain again the new behaviour with the
     * highest priority in the next schedule.
     */
    private boolean reschedOnNext = false;
    
    /**
     * A flag to indicate if it is needed to skip the blocked behaviours when selecting the next
     * one to be executed. 
     */
    protected boolean skipBlocked = false;

    /**
     * Default constructor. Creates a <code>CompositeBehaviour</code> without
     * setting the owner agent, and executes its children in a priority-based
     * sequentially manner, and it terminates when all children are done. It
     * always selects the child with the highest priority.
     */
    public SequentialPriorityBehaviour() {
        super();
    }

    /**
     * Creates a <code>SequentialPriorityBehaviour</code> setting the
     * owner agent. It always selects the child with the highest priority.
     * 
     * @param a The owner of this behaviour.
     */
    public SequentialPriorityBehaviour(Agent a) {
        super(a);
    }
    
    /**
     * Creates a <code>SequentialPriorityBehaviour</code> setting the
     * owner agent.

     * @param a The owner of this behaviour.
     * @param skipBlocked True if it is always selected the child with the
     * highest priority that is runnable, or False if it is always selected
     * the child with the highest priority, whether it is runnable or not.
     */
    public SequentialPriorityBehaviour(Agent a, boolean skipBlocked) {
        super(a);
        this.skipBlocked = skipBlocked;
    }
    
    /**
     * Creates a <code>SequentialPriorityBehaviour</code>.
     * 
     * @param skipBlocked True if it is always selected the child with the
     * highest priority that is runnable, or False if it is always selected
     * the child with the highest priority, whether it is runnable or not.
     */
    public SequentialPriorityBehaviour(boolean skipBlocked) {
        super();
        this.skipBlocked = skipBlocked;
    }

    /**
     * Add a priority behaviour to this
     * <code>SequentialPriorityBehaviour</code>, with a default priority.
     * 
     * @param b
     *            The behaviour to be added.
     */
    public void addSubBehaviour(Behaviour b) {
        addSubBehaviour(b, SequentialPriorityBehaviour.DEFAULT_PRIORITY);
    }

    /**
     * Add a priority behaviour to this <code>SequentialPriorityBehaviour</code>.
     * 
     * @param b
     *            The behaviour to be added.
     * @param priority
     *            The priority of the behaviour. If it is less than 0 then it is
     *            changed to 0.
     */
    public void addSubBehaviour(Behaviour b, int priority) {
        EncapsulatedPriorityBehaviour epb = new EncapsulatedPriorityBehaviour(b, priority);
        b.setParent(this);
        b.setAgent(myAgent);
        allBehaviours.addElement(epb);
        children.add(b);
        // If this behaviour preempts the current one
        if (epb.getDynamicPriority() < maxPriority) {
            reschedOnNext = true;
            currentExecuted=true;
        }
        // Case when this CompositeBehaviour is waiting for a runnable behaviour
        if (b.isRunnable()) {
            if (!isRunnable()) {
                if (skipBlocked || reschedOnNext) {
                    myEvent.init(true, NOTIFY_UP);
                    super.handle(myEvent);
                    if (myAgent != null)
                        myAgent.notifyRestarted(this);
                    reschedOnNext = true;
                    currentExecuted=true;
                }
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
     */
    public void changePriority(Behaviour b, int newPriority) {
        EncapsulatedPriorityBehaviour epb = allBehaviours.searchBehaviour(b);
        // If this behaviour is a child
        if (epb != null) {
            epb.setStaticPriority(newPriority);
            epb.resetDynamicPriority();
            // If this behaviour preempts the current one
            if (epb.getDynamicPriority() < maxPriority)
                reschedOnNext=true;
                currentExecuted=true;
        }
    }
     
    /**
     * Returns true if this behaviour must be terminated.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#checkTermination(boolean,
     *      int)
     */
    protected boolean checkTermination(boolean currentDone, int currentResult) {
        // We check that the current behaviour was not already removed by the
        // user through the removeSubBehaviour method.
        if ((currentDone) && (!currentWasRemoved)) {
            EncapsulatedPriorityBehaviour current = readyBehaviours.getElement(0);
            allBehaviours.removeElement(current);
            readyBehaviours.removeElement(0);
            children.remove(current.getBehaviour());
            current.getBehaviour().setParent(null);
            doneBehaviours.addElement(current);
            currentWasRemoved = true;
        } 
        if (allBehaviours.isEmpty())
            return true;
        else {
            // Checks if the current done behaviour is the only runnable child, so
            // this composite behaviour is blocked.
            if (allBehaviours.size() == numBlockedBehaviours) {
                myEvent.init(false, NOTIFY_UP);
                super.handle(myEvent);
            }
            return false;
        }
    }

    /**
     * Insert into the <code>readyBehaviours</code> list all behaviours with a 
     * concrete priority.
     * This list is not empty at first.
     * 
     * @param priority The priority of the behaviours that will be inserted.
     */
    private void filterBehaviours(int priority) {
       Iterator it = allBehaviours.iterator();
       while (it.hasNext()) {
           EncapsulatedPriorityBehaviour epb = (EncapsulatedPriorityBehaviour) it.next();
           if (epb.getDynamicPriority() == priority)
               readyBehaviours.addElement(epb);
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
        if (rce.isUpwards()) {
            // Upwards notification
            if (rce.getSource() == this) {
                // If the event is from this behaviour, set the new
                // runnable state and notify upwords.
                super.handle(rce);
            } else {
                // If the event is from a child
                EncapsulatedPriorityBehaviour epb = allBehaviours.searchBehaviour(rce.getSource());
                if (epb !=  null) {
                    if (rce.isRunnable()) {
                        numBlockedBehaviours--;
                        // If the waked behaviour has a higher priority
                        if (epb.getDynamicPriority() < maxPriority) {
                            reschedOnNext = true;
                            currentExecuted=true;
                            myEvent.init(rce.isRunnable(), NOTIFY_UP);
                            super.handle(myEvent);
                        }
                    } else
                        numBlockedBehaviours++;
                    if (rce.getSource() == getCurrent()) {
                        // If the event is from the currently executing child,
                        // create a new event, set the new runnable state and
                        // notify upwords.
                        myEvent.init(rce.isRunnable(), NOTIFY_UP);
                        super.handle(myEvent);
                    }
                } else {
                    // Downwards notifications
                    setRunnable(rce.isRunnable());
                    // Notify all children
                    Iterator it = getChildren().iterator();
                    while (it.hasNext()) {
                        Behaviour b = (Behaviour) it.next();
                        b.handle(rce);
                    }
                    // Clear or completely fill the list of blocked children
                    // according to whether this is a block or restart
                    if (rce.isRunnable())
                        numBlockedBehaviours = 0;
                    else
                        numBlockedBehaviours = allBehaviours.size();
                }     
            }
        }
    }

    /**
     * Prepares the next behaviour to be executed. That is, selects the
     * behaviours with the highest priority. If the <code>CompositeBehaviour</code>
     * needs to skip the blocked behaviours, then only the runnable ones are
     * selected.
     */
    private void prepareNextBehaviour() {
        if (readyBehaviours.isEmpty())
            filterBehaviours(maxPriority);
        // If there are some runnable behaviour
        if (numBlockedBehaviours < allBehaviours.size()) {
            if (skipBlocked)
                readyBehaviours.pruneNotRunnableBehaviours();
            while (readyBehaviours.isEmpty()) {
                EncapsulatedPriorityBehaviour mpb = allBehaviours.searchMaxPriorityBehaviour(maxPriority+1);
                if (mpb != null)
                    maxPriority = mpb.getDynamicPriority();
                filterBehaviours(maxPriority);
                if (skipBlocked)
                    readyBehaviours.pruneNotRunnableBehaviours();
            }
        }
    }
    
    /**
     * Remove a behaviour from this <code>SequentialBehaviour</code>.
     * 
     * @param pb
     *            The behaviur to be removed.
     */
    public void removeSubBehaviour(Behaviour b) {
        EncapsulatedPriorityBehaviour removed = allBehaviours.removeElement(b);
        if (removed != null) {
            children.remove(b);
            b.setParent(null);
            if (readyBehaviours.getElement(0) == removed)
                currentWasRemoved = true;
            readyBehaviours.removeElement(removed);
            if (!b.isRunnable())
                numBlockedBehaviours--;
            // If some children still exist and they are all blocked,
            // block this SequentialPriorityBehaviour and notify upwards
            if ((!allBehaviours.isEmpty())
                    && (numBlockedBehaviours == allBehaviours.size()) && isRunnable()) {
                myEvent.init(false, NOTIFY_UP);
                super.handle(myEvent);
            }
        }
    }

    /**
     * Restores this behaviour to its initial state.
     * 
     * @see jade.core.behaviours.Behaviour#reset()
     */
    public void reset() {
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
        // Finally, super is in charge of reset the encapsulated behaviours.
        super.reset();
        numBlockedBehaviours = 0;
        reschedOnNext=true;
        currentExecuted=true;
    }

    /**
     * Prepares the first child to execute.
     * 
     * @see jade.core.behaviours.CompositeBehaviour#scheduleFirst()
     */
    protected void scheduleFirst() {
        readyBehaviours.clear();
        reschedOnNext=false;
        currentWasRemoved=false;
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
        // If it is necessary to restart the schedule
        if (reschedOnNext) {
            scheduleFirst();
        // If the current behaviour was removed, because either it ended or the user
        // removed it
        } else if (currentWasRemoved) {
            currentWasRemoved=false;
            prepareNextBehaviour();
        // If the current behaviour is blocked, and the policy is to skip the
        // blocked ones
        } else if (skipBlocked && !getCurrent().isRunnable()) {
            readyBehaviours.removeElement(0);
            prepareNextBehaviour();
        }
    }
    

    //////////////////////////////////////////////////////////////////////////////
    // UNCONMENT THIS TO TEST THE CLASS
    //
    // protected EncapsulatedPriorityBehaviourList getAllBehaviours() {
    //    return allBehaviours;
    // }
    //
    // protected LinkedList getAsListChildren() {
    //    return children;
    // }
    //
    // protected EncapsulatedPriorityBehaviourList getDoneBehaviours() {
    //    return doneBehaviours;
    // }
    //
    // protected int getNumBlockedBehaviours() {
    //    return numBlockedBehaviours;
    // }
    //////////////////////////////////////////////////////////////////////////////

}

