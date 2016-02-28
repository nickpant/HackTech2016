package Components;

import java.util.ArrayList;

/**
 * Created by MasterPiyasirisilp on 2016-02-28.
 */
public class SuperNode {

    private ArrayList<Integer> childNodes;
    private int voltage = -999;
    private ArrayList<Resistor> elementsConnected;
    private String nodeName;
    private boolean reference, thevA;

    public SuperNode(ArrayList<Integer> childNodes, String nodeName) {
        this.childNodes = childNodes;
        this.elementsConnected = new ArrayList<Resistor>();
        this.nodeName = nodeName;
    }

    public SuperNode(ArrayList<Integer> childNodes, String nodeName, boolean isThevA) {
        this.childNodes = childNodes;
        this.elementsConnected = new ArrayList<Resistor>();
        this.nodeName = nodeName;
        // if it's a reference node
        if (isThevA){
            this.thevA = true;
            this.setVoltage(5);
        }
    }

    // set the voltage associated to this super node
    // only the end nodes will get a voltage associated
    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public Object getVoltage() {
        if (this.voltage != -999)
            return new Integer(this.voltage);
        else return new String("v_" + this.nodeName);
    }

    // add a resistor to the element list of the super node object
    public void addElement(Resistor r) {
        elementsConnected.add(r);
    }

    public ArrayList<Resistor> getElements() {
        return elementsConnected;
    }

    public ArrayList<Integer> getChildren() {
        return childNodes;
    }

    // returns true if the node parameter is a child node
    // of this super node
    public boolean containsChild(Integer node) {
        for (Integer childNode : childNodes)
            if (node.equals(childNode))
                return true;
        return false;
    }

    public void addChild(Integer child) {
        this.childNodes.add(child);
    }

    public int getSize() {
        return childNodes.size();
    }

    public void setReference() {
        this.reference = true;
        this.setVoltage(0);
    }

    public boolean isReference() {
        return reference;
    }

    public boolean isThevA() {
        return thevA;
    }

}
