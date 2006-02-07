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

package test.jade.core.behaviours.test1;

import jade.core.Agent;
import jade.core.behaviours.PriorityBehaviour;

/**
 * Vamos a crear un comportamiento circular pero con prioridad. Le pondremos un
 * nombre y veremos cómo lo va ejecutando el scheduler. Para ello definiremos
 * diferentes instancias de la clase con diferentes prioridades.
 */
public class ImprimirSubBehaviour extends PriorityBehaviour {
    private static final long serialVersionUID = 1L;

    String miNombre;

    // El número de veces que se ejecuta este behaviour.
    int iteracion = 0;

    public ImprimirSubBehaviour(Agent ag, String nombre, int prioridad) {
        super(ag, prioridad);
        miNombre = nombre;
    }

    public void action() {
        iteracion++;
        System.out.println("<Comportamiento " + miNombre + this.getPriority()
                + " >  Iteracion " + iteracion);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean done() {
        // TODO Auto-generated method stub
        return false;
    }
}
