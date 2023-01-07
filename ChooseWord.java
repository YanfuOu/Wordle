package org.cis1200.wordle;

import java.util.NoSuchElementException;

public class ChooseWord {

    FileLineIterator r;
    private static final String filePath = "./files/FiveLetterWord.csv";
    private String choosenWord;

    public ChooseWord(){

        FileLineIterator r = new FileLineIterator(filePath);
        boolean runWordSelector = true;
        while(runWordSelector && r.hasNext())
        {
            double randomNum = Math.random();
            try {
                r.next();
                if (randomNum < 0.05) //randomly selects the next word
                {
                    choosenWord = r.next();
                    System.out.println("The word is" + r.next());
                    runWordSelector = false;
                }
            }catch (NoSuchElementException e)
            {
                throw new NoSuchElementException();
            }


        }
        //just in the rare case that no word is selected for choosenWord
        if(choosenWord == null)
        {
            choosenWord = "hello";
        }

    }

    public String getChoosenWord()
    {
        System.out.println("The choosen word is " + choosenWord);
        return choosenWord;
    }





}
