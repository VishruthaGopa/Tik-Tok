// Glic-Glok
// This program creates and maintains both a dataFile which includes
// the raw data inputted by a user, and a markdown file which outputs
// this data neatly. The program has the capability to view, delete,
// add to, and modify the dataFile. It sorts the data, and then organizes
// it into a table.

import java.io.*;
import java.util.regex.*;
import java.lang.*;
import java.text.DecimalFormat;

class Main {

  /**
   * Main method
   * 
   * @param args argument list passed to main method
   */
  public static void main(String[] args) {
    String c = " ";
    do {
      c = viewMenu();
    } while (c.equalsIgnoreCase("Y"));
    System.out.println("Thank you, bye!");
    makeMarkdown();
  }

  /**
   * Prints the menu and takes in the input given by the user.
   * 
   * @return input given by the user
   */
  public static String viewMenu() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String userEntry = " ";

    try {
      System.out.println("1. View\n2. Delete\n3. Add\n4. Quit\n5. Modify");
      System.out.println("Choose option:");
      userEntry = br.readLine();
    } catch (IOException err) {
      System.out.println(err);
    }

    switch (userEntry) {
    case "1":
      viewFile();// Outputs the values from the text file to the console.
      break;
    case "2":
      deleteFile("dataFile.txt");// Delete the entire text file
      break;
    case "3":
      addInfo(br);// Adds & Saves multiple users to the current file
      break;
    case "4":
      userEntry = quitProgram();
      break;
    case "5":
      modify("dataFile.txt", br);
      break;
    default:
      System.out.println("Choose the correct option");
    }

    try {
      if (userEntry != "N") {
        System.out.println("Do you wish to continue? Enter Y or N");
        userEntry = br.readLine();
        userEntry = userEntry.trim();
      }
    } catch (IOException err) {
      System.out.println(err);
    }
    // System.out.println("userEntry" + userEntry);

    return userEntry;
  }

  /**
   * Outputs the values from the text file to the console.
   * 
   * 
   */
  public static void viewFile() {
    try {
      FileReader fr = new FileReader("dataFile.txt");
      BufferedReader br = new BufferedReader(fr);
      String line = br.readLine();

      while (line != null) {
        System.out.println(line);
        line = br.readLine();
      }
      br.close();
    } catch (IOException err) {
      System.out.println("Err reading from file");
    }
  }

  /**
   * Deletes the entire text file.
   * 
   * @param file the name of the file
   */
  public static void deleteFile(String file) {
    File dtaFile = new File(file);
    if (dtaFile.delete())
      System.out.println("File deleted");
    else
      System.out.println("File not deleted");
  }

  /**
   * Adds and saves multiple users to the current text file.
   * 
   * @param br Buffered Reader
   */
  public static void addInfo(BufferedReader br) {
    int length = 20;

    String regex = "^([0-9a-z_.]+$)";
    Pattern p = Pattern.compile(regex);
    String numberPattern = "\\d+(\\.\\d+)?";
    String numberPatternForLong = "^[0-9]*$";
    Pattern pNumber = Pattern.compile(numberPattern);
    Pattern pLong = Pattern.compile(numberPatternForLong);
    Matcher m;
    String userEntry;
    boolean valid = false;

    try {
      do {
        System.out.println("Enter username: ");
        userEntry = br.readLine();
        m = p.matcher(userEntry);

        if (m.matches() && userEntry.length() <= length) {
          outputToFile(userEntry);
          valid = true;
        } else {
          System.out.println("Invalid username");
        }
      } while (!valid);

      valid = false;

      do {
        System.out.println("Enter number of followers:");
        userEntry = br.readLine();
        m = pLong.matcher(userEntry);

        if (m.matches()) {
          long followers = Long.parseLong(userEntry);
          outputToFile(String.valueOf(followers));
          valid = true;
        } else {
          System.out.println("Invalid number. Do NOT enter decimals or letters.");
        }

      } while (!valid);

      valid = false;

      do {
        System.out.println("Enter number of likes:");
        userEntry = br.readLine();
        m = pLong.matcher(userEntry);

        if (m.matches()) {
          long likes = Long.parseLong(userEntry);
          outputToFile(String.valueOf(likes));
          valid = true;
        } else {
          System.out.println("Invalid number. Do NOT enter decimals or letters.");
        }

      } while (!valid);

      valid = false;

      do {
        System.out.println("Enter number of Annual ad revenue:");
        userEntry = br.readLine();
        m = pNumber.matcher(userEntry);

        if (m.matches()) {
          double revenue = Double.parseDouble(userEntry);
          revenue = Math.round(revenue * 100) / 100.0;
          outputToFile(String.valueOf(revenue));
          valid = true;
        } else {
          System.out.println("Invalid number. Enter ONLY numerals.");
        }
      } while (!valid);

    } catch (IOException err) {
      System.out.println(err);

    }
    sortLexicographically("dataFile.txt");
  }

  /**
   * Writes the formatted text to the text file.
   * 
   * @param s user input to write to the file
   */
  public static void outputToFile(String s) {
    try {
      FileWriter fw = new FileWriter("dataFile.txt", true);
      PrintWriter pw = new PrintWriter(fw);
      pw.println(s);
      pw.close();
    } catch (IOException err) {
      System.out.println("Error writing to file");
    }
  }

  /**
   * Quits and exits the program.
   * 
   * @return the string "N"
   */
  public static String quitProgram() {

    return "N";
  }

  /**
   * Replaces specific string in text file.
   * 
   * @param fileName the name of the file
   * @param br       BufferedReader
   */
  public static void modify(String fileName, BufferedReader br) {

    File fileToBeModified = new File(fileName);
    String oldContent = "";
    BufferedReader reader = null;
    FileWriter writer = null;

    try {
      System.out.println("Enter the text you want to replace");
      String original = br.readLine();
      System.out.println("Enter the text you want to replace with");
      String replaceText = br.readLine();
      reader = new BufferedReader(new FileReader(fileToBeModified));

      // Reading all the lines of input text file into oldContent
      String line = reader.readLine();

      while (line != null) {
        oldContent = oldContent + line + System.lineSeparator();
        line = reader.readLine();
      }

      // Replacing oldString with newString in the oldContent
      String newContent = oldContent.replaceAll(original, replaceText);

      // Rewriting the input text file with newContent
      writer = new FileWriter(fileToBeModified);
      writer.write(newContent);
    } catch (IOException err) {
      System.out.println(err);

    } finally {
      try {
        // Closing the resources
        reader.close();
        writer.close();
      } catch (IOException err) {
        System.out.println(err);
      }
      sortLexicographically("dataFile.txt");

    }
  }

  /**
   * Counts the number of entries in the text file.
   * 
   * @param fileName the name of the text file
   * @return line the number of lines in the file
   */
  public static int readFile(String fileName) {
    int line = 0;
    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      while ((br.readLine()) != null) {
        line++;
      }
    } catch (IOException err) {
      System.out.println(err);
    }
    return line;
  }

  /**
   * Sorts entries lexicographically in the text file.
   * 
   * @param fileName the name of the text file
   */
  public static void sortLexicographically(String fileName) {

    FileWriter fw = null;
    int arraySize = readFile(fileName);

    String[] stringList = new String[(arraySize / 4)];
    String[] inputList = new String[arraySize];
    String[] temp3 = new String[arraySize];
    String content = " ";
    int i = 0;
    int j = 0;
    int k = 0;
    int x = 0;

    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);

      while ((content = br.readLine()) != null) {
        inputList[j] = content;
        if (x == j) {
          stringList[i] = content;
          x = j + 4;
          i++;
        }
        j++;
      }

      /* Sorting the stringList */

      for (i = 0; i < stringList.length; i++) {
        for (j = i + 1; j < stringList.length; j++) {
          if ((stringList[i].compareToIgnoreCase(stringList[j]) > 0)) {
            String swap = stringList[i];
            stringList[i] = stringList[j];
            stringList[j] = swap;
          }
        }
      }

      for (i = 0; i < stringList.length; i++) {
        for (j = 0; j < inputList.length; j++) {
          if (stringList[i].equalsIgnoreCase(inputList[j])) {
            for (int l = j; l < (j + 4); l++) {
              temp3[k] = inputList[l];
              k++;
            }
          }
        }
      }

      fw = new FileWriter(fileName);
      for (i = 0; i < arraySize; i = i + 1) {
        fw.write(temp3[i] + "\n");
      }

      br.close();
      fw.close();

    } catch (Exception err) {
      System.out.println(err);
    }

  }

  /* PART 2 */

  /**
   * Invokes methods to: Reads the entries for account names, followers, likes,
   * and revenue. Formats numerical values into strings ready to be outputted.
   * Print values into a well formatted markdown file.
   */
  public static void makeMarkdown() {
    int entries = countEntries();
    String[] names = new String[entries];
    long[] followers = new long[entries];
    long[] likes = new long[entries];
    double[] revenue = new double[entries];

    readValues(names, followers, likes, revenue);

    String[] formattedFollowers = formatNum(followers);
    String[] formattedLikes = formatNum(likes);
    String[] formattedRevenue = formatNum(revenue);

    printMarkdown(names, formattedFollowers, formattedLikes, formattedRevenue, revenue);
  }

  /**
   * Prints a markdown including the name of the product, the total number of
   * users, the total ad revenue, and a table including the name, number of
   * followers, number of likes, and ad revenue of each user.
   *
   * @param names      the username of each user
   * @param followers  the number of followers of each user
   * @param likes      the number of likes of each user
   * @param revenue    the revenue of each user
   * @param rawRevenue the numerical value representing the revenue of each user
   */
  public static void printMarkdown(String[] names, String[] followers, String[] likes, String[] revenue,
      double[] rawRevenue) {
    try {
      FileWriter fw = new FileWriter("userStats.md");
      PrintWriter pw = new PrintWriter(fw);

      int users = names.length;

      pw.println("# **Glic-Glok**\n");
      pw.println("## Total Number of Glic-Glok Users: **" + users + "**\n");
      pw.println("**User Names**|**Followers**|**Likes**|**Ad Revenue ($)**");
      pw.println("---|---|---|---");

      for (int i = 0; i < users; i++) {
        pw.println(names[i] + "|" + followers[i] + "|" + likes[i] + "|" + revenue[i]);
      }

      pw.printf("%n `Total Ad Revenue: %s`%n", findSum(rawRevenue));
      pw.println("-");
      pw.close();
    } catch (IOException err) {
      System.out.println("An error occurred while creating the markdown.");
    }
  }

  /**
   * Finds the sum of an array and formats it to be outputted to the markdown.
   *
   * @param arr the array of which this method finds the sum
   * @return the sum of the values in the array
   */
  public static String findSum(double[] arr) {
    double sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
    }
    DecimalFormat dF = new DecimalFormat("$###,###,###,##0.00");
    return dF.format(sum);
  }

  /**
   * Formats an array of longs to be outputted as followers/likes to the markdown
   * table.
   *
   * @param nums the array of longs which will be formatted
   * @return the formatted array of strings
   */
  public static String[] formatNum(long[] nums) {
    String[] formatted = new String[nums.length];
    double temp = 0;

    for (int i = 0; i < nums.length; i++) {

      if (nums[i] > 1000000000) {
        DecimalFormat dF = new DecimalFormat("#0.0");
        temp = Math.floor(nums[i] / 100000000) / 10.0;
        if ((long) temp == temp)
          formatted[i] = dF.format(temp).substring(0, dF.format(temp).length() - 2) + "B";
        else
          formatted[i] = dF.format(temp) + "B";
      } else if (nums[i] > 1000000) {
        DecimalFormat dF = new DecimalFormat("##0.0");
        temp = Math.floor(nums[i] / 100000) / 10.0;
        if ((long) temp == temp)
          formatted[i] = dF.format(temp).substring(0, dF.format(temp).length() - 2) + "M";
        else
          formatted[i] = dF.format(temp) + "M";
      } else if (nums[i] > 1000) {
        DecimalFormat dF = new DecimalFormat("##0.0");
        temp = Math.floor(nums[i] / 100) / 10.0;
        if ((long) temp == temp)
          formatted[i] = dF.format(temp).substring(0, dF.format(temp).length() - 2) + "K";
        else
          formatted[i] = dF.format(temp) + "K";
      } else {
        formatted[i] = "" + nums[i];
      }
    }
    return formatted;
  }

  /**
   * Formats an array of doubles to be outputted as revenue to the markdown table.
   *
   * @param nums the array of doubles which will be formatted
   * @return the formatted array of strings
   */
  public static String[] formatNum(double[] nums) {
    String[] formatted = new String[nums.length];
    DecimalFormat dF = new DecimalFormat("##,###,###,##0.00");

    for (int i = 0; i < nums.length; i++) {
      formatted[i] = dF.format(nums[i]);
    }
    return formatted;
  }

  /**
   * Counts the number of entries currently stored in dataFile.txt.
   *
   * @return the number of entries
   */
  public static int countEntries() {
    int i = 0;
    try {
      FileReader fr = new FileReader("dataFile.txt");
      BufferedReader br = new BufferedReader(fr);

      String line = "";

      do {
        line = br.readLine();
        i++;
      } while (line != null);

      br.close();
    } catch (IOException err) {
      System.out.println(err);
    }
    return i / 4;
  }

  /**
   * Reads the values stored in dataFile.txt and stores each type of data in a
   * separate array.
   *
   * @param names     array in which the usernames are stored
   * @param followers array in which the numbers of followers are stored
   * @param likes     array in which the numbers of likes are stored
   * @param revenue   array in which the revenues are stored
   */
  public static void readValues(String[] names, long[] followers, long[] likes, double[] revenue) {
    try {
      FileReader fr = new FileReader("dataFile.txt");
      BufferedReader br = new BufferedReader(fr);

      for (int i = 0; i < names.length; i++) {
        names[i] = br.readLine();
        followers[i] = Long.parseLong(br.readLine());
        likes[i] = Long.parseLong(br.readLine());
        revenue[i] = Double.parseDouble(br.readLine());
      }
      br.close();
    } catch (IOException err) {
      System.out.println(err);
    }
  }

}
