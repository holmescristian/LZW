
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * LZW Compression Implementation
 *
 * @author Stephen Costeira <coste011@rangers.uwp.edu>
 */
public class StephenLZWCompression {
    /**
     * Main Entrypoint for Compression
     * @param args 
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        try {
            // read in file to compress
            // and ready output file
            String in  = keyboard.nextLine();
            String out = in + ".lzw";
            // compress
            compress(in, out);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Compresses a file using LZW algorithm.
     * @param in    Path to input file
     * @param out   Path to output file
     * @throws IOException 
     */
    private static void compress(String in, String out) throws IOException {

        // Pre-fill dictionary with standard ASCII characters indexed 0-127.
        HashMap<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i <= 127; i++) {
            dictionary.put(Character.toString((char) i), i);
        }

        // Compress
        //
        // Read in the bytes of a file in a Stream
        FileInputStream input = new FileInputStream(in);
        ArrayList<Integer> data = new ArrayList<>();
        int character;
        String str = "";
        // While we're able to read bytes
        while ((character = input.read()) != -1) {
            // if str + ch is in the dictionary
            String test = str + (char)character;
            if (dictionary.containsKey(test)) {
                // set str to str + ch
                str = test;
            } else {
                // else
                // output the index of the str
                data.add(dictionary.get(str));
                // add str + ch to the dictionary
                dictionary.put(test, dictionary.size());
                // set str to ch
                str = "" + (char)character;
            }
        }
        
        // Capture final byte
        if (!str.equals("")) {
            data.add(dictionary.get(str));
        }

        // Write to file each byte
        FileOutputStream output = new FileOutputStream(out, false);
        for (Integer a_byte : data) {
            output.write(a_byte);
        }
        // Close and Save
        output.close();
    }
}
