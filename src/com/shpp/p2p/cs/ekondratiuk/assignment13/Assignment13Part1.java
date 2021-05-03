package com.shpp.p2p.cs.ekondratiuk.assignment13;

import static com.shpp.p2p.cs.ekondratiuk.assignment13.Constants.*;

import java.io.IOException;

/**
 * Identifies silhouettes number on the passed image and prints result.
 * The image file must be passed in program arguments like "fileName.jpg"
 * The program uses "test.jpg" if the image file is not specified.
 * Test images must be in the "assets" folder located in the root of the project.
 * Otherwise the other folder must be specified in Constants class in
 * PATH_TO_FILE constant.
 */
public class Assignment13Part1 {

    public static void main(String[] args) {

        SilhouettesIdentifier identifier = new SilhouettesIdentifier();
        int silhouettesNumber;
        String file = PATH_TO_FILE + (args.length == 0 ? DEFAULT_FILE_NAME : args[0]);
        try {
            silhouettesNumber = identifier.identifySilhouettes(file);
            System.out.println(silhouettesNumber + " silhouettes identified in " + "\"" + file + "\"");
        } catch (IOException e) {
            System.out.println("Check the specified file name or(and) its path\n" + e.getMessage());
        }
    }
}