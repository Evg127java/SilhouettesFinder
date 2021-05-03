package com.shpp.p2p.cs.ekondratiuk.assignment13;

/** Constants for the silhouettes identifier */
public final class Constants {

    /** Minimal percents of pixels amount relatively to the largest object
     * which might be identified as an object
     * */
    protected static final int PERCENTAGE_PART_OF_LARGEST_OBJECT = 10;

    /** Color value difference between a background and lighter colors */
    protected static final int RANGE_BETWEEN_BG_AND_LIGHTER_COLORS = 50;

    /** Color value difference between a background and darker colors */
    protected static final int RANGE_BETWEEN_BG_AND_DARKER_COLORS = 50;

    /** threshold value for images with alfa channel
     * The colors which less than the threshold are dark
     * The colors which more than the threshold are light
     * */
    protected static final int ALFA_THRESHOLD_VALUE = 127;

    /** Maximal value for alfa which means the image is fully non-transparent */
    protected static final int FULLY_NON_TRANSPARENCY_VALUE = 255;

    /** Default path to folder with images */
    protected static final String PATH_TO_FILE = "assets/";

    /** Default image file name with extension */
    protected static final String DEFAULT_FILE_NAME = "test.jpg";

    /** The marker which identifies a pixel as an object part */
    protected static final int MARKED = 1;

    /** The marker which identifies a pixel as not an object */
    protected static final int UNMARKED = 0;

    /** The marker which identifies a pixel as visited during processing */
    protected static final int VISITED = 2;
}

