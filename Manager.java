//Swing packages
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Manager extends JFrame {

    JFrame frame = new JFrame();

    JLabel label = new JLabel("Type the nickname of the password you want to view.");

    JButton enter = new JButton("Enter");
    JButton view = new JButton("View");
    JTextField nickname = new JTextField(15);

    JPanel centerPanel = new JPanel(new FlowLayout());
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel buttonsPanel = new JPanel(new FlowLayout());

    Manager() {

        mainPanel.setBackground(new Color(229, 229, 234));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
  
        mainPanel.add(label, BorderLayout.NORTH);

        buttonsPanel.setBackground(new Color(229, 229, 234));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        enter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        enter.setFocusable(false);
        enter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                View newViewWindow = new View(false, null);
            }
        });

        buttonsPanel.add(enter);

        view.setFont(new Font("Segoe UI", Font.BOLD, 12));
        view.setFocusable(false);
        view.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(nickname.getText().equals("")) 
                    JOptionPane.showMessageDialog(frame, "Please enter a nickname to view."
                        , "Error Message", JOptionPane.ERROR_MESSAGE);

                else {
                    View newViewWindow = new View(true, nickname.getText());
                }
            }});

        buttonsPanel.add(view);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        centerPanel.setBackground(new Color(229, 229, 234));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(nickname);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Password Manager");
        frame.setSize(585, 200);
        frame.setResizable(false);
        frame.add(mainPanel);

        frame.setVisible(true);
    }
}