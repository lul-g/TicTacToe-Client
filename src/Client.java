//LULSEGED ADMASU 2022

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static String hostName = "localhost";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket toSeverSocket;
    private static char[][] board;
    private static int row,col;

    //https://www.codegrepper.com/code-examples/java/how+to+change+the+color+to+the+output+in+java
    // Define color constants
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";



    public static void main(String[] args) {
        board = new char[4][4];
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                board[x][y] = ' ';
            }
        }

        row = -1;
        col = -1;

        try {
            toSeverSocket = new Socket(hostName, 8787);
            inputStream = new  DataInputStream(toSeverSocket.getInputStream());
            outputStream = new DataOutputStream(toSeverSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(inputStream));
            out = new PrintWriter(outputStream, true);

            playgame(in, out);
        } catch (Exception e) {
            System.err.println("CLIENT: " + e);
        }
    }
    public static void  playgame(BufferedReader in, PrintWriter out) throws IOException {
        Scanner inp = new Scanner(System.in);
        String response;
        boolean userTurn = false;
        boolean gameover = false;

        do {
            if (userTurn) {
                do {
                    System.out.print("\nEnter Row: ");
                    row = inp.nextInt();
                    System.out.print("Enter Column: ");
                    col = inp.nextInt();
                } while (row < 0 || row > 3 || col > 3 || col < 0 || board[row][col] != ' ');
                board[row][col] = 'O';

                out.println("MOVE " + row + " " + col);
            }
            else {
                response = in.readLine();
                System.out.println("\n" + TEXT_BLUE + "MSG RECEIVED BY CLIENT: " + response + TEXT_RESET + "\n" );
                if (!response.equals("CLIENT")) {
                    String[] arg = response.split("\\s+");
                    if (arg.length > 3) {
                        row = Integer.parseInt(arg[1]);
                        col = Integer.parseInt(arg[2]);
                        if (!arg[3].equals("WIN") && row != -1) {
                            board[row][col] = 'X';
                        }
                        switch (arg[3]) {
                            case "WIN":
                                System.out.println("\n\n" + TEXT_GREEN + "Congratulations!!! You WON the game!" + TEXT_RESET);
                                break;
                            case "TIE":
                                System.out.println("\n" + TEXT_PURPLE + "The game was a TIE!" + TEXT_RESET);
                                break;
                            case "LOSS":
                                System.out.println("\n" + TEXT_RED + "SORRY! You LOST the game!" + TEXT_RESET);
                                break;
                        }
                        gameover = true;
                        System.out.println("Here is the final game board");
                    }
                    else {
                        row = Integer.parseInt(arg[1]);
                        col = Integer.parseInt(arg[2]);
                        board[row][col] = 'X';
                    }
                }
                else {
                    System.out.println("\nYOU MOVE FIRST");
                }
            }
            printboard();
            userTurn = !userTurn;
        }while(!gameover);

    }

    public static void printboard() {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if(i != 3) {
                    if(board[j][i] == ' ')
                        System.out.print(board[j][i] + "\t|");
                    else {
                        if (i == 0)
                            System.out.print("  " + board[j][i] + "\t|");
                        else
                            System.out.print(" " + board[j][i] + " |");
                    }
                }
                else
                    System.out.print(" " + board[j][i]);
            }
            if(j < 3) System.out.print("\n ---------------\n");
        }
        System.out.print("\n");
    }

}


