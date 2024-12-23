import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class KayıtOlEkranı extends JFrame {
    public KayıtOlEkranı() {
       
        JFrame frame = new JFrame("Kayıt Ol");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/crnck/eclipse-workspace/GUI.oop/GUI.oop/src/images/invoices.png"));
        frame.setSize(524, 438);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Kayıt Ol", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBounds(0, 0, 524, 40);
        titleLabel.setForeground(new Color(50, 50, 50));
        frame.getContentPane().add(titleLabel);

        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        userLabel.setBounds(22, 70, 153, 30);
        frame.getContentPane().add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 70, 258, 30);
        userField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        frame.getContentPane().add(userField);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEmail.setBounds(23, 120, 100, 30);
        frame.getContentPane().add(lblEmail);

        JTextField emailField = new JTextField();
        emailField.setBounds(150, 120, 258, 30);
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        frame.getContentPane().add(emailField);

        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        passLabel.setBounds(23, 170, 100, 30);
        frame.getContentPane().add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 170, 258, 30);
        passField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        frame.getContentPane().add(passField);


        JLabel roleLabel = new JLabel("Rol:");
        roleLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        roleLabel.setBounds(23, 220, 100, 30);
        frame.getContentPane().add(roleLabel);

        String[] roles = {"Kullanıcı", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        roleBox.setBounds(152, 220, 180, 30);
        frame.getContentPane().add(roleBox);

        // Kayıt Ol Butonumuzz
        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        registerButton.setBounds(154, 270, 100, 35);
        registerButton.setBackground(new Color(50, 205, 50));  // Yeşil renk
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(50, 205, 50), 1));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getContentPane().add(registerButton);

        // Kayıt Ol Butonu İşlemleriii
        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tüm alanları doldurunuz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

 
            if (registerUser(username, email, password, role)) {
                JOptionPane.showMessageDialog(null, "Kayıt Başarılı!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Kayıt Başarısız! Kullanıcı adı veya email zaten kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

   
    private boolean registerUser(String username, String email, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Veritabanına bağlanılamadı!");
                return false;
            }

            String checkQuery = "SELECT username FROM test_users WHERE username = ? OR email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) { // kullanıcı adı veya emaili varsa sistemde
                return false;
            }

            // Yeni kullanıcı ekleme
            String insertQuery = "INSERT INTO test_users(username, email, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);
            insertStmt.setString(4, role); 
            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0; // kayıt başarılı mı?
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Kayıt başarısız
    }

    public static void main(String[] args) {
        new KayıtOlEkranı();
    }
}
