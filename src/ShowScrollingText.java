import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Based on tutorial from http://alvinalexander.com
 */
public class ShowScrollingText
{
    private String messageToDisplay;
    private boolean isStops;
    private boolean isTrips;

    ShowScrollingText(String message, String typeOfMessage) {

        if (typeOfMessage.equals("Stops"))
            this.isStops = true;
        else if (typeOfMessage.equals("Trips"))
            this.isTrips = true;

        this.messageToDisplay = message;
        ShowScrollingDialog myDialog = new ShowScrollingDialog();
        myDialog.display();
    }

    class ShowScrollingDialog {

        JScrollPane scrollPane;

        ShowScrollingDialog()
        {
            JTextArea textArea = new JTextArea(19, 80);
            textArea.setText(messageToDisplay);
            textArea.setEditable(false);

            //Wrap JScrollPane around textArea
            scrollPane = new JScrollPane(textArea);
        }

        void display() {

            if (isStops==true) {
                JOptionPane.showMessageDialog(null, scrollPane,
                        "All relevant stops", JOptionPane.INFORMATION_MESSAGE);
            }

            else if (isTrips==true) {
                JOptionPane.showMessageDialog(null, scrollPane,
                        "All relevant trips, sorted by ascending Trip ID", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}