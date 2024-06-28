package com.kuznetsov.roulette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

@SuppressWarnings("all")
public class Roulette {

    private final Color WHITE = new Color(225, 225, 225);
    private final Color LIGHT_GRAY = new Color(175, 175, 175);
    private final Color GREEN = new Color(0, 225, 0);
    private final Color RED = new Color(225, 0, 0);
    private final Color BLACK = new Color(25, 25, 25);
    private JFrame frame;
    private JPanel gamePanel;
    private JPanel betsPanel;
    private JLabel cashLabel;
    private JLabel betLabel;
    private JLabel infoLabel;
    private String betTitle;
    private String betType;
    private int cash;
    private int betCash;
    private int rolledNumber;

    Roulette(int initialCash) {
        setupFrame();
        setupGamePanel();
        setupInfoPanel();
        setCash(initialCash);

        addComponentsToTheFrame();
    }

    private void setupFrame() {
        frame = new JFrame();
        frame.setTitle("Roulette");
        frame.setSize(new Dimension(800, 600));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void addComponentsToTheFrame() {
        frame.add(gamePanel, BorderLayout.WEST);
        frame.add(betsPanel, BorderLayout.EAST);
    }

    private void setupGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setSize(new Dimension(frame.getWidth() / 10 * 7, frame.getHeight()));
        gamePanel.setPreferredSize(new Dimension(frame.getWidth() / 10 * 7, frame.getHeight()));
        gamePanel.setLayout(new BorderLayout());

        //main panel    -->
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(gamePanel.getWidth() / 10 * 6, gamePanel.getHeight()));
        mainPanel.setPreferredSize(new Dimension(gamePanel.getWidth() / 10 * 6, gamePanel.getHeight()));
        mainPanel.setLayout(new GridLayout(14, 3));

        JButton[] mainPanelButtons = new JButton[42];

        ActionListener actionListenerMainPanelButtons = e -> {
            for (JButton button : mainPanelButtons) {
                if (e.getSource() == button) {
                    // * all
                    betTitle = button.getText();
                    updateBetLabel();
                    updateInfoLabel(2);

                    // * unique
                    if (e.getSource() == mainPanelButtons[39]) betType = "1st 2 to 1";
                    if (e.getSource() == mainPanelButtons[40]) betType = "2nd 2 to 1";
                    if (e.getSource() == mainPanelButtons[41]) betType = "3rd 2 to 1";

                    // * all numbers
                    if (e.getSource() != mainPanelButtons[39] && e.getSource() != mainPanelButtons[40] && e.getSource() != mainPanelButtons[41])
                        betType = "number";
                }
            }
        };

        for (int i = 0, j = 0; i < mainPanelButtons.length; i++) {
            // * all
            mainPanelButtons[i] = new JButton();
            mainPanelButtons[i].setText(String.valueOf(i - j));
            mainPanelButtons[i].setForeground(WHITE);
            mainPanelButtons[i].setFocusable(false);
            mainPanelButtons[i].setFont(new Font("Bahnschrift", Font.PLAIN, 20));
            mainPanelButtons[i].addActionListener(actionListenerMainPanelButtons);

            mainPanel.add(mainPanelButtons[i]);

            // * group cases
            if (i >= 3 && i <= 11 || i >= 21 && i <= 29) {
                if (i % 2 == 0) mainPanelButtons[i].setBackground(BLACK);
                else mainPanelButtons[i].setBackground(RED);
            }//1-9 & 19-27
            if (i >= 12 && i <= 20 || i >= 30 && i <= 38) {
                if (i % 2 == 0) mainPanelButtons[i].setBackground(RED);
                else mainPanelButtons[i].setBackground(BLACK);

                // * unique cases in groups
                if (i == 12) mainPanelButtons[i].setBackground(BLACK);
                if (i == 30) mainPanelButtons[i].setBackground(BLACK);
            }//10-18 & 28-36

            // * unique cases
            if (i == 0 || i == 2) {
                mainPanelButtons[i].setText("");
                mainPanelButtons[i].setVisible(false);
                j++;
            }

            if (i == 1) mainPanelButtons[i].setBackground(GREEN);

            if (i >= 39 && i <= 41) {
                mainPanelButtons[i].setBackground(LIGHT_GRAY);
                mainPanelButtons[i].setText("2 to 1");
                j++;
            }
        }
        //<--   main panel

        //additional panel  -->
        JPanel additionalPanel = new JPanel();
        additionalPanel.setSize(new Dimension(gamePanel.getWidth() / 10 * 4, gamePanel.getHeight()));
        additionalPanel.setPreferredSize(new Dimension(gamePanel.getWidth() / 10 * 4, gamePanel.getHeight()));
        additionalPanel.setLayout(new BorderLayout());

        // * invisible panels are used to straighten other panels
        JPanel invisiblePanelNorth = new JPanel();
        invisiblePanelNorth.setSize(new Dimension(additionalPanel.getWidth(), additionalPanel.getHeight() / 14 - 1));
        invisiblePanelNorth.setPreferredSize(new Dimension(additionalPanel.getWidth(), additionalPanel.getHeight() / 14 - 1));
        additionalPanel.add(invisiblePanelNorth, BorderLayout.NORTH);

        // * invisible panels are used to straighten other panels
        JPanel invisiblePanelSouth = new JPanel();
        invisiblePanelSouth.setSize(new Dimension(additionalPanel.getWidth(), additionalPanel.getHeight() / 14 - 1));
        invisiblePanelSouth.setPreferredSize(new Dimension(additionalPanel.getWidth(), additionalPanel.getHeight() / 14 - 1));
        additionalPanel.add(invisiblePanelSouth, BorderLayout.SOUTH);

        JPanel westPanel = new JPanel();
        westPanel.setSize(new Dimension(additionalPanel.getWidth() / 2, additionalPanel.getHeight()));
        westPanel.setPreferredSize(new Dimension(additionalPanel.getWidth() / 2, additionalPanel.getHeight()));
        westPanel.setLayout(new GridLayout(6, 1));
        additionalPanel.add(westPanel, BorderLayout.WEST);

        JPanel eastPanel = new JPanel();
        eastPanel.setSize(new Dimension(additionalPanel.getWidth() / 2, additionalPanel.getHeight()));
        eastPanel.setPreferredSize(new Dimension(additionalPanel.getWidth() / 2, additionalPanel.getHeight()));
        eastPanel.setLayout(new GridLayout(3, 1));
        additionalPanel.add(eastPanel, BorderLayout.EAST);

        JButton[] additionalPanelButtons = new JButton[9];

        ActionListener actionListenerAdditionalPanelButtons = e -> {
            for (JButton button : additionalPanelButtons) {
                if (e.getSource() == button) {
                    betTitle = button.getText();
                    betType = "additional bet";
                    updateBetLabel();
                    updateInfoLabel(2);
                }
            }
        };

        for (int i = 0; i < additionalPanelButtons.length; i++) {
            // * all
            additionalPanelButtons[i] = new JButton();
            additionalPanelButtons[i].setForeground(WHITE);
            additionalPanelButtons[i].setBackground(LIGHT_GRAY);
            additionalPanelButtons[i].setFocusable(false);
            additionalPanelButtons[i].setFont(new Font("Bahnschrift", Font.PLAIN, 20));
            additionalPanelButtons[i].addActionListener(actionListenerAdditionalPanelButtons);

            // * west panel buttons
            if (i >= 0 && i <= 5) {
                westPanel.add(additionalPanelButtons[i]);

                if (i == 0) additionalPanelButtons[i].setText("1 to 18");
                if (i == 1) additionalPanelButtons[i].setText("EVEN");

                if (i == 2) {
                    additionalPanelButtons[i].setText("RED");
                    additionalPanelButtons[i].setBackground(RED);
                }
                if (i == 3) {
                    additionalPanelButtons[i].setText("BLACK");
                    additionalPanelButtons[i].setBackground(BLACK);
                }

                if (i == 4) additionalPanelButtons[i].setText("ODD");
                if (i == 5) additionalPanelButtons[i].setText("19 to 36");
            }

            // * east panel buttons
            if (i >= 6 && i <= 8) {
                eastPanel.add(additionalPanelButtons[i]);

                if (i == 6) additionalPanelButtons[i].setText("1st 12");
                if (i == 7) additionalPanelButtons[i].setText("2nd 12");
                if (i == 8) additionalPanelButtons[i].setText("3rd 12");
            }
        }
        //<--   additional panel

        gamePanel.add(mainPanel, BorderLayout.CENTER);
        gamePanel.add(additionalPanel, BorderLayout.WEST);
    }

    private void setupInfoPanel() {
        betsPanel = new JPanel();
        betsPanel.setSize(new Dimension(frame.getWidth() / 10 * 3 - 15, frame.getHeight()));
        betsPanel.setPreferredSize(new Dimension(frame.getWidth() / 10 * 3 - 15, frame.getHeight()));
        betsPanel.setLayout(new BorderLayout());

        //north panel   -->
        JPanel northPanel = new JPanel();
        northPanel.setSize(new Dimension(betsPanel.getWidth(), betsPanel.getHeight() / 10 * 2));
        northPanel.setPreferredSize(new Dimension(betsPanel.getWidth(), betsPanel.getHeight() / 10 * 2));
        northPanel.setLayout(new GridLayout(2, 1));

        setupCashLabel();
        setupBetLabel();

        northPanel.add(cashLabel);
        northPanel.add(betLabel);
        //<-- north panel

        //center panel  -->
        JPanel centerPanel = new JPanel();
        centerPanel.setSize(new Dimension(betsPanel.getWidth(), betsPanel.getHeight() / 10 * 6));
        centerPanel.setPreferredSize(new Dimension(betsPanel.getWidth(), betsPanel.getHeight() / 10 * 6));
        centerPanel.setLayout(new GridLayout(10, 1));

        JButton[] centerPanelButtons = new JButton[10];

        ActionListener actionListenerCenterPanelButtons = e -> {
            for (JButton button : centerPanelButtons) {
                if (e.getSource() == button) {
                    //CLEAR BET
                    if (e.getSource() == centerPanelButtons[1]) {
                        betCash = 0;
                        betTitle = null;
                        updateBetLabel();
                        updateInfoLabel(2);
                    }

                    //CONFIRM BET
                    if (e.getSource() == centerPanelButtons[8]) {
                        if (cash < betCash) updateInfoLabel(3);
                        if (betCash == 0 || betTitle == null) updateInfoLabel(4);
                        if (betCash != 0 && betCash <= cash && betTitle != null) spinTheWheel();
                    }

                    //BET VALUE
                    if (e.getSource() != centerPanelButtons[1] && e.getSource() != centerPanelButtons[8]) {
                        int betVal = Integer.parseInt(button.getText());
                        if (cash < betCash + betVal) updateInfoLabel(3);
                        if (cash >= betCash + betVal) {
                            betCash += betVal;
                            updateBetLabel();
                            updateInfoLabel(2);
                        }
                    }
                }
            }
        };

        for (int i = 0; i < centerPanelButtons.length; i++) {
            centerPanelButtons[i] = new JButton();
            centerPanelButtons[i].setForeground(WHITE);
            centerPanelButtons[i].setBackground(LIGHT_GRAY);
            centerPanelButtons[i].setFocusable(false);
            centerPanelButtons[i].setFont(new Font("Bahnschrift", Font.PLAIN, 20));
            centerPanelButtons[i].addActionListener(actionListenerCenterPanelButtons);

            if (i == 0) centerPanelButtons[i].setVisible(false);
            if (i == 1) centerPanelButtons[i].setText("CLEAR THE BET");
            if (i == 2) centerPanelButtons[i].setText("1");
            if (i == 3) centerPanelButtons[i].setText("10");
            if (i == 4) centerPanelButtons[i].setText("100");
            if (i == 5) centerPanelButtons[i].setText("1000");
            if (i == 6) centerPanelButtons[i].setText("10000");
            if (i == 7) centerPanelButtons[i].setText("100000");
            if (i == 8) centerPanelButtons[i].setText("CONFIRM THE BET");
            if (i == 9) centerPanelButtons[i].setVisible(false);

            centerPanel.add(centerPanelButtons[i]);
        }
        //<-- center panel

        //south panel   -->
        JPanel southPanel = new JPanel();
        southPanel.setSize(new Dimension(betsPanel.getWidth(), betsPanel.getHeight() / 10 * 2));
        southPanel.setPreferredSize(new Dimension(betsPanel.getWidth(), betsPanel.getHeight() / 10 * 2));
        southPanel.setLayout(new GridLayout(1, 1));

        setupResultLabel();

        southPanel.add(infoLabel);
        //<-- south panel

        betsPanel.add(northPanel, BorderLayout.NORTH);
        betsPanel.add(centerPanel, BorderLayout.CENTER);
        betsPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private void setupCashLabel() {
        cashLabel = new JLabel();
        cashLabel.setForeground(BLACK);
        cashLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        cashLabel.setHorizontalAlignment(JLabel.CENTER);
        cashLabel.setVerticalAlignment(JLabel.CENTER);
        updateCashLabel();
    }

    private void setupBetLabel() {
        betLabel = new JLabel();
        betLabel.setForeground(BLACK);
        betLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        betLabel.setHorizontalAlignment(JLabel.CENTER);
        betLabel.setVerticalAlignment(JLabel.CENTER);
        updateBetLabel();
    }

    private void setupResultLabel() {
        infoLabel = new JLabel();
        infoLabel.setForeground(BLACK);
        infoLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        updateInfoLabel(2);
    }

    private void updateCashLabel() {
        cashLabel.setText("CASH: " + cash);
    }

    private void updateBetLabel() {
        betLabel.setText("Current bet: " + betCash + "$" + " on " + betTitle);
    }

    private void updateInfoLabel(int action) {
        infoLabel.setForeground(BLACK);

        if (action == 0) {
            infoLabel.setText("You lost! Rolled number was: " + rolledNumber);
            infoLabel.setForeground(RED);
        }//lost
        if (action == 1) {
            infoLabel.setText("You won! Rolled number was: " + rolledNumber);
            infoLabel.setForeground(GREEN);
        }//won
        if (action == 2) infoLabel.setText("");
        if (action == 3) infoLabel.setText("Not enough money!");
        if (action == 4) infoLabel.setText("Invalid bet!");
    }

    private void setCash(int amount) {
        cash = amount;
        updateCashLabel();
    }

    private void addCash(int toAdd) {
        cash += toAdd;
        updateCashLabel();
    }

    private void removeCash(int toRemove) {
        cash -= toRemove;
        updateCashLabel();
    }

    private void spinTheWheel() {
        Random random = new Random();
        rolledNumber = random.nextInt(0, 36);
        checkRoundResults();
    }

    private void checkRoundResults() {
        removeCash(betCash);
        updateInfoLabel(0);

        if (betType.equals("1st 2 to 1")) {
            if (rolledNumber % 3 == 1) {
                addCash(betCash * 3);
                updateInfoLabel(1);
            }
        }

        if (betType.equals("2nd 2 to 1")) {
            if (rolledNumber % 3 == 2) {
                addCash(betCash * 3);
                updateInfoLabel(1);
            }
        }

        if (betType.equals("3rd 2 to 1")) {
            if (rolledNumber % 3 == 0) {
                addCash(betCash * 3);
                updateInfoLabel(1);
            }
        }

        if (betType.equals("number")) {
            if (rolledNumber == Integer.parseInt(betTitle)) {
                if (rolledNumber == 0) {
                    addCash(betCash * 36);
                    updateInfoLabel(1);
                }
                if (rolledNumber != 0) {
                    addCash(betCash * 18);
                    updateInfoLabel(1);
                }
            }
        }

        if (betType.equals("additional bet")) {

            if (betTitle.equals("1st 12")) {
                if (rolledNumber >= 1 && rolledNumber <= 12) {
                    addCash(betCash * 3);
                    updateInfoLabel(1);
                }
            }

            if (betTitle.equals("2nd 12")) {
                if (rolledNumber >= 13 && rolledNumber <= 24) {
                    addCash(betCash * 3);
                    updateInfoLabel(1);
                }
            }

            if (betTitle.equals("3rd 12")) {
                if (rolledNumber >= 25 && rolledNumber <= 36) {
                    addCash(betCash * 3);
                    updateInfoLabel(1);
                }
            }

            if (betTitle.equals("1 to 18")) {
                if (rolledNumber >= 1 && rolledNumber <= 18) {
                    addCash(betCash * 2);
                    updateInfoLabel(1);
                }
            }

            if (betTitle.equals("EVEN")) {
                if (rolledNumber % 2 == 0) {
                    addCash(betCash * 2);
                    updateInfoLabel(1);
                }
            }

            if (betTitle.equals("RED")) {
                if (rolledNumber >= 1 && rolledNumber <= 9 || rolledNumber >= 19 && rolledNumber <= 27) {
                    if (rolledNumber % 2 != 0) {
                        addCash(betCash * 2);
                        updateInfoLabel(1);
                    }
                }
                if (rolledNumber >= 10 && rolledNumber <= 18 || rolledNumber >= 28 && rolledNumber <= 36) {
                    if (rolledNumber % 2 == 0 && rolledNumber != 10 && rolledNumber != 28) {
                        addCash(betCash * 2);
                        updateInfoLabel(1);
                    }
                }
            }

            if (betTitle.equals("BLACK")) {
                if (rolledNumber >= 1 && rolledNumber <= 9 || rolledNumber >= 19 && rolledNumber <= 27) {
                    if (rolledNumber % 2 == 0) {
                        addCash(betCash * 2);
                        updateInfoLabel(1);
                    }
                }
                if (rolledNumber >= 10 && rolledNumber <= 18 || rolledNumber >= 28 && rolledNumber <= 36) {
                    if (rolledNumber % 2 != 0 || rolledNumber == 10 || rolledNumber == 28) {
                        addCash(betCash * 2);
                        updateInfoLabel(1);
                    }
                }
            }

            if (betTitle.equals("ODD")) {
                if (rolledNumber % 2 != 0) {
                    addCash(betCash * 2);
                    updateInfoLabel(1);
                }
            }

            if (betTitle.equals("19 to 36")) {
                if (rolledNumber >= 19 && rolledNumber <= 36) {
                    addCash(betCash * 2);
                    updateInfoLabel(1);
                }
            }
        }

    }
}