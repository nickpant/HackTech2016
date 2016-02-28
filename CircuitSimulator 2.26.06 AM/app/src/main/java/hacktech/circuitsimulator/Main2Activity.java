package hacktech.circuitsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import Components.SuperNode;
import ImageProcessor.DataFetcher;
import Components.Resistor;

// We perform our nodal analysis in this class

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        DataFetcher fetch = new DataFetcher();
        int[][] elementMatrix = fetch.loadData();
        ArrayList<SuperNode> superNodes = new ArrayList<SuperNode>();

        int k = 0;

        // populate the super nodes array list
        for (int i = elementMatrix.length - 1; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                // if the element is a short, create a superNode
                // as long as another super node does not already contain
                // the child nodes i or j
                boolean existingNode = false;
                // if the element is a short,create super nodes
                // -1 corresponds to a short
                if (elementMatrix[i][j] == -1) {
                    // if a super node with one of the child node (i,j) exists
                    // then add the other child to the superNode
                    for (SuperNode node : superNodes) {
                        if (node.containsChild(new Integer(i)) && !node.containsChild(new Integer(j))) {
                            node.addChild(new Integer(j));
                            existingNode = true;
                        } else if (node.containsChild(new Integer(j)) && !node.containsChild(new Integer(i))) {
                            node.addChild(new Integer(i));
                            existingNode = true;
                        }
                    }

                    // if no super nodes with the child nodes (i or j) exist
                    // then create a new super node with child nodes (i, j)
                    if (!existingNode) {
                        ArrayList<Integer> childNodes = new ArrayList<Integer>();
                        childNodes.add(i);
                        childNodes.add(j);
                        superNodes.add(new SuperNode(childNodes, Integer.toString(k++)));
                    }
                }

            }
        }

        // populate the super nodes with resistors
        for (SuperNode node : superNodes) {
            ArrayList<Integer> childNodes = node.getChildren();
            for (int i = elementMatrix.length - 1; i >= 0; i--) {
                for (int j = i; j >= 0; j--) {
                    if (elementMatrix[i][j] != -999 && elementMatrix[i][j] != -1 && elementMatrix[i][j] != 0){
                        if (node.containsChild(i)) {
                            node.addElement(new Resistor(new int[]{i, j}, elementMatrix[i][j]));
                        } else if (node.containsChild(j)) {
                            node.addElement(new Resistor(new int[]{i, j}, elementMatrix[i][j]));
                        }
                    }
                }
            }
        }

        // determine the largest super node, and set it to reference
        int largestNodeIndex = 0;
        for (int n = 0; n < superNodes.size(); n++) {
            if (superNodes.get(n).getSize() > superNodes.get(largestNodeIndex).getSize())
                largestNodeIndex = n;
        }
        superNodes.get(largestNodeIndex).setReference();

        // set the voltage of superNodes containing node A to 5V
        boolean containsRefA = false;
        for (SuperNode node : superNodes) {
            if (node.containsChild(new Integer(0))) {
                node.setVoltage(5);
                containsRefA = true;
            }
        }

        String[] voltageEquations = new String[superNodes.size() - 1];
        // if the super nodes does not contain refA add it manually
        // and declare the string array
        if (!containsRefA)
            superNodes.add(new SuperNode(new ArrayList<Integer>(Arrays.asList(0)), Integer.toString(k), true));

        int i = 0;

        for (SuperNode node : superNodes) {
            // only do calculations for super nodes
            // that are NOT the reference
            if (!node.isReference() && !node.isThevA()) {
                voltageEquations[i] = "0 = ";
                // iterate through each resistor connected to node
                // to determine what the other node connected to R is
                for (Resistor R : node.getElements()) {
                    // create a temporary otherSuperNode
                    // for instantiating purposes
                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    temp.add(-1);
                    temp.add(-1);
                    SuperNode otherSuperNode = new SuperNode(temp, "-1");
                    // determine the node number of the other node that's connected to R
                    int otherNode = R.getOtherNodeConnected(node.getChildren());
                    // determine the superNode that's associated with the other node
                    for (SuperNode node2 : superNodes) {
                        if (node2.containsChild(new Integer(otherNode))) {
                            otherSuperNode = node2;
                            break;
                        }
                    }
                    // if no superNode is associate with the other node, get the node info directly
                    // form the equation
                    voltageEquations[i] += "+(" + node.getVoltage() + "-" + otherSuperNode.getVoltage() + ")/" + R.getResistance();
                }
                i++;
            }
        }

        // some wolfram stuff happens here
        // make sure to setVoltage of the super nodes to the proper values!!!
        int v_1 = 3; // CHANGE THIS!!!!!!!!!!

        // find the super node right before reference A
        int secondLastIndex = superNodes.size() - 2;
        SuperNode secondLastNode = superNodes.get(secondLastIndex);
        secondLastNode.setVoltage(3);  // CHANGE THIS!!!!!!!!!!
        // perform conservation of current
        String currentEquation = "I = ";
        for (Resistor R : secondLastNode.getElements()) {
            int otherNode = R.getOtherNodeConnected(secondLastNode.getChildren());
            // do calculations only if the other node is not the reference node
            if (otherNode != 0) {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                temp.add(-1);
                temp.add(-1);
                SuperNode otherSuperNode = new SuperNode(temp, "-1");
                // determine the superNode that's associated with the other node
                for (SuperNode node : superNodes) {
                    if (node.containsChild(new Integer(otherNode))) {
                        otherSuperNode = node;
                        break;
                    }
                }
                int voltageDrop = (Integer) secondLastNode.getVoltage() - (Integer) otherSuperNode.getVoltage();
                currentEquation += "+" + voltageDrop + "/" + R.getResistance() + " ";
            }
        }

        // wolfram alpha stuff here
        // calculate I from current equation
        // calculate Req by doing V / I;


        TextView e = (TextView) findViewById(R.id.output_text);
        TextView f = (TextView) findViewById(R.id.output_text2);
        e.setText(voltageEquations[0]);
        f.setText(currentEquation);

        File imgFile = new  File("/sdcard/Circuit Simulator/cam_image.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.image);
            myImage.setImageBitmap(myBitmap);
        }



        Button refreshButton = (Button)findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
    }
}


