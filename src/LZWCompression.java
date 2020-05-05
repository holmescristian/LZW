/*
 * Name: Cristian Holmes
 * Date: 4/18/2018
 * Description: Compresses a text file using the lzw algorithm
 */
import java.io.*;
import java.util.*;

public class LZWCompression {
    /**
     * Compresses A Text file using the LZW algorithm
     * @param fout  File output for writing the compressed text
     * @param fin  File input for reading in the text to be compressed
     * @throws Exception IO exception possible when reading in
     */
    public static void compress(RandomAccessFile fout, RandomAccessFile fin) throws Exception{

        //Dictionary starts with every individual character
        int dictLength = 128;
        Map<String,Integer> dictionary = new HashMap<>();
        //Set every singular character in the dictionary
        for (int i = 0; i <= 127; i++)
            dictionary.put("" + (char)i, i);

        //The initial String
        String str = "";
        //Read in the first character
        char ch = (char) fin.read();

        //As long as it keeps reading in characters keep going
        while (ch >= 0 && ch <= 127) {
            //Add the new character to the string
            String newWord = str + ch;
            //If it is in the dictionary add the character to the string
            if (dictionary.containsKey(newWord))
                str = newWord;
            //Otherwise Add str to the text file and put str + ch in the dictionary
            else {
                fout.write(dictionary.get(str));
                // Add str + ch to the dictionary and increment the dictionary
                dictionary.put(newWord, dictLength++);
                //Make the character the new string
                str = "" + ch;
            }
            //Read in the next character
            ch = (char) fin.read();
        }

        // Output the code for the final str.
        if (!str.equals("")){
            fout.write(dictionary.get(str));
        }
    }

    public static void main(String[] args) throws Exception {
        //Scanner for user input
        Scanner input = new Scanner(System.in);
        //Get the name of the file
        System.out.print("Enter the file to be compressed: ");
        StringBuilder filename = new StringBuilder(input.next());

        //Used to read in the file
        RandomAccessFile fileIn = new RandomAccessFile(filename.toString(), "r");

        //Append .lzw to the compressed file
        filename.append(".lzw");
        // Used to output the compressed text
        RandomAccessFile fileOut = new RandomAccessFile(filename.toString(), "rw");
        //Compress the file
        compress(fileOut, fileIn);
        //Close the file
        fileIn.close();
        fileOut.close();
    }
}
