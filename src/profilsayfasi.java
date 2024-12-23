import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class profilsayfasi extends JFrame {

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private Connection connection;
    private String username;

    public profilsayfasi(String username) {
        this.username = username;  // Kullanıcı adı sınıf seviyesinde saklanır.
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\crnck\\Downloads\\WhatsApp Image 2024-12-18 at 14.55.57.jpeg"));
        setBackground(new Color(245, 245, 245));  // Soft light background color
        setTitle("Profil Sayfası");
        setSize(650, 550);  // Slightly larger frame for professional feel
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        connectToDatabase();
        initializeUI();
        loadUserData(username);
    }

    // Veritabanı bağlantısı
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "*Crn123.*");
            System.out.println("Veritabanına bağlanıldı.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı bağlantı hatası!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Kullanıcı verilerini alıp, UI'de göstermek için
    private void loadUserData(String username) {
        try {
            String query = "SELECT * FROM test_users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username); 
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Veritabanından gelen verilerle UI'yi güncelle
                txtUsername.setText(rs.getString("username"));
                txtEmail.setText(rs.getString("email"));
                txtPassword.setText("");  // Şifreyi göstermiyoruz
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veri çekme hatası!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Kullanıcı bilgilerini güncelleme
    private void updateUserData() {
        try {
            String newUsername = txtUsername.getText();  // Yeni kullanıcı adı
            String email = txtEmail.getText();            // E-posta
            String password = new String(txtPassword.getPassword());  // Şifre

            // Kullanıcı adının veritabanındaki `id`'sini almak için sorgu
            String getIdQuery = "SELECT id FROM test_users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(getIdQuery);
            stmt.setString(1, this.username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("id");

                // Kullanıcıyı id ile güncelleme
                String updateQuery = "UPDATE test_users SET username = ?, password = ?, email = ? WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, newUsername);
                updateStmt.setString(2, password);
                updateStmt.setString(3, email);
                updateStmt.setInt(4, userId);  // Kullanıcı id'si ile güncelle

                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    this.username = newUsername;  // Yeni kullanıcı adını güncelle
                    JOptionPane.showMessageDialog(null, "Bilgiler başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

                    // Üst kısımdaki kullanıcı adı etiketi güncelleniyor
                    JLabel usernameLabel = (JLabel) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
                    usernameLabel.setText("Hoş geldiniz, " + this.username + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "Güncelleme başarısız! Kullanıcı adı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Kullanıcı adı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı güncelleme hatası!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Profil sayfası UI tasarımı
    private void initializeUI() {
        // Set layout manager
        setLayout(new BorderLayout());
        
        // Header Panel with dynamic Username
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(173, 216, 230));  // Light blue background for header
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("Profil Sayfası", JLabel.CENTER);
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        headerLabel.setForeground(new Color(50, 50, 50));  // Soft dark gray text
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        // Dynamic username display in the top-right corner
        JLabel usernameLabel = new JLabel("Hoş geldiniz, " + this.username + "!", JLabel.RIGHT);
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        usernameLabel.setForeground(new Color(50, 50, 50));  // Soft dark gray text
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(usernameLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content panel with modern input styling
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));  // Light background for form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        JLabel lblUsername = new JLabel("Kullanıcı Adı:");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUsername.setForeground(new Color(70, 70, 70));  // Soft dark gray text
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtUsername.setBackground(new Color(230, 230, 250));  // Light lavender background
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2));
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(txtUsername, gbc);

        // Email field
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblEmail.setForeground(new Color(70, 70, 70));  // Soft dark gray text
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtEmail.setBackground(new Color(230, 230, 250));  // Light lavender background
        txtEmail.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2));
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(txtEmail, gbc);

        // Password field
        JLabel lblPassword = new JLabel("Şifre:");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPassword.setForeground(new Color(70, 70, 70));  // Soft dark gray text
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtPassword.setBackground(new Color(230, 230, 250));  // Light lavender background
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2));
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(txtPassword, gbc);

        // Update button with soft hover effect
        JButton btnUpdate = new JButton("Güncelle");
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnUpdate.setBackground(new Color(173, 216, 230));  // Soft blue for button
        btnUpdate.setForeground(new Color(50, 50, 50));  // Dark gray text
        btnUpdate.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserData();  
            }
        });

        // Add button to panel
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(btnUpdate, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        String currentUsername = "test_user";  
        SwingUtilities.invokeLater(() -> {
            profilsayfasi frame = new profilsayfasi(currentUsername);
            frame.setVisible(true);
        });
    }
}
