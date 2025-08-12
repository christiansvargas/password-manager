//Swing packages
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Read/Write package
import java.io.*;
import java.nio.charset.*;

//ArrayList package
import java.util.*;

//Password encryption package (AES Encryption Algorithm)
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

//Byte Array to String Array package
import javax.xml.bind.DatatypeConverter;
//import javax.xml.*;

// Browser Launching package
import java.net.URI;

class View extends JFrame {

    static final String ENC_KEY = "0e329232ea6d0d73";
    
    static UserPassword upobject;

    JFrame frame = new JFrame();

    JLabel label = new JLabel("Enter the credentials and click 'Enter' to save it:");

    JLabel nicknameLabel = new JLabel("Nickname:");
    JLabel usernameLabel = new JLabel("User Name:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel urlLabel = new JLabel("URL:");

    JButton enter = new JButton("Enter");
    JButton cancel = new JButton("Cancel");
    JButton visit = new JButton("Browse");

    JTextField nicknameField = new JTextField(15);
    JTextField usernameField = new JTextField(15);
    JTextField passwordField = new JTextField(15);
    JTextField urlField = new JTextField(15);

    JPanel centerPanel;
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel buttonsPanel = new JPanel(new FlowLayout());

    View(boolean readOnly, String inputNickname) {
try {
       ArrayList<UserPassword> testList = new ArrayList<UserPassword>();

       ReadPasswords(testList, Login.FILEPATH);

       mainPanel.setBackground(new Color(229, 229, 234));
       mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

       nicknameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
       usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
       passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
       urlLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
 
       mainPanel.add(label, BorderLayout.NORTH);

       centerPanel = new JPanel();

       GroupLayout layout = new GroupLayout(centerPanel);
       centerPanel.setLayout(layout);
       layout.setAutoCreateGaps(true);
       layout.setAutoCreateContainerGaps(true);

       layout.setHorizontalGroup(
        layout.createSequentialGroup()
           .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nicknameLabel)
                .addComponent(usernameLabel)
                .addComponent(passwordLabel)
                .addComponent(urlLabel)
                )
           .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(nicknameField)
                .addComponent(usernameField)
                .addComponent(passwordField)
                .addComponent(urlField)
                )
     );
     layout.setVerticalGroup(
        layout.createSequentialGroup()
           .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
              .addComponent(nicknameLabel)
              .addComponent(nicknameField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
              .addComponent(usernameLabel)
              .addComponent(usernameField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
              .addComponent(passwordLabel)
              .addComponent(passwordField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
              .addComponent(urlLabel)
              .addComponent(urlField))
     );

       buttonsPanel.setBackground(new Color(229, 229, 234));
       buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       enter.setFont(new Font("Segoe UI", Font.BOLD, 12));
       enter.setFocusable(false);

       visit.setFont(new Font("Segoe UI", Font.BOLD, 12));
       visit.setFocusable(false);
       visit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if(urlField.getText() == "") JOptionPane.showMessageDialog(frame, "Please provide a URL to browse.", 
              "Error Message", JOptionPane.ERROR_MESSAGE);

            else {
              try {

                URI uri = new URI("https://" + urlField.getText());
                java.awt.Desktop.getDesktop().browse(uri);
              } catch(Exception f) {f.printStackTrace();}
            }
        }
    });

       if(readOnly) {

         enter.setVisible(false);

         nicknameField.setEnabled(false);

         UserPassword search = new UserPassword();

         search.nickname = inputNickname;

         int itemIndex = testList.indexOf(search);

         if(itemIndex == -1) {

           JOptionPane.showMessageDialog(frame, "The nickname you have entered is not in your passwords.", 
            "Error Message", JOptionPane.ERROR_MESSAGE);

           frame.dispose();

           return;
         }

         else {

          label.setText("Credentials:");
           search = testList.get(itemIndex);

           nicknameField.setText(search.nickname);
           usernameField.setText(DecryptText(search.username, ENC_KEY, "AES"));
           passwordField.setText(DecryptText(search.password, ENC_KEY, "AES"));
           urlField.setText(search.url);
         }
       }

       enter.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {

               upobject = new UserPassword();

               upobject.nickname = nicknameField.getText();
               upobject.username = EncryptText(usernameField.getText(), ENC_KEY, "AES");
               upobject.password = EncryptText(passwordField.getText(), ENC_KEY, "AES");
               upobject.url = urlField.getText();

               int idx = testList.indexOf(upobject);

               if(idx > -1) testList.remove(upobject);
               
               testList.add(upobject);          

               SavePasswords(testList, Login.FILEPATH);
               JOptionPane.showMessageDialog(frame, "Successful Entry", "Entry Message", JOptionPane.INFORMATION_MESSAGE);

               frame.dispose();
           }
       });

       buttonsPanel.add(enter);
       buttonsPanel.add(visit);

       cancel.setFont(new Font("Segoe UI", Font.BOLD, 12));
       cancel.setFocusable(false);
       cancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            frame.dispose();
        }
    });

       buttonsPanel.add(cancel);

       mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

       centerPanel.setBackground(new Color(229, 229, 234));
       centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       mainPanel.add(centerPanel, BorderLayout.CENTER);

       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setTitle("Enter or View");
       frame.setSize(400, 300);
       frame.setResizable(false);
       frame.add(mainPanel);

       frame.setVisible(true);
   
} catch (Exception e) {
    System.out.println(e.getMessage());
}
    }

static void ReadPasswords(ArrayList<UserPassword> L, String path) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(path));
      
      //This "readLine();" is meant to skip the master password.
      String s = in.readLine();
      
      s = in.readLine();
      while(s != null) {
        UserPassword up = new UserPassword(s);
        
        L.add(up);
        s = in.readLine();
      }
      in.close();
    }
    catch(java.io.FileNotFoundException e) {System.out.println("FileNotFoundException");}
    catch(java.io.IOException t) {System.out.println("IOException");}
  }

  private static String EncryptText(String plainText, String key, String algorithm) {
    String hex = null;
    try {
      Cipher c = Cipher.getInstance(algorithm);
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
      c.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      
      byte[] encBytes = c.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      hex = DatatypeConverter.printHexBinary(encBytes);
    } catch (Exception ex) {
      System.out.println("Error encrypting data");
      ex.printStackTrace();
    }
    return hex;
  }

  private static void SavePasswords(ArrayList<UserPassword> UPList, String FilePath) {
    try
    { 
      PrintWriter out = new PrintWriter( new BufferedWriter (new FileWriter(FilePath)));
      
      //save the master password on the first line of the file.
      out.println(Login.mp);
      
      for (UserPassword p : UPList) {
        out.println(p.nickname + ";" + p.username + ";" + p.password + ";" + p.url);
      }
      out.flush();
      out.close();
    }
    catch (IOException e) { System.out.println("Problem Saving Passwords"); }
  }

  private static String DecryptText(String cipherText, String key, String algorithm) {
    String decStr = null;
    try {
      Cipher c = Cipher.getInstance(algorithm);
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(java.nio.charset.StandardCharsets.UTF_8), algorithm);
      c.init(Cipher.DECRYPT_MODE, secretKeySpec);
      
      byte[] encBytes = DatatypeConverter.parseHexBinary(cipherText);
      decStr = new String(c.doFinal(encBytes), java.nio.charset.StandardCharsets.UTF_8);
    } catch (Exception ex) {
      System.out.println("Error encrypting data");
      ex.printStackTrace();
    }
    return decStr;
  }
}
