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

/**
 * A special <code>PriorityBehaviour</code> which acts as a wrapper for
 * a <code>Behaviour</code> in order to add priorities to it.
 * 
 * @author Juan A. Suárez Romero - University of A Coruña
 * @version $Revision$
 */
public class EncapsulatedPriorityBehaviour extends PriorityBehaviour {
    private static final long serialVersionUID = 548619304232112354L;
    private Behaviour encapsulatedBehaviour;

    /**
     * Encapsulates a behaviour with the default priority. Also it does not
     * set the agent owning this behaviour object.
     * @param b The behaviour to be encapsulated.
     * @see PriorityBehaviour.DEFAULT_PRIORITY;
     */
    public EncapsulatedPriorityBehaviour(Behaviour b) {
        super();
        encapsulatedBehaviour = b;
    }

    /**
     * Encapsulates a behaviour. It does not set the agent owning this behaviour
     * object.
     * @param b The behaviour to be encapsulated.
     * @param priority The priority of this behaviour object.
     */
    public EncapsulatedPriorityBehaviour(Behaviour b, int priority) {
        super(priority);
        encapsulatedBehaviour = b;
    }

    /**
     * Encapsulates a behaviour with the default priority and an owner agent.
     * @param b The behaviour to be encapsulated.
     * @param a The agent owning this behaviour.
     * @see PriorityBehaviour.DEFAULT_PRIORITY;
     */
    public EncapsulatedPriorityBehaviour(Behaviour b, Agent a) {
        super(a);
        encapsulatedBehaviour =  b;
    }

    /**
     * Encapsulates a behaviour with a priority and an owner agent.
     * @param b The behaviour to be encapsulated.
     * @param a The agent owning this behaviour.
     * @param priority The priority of this behaviour object.
     */
    public EncapsulatedPriorityBehaviour(Behaviour b, Agent a, int priority) {
        super(a, priority);
        encapsulatedBehaviour = b;
    }

    /**
     * The action to be executed.
     * @see jade.core.behaviours.Behaviour#action()
     */
    public void action() {
        if (encapsulatedBehaviour.isRunnable()) {
            encapsulatedBehaviour.action();
            if (!encapsulatedBehaviour.isRunnable()) {
                myEvent.init(false, NOTIFY_UP);
                super.handle(myEvent);
            }
        }
        else {
            myEvent.init(false, NOTIFY_UP);
            super.handle(myEvent);
        }
    }

    /**
     * Returns true if this behaviour is done.
     * @see jade.core.behaviours.Behaviour#done()
     */
    public boolean done() {
      return encapsulatedBehaviour.done();
    }
    
    /**
     * Method to be executed when this behaviour is done.
     * @see jade.core.behaviours.Behaviour#onEnd()
     */
    public int onEnd() {
        return encapsulatedBehaviour.onEnd();
    }
    
    /**
     * Method to be executed before starting this behaviour.
     * @see jade.core.behaviours.Behaviour#onStart()
     */
    public void onStart() {
        encapsulatedBehaviour.onStart();
    }
    
    /**
     * Restores this behaviour.
     * @see jade.core.behaviours.Behaviour#reset()
     */
    public void reset() {
        encapsulatedBehaviour.reset();
        super.reset();
    }
    
    /**
     * Handler for the block/restart events.
     * @see jade.core.behaviours.Behaviour#handle(jade.core.behaviours.Behaviour.RunnableChangedEvent)
     */
    protected void handle(RunnableChangedEvent rce) {
        if (!rce.isUpwards()) {
            encapsulatedBehaviour.handle(rce);
        }
    }
    
    /**
     * Returns the root of this behaviour.
     * @see jade.core.behaviours.Behaviour#root()
     */
    public Behaviour root() {
        return encapsulatedBehaviour.root();
    }
    
    /**
     * Sets the runnable/not-runnable state.
     * @see jade.core.behaviours.Behaviour#setRunnable(boolean)
     */
    void setRunnable(boolean runnable) {
        encapsulatedBehaviour.setRunnable(runnable);
        super.setRunnable(runnable);
    }
    
    /**
     * Returns true if this behaviour is runanble.
     * @see jade.core.behaviours.Behaviour#isRunnable()
     */
    public boolean isRunnable() {
        return encapsulatedBehaviour.isRunnable();
    }
    
    /**
     * Blocks this behaviour.
     * @see jade.core.behaviours.Behaviour#block()
     */
    public void block() {
        //encapsulatedBehaviour.block();
        super.block();
        
        // Then notify downwards
        myEvent.init(false, NOTIFY_DOWN);
        handle(myEvent);        
    }
        
    /**
     * Makes this behaviour runnable.
     * @see jade.core.behaviours.Behaviour#restart()
     */
    public void restart() {
        encapsulatedBehaviour.restart();
        super.restart();
    }
    
    /**
     * Set the agent owning this behaviour.
     * @see jade.core.behaviours.Behaviour#setAgent(jade.core.Agent)
     */
    public void setAgent(Agent a) {
        encapsulatedBehaviour.setAgent(a);
    }
    
    /**
     * Returns the private datastore of this behaviour.
     * @see jade.core.behaviours.Behaviour#getDataStore()
     */
    public DataStore getDataStore() {
        return encapsulatedBehaviour.getDataStore();
    }
    
    /**
     * Sets a private datastore for this behaviour.
     * @see jade.core.behaviours.Behaviour#setDataStore(jade.core.behaviours.DataStore)
     */
    public void setDataStore(DataStore ds) {
        encapsulatedBehaviour.setDataStore(ds);
        super.setDataStore(ds);
    }
}
