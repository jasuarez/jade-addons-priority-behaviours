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

package test.jade.core.behaviours.test2;

import jade.core.Agent;
import jade.core.behaviours.PriorityBasedCompositeBehaviour;

public class TestComposePriorityBehaviour extends PriorityBasedCompositeBehaviour {
    private static final long serialVersionUID = -6667805894469793530L;
    private long startTime;
    private int numChilds;
    private int numExecutions = 10000;
    private int defaultPriority = 25;

    public TestComposePriorityBehaviour(Agent a, int numChilds) {
        super(a);
        /*for (int i = 0; i < numChilds; i++)
            this.addSubBehaviour(new TestSimpleBehaviour(a, defaultPriority, numExecutions));*/
        /*for (int i = 0; i < numChilds/3; i++)
            this.addSubBehaviour(new TestSimpleBehaviour(a, 5, numExecutions));
        for (int i = numChilds/3; i < numChilds; i++)
            this.addSubBehaviour(new TestSimpleBehaviour(a, 11, numExecutions));*/
        for (int i = 0; i < numChilds; i++)
            this.addSubBehaviour(new TestSimpleBehaviour(a, 1+((int)Math.round(100*Math.random())%defaultPriority), numExecutions));        
        this.numChilds = numChilds;
    }
    
    public void onStart() {
        startTime = System.currentTimeMillis();
    }
    
    public int onEnd() {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Resultado de las pruebas (PriorityBehaviourExample):");
        System.out.println("\tNúmero de hijos..........................: "  + numChilds);
        System.out.println("\tNúmero de ejecuciones por hijo hijo......: " + numExecutions);
        System.out.println("\tNúmero de ejecuciones totales............: " + numExecutions*numChilds);
        System.out.println("\t----------");
        System.out.println("\tTiempo total de la prueba................: " + duration + " ms.");
        System.out.println("\tTiempo de ejecución media de cada hijo...: " + duration/numChilds + " ms.");
        System.out.println("\tTiempo de ejecución media de cada llamada: " + duration/(numChilds*numExecutions) + " ms.");
        return 0;
    }
    

}
