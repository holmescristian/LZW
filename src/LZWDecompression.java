/*
 * Name: Cristian Holmes
 * Date: 3/22/2018
 * Description: Decompresses a text file that has been compressed with the lzw
 * algorithm
 */
import java.io.*;
import java.util.*;

public class LZWDecompression {
    /**
     * Decompresses a file that was compressed with the LZW algorithm
     * @param fout Output to the decompressed file
     * @param fin  Input from the compressed file
     * @throws Exception IO exception possible when reading in
     */
    public static void decompress(BufferedWriter fout, RandomAccessFile fin)throws Exception {
        // Build the dictionary with all individual characters
        int dictSize = 128;
        Map<Integer,String> dictionary = new HashMap<>();
        for (int i = 0; i < 128; i++)
            dictionary.put(i, "" + (char)i);

        //The value of the compressed text
        int num;
        //Read in the first character of the file
        String text = "" + (char) fin.read();
        //Add the first decoded character to the file
        fout.write(text);
        //Read in characters until there aren't anymore
        while((num = fin.read()) != -1) {
            //What will be entered into the file
            String entry;
            //If the value already exists add that text to the file
            if (dictionary.containsKey(num))
                entry = dictionary.get(num);
            //Otherwise make the new code with the text and the first character repeated
            else if (num == dictSize)
                entry = text + text.charAt(0);
            // If the number is bigger than the dictionary size it is incorrectly compressed
            else
                throw new IllegalArgumentException("Bad compressed num: " + num);
            //Write the text to the file
            fout.write(entry);
            // Add text+entry[0] to the dictionary.
            dictionary.put(dictSize++, text + entry.charAt(0));
            //Make the entry the new text
            text = entry;
        }
    }

    public static void main(String[] args) throws Exception{
        //Scanner for user input
        Scanner input = new Scanner(System.in);
        //Ask the user what file will be decompressed
        System.out.print("Enter a valid filename: ");
        StringBuilder filename = new StringBuilder(input.next());

        //If the file is not a .lzw file it isn't a valid file and the user should be asked again
        int extensionIndex = filename.indexOf(".lzw");
        while(extensionIndex == -1){
            System.out.print("Please Enter a valid filename: ");
            filename = new StringBuilder(input.next());
            extensionIndex = filename.indexOf(".lzw");
        }
        //Cut off the file extension
        String newFile = filename.substring(0, extensionIndex);
        //Make the file input for reading in the compressed file
        RandomAccessFile fileIn = new RandomAccessFile(filename.toString(), "r");
        //Make the file output for writing in the decompressed file
        BufferedWriter fileOut = new BufferedWriter(new FileWriter( new File(newFile)));

        //Decompress the file and close them both
        decompress(fileOut, fileIn );
        fileOut.close();
        fileIn.close();


    }
}
