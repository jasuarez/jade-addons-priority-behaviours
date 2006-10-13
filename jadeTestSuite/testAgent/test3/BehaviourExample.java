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

package testAgent.test3;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class BehaviourExample extends Behaviour {
    private static final long serialVersionUID = 8052921726286633878L;
    private int counter = 3;
    private int id;
    
    public BehaviourExample() {
        super();
    }
    
    public BehaviourExample(int id) {
        super();
        this.id = id;
    }

    public BehaviourExample(Agent a) {
       super(a);
    }
    
    public BehaviourExample(Agent a, int id) {
        super(a);
        this.id = id;
     }

    public void action() {
        System.out.print(id+"-");
        counter--;
    }
    
    public int getCounter() {
        return counter;
    }

    public boolean done() {
        return counter==0;
    }
}
