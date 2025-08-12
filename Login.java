//Swing packages
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Read and Write package
import java.io.*;

//Hashing package
import java.security.*;
import java.nio.charset.*;

class Login extends JFrame {

    JLabel prompt;
    JPasswordField masterPassword;
    JButton loginButton;

    JPanel centerPanel = new JPanel(new FlowLayout());
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel buttonsPanel = new JPanel(new FlowLayout());

    static Login myFrame;

    static String mp;
    static final String FILEPATH = "Passwords.txt";

    void initialize() {

        prompt = new JLabel("");
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(prompt, BorderLayout.NORTH);

        masterPassword = new JPasswordField(15);
        masterPassword.setEchoChar('*');
        centerPanel.add(masterPassword);
        centerPanel.setBackground(new Color(229, 229, 234));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginButton.setFocusable(false);
        buttonsPanel.add(loginButton);
        buttonsPanel.setBackground(new Color(229, 229, 234));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setTitle("Login");
        setSize(215, 175);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new Color(229, 229, 234));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setResizable(false);

        mp = ReadMasterPassword(FILEPATH);
        if(mp == null) {

            prompt.setText("Please create a master password:");

            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  // Convert char[] to String.  
                  String password = new String(masterPassword.getPassword());

                  mp = hexDigest(password, "SHA-256");

                  SaveMasterPassword(mp, FILEPATH);

                  JOptionPane.showMessageDialog(myFrame, "Master password created. We will log you in now.", 
                      "Password Manager Ready", JOptionPane.INFORMATION_MESSAGE);

                  myFrame.dispose();
                  Manager newWindow = new Manager();
          }
            });
        }

        else {

            prompt.setText("Please verify your master password:");

            loginButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    // Convert char[] to String.  
                    String password = new String(masterPassword.getPassword());

                    String verification = hexDigest(password, "SHA-256");

                    if(verification.equals(mp)) {
                        myFrame.dispose();
                        Manager newWindow = new Manager();
                    }
                    else {
                        JOptionPane.showMessageDialog(myFrame, "Invalid Master Password. Please Try Again.", 
                            "Error Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }

        setVisible(true);
    }

    public static void main(String[]args) {
        myFrame = new Login();
        myFrame.initialize();
    }

    static String ReadMasterPassword(String FilePath) {
        //if there is no file or the file is empty, then return the null string.
        String MasterPass = null;
        
        // attempt to read master password from file.
        try {
          BufferedReader in = new BufferedReader(new FileReader(FilePath));
          String s = in.readLine();
          MasterPass = s;
          in.close();
        }
        catch(java.io.FileNotFoundException e) { }
        catch(java.io.IOException t) { }
        
        return MasterPass;
      }

      static String hexDigest(String str, String digestName) {
        try {
          MessageDigest md = MessageDigest.getInstance(digestName);
          byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
          char[] hex = new char[digest.length * 2];
          for (int i = 0; i < digest.length; i++) {
            hex[2 * i] = "0123456789abcdef".charAt((digest[i] & 0xf0) >> 4);
            hex[2 * i + 1] = "0123456789abcdef".charAt(digest[i] & 0x0f);
          }

          return new String(hex);
        } catch (NoSuchAlgorithmException e) {
          throw new IllegalStateException(e);
        }
      }

      private void SaveMasterPassword(String pwd, String FilePath) {
        try
        { 
          PrintWriter out = new PrintWriter(new BufferedWriter (new FileWriter(FilePath)));
          
          out.println(pwd);
          out.flush();
          out.close();
        }
    catch (IOException e) {}
      }
}