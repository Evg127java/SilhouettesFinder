package com.shpp.p2p.cs.ekondratiuk.assignment13;

import static com.shpp.p2p.cs.ekondratiuk.assignment13.Constants.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/** Implements identifying silhouettes logic */
public class SilhouettesIdentifier {

    /**
     * Identifies objects number in the specified image
     * Throws an exception if cannot process the file
     *
     * @param imageFileName Input file name
     * @return              Number of filtered identified objects as silhouettes
     * @throws IOException  IOException
     */
    public int identifySilhouettes(String imageFileName) throws IOException {
        int[][] binaryImage = getBinaryImage(imageFileName);
        return getFilteredObjects(getObjectsArray(binaryImage));
    }

    /**
     * Gets only that objects number which are under the required condition
     *
     * @param objects Object to check
     * @return        Number of objects
     */
    private int getFilteredObjects(List<Integer> objects) {
        int maxObjectSize = Collections.max(objects);
        int counter = 0;
        for (Integer object : objects) {
            if (object > (maxObjectSize * PERCENTAGE_PART_OF_LARGEST_OBJECT / 100)) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gets all found objects as pixels amounts list
     *
     * @param binaryImage Binary representation of the image to process
     * @return            Identified objects number
     */
    private List<Integer> getObjectsArray(int[][] binaryImage) {

        List<Integer> foundObjectsList = new ArrayList<>();

        for (int y = 0; y < binaryImage.length; y++) {
            for (int x = 0; x < binaryImage[0].length; x++) {
                /* If current vertex is already visited just go on */
                if (binaryImage[y][x] == VISITED) {
                    continue;
                }
                /* If current vertex is not visited yet but unmarked */
                if (binaryImage[y][x] == UNMARKED) {
                    binaryImage[y][x] = VISITED;
                } else {
                    /* Run "bfs" and try to fill the objects list with found objects */
                    int objectPixelsNumber = getObjectPixelsAmount(y, x, binaryImage);
                    if (objectPixelsNumber > 0) {
                        foundObjectsList.add(objectPixelsNumber);
                    }
                }
            }
        }
        return foundObjectsList;
    }

    /**
     * Tries to get some pixels amount as an object using breadth first search algorithm
     *
     * @param y            y - coordinate for the entry-point to run BFS
     * @param x            x - coordinate for the entry-point to run BFS
     * @param binaryImage  Binary image for objects searching
     * @return             Connected pixels amount as an integer number
     */
    private int getObjectPixelsAmount(int y, int x, int[][]binaryImage) {
        int objectPixelsCounter;
        List<int[]> neighbourPixels;
        Queue<int[]> queue = new LinkedList<>();
        objectPixelsCounter = 0;
        /* Entry point init */
        int[] entryCoordinate = new int[]{y, x};
        queue.add(entryCoordinate);
        /* Count connected pixels until the queue is empty */
        while (!queue.isEmpty()) {
            objectPixelsCounter++;
            /* Get the pixel's coordinates from the queue and process it */
            int[] temp = queue.poll();
            y = temp[0];
            x = temp[1];
            binaryImage[y][x] = VISITED;
            /* Get and check neighbour pixels to the current */
            neighbourPixels = getNeighbourPixels(x, y, binaryImage);
            for (int[] pixelCoordinates : neighbourPixels ) {
                /* Put the pixel's coordinates to the queue if it is incident to current */
                y = pixelCoordinates[0];
                x = pixelCoordinates[1];
                queue.add(new int[]{y, x});
                binaryImage[y][x] = VISITED;
            }
        }
        return objectPixelsCounter;
    }

    /**
     * Gets all the incident pixels' coordinates to the specified one
     *
     * @param x           y - coordinate for the current pixel to check
     * @param y           x - coordinate for the current pixel to check
     * @param binaryImage Binary image with the set of pixels to check
     * @return            Set of pixels' coordinates if they exist
     */
    private List<int[]> getNeighbourPixels(int x, int y, int[][] binaryImage) {
        List<int[]> pixels = new ArrayList<>();
        int[] yOffset = {-1, 1, -1, 1, 0, 0, 1, -1};
        int[] xOffset = {0, 0, -1, -1, -1, 1, 1, 1};
        for (int i = 0; i < 8; i++) {
            if (
                    y + yOffset[i] >= 0 && y + yOffset[i] < binaryImage.length &&
                            x + xOffset[i] >= 0 && x + xOffset[i] < binaryImage[0].length &&
                            binaryImage[y + yOffset[i]][x + xOffset[i]] == MARKED
            ) {
                pixels.add(new int[]{y + yOffset[i], x + xOffset[i]});
            }
        }
        return pixels;
    }

    /**
     * Gets binary representation of passed image file
     *
     * @param file File name of the image to represent
     * @return              Binary image array of the passed image
     * @throws IOException  IOException if something wrong with the file with the passed name
     */
    private int[][] getBinaryImage(String file) throws IOException {
        BufferedImage image = ImageIO.read(new File(file));

        /* Get background's average color value */
        int avgBgColorValue = getAvgBackgroundColorValue(image);

        /* Make binary image analysing every pixel's color value */
        int[][] binaryImage = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {

                /* Get average pixel's color value */
                Color currentColor = new Color(image.getRGB(j, i), true);
                int avgCurrentColorValue =
                        (currentColor.getRed() + currentColor.getBlue() + currentColor.getGreen()) / 3;

                /* Get transparency value for alfa-channel images */
                int imageTransparency = (new Color(image.getRGB(0, 0), true)).getAlpha();

                /* set current pixel's binary value for alfa-channel images */
                if (image.getColorModel().hasAlpha() && imageTransparency != FULLY_NON_TRANSPARENCY_VALUE) {
                    binaryImage[i][j] = currentColor.getAlpha() >= ALFA_THRESHOLD_VALUE ? MARKED : UNMARKED;
                } else {
                    /* set current pixel's binary value for alfa-channel images */
                    binaryImage[i][j] =
                            avgCurrentColorValue < avgBgColorValue - RANGE_BETWEEN_BG_AND_DARKER_COLORS ||
                            avgBgColorValue + RANGE_BETWEEN_BG_AND_LIGHTER_COLORS < avgCurrentColorValue ?
                            MARKED : UNMARKED;//System.out.print(binaryImage[i][j]);
                }
            }
        }
        return binaryImage;
    }

    /** Gets average background color's value for the specified image */
    public static int getAvgBackgroundColorValue(BufferedImage image) {
        /* Use four image corner points */
        int[] colorsArray = {
                image.getRGB(0, 0),
                image.getRGB(0, image.getHeight() - 1),
                image.getRGB(image.getWidth() - 1, 0),
                image.getRGB(image.getWidth() - 1, image.getHeight() - 1),
        };
        int searchValue;
        int searchValueCounter = 0, currentValueCounter = 0;

        Arrays.sort(colorsArray);
        searchValue = colorsArray[0];

        for (int i = 0; i < colorsArray.length - 1; i++) {
            if (colorsArray[i] == (colorsArray[i + 1])) {
                currentValueCounter++;
            }
            if (currentValueCounter > searchValueCounter) {
                searchValueCounter = currentValueCounter;
                searchValue = colorsArray[i];
            }
        }
        int bgColorValue = searchValue;
        Color bgColor = new Color(bgColorValue, true);
        return (bgColor.getRed() + bgColor.getBlue() + bgColor.getGreen()) / 3;
    }
}