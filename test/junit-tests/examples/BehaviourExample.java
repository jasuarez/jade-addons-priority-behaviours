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

package examples;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 * A simple behaviour that can be executed 5 times before ending.
 * 
 * @author Juan A. Suarez Romero - University of A Coruna
 * @version $Date$ $Revision$
 */
public class BehaviourExample extends Behaviour {
    private static final long serialVersionUID = -3134519462636770274L;
    private int counter = 5;
    
    public BehaviourExample() {
        super();
    }
    
    public BehaviourExample(Agent a) {
       super(a);
    }
    
    public void action() {
        //System.out.print(this.getBehaviourName()+"-");
        counter--;
    }
    
    public int getCounter() {
        return counter;
    }

    public boolean done() {
        return counter==0;
    }
    
    public int onEnd() {
        //System.out.println("");
        return 0;
    }
}
