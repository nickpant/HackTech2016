package Components;

import java.util.ArrayList;

/**
 * Created by MasterPiyasirisilp on 2016-02-28.
 */
public class Resistor {

    // array containing nodes connected to this resistor
    private int[] nodesConnected;
    // resistance associated with the resistor
    private int resistance;

    // constructor
    public Resistor(int[] nodesConnected, int resistance){
        this.nodesConnected = nodesConnected;
        this.resistance = resistance;
    }

    // given a node n, returns the other node that is connected to the resistor
    public int getOtherNodeConnected(ArrayList<Integer> elementsConnected) {
        for (int node : nodesConnected)
            // if the elements connected to the super node being evaluated
            // does not contain the node in the nodesConnected array
            // then that must be the other node
            if (!elementsConnected.contains(new Integer(node)))
                return node;
        return -1;
    }

    public int getResistance() {
        return resistance;
    }

}
