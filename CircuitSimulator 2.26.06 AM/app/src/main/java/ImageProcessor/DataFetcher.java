package ImageProcessor;

import java.util.ArrayList;

/**
 * Created by MasterPiyasirisilp on 2016-02-28.
 */
public class DataFetcher {

    int[][] elementMatrix;

    public DataFetcher() {

    }

    public int[][] loadData() {

        // set always the left most node to be zero

        // calculate number of nodes

        // loop to determine what element connects which nodes

        // -1 is a short
        // 0 is an open
        // all other numbers are resistances in ohms

        // A and B (the nodes we are trying to find
        // the thevenin resistance with respect to)
        // cant be nodes

        elementMatrix = new int[][] {
                {-999,100,-999,-999,-999,0},
                {100, -999, -1, -999, 200, -999},
                {-999, -1, -999, 300, -999, -999},
                {-999, -999, 300, -999, -1, -999},
                {-999, -999, -999, -1, -999, -1},
                {0, -999, -999, -999, -999, -1}};



        return elementMatrix;

    }

}
