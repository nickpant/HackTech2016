package ImageProcessor;

import com.wolfram.jlink.*;

/**
 * Created by MasterPiyasirisilp on 2016-02-28.
 */
public class DataFetcher {

    int[][] elementMatrix;

    public int[][] loadData() {

        // The code below is meant to communicate with the Wolfram API
        // We will eventually:
        // Detect corners (i.e. nodes) in the circuit
        // Return pictures of the elements in between the nodes
        // Use Wolfram's Classify[] machine learning function to determine what the element is (i.e. a resistor? a short?)
        // Then, we will populate an array (the element in the (i,j) position of the array will tell us which circuit element
        // is between nodes i and j

        // Due to lack of time, we were not able to successfully communicate Java with the Wolfram API
        // however, we were able to show (on Wolfram) that the image detection features we wish to use
        // including machine learning works as we wish
        // It is only a mattery of making sure our app communicates with the cloud

        /*
        KernelLink ml = null;
        try {
            String[] mlArgs = {"-linkmode", "launch", "-linkname", "\'/Applications/Mathematica.app/Contents/MacOS/MathKernel\" -mathlink\'"};
                    ml = MathLinkFactory.createKernelLink(mlArgs);
            }catch (MathLinkException e) {
            if (ml != null)
                ml.close();
            return null;
        }
        try {
          // wolfram code goes here
            ml.discardAnswer();
            ml.evaluate("ImageCorners[\"sdcard/Circuit Simulator/cam_image.jpg\"]");
            ml.waitForAnswer();

            int[][] result = ml.getIntArray2();

        }
        catch (MathLinkException e) {}
        finally {
            ml.close();
        }

        */


        // IN INTEREST OF TIME, WE HAVE PRELOADED CIRCUIT DATA BELOW
        // TO SHOW THAT OUR NODAL ANALYSIS ALGORITHM DOES WORK
        // now it is just a matter of connectign it with Wolfram's API

        // -999 represents junk
        // -1 is a short
        // 0 is an open
        // all other numbers are resistances in ohms

        // this matrix rep
        elementMatrix = new int[][] {
                {-999,100,-999,-999,-999,0},
                {100, -999, -1, -999, 200, -999},
                {-999, -1, -999, 300, -999, -999},
                {-999, -999, 300, -999, -1, -999},
                {-999, 200, -999, -1, -999, -1},
                {0, -999, -999, -999, -1, -999}};



        return elementMatrix;
    }

}
