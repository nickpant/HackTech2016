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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import Components.SuperNode;
import ImageProcessor.DataFetcher;
import Components.Resistor;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        // counter variable for the super node name
        int k = 0;

        DataFetcher fetch = new DataFetcher();
        int[][] elementMatrix = fetch.loadData();
        ArrayList<SuperNode> superNodes = new ArrayList<SuperNode>();

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
        for (SuperNode node : superNodes)
                if (node.containsChild(new Integer(0)))
                    node.setThevA();

        // populate the equations array with the linear equations
        // that describe the circuit
        String[] equations = new String[superNodes.size() - 2];

        int i = 0;

        for (SuperNode node : superNodes) {
            // only do calculations for super nodes
            // that are NOT the reference
            if (!node.isReference() && !node.isThevA()) {
                equations[i] = "0 = ";
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
                    // form the equation
                    equations[i++] += "(" + node.getVoltage() + "-" + otherSuperNode.getVoltage() + ")/" + R.getResistance();
                }
            }
        }

        TextView e = (TextView) findViewById(R.id.output_text);
        e.setText(equations[0]);



        Button refreshButton = (Button)findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
            }
        });
    }


        /*
        File imgFile = new  File("/sdcard/Circuit Simulator/cam_image.jpg");

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.image);
            myImage.setImageBitmap(myBitmap);
        }
        */
}


