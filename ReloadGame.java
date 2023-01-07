package org.cis1200.wordle;

import java.util.NoSuchElementException;

public class ReloadGame {

    FileLineIterator r;
    private static final String filePath = "./files/StoredGame.csv";
    private String THEWORD;
    private String[][] board = new String[5][5];
    private int numGuesses = 0;
    public ReloadGame(){

        FileLineIterator r = new FileLineIterator(filePath);
        boolean runWordSelector = true;
        while(runWordSelector && r.hasNext())
        {
            try {
                //retrieves the word
                if(r.next().equals("THEWORD:"))
                {
                    if(r.hasNext())
                    {
                        THEWORD = r.next();
                        System.out.println("FILE retrived word is" + THEWORD);
                    }

                }
                if(r.next().equals("THEBOARD:"))
                {
                    while(r.hasNext())
                    {
                        String tempGuessedWord = r.next();
                        for(int i = 0; i < tempGuessedWord.length(); i++)
                        {
                            //individually put the characters into the board
                            String tempChar = tempGuessedWord.substring(i,i+1);
                            if(tempChar.equals("-"))
                            {
                                board[numGuesses][i] = null;
                            }
                            else
                            {
                                board[numGuesses][i] = tempGuessedWord.substring(i,i+1);
                            }
                            System.out.println("Putting this back into the board:"  + tempGuessedWord.substring(i,i+1));
                            //boardStatus[numGuesses][i] = checkCharactarScenarios( input.substring(i,i+1), i );
                            //System.out.println("enum values of " + input.substring(i,i+1) + " " + "at" + i + " " + boardStatus[numGuesses][i].name());
                        }
                        numGuesses++;
                        System.out.println("Numguesses " + numGuesses);
                    }
                }



            }catch (NoSuchElementException e)
            {
                throw new NoSuchElementException();
            }


        }


    }

    public String getTHEWORD()
    {
        System.out.println("The roloaded word is " + THEWORD);
        return THEWORD;
    }

    public String[][] getBoard()
    {
        return board;
    }
    public int getNumGuesses(){
        return numGuesses;
    }





}
