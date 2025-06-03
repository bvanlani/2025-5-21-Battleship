package dev.bvanlani.battleship;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.function.*;

public class Display extends JFrame{

    // player variable
    private ArrayList<Battleship> fleet = new ArrayList<Battleship>();
    private String name;
    private JButton start;
    private JPanel controlPanel;
    private JButton rotateButton;
    private JLabel shipLengthLabel;

    private final Player player;

    public Display(String name, Player player){
        this.player = player;

        this.name = name;
        setTitle(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel gridsPanel = new JPanel();
        gridsPanel.setLayout(new BoxLayout(gridsPanel, BoxLayout.Y_AXIS));
        gridsPanel.setBackground(ColorPalette.getBackgroundColor());

        gridsPanel.add(player.getTargetGrid());
        gridsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gridsPanel.add(player.getPlayerGrid());
        this.add(gridsPanel, BorderLayout.CENTER);

        rotateButton = new JButton("Rotate (" + player.getDirection() + ")");
        rotateButton.addActionListener(e -> {
            player.setDirection(player.getDirection().next());
            rotateButton.setText("Rotate (" + player.getDirection() + ")");
        });

        shipLengthLabel = new JLabel("-");
        shipLengthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(shipLengthLabel, BorderLayout.CENTER);
        controlPanel.add(rotateButton, BorderLayout.WEST);

        this.add(controlPanel, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    public JButton getStart(){
        return start;
    }

    public void setShipLengthLabel(String text){
        shipLengthLabel.setText(text);
    }

    public void readyState(){
        controlPanel.remove(rotateButton);
        controlPanel.remove(shipLengthLabel);

        start = new JButton("Ready Up");

        start.addActionListener(e -> {
            player.gameRun();
            start.setEnabled(false);
            start.setText("Ready");
        });

        controlPanel.add(start, BorderLayout.CENTER);
        controlPanel.revalidate();
        controlPanel.repaint();

    }

}