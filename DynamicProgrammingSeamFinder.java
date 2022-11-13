import graphs.Graph;

import java.util.*;

public class DynamicProgrammingSeamFinder implements SeamFinder { 
    private double[][] pixelEnergy;

    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        // TODO: Your code here!
        pixelEnergy = new double[picture.height()][picture.width()];
        ArrayList<Integer> result = new ArrayList<>();
        int rows = picture.height();
        int cols = picture.width();
        for (int j = 0; j < rows; j++) {
            pixelEnergy[j][0] = f.apply(picture, 0, j);
        }
        for (int i = 1; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (j == 0) {
                    pixelEnergy[j][i] = f.apply(picture, i, j) + Math.min(pixelEnergy[j][i - 1], 
                                                                          pixelEnergy[j+ 1][i - 1]);
                } else if (j == rows - 1) {
                    pixelEnergy[j][i] = f.apply(picture, i, j) + Math.min(pixelEnergy[j - 1][i - 1], 
                                                                          pixelEnergy[j][i - 1]);
                } else {
                    pixelEnergy[j][i] = f.apply(picture, i, j) + Math.min(pixelEnergy[j - 1][i - 1], 
                                        Math.min(pixelEnergy[j][i - 1], pixelEnergy[j + 1][i - 1]));
                }
            }
        }
        double min = pixelEnergy[0][cols - 1];
        int minRow = 0;
        for (int j = 1; j < rows; j++) {
            if (pixelEnergy[j][cols - 1] < min) {
                min = pixelEnergy[j][cols - 1];
                minRow = j;
            }
        }
        findSeamHelper(picture, result, cols - 1, minRow);
        Collections.reverse(result);
        return result;
    }

    private ArrayList<Integer> findSeamHelper(Picture picture, ArrayList<Integer> result, int col, int row) {
        if (col == 0) {
            result.add(row);
            return result;
        } else {
            result.add(row);
            if (row > 0 && row < picture.height() - 1) {
                if (pixelEnergy[row - 1][col - 1] <= pixelEnergy[row][col - 1] && 
                    pixelEnergy[row - 1][col - 1] <= pixelEnergy[row + 1][col - 1]) {
                        return findSeamHelper(picture, result, col - 1, row - 1);
                } else if (pixelEnergy[row][col - 1] <= pixelEnergy[row - 1][col - 1] && 
                           pixelEnergy[row][col - 1] <= pixelEnergy[row + 1][col - 1]) {
                        return findSeamHelper(picture, result, col - 1, row);
                } else {
                        return findSeamHelper(picture, result, col - 1, row + 1);
                }
            } else if (row == 0) {
                if (pixelEnergy[row][col - 1] <= pixelEnergy[row + 1][col - 1]) {
                        return findSeamHelper(picture, result, col - 1, row);
                } else {
                        return findSeamHelper(picture, result, col - 1, row + 1);
                }
            } else {
                if (pixelEnergy[row - 1][col - 1] <= pixelEnergy[row][col - 1]) {
                        return findSeamHelper(picture, result, col - 1, row - 1);
                } else { 
                        return findSeamHelper(picture, result, col - 1, row);
                }
            }
        }
    }
}
