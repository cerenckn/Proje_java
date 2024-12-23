import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.Font;

public class KayıtOlEkranı extends JFrame {
    public KayıtOlEkranı() {
        JFrame frame = new JFrame("Kayıt Ol");
        frame.setSize(524, 438);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
        

        // Kullanıcı Adı Label ve Field
        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        userLabel.setBounds(22, 25, 153, 30);
        frame.getContentPane().add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 28, 258, 30);
        frame.getContentPane().add(userField);

        // Email Label ve Field
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEmail.setBounds(23, 84, 100, 30);
        frame.getContentPane().add(lblEmail);

        JTextField emailField = new JTextField(); // Email alanı düzeltildi
        emailField.setBounds(150, 87, 258, 30);
        frame.getContentPane().add(emailField);

        // Şifre Label ve Field
        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        passLabel.setBounds(23, 143, 100, 30);
        frame.getContentPane().add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 146, 258, 30);
        frame.getContentPane().add(passField);

        // Rol Label ve ComboBox
        JLabel roleLabel = new JLabel("Rol:");
        roleLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        roleLabel.setBounds(23, 186, 100, 30);
        frame.getContentPane().add(roleLabel);

        String[] roles = {"Kullanıcı", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(new Font("Tahoma", Font.BOLD, 14));
        roleBox.setBounds(152, 186, 180, 30);
        frame.getContentPane().add(roleBox);

        // Kayıt Ol Butonu
        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        registerButton.setBounds(154, 226, 100, 30);
        frame.getContentPane().add(registerButton);

        // Kayıt Ol Butonu Aksiyonları
        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String email = emailField.getText(); // Email alanı alındı
            String password = new String(passField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            // Girdi Kontrolleri
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tüm alanları doldurunuz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Veritabanına Kaydetme
            if (registerUser(username, email, password, role)) {
                JOptionPane.showMessageDialog(null, "Kayıt Başarılı!");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Kayıt Başarısız! Kullanıcı adı veya email zaten kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    /**
     * Kullanıcıyı veritabanına kaydeder. 
     * Eğer kullanıcı adı veya email zaten kayıtlıysa false döner.
     */
    private boolean registerUser(String username, String email, String password, String role) {
        try (Connection conn = DatabaseConnection.getConnection()) { // Veritabanı bağlantısını alın
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Veritabanına bağlanılamadı!");
                return false;
            }

            // Kullanıcı adı ve email kontrolü
            String checkQuery = "SELECT username FROM test_users WHERE username = ? OR email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) { // Kullanıcı adı veya email mevcutsa
                return false;
            }

            // Yeni kullanıcı ekleme
            String insertQuery = "INSERT INTO test_users(username, email, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);
            insertStmt.setString(4, role); 
            int rowsInserted = insertStmt.executeUpdate(); // Kaç satırın eklendiğini kontrol edin
            return rowsInserted > 0; // Kayıt başarılı mı kontrol et
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
