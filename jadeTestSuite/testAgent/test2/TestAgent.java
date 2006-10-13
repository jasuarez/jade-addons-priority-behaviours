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

package testAgent.test2;

import jade.core.Agent;
import jade.core.behaviours.ParallelPriorityBehaviour;

public class TestAgent extends Agent {

    private static final long serialVersionUID = -2360612346092912549L;

    public TestAgent() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void setup() {
        printExpected();
        ParallelPriorityBehaviour ppb = new ParallelPriorityBehaviour(this);
        ppb.addSubBehaviour(new BehaviourExample(1), 2);
        ppb.addSubBehaviour(new BehaviourExample(2), 1);
        ppb.addSubBehaviour(new BehaviourExample(3), 4);
        addBehaviour(ppb);
    }
    
    private void printExpected() {
        System.out.println("EXPECTED: 2-1-2-2-1-3-1-3-3-");
        System.out.print("ACTUAL..: ");
    }
}