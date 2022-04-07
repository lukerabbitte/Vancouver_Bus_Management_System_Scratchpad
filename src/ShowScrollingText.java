import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Based on tutorial from http://alvinalexander.com
 */
public class ShowScrollingText
{
    private String messageToDisplay;

    ShowScrollingText(String message) {
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
            JOptionPane.showMessageDialog(null, scrollPane,
                    "All relevant stops", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}