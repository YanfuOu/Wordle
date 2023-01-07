package org.cis1200.wordle;

import java.awt.event.KeyEvent;

public class Wordle{

    //invariant: each row has the same number of elements
    private int[][] board;

    private String typed;
    private String theWord;

    public Wordle(){
        theWord = "Hello";
    }



    public int getWidth(){
        return board[0].length;
    }
    public int getHeight(){
        return board.length;
    }
    public String getTyped()
    {
        return typed;
    }
    /** Handle the key typed event from the text field. */
    public void userInput(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println(c + " ");
        typed += c;
        //displayInfo(e, "KEY TYPED: ");
    }

    public void userInput(String c) {
        typed += c;
        System.out.println(typed + " ");
        //displayInfo(e, "KEY TYPED: ");
    }

    public void reset() {
        board = new int[5][5];
    }

}
