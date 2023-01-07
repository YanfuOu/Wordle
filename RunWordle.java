package org.cis1200.wordle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RunWordle implements Runnable {
    public void run(){
        //top level frame
        final JFrame frame = new JFrame("Wordle");
        frame.setLocation(300, 300);

        //-------status panel(South Panel)------
        final JPanel status_panel = new JPanel();

        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        //---------Game board panel-------------
        JPanel middlePanel = new JPanel();

        // Game board
        final WGameBoard board = new WGameBoard(status);
        middlePanel.add(board, BorderLayout.NORTH);

        //uses game board state to change the color of the status bar
        JFrame instructions = new JFrame();
        JOptionPane.showMessageDialog(instructions,"Welcome to Wordle!\n" +
                "Here's a few Instructions: \n" +
                "Try your best to guess the 5 letter word in 5 attempts or less.\n" +
                "If the letter is in the correct spot...................... the box containing the letter will be green.\n" +
                "If the letter is in in the word but the wrong spot........ the box containing the letter will be yellow.\n" +
                "If the letter is not in the word.......................... the box containing the letter will be grey.\n" +
                "Best of luck!");

        //----user input----
        JPanel userInputPanel = new JPanel();
        userInputPanel.setBackground(Color.BLACK);
        userInputPanel.setLayout(new GridLayout(1,2));
        userInputPanel.setBounds(300, 150, 60, 30);
        //text field
        JTextField txtField = new JTextField();
        userInputPanel.add( txtField);

        JButton enterBut = new JButton("Enter");
        enterBut.setOpaque(true);
        enterBut.setBackground(Color.BLUE);
        //adds action listener
        enterBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.checkAndUpdateInput(txtField.getText().toLowerCase());
                displaysGameWinOrLossOnStatus();
                switch (board.getCurrentState())
                {
                    case WON : status_panel.setBackground(Color.GREEN); System.out.println("Have we won yett???" + board.getCurrentState().name());
                    break;
                    case LOST : status_panel.setBackground(Color.RED);
                    break;
                    case PLAYING : status_panel.setBackground(Color.GRAY);
                    //default -> status_panel.setBackground(Color.GRAY);
                    //throw new IllegalStateException("Unexpected value: " + board.getCurrentState());
                }


            }
            public void displaysGameWinOrLossOnStatus()//displays whether the game has been won or loss
            // through the background color of the status panel
            {
                switch (board.getCurrentState())
                {
                    case WON : status_panel.setBackground(Color.GREEN); System.out.println("Have we won yett???" + board.getCurrentState().name());
                        JFrame f = new JFrame();
                        //JOptionPane.showMessageDialog(f,"CONGRATSSSS! YOU DID IT!!!");
                        int a = JOptionPane.showConfirmDialog(f,"CONGRATSSSS! YOU DID IT!!! Wanna play again?");
                        if(a==JOptionPane.YES_OPTION){
                            board.reset();
                        } else if (a == JOptionPane.NO_OPTION) {
                            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.dispose();
                        }

                        break;
                    case LOST : status_panel.setBackground(Color.RED);
                        JFrame g = new JFrame();
                        int b = JOptionPane.showConfirmDialog(g, "The word was " + board.getTheWord() + "\nGreat try! Wanna play again?");
                        if(b==JOptionPane.YES_OPTION){
                            board.reset();
                        } else if (b == JOptionPane.NO_OPTION) {
                            g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.dispose();
                        }
                        break;
                    case PLAYING : status_panel.setBackground(Color.GRAY);
                        //default -> status_panel.setBackground(Color.GRAY);
                        //throw new IllegalStateException("Unexpected value: " + board.getCurrentState());
                }
            }
        });
        //TODO: how to add enter key to replace enter?
        //enterBut.addKeyListener(e -> board.checkAndUpdateInput(txtField.getText().toLowerCase()); );
        txtField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                enterKeyforEnterBut(e);
                /*board.setIsPreview(true);
                if(txtField.getText().length() < 6)
                {
                    board.setPreviewWord(txtField.getText().toLowerCase());
                } */


                //board.preview(txtField.getText().toLowerCase(), board.getGraphics());
                displaysGameWinOrLossOnStatus();

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                enterKeyforEnterBut(e);
                /*board.setIsPreview(true);
                if(txtField.getText().length() < 6)
                {
                    board.setPreviewWord(txtField.getText().toLowerCase());
                    //System.out.println("previewing: " + txtField.getText().toLowerCase());
                } */
                displaysGameWinOrLossOnStatus();



                //board.preview(txtField.getText().toLowerCase(), board.getGraphics());
            }

            public void enterKeyforEnterBut(KeyEvent e){
                int num = e.getKeyChar();
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                    status.setText("Enter Pressed");
                    board.checkAndUpdateInput(txtField.getText().toLowerCase());
                    e.consume();
                }
                displaysGameWinOrLossOnStatus();
            }
            public void displaysGameWinOrLossOnStatus()//displays whether the game has been won or loss
            // through the background color of the status panel
            {
                switch (board.getCurrentState())
                {
                    case WON : status_panel.setBackground(Color.GREEN); System.out.println("Have we won yett???" + board.getCurrentState().name());
                        JFrame f = new JFrame();
                        //JOptionPane.showMessageDialog(f,"CONGRATSSSS! YOU DID IT!!!");
                        int a = JOptionPane.showConfirmDialog(f,"CONGRATSSSS! YOU DID IT!!! Wanna play again?");
                        if(a==JOptionPane.YES_OPTION){
                            board.reset();
                        } else if (a == JOptionPane.NO_OPTION) {
                            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.dispose();
                        }

                        break;
                    case LOST : status_panel.setBackground(Color.RED);
                        JFrame g = new JFrame();
                        int b = JOptionPane.showConfirmDialog(g, "The word was " + board.getTheWord() + "\nGreat try! Wanna play again?");
                        if(b==JOptionPane.YES_OPTION){
                            board.reset();
                        } else if (b == JOptionPane.NO_OPTION) {
                            g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.dispose();
                        }
                        break;
                    case PLAYING : status_panel.setBackground(Color.GRAY);
                        //default -> status_panel.setBackground(Color.GRAY);
                        //throw new IllegalStateException("Unexpected value: " + board.getCurrentState());
                }
            }

        });
        userInputPanel.add(enterBut);

        middlePanel.add(userInputPanel, BorderLayout.SOUTH);

        frame.add(middlePanel, BorderLayout.CENTER);

        //---------North Panel---------
        //reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final  JButton reset = new JButton("Reset");
        //reset.addActionListener(e -> board.reset());
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.reset();
                status_panel.setBackground(Color.GRAY);
            }
        });
        control_panel.add(reset);

        //save button
        final JButton save = new JButton("Save game");
        save.addActionListener(e -> board.saveGame());
        control_panel.add(save);

        //reload button
        final JButton reloadBut = new JButton("Reload game");
        reloadBut.addActionListener(e -> board.reloadGame());
        control_panel.add(reloadBut);

        //undo button
        final JButton undoBut = new JButton("Undo");
        undoBut.addActionListener(e -> board.undo());
        control_panel.add(undoBut);


        //put the frames on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        board.reset();

    }
}
