package GUIElements;/*
 * CheckerGUI.java
 * 
 * The actual board.
 *
 * Created on January 25, 2002, 2:34 PM
 * 
 * Version
 * $Id: CheckerGUI.java,v 1.1 2002/10/22 21:12:52 se362 Exp $
 * 
 * Revisions
 * $Log: CheckerGUI.java,v $
 * Revision 1.1  2002/10/22 21:12:52  se362
 * Initial creation of case study
 *
 */

import Game.Board;
import Game.Facade;
import Game.Rules;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.net.*;

/**
 * @author
 */

public class CheckerGUI extends JFrame implements ActionListener {

    //the facade for the game
    private static Facade theFacade; //the facade
    private Rules rules;
    private Vector possibleSquares = new Vector();  // a vector of the squares
    private int timeRemaining;  //the time remaining

    private JLabel PlayerOneLabel;
    private JLabel playerTwoLabel;
    private JLabel timeRemainingLabel;
    private JLabel secondsLeftLabel;
    private JButton ResignButton;
    private JButton DrawButton;
    private JLabel warningLabel, whosTurnLabel;

    //the names and time left
    private String playerOnesName = "", playerTwosName = "", timeLeft = "";

    /**
     * Constructor, creates the GUI and all its components
     *
     * @param facade the facade for the GUI to interact with
     * @param name1  the first players name
     * @param name2  the second players name
     */

    public CheckerGUI(Facade facade, String name1, String name2) {

        super("Checkers");

        playerOnesName = makeShortName(name1);
        playerTwosName = makeShortName(name2);
        theFacade = facade;
        rules = new Rules(theFacade.stateOfBoard(), theFacade.theDriver);
        register();

        initComponents();
        pack();
        update();
    }


    /*
     * This method to make shorter name if it is longer than 7
     */
    private String makeShortName(String name) {
        if (name.length() > 7) {
            return name.substring(0, 7);
        }
        return name;
    }
    
    /*
     * This method handles setting up the timer
     */

    private void register() {
        try {
            theFacade.addActionListener(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form. It initializes the components
     * adds the buttons to the Vecotr of squares and adds
     * an action listener to the components
     */
    private void initComponents() {

        this.setResizable(false);

        //sets the layout and adds listener for closing window
        int y = 0;
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;
        Color evenColor = Color.white;
        Color oddColor = new Color(204, 204, 153);

        for (int i = 0; i < 64; i++) {
            JButton jButton = new JButton();
            possibleSquares.add(jButton);
            jButton.addActionListener(this);

            jButton.setPreferredSize(new Dimension(80, 80));
            jButton.setActionCommand(Integer.toString(i));
            // swap color
            if (i % 8 == 0) {
                Color temp = evenColor;
                evenColor = oddColor;
                oddColor = temp;
            }
            if (i % 2 == 0) {
                jButton.setBackground(evenColor);
            } else {
                jButton.setBackground(oddColor);
            }

            gridBagConstraints1 = new java.awt.GridBagConstraints();
            gridBagConstraints1.gridx = i % 8;
            if (i % 8 == 0) {
                y += 1;
            }
            gridBagConstraints1.gridy = y;
            getContentPane().add(jButton, gridBagConstraints1);
        }

        PlayerOneLabel = new JLabel();
        playerTwoLabel = new JLabel();
        whosTurnLabel = new JLabel();

        warningLabel = new JLabel();
        timeRemainingLabel = new JLabel();
        secondsLeftLabel = new JLabel();

        ResignButton = new JButton();
        ResignButton.addActionListener(this);

        DrawButton = new JButton();
        DrawButton.addActionListener(this);

        //add window listener
        addWindowListener(new WindowAdapter() {
                              public void windowClosing(WindowEvent evt) {
                                  exitForm(evt);
                              }
                          }
        );

        PlayerOneLabel.setText("Players 1:     " + playerOnesName);
        PlayerOneLabel.setForeground(Color.black);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 4;
        getContentPane().add(PlayerOneLabel, gridBagConstraints1);

        playerTwoLabel.setText("Players 2:     " + playerTwosName);
        playerTwoLabel.setForeground(Color.black);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = 4;
        getContentPane().add(playerTwoLabel, gridBagConstraints1);

        whosTurnLabel.setText("");
        whosTurnLabel.setForeground(new Color(0, 100, 0));

        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 1;
        getContentPane().add(whosTurnLabel, gridBagConstraints1);

        warningLabel.setText("");
        warningLabel.setForeground(Color.red);

        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 2;
        getContentPane().add(warningLabel, gridBagConstraints1);

        timeRemainingLabel.setText("Time Remaining:");
        timeRemainingLabel.setForeground(Color.black);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 3;
        getContentPane().add(timeRemainingLabel, gridBagConstraints1);

        secondsLeftLabel.setText(timeLeft + " sec.");
        secondsLeftLabel.setForeground(Color.black);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 4;
        getContentPane().add(secondsLeftLabel, gridBagConstraints1);

        ResignButton.setActionCommand("resign");
        ResignButton.setText("Resign");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 7;
        getContentPane().add(ResignButton, gridBagConstraints1);

        DrawButton.setActionCommand("draw");
        DrawButton.setText("Draw");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 6;
        getContentPane().add(DrawButton, gridBagConstraints1);

    }

    /**
     * Exit the Application
     *
     * @param evt
     */
    private void exitForm(java.awt.event.WindowEvent evt) {
        theFacade.pressQuit();
    }

    /**
     * Takes care of input from users, handles any actions performed
     *
     * @param e the event that has occured
     */

    public void actionPerformed(ActionEvent e) {
        try {
            //if a square gets clicked
            if (e.getActionCommand().equals("1") ||
                    e.getActionCommand().equals("3") ||
                    e.getActionCommand().equals("5") ||
                    e.getActionCommand().equals("7") ||
                    e.getActionCommand().equals("8") ||
                    e.getActionCommand().equals("10") ||
                    e.getActionCommand().equals("12") ||
                    e.getActionCommand().equals("14") ||
                    e.getActionCommand().equals("17") ||
                    e.getActionCommand().equals("19") ||
                    e.getActionCommand().equals("21") ||
                    e.getActionCommand().equals("23") ||
                    e.getActionCommand().equals("24") ||
                    e.getActionCommand().equals("26") ||
                    e.getActionCommand().equals("28") ||
                    e.getActionCommand().equals("30") ||
                    e.getActionCommand().equals("33") ||
                    e.getActionCommand().equals("35") ||
                    e.getActionCommand().equals("37") ||
                    e.getActionCommand().equals("39") ||
                    e.getActionCommand().equals("40") ||
                    e.getActionCommand().equals("42") ||
                    e.getActionCommand().equals("44") ||
                    e.getActionCommand().equals("46") ||
                    e.getActionCommand().equals("49") ||
                    e.getActionCommand().equals("51") ||
                    e.getActionCommand().equals("53") ||
                    e.getActionCommand().equals("55") ||
                    e.getActionCommand().equals("56") ||
                    e.getActionCommand().equals("58") ||
                    e.getActionCommand().equals("60") ||
                    e.getActionCommand().equals("62")) {

                //call selectSpace with the button pressed
                theFacade.selectSpace(
                        Integer.parseInt(e.getActionCommand()));

                //if draw is pressed
            } else if (e.getActionCommand().equals("draw")) {
                //does sequence of events for a draw
                theFacade.pressDraw();

                //if resign is pressed
            } else if (e.getActionCommand().equals("resign")) {
                //does sequence of events for a resign
                theFacade.pressQuit();

                //if the source came from the facade
            } else if (e.getSource().equals(theFacade)) {

                //if its a player switch event
                if ((e.getActionCommand()).equals(theFacade.playerSwitch)) {
                    //set a new time
                    timeRemaining = theFacade.getTimer();
                    //if it is an update event
                } else if ((e.getActionCommand()).equals(theFacade.update)) {
                    //update the GUI
                    update();
                } else {
                    throw new Exception("unknown message from facade");
                }
            }
        } catch (NumberFormatException excep) {
            System.err.println(
                    "GUI exception: Error converting a string to a number");
        } catch (NullPointerException exception) {
            System.err.println("GUI exception: Null pointerException "
                    + exception.getMessage());
            exception.printStackTrace();
        } catch (Exception except) {
            System.err.println("GUI exception: other: "
                    + except.getMessage());
            except.printStackTrace();
        }
    }


    /**
     * Updates the GUI reading the pieces in the board
     * Puts pieces in correct spaces, updates whos turn it is
     */

    private void update() {

        if (rules.checkEndConditions()) {
            theFacade.showEndGame(" ");
        }

        //the board to read information from
        Board board = theFacade.stateOfBoard();

        //a temp button to work with
        JButton temp;

        //go through the board
        for (int i = 1; i < board.sizeOf(); i++) {

            // if there is a piece there
            if (board.occupied(i)) {
                updatePiece(board, i);
            } else {
                //show no picture
                temp = (JButton) possibleSquares.get(i);
                temp.setIcon(null);
            }
        }

        //this code updates whos turn it is
        if (theFacade.whosTurn() == 2) {
            playerTwoLabel.setForeground(Color.red);
            PlayerOneLabel.setForeground(Color.black);
            whosTurnLabel.setText(playerTwosName + "'s turn ");
        } else if (theFacade.whosTurn() == 1) {
            PlayerOneLabel.setForeground(Color.red);
            playerTwoLabel.setForeground(Color.black);
            whosTurnLabel.setText(playerOnesName + "'s turn");
        }
    }

    private void updatePiece(Board board, int index) {
        JButton temp;

        //check to see if color is blue
        if (board.colorAt(index) == Color.blue) {

            //if there is a  single piece there
            if ((board.getPieceAt(index)).getType() == board.SINGLE) {

                //show a blue single piece in that spot board
                temp = (JButton) possibleSquares.get(index);
                updateImageIcon(temp, "file:BlueSingle.gif");

                //if there is a king piece there
            } else if ((board.getPieceAt(index)).getType() == board.KING) {

                //show a blue king piece in that spot board
                temp = (JButton) possibleSquares.get(index);
                updateImageIcon(temp, "file:BlueKing.gif");
            }

            //check to see if the color is white
        } else if (board.colorAt(index) == Color.white) {

            //if there is a single piece there
            if ((board.getPieceAt(index)).getType() == board.SINGLE) {

                //show a white single piece in that spot board
                temp = (JButton) possibleSquares.get(index);
                updateImageIcon(temp, "file:WhiteSingle.gif");

                //if there is a king piece there
            } else if ((board.getPieceAt(index)).getType() == board.KING) {

                //show a white king piece in that spot board
                temp = (JButton) possibleSquares.get(index);
                updateImageIcon(temp, "file:WhiteKing.gif");
            }
            //if there isnt a piece there
        }
    }

    private void updateImageIcon(JButton temp, String fileName) {
        //get the picture from the web
        try {
            temp.setIcon(
                    new ImageIcon(new URL(fileName)));
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
    }

}//checkerGUI.java
