
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * LZW Decompression Implementation
 *
 * @author Stephen Costeira <coste011@rangers.uwp.edu>
 */
public class StephenLZWDecompression {

    /**
     * Main Entrypoint for Decompression
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	// read in file to compress
	// and ready output file
        Scanner keyboard = new Scanner(System.in);
        try {
            //input file name must end with lzw per instructions.
            String in = keyboard.nextLine();
            if (!in.endsWith(".lzw")) {
                System.out.println("Input file name must end with '.lzw' !");
                return;
            }
            String out = in.substring(0, in.length() - 4);
            //decompress
            decompress(in, out);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Decompresses a file using the LZW algorithm
     *
     * @param in Path to compressed input file
     * @param out Path to file to save uncompressed output
     * @throws IOException
     */
    private static void decompress(String in, String out) throws IOException {

        StringBuilder data = new StringBuilder("");
        //Pre-fill dictionary with standard ASCII characters indexed 0-127.
        HashMap<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i <= 127; i++) {
            dictionary.put(i, Character.toString((char) i));
        }

        //Decompress
        //
        //Read in the characters as a Stream
        FileInputStream input = new FileInputStream(in);

        //Capture first character
        String w = "" + (char) (int) input.read();
        data.append(w);

        int character;
        // While we're able to read bytes
        while ((character = input.read()) != -1) {
            //if str + ch is in the dictionary
            String phrase = "" + (char) character;

            //does the dictionary have this already?
            if (dictionary.containsKey(character)) {
                phrase = dictionary.get(character);
            } else if (character == dictionary.size()) {
                //is it the
                phrase = w + w.charAt(0);
            } else {
                throw new IllegalArgumentException("Bad compression: " + w);
            }

            data.append(phrase);

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictionary.size(), w + phrase.charAt(0));

            w = phrase;
        }

        //Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(out)))) {
            writer.write(data.toString());
        }
    }
}
