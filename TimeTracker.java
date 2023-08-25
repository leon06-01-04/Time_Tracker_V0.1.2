import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

//Author Leon Schobert

public class TimeTracker {
    private static Map<String, Map<String, String>> gameTrackTimes = new HashMap<>();
    private static String gameString[] = {"F1", "AMS2", "DTM_RD3", "grandprix4"}; //stringArray as game list 
    private static String currentGame = ""; // current game

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

        for (int i = 0; i < gameString.length; i++) {
            JButton gameButton = new JButton(gameString[i]);

            gameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentGame = gameButton.getText();
                    showTrackTimesGUI(currentGame);
                }
            });

            startPanel.add(gameButton);
        }
        frame.getContentPane().add(startPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void showTrackTimesGUI(String selectedGame) {
        Map<String, String> currentGameTrackTimes = gameTrackTimes.get(selectedGame);

        JFrame frame = new JFrame("Time Trail table " + selectedGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0, 3));

        for (String track : currentGameTrackTimes.keySet()) {
            JButton button = new JButton(track);
            button.addActionListener(new ActionListener() {
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
        frame.pack();
        frame.setVisible(true);
    }
    private static String showEditTimeDialog(String track, String oldTime) {
        return JOptionPane.showInputDialog(null,
                "Neue Rundenzeit für " + track + " eingeben:", oldTime);
    }

    private static void loadTrackTimesFromFile(String filename, Map<String, String> trackTimes) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
    

    private static void saveTrackTimesToFile(String filename, Map<String, String> trackTimes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : trackTimes.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
