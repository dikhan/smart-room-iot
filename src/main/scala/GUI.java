
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
 import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by jadavey on 02/12/2016.
 */
public class GUI {

    JFrame frame;
    private JLabel roomOccupied;
    private JLabel roomSatus;
    // private Exchange exchange;

    public GUI(String meetingRoom) throws Exception {
        frame = new JFrame();
        JLabel room = new JLabel(meetingRoom);
        roomOccupied = new JLabel("");
        roomOccupied.setFont(new Font("Serif", Font.PLAIN, 50));
        roomSatus = new JLabel("");
        roomSatus.setFont(new Font("Serif", Font.PLAIN, 50));
        room.setFont(new Font("Serif", Font.PLAIN, 50));
        frame.getContentPane().add(room);
        frame.getContentPane().add(roomSatus);
        frame.getContentPane().add(roomOccupied);
        JPanel bookingBar = new JPanel();
        JButton roomBookingButton = new JButton(" book room for " + 15 + " minutes");
        roomBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isBooked = bookRoom(15);
                    if(isBooked) {
                        showMessageDialog(null,"room booked");
                    } else {
                        showMessageDialog(null,"room not booked");
                    }
                }
                catch (Exception error) {
                    System.out.println(error);
                }
            }
        });
        bookingBar.add(roomBookingButton);
        JButton thirtyButton = new JButton(" book room for " + 30 + " minutes");
        thirtyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isBooked = bookRoom(30);
                    if(isBooked) {
                        showMessageDialog(null,"room booked");
                    } else {
                        showMessageDialog(null,"room not booked");
                    }
                }
                catch (Exception error) {
                    System.out.println(error);
                }
            }
        });
        bookingBar.add(thirtyButton);
        JButton fortyFiveButton = new JButton(" book room for " + 45 + " minutes");
        fortyFiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isBooked = bookRoom(45);
                    if(isBooked) {
                        showMessageDialog(null,"room booked");
                    } else {
                        showMessageDialog(null,"room not booked");
                    }
                }
                catch (Exception error) {
                    System.out.println(error);
                }
            }
        });
        bookingBar.add(fortyFiveButton);
        JButton sixtyMinuteButton = new JButton(" book room for " + 60 + " minutes");
        sixtyMinuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isBooked = bookRoom(60);
                    if(isBooked) {
                        showMessageDialog(null,"room booked");
                    } else {
                        showMessageDialog(null,"room not booked");
                    }
                }
                catch (Exception error) {
                    System.out.println(error);
                }
            }
        });
        bookingBar.add(sixtyMinuteButton);
        frame.add(bookingBar);
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
        frame.pack();
        frame.setVisible(true);

    }


    private String convertBooleanToString(boolean value, String trueResult, String falseResult) {
        if(value == true) {
            return trueResult;
        }
        else {
            return  falseResult;
        }
    }


    public static boolean bookRoom(int duration) throws Exception {

        try {

            URL url = new URL(endpoint + duration);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/text");

            String input = new Integer(duration).toString();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getResponseMessage().contains("booked");
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();
            return false;

        } catch (IOException e) {

            e.printStackTrace();
            return false;

        }
        return false;
    }

    public void displayRoomAvailibility(Boolean isAvailible) {
        roomOccupied.setText("room availiblity " + convertBooleanToString(isAvailible,"free","booked"));
    }

    public void displayRoomStatus(boolean isFree) {
        roomSatus.setText("room status  " + convertBooleanToString(isFree,"availible","in use"));
    }

    private static String endpoint;

    public static  void main(String [] args) throws Exception {
        if(args.length < 1) {
            throw new IllegalArgumentException("no swing endpoint specified");
        }
        endpoint = args[0];
        GUI gui =  new GUI("alto");
        gui.displayRoomAvailibility(true);
        gui.displayRoomStatus(true);
    }

}
