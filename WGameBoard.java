package org.cis1200.wordle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import static java.awt.Color.*;


public class WGameBoard extends JPanel {
    //private Wordle w;
    private String[][] board;
    //holds the scenario of each letter,
    private scenarios[][] boardStatus;

    private gameState currentState = gameState.PLAYING; //state of the current game. PLAYING/WON/LOST
    private String THEWORD = "hello";
    private ChooseWord word;//generates the word
    private String typed;//whatever the user typed into the text box
    private JLabel status; // current status text
    private FileWriter w; //for writing and saving the game
    private ReloadGame reload; //reloads the game
    private LinkedList<String[][]> prevGuesses = new LinkedList<>(); //stores the previous guesses to allow for a undo button
    private boolean isPreview = true;//used for preview function
    private String letterToPreview = "";
    private int numGuesses = 0;
    private final int boxWidth = 30;
    private final int boxHeight = 30;
    private final int margins = 10;
    // Game constants
    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;
    private static final String BAD_WORD_REGEX = ".*[\\W&&[^'1234567890]].*";
    private static final String BAD_WORD_REGEX2 = "[1234567890]";
    private final String savePath = "./files/StoredGame.csv";

    //TODO: do exception handling etc
    private static final String filePath = "./files/FiveLetterWord.csv";


    enum scenarios{
        CORRECTSPOT,
        WRONGSPOT,
        NOTINWORD
    }

    enum gameState{
        WON,
        LOST,
        PLAYING
    }
    public WGameBoard(JLabel statusInit){
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(BLACK));
        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        status = statusInit; // initializes the status JLabel
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                repaint(); // repaints the game board
            }

        });
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(BLACK);
        //draws boxes
        for(int i = 0 ; i < board.length; i++)//stacks/columns
        {
            for(int j = 0; j < board[0].length; j++)//horizontal row
            {
                int px = 10 + j*boxWidth + margins*j;
                int py = 10 + i * boxHeight + margins*i;

                if(numGuesses <5 ) {
                    //basically makes a cursor box to display what guess the user is on
                    g.drawRect(6, 10 + numGuesses * boxHeight + margins * numGuesses, (boxWidth + margins) * 5, boxHeight);
                }
                preview(letterToPreview, g);
                //switch cases determines color of the boxes
                if(boardStatus[i][j] != null)
                {
                    switch(boardStatus[i][j])
                    {
                        case CORRECTSPOT : g.setColor(GREEN);
                        break;
                        case WRONGSPOT : g.setColor(YELLOW);
                        break;
                        case NOTINWORD : g.setColor(GRAY);
                        break;
                    }
                }

                g.fillRect(px, py, boxWidth, boxHeight );

                //g.drawRect(px, py, boxWidth, boxHeight );
                // create a label to display text
                JLabel l = new JLabel("nothing entered");
                //g.drawString("H", px + margins, py + margins*2);
                if(!(board[i][j] == null))
                {
                    g.setColor(BLACK);
                    g.drawString(board[i][j], px + margins, py + margins*2);
                    //status.setText(board[i][j]);

                }

            }

        }
        //JLabel typedWords = new JLabel(w.getTyped());
        g.drawString("This is getting typed: " + typed, 0, 250);


    }
    public void setIsPreview(boolean isPreview)
    {
        isPreview = isPreview;
    }
    public void setPreviewWord(String wordToPreview)
    {
        //System.out.println("setPreviewWord" + wordToPreview);
        letterToPreview = wordToPreview;
    }

    public void preview(String previewLetters, Graphics g)
    {
        //System.out.println("!!!!!!");
        //while(previewLetters.length()<=5)
        //{
            for(int j = 0; j < previewLetters.length(); j++)
            {
                if(previewLetters.length() <6 && numGuesses < 5) {

                    //System.out.println("Here's the preview" + previewLetters.substring(j, j + 1));
                    int px = 10 + j * boxWidth + margins * j;
                    int py = 10 + numGuesses * boxHeight + margins * numGuesses;
                    g.setColor(BLUE);
                    g.drawString(previewLetters.substring(j, j + 1), px + margins, py + margins * 2);

                }

            }
        //}
        repaint();

    }
    //----------getters and setters---------
    public String getTheWord()
    {
        return THEWORD;
    }
    public String[][] getBoard()
    {
        String [][] temp = new String[board.length][board[0].length];
        for(int i = 0 ; i < board.length; i++)//stacks/columns
        {
            String tempGuessWord = "";
            for(int j = 0; j < board[0].length; j++)//horizontal row
            {
                temp[i][j] = board[i][j];
            }
        }

        return temp;
    }

    public gameState getCurrentState()
    {
        return currentState;
    }


    public void setWord()
    {
        System.out.println("The word is " + word.getChoosenWord());
        THEWORD = word.getChoosenWord();
    }

    //TODO: implement this
    /**
     * Do not modify this method.
     * <p>
     * Cleans a word by removing leading and trailing whitespace and converting
     * it to lower case. If the word matches the BAD_WORD_REGEX or BAD_WORD_REGEX2 or is the empty
     * String, returns null instead.
     *
     * @param input - a (non-null) String to clean
     * @return - a trimmed, lowercase version of the word if it contains no
     *         illegal characters and is not empty, and null otherwise.
     */
    public boolean isValidCharacters(String input){
        String input2 = input.trim().toLowerCase();
        if (input2.matches(BAD_WORD_REGEX) || input2.matches(BAD_WORD_REGEX2)
                || input2.length() !=5) {
            return false;
        }
        return true;
    }

    //checks and updates the input
    public void checkAndUpdateInput(String input){
        requestFocusInWindow();
        if(input.length() == 5 && isValidCharacters(input))
        {
            typed = input;
            status.setText("Great guess!");
            updateBoard(input);
            //numGuesses++;
        }
        else
        {
            status.setText("Please enter a valid guess");
        }
        repaint();
    }

    //checks the whether the character is in the right spot, wrong spot, or doesn't exist
    public scenarios checkCharactarScenarios(String c, int position)
    {
        if(c.equals(THEWORD.substring(position,position+1)))
        {
            return scenarios.CORRECTSPOT;
        }
        else if(THEWORD.contains(c))
        {
            return scenarios.WRONGSPOT;
        }
        else
        {
            return scenarios.NOTINWORD;
        }

    }


    //updates and stores the valid guesses
    public void updateBoard(String input)
    {
        String[][] tempBoard = new String [5][5];
        //updates the tempboard with the current board so it can be added into the linked list later
        for(int i = 0; i < input.length(); i++)
        {
            for(int j = 0; j < input.length(); j++)
            {
                tempBoard[i][j] = board[i][j]; //stores a copy for undo
            }
        }

        //store the previous guesses
        prevGuesses.add(tempBoard);

        System.out.println("Have we won yet? " +input.equals(THEWORD) );
        if(input.equals(THEWORD))
        {
            currentState = gameState.WON;
            status.setText("Congrats! You guessed the word!");
            System.out.println("!!!!!!!");
            //RunWordle.status_panel.setBackground(GREEN);
            status.setBackground(GREEN);
            repaint();
        }
        if(numGuesses >= 4)
        {
            currentState = gameState.LOST;
            status.setText("Better luck next time!");
            status.setBackground(RED);
        }

        for(int i = 0; i < input.length(); i++)
        {
            board[numGuesses][i] = input.substring(i,i+1);
            if(boardStatus[numGuesses][i]!= null)
            {
                boardStatus[numGuesses][i] = checkCharactarScenarios( input.substring(i,i+1), i );
            }
            //System.out.println("enum values of " + input.substring(i,i+1) + " " + "at" + i + " " + boardStatus[numGuesses][i].name());
        }
        //update the guesses
        numGuesses++;
        System.out.println("Numguesses:" + numGuesses);
        repaint();
        //Toubleshooting
        /*for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[0].length; j++)
            {
                System.out.println(i +" " + board[i][j] +" ");
            }
            System.out.println("\n");
        } */
    }
    //updates the boared with an array
    public void updateBoard(String [][] input)
    {

        if(numGuesses >= 5)
        {
            currentState = gameState.LOST;
            status.setText("Better luck next time!");
            status.setBackground(RED);
        }
        for(int i = 0; i < 5; i++)//TODO: think about 5
        {
            for(int j = 0; j < input[i].length; j++) {
                board[i][j] = input[i][j];
                if(input[i][j] != null)
                {
                    boardStatus[i][j] = checkCharactarScenarios(input[i][j], j);
                }
                else//if it is null, set it to the default NOTINWORD scenario so it would display grey
                {
                    boardStatus[i][j] = scenarios.NOTINWORD;
                }
                //System.out.println("enum values of " + input.substring(i,i+1) + " " + "at" + i + " " + boardStatus[numGuesses][i].name());
            }
        }

        //store the previous guesses
        repaint();
    }





    public void saveGame()
    {
        try {
            w = new FileWriter(savePath);
            w.write("THEWORD:\n");
            w.write(THEWORD + "\n");

            //store the board
            w.write("THEBOARD:\n");
            for(int i = 0 ; i < board.length; i++)//stacks/columns
            {
                for(int j = 0; j < board[0].length; j++)//horizontal row
                {
                    if(board[i][j]==null)//make a special demarkation for null entries
                    {
                        w.write("-");//special character
                    }
                    else
                    {
                        w.write(board[i][j]);
                    }

                }
                w.write("\n");

            }

            w.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void reloadGame()
    {
        reload = new ReloadGame();
        THEWORD = reload.getTHEWORD();
        board = reload.getBoard();
        //numGuesses = reload.getNumGuesses();
        System.out.println("new THEWORD is: " + THEWORD);
        //prints out stuff
        for(int i = 0 ; i < board.length; i++)//stacks/columns
        {
            String tempGuessWord = "";
            for(int j = 0; j < board[0].length; j++)//horizontal row
            {
                if(tempGuessWord == null)
                {
                    tempGuessWord += "";
                }
                else
                {
                    tempGuessWord += board[i][j];
                }

            }
            System.out.println("Updating the status for" + tempGuessWord);
            checkAndUpdateInput(tempGuessWord);
        }
        repaint();
    }

    public void undo()
    {
        if(prevGuesses.size() >0) //prevent exceptions
        {
            //System.out.println("Num prev moves left: " + prevGuesses.size());

            String[][] tempLastGuess = prevGuesses.removeLast();
            board = tempLastGuess;
            updateBoard(tempLastGuess);
            //prevents negative number of guesses
            if(numGuesses > 0)
            {
                numGuesses -= 1;
            }

            System.out.println("Num Guesses" + numGuesses);
            System.out.println("Num prev moves left: " + prevGuesses.size());
            status.setText("NumGuess: " + numGuesses);

        }
        System.out.println("-------------");
        printoutAllPrevGuesses();
        repaint();
    }
    //troubleshooting
    public void printoutAllPrevGuesses()
    {
        for(String [][] prevG : prevGuesses) {
            //prints out the new board
            for (int i = 0; i < 5; i++)//stacks/columns
            {
                String tempt = "";
                for (int j = 0; j < 5; j++)//horizontal row
                {
                    tempt += prevG[i][j];
                }
                System.out.println(tempt);
            }
            System.out.println("\n");
        }
    }

    //----------board reset and setup-----------
    public void initBoardStatus()
    {
        for(int i = 0; i < boardStatus.length; i++)
        {
            for(int j =0; j < boardStatus[0].length; j++)
            {
                boardStatus[i][j] = scenarios.NOTINWORD;
            }
        }
    }
    //resets the board
    public void reset() {
        board = new String[5][5];
        boardStatus = new scenarios[5][5];
        initBoardStatus();
        currentState = gameState.PLAYING;
        numGuesses = 0;
        word = new ChooseWord();
        THEWORD = word.getChoosenWord();
        System.out.println("The word is " + word.getChoosenWord());
        prevGuesses.clear();//clears everything in a linked list
        prevGuesses.add(new String[5][5]); //make the first one null
        status.setText("Make a guess!");
        // Makes sure this component has keyboard/mouse focus
        repaint();
        requestFocusInWindow();
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}
