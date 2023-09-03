import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Author Leon Schobert

public class TimeTracker {
    private static Map<String, Map<String, String>> gameTrackTimes = new HashMap<>();
    private static ArrayList<String> gamelist = new ArrayList<>(); //Array List as game list 
    private static String gameString[] = {"F1", "AMS2", "DTM_RD3", "grandprix4"}; 
    private static String currentGame = ""; // current game
    
    static Icon iconString[]= { 
        loadImageAsIcon("F1_icon.jpg"),
        loadImageAsIcon("AMS2_icon.jpeg"),
        loadImageAsIcon("DTM_icon.png"),
        loadImageAsIcon("GP4_icon.jpg")
    };


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loadAllTrackTimes(); // load all track times for every game
            chooseGame(); 
        });
    }
    
    private static void loadAllTrackTimes() { 
        for (String game : gameString) {
            Map<String, String> trackTimes = new HashMap<>();
            loadTrackTimesFromFile(game + ".txt", trackTimes);
            gameTrackTimes.put(game, trackTimes);
        }
    }

    private static void chooseGame() {
        JFrame frame = new JFrame("Time Tracker");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel startPanel = new JPanel(new GridLayout(0, 2));

        JButton addGamebtn = new JButton("Add game");
        
        for (int i = 0; i < gameString.length; i++) {
            JButton gameButton = new JButton(gameString[i]);
            gameButton.setIcon(iconString[i]);
            
            gameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentGame = gameButton.getText();
                    showTrackTimesGUI(currentGame);
                   
                }
            });
            startPanel.add(gameButton);
        }
            addGamebtn.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent ad) {
                    JOptionPane gameOptionPane = new JOptionPane("");
                    String newGame = addGame(gameOptionPane);
                    JButton newGamebtn = new JButton(newGame);
                    
                    startPanel.add(gameOptionPane);
                    startPanel.add(newGamebtn);
                    frame.pack();
                }
                
            });
        
        startPanel.add(addGamebtn);
        frame.getContentPane().add(startPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    private static String addGame(JOptionPane addOptionPane) {
        
        String addGame = addOptionPane.showInputDialog("Game Name");
        return addGame;
    }
    private static void showTrackTimesGUI(String selectedGame) {
        Map<String, String> currentGameTrackTimes = gameTrackTimes.get(selectedGame);

        JFrame frame = new JFrame("Time Trail table " + selectedGame);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0, 3)); //x * 3 Grid Layout

        for (String track : currentGameTrackTimes.keySet()) {
            JButton button = new JButton(track); //button for every track
            button.addActionListener(new ActionListener() { //action listener for button
                @Override
                public void actionPerformed(ActionEvent e) {
                    String time = currentGameTrackTimes.get(track);
                    String updatedTime = showEditTimeDialog(track, time);
                    if (updatedTime != null) {
                        currentGameTrackTimes.put(track, updatedTime);
                        saveTrackTimesToFile(selectedGame + ".txt", currentGameTrackTimes);
                    }
                }
            });
            mainPanel.add(button);
        }

        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
    private static String showEditTimeDialog(String track, String oldTime) {
        return JOptionPane.showInputDialog(null,
                "Neue Rundenzeit für " + track + " eingeben:", oldTime);
    }

    private static void loadTrackTimesFromFile(String filename, Map<String, String> trackTimes) { //loading all needed tracks and times from txt file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { //reading the file
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2);
                if (parts.length == 2) {
                    trackTimes.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    

    private static void saveTrackTimesToFile(String filename, Map<String, String> trackTimes) { //saving/overwrite the new times
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : trackTimes.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Icon loadImageAsIcon(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Hier können Sie die Größe des Icons festlegen
        return new ImageIcon(scaledImage);
    }
    
   
    
}
