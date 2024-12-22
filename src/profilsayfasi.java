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
        setBackground(new Color(230, 230, 250));
        setTitle("Profil Sayfası");
        setSize(525, 435);
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
              
                txtPassword.setText("");  
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veri çekme hatası!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Kullanıcı bilgilerini güncelleme
    private void updateUserData(String username) {
        try {
            String query = "UPDATE test_users SET email = ?, password = ? WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, txtEmail.getText());
            stmt.setString(2, new String(txtPassword.getPassword()));  // Şifreyi alıyoruz
            stmt.setString(3, username);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Bilgiler güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Güncellenemedi. Lütfen tekrar deneyin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı güncelleme hatası!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Profil sayfası UI tasarımı
    private void initializeUI() {
        JPanel panel = new JPanel();
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{188, 188, 0};
        gbl_panel.rowHeights = new int[]{58, 58, 58, 58, 0};
        gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
                JLabel lblUsername = new JLabel("Kullanıcı Adı:");
                lblUsername.setFont(new Font("Tahoma", Font.BOLD, 15));
                lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
                
                        GridBagConstraints gbc_lblUsername = new GridBagConstraints();
                        gbc_lblUsername.fill = GridBagConstraints.BOTH;
                        gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
                        gbc_lblUsername.gridx = 0;
                        gbc_lblUsername.gridy = 0;
                        panel.add(lblUsername, gbc_lblUsername);
        
                txtUsername = new JTextField();
                txtUsername.setForeground(new Color(0, 0, 0));
                GridBagConstraints gbc_txtUsername = new GridBagConstraints();
                gbc_txtUsername.fill = GridBagConstraints.BOTH;
                gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
                gbc_txtUsername.gridx = 1;
                gbc_txtUsername.gridy = 0;
                panel.add(txtUsername, gbc_txtUsername);
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        GridBagConstraints gbc_lblEmail = new GridBagConstraints();
        gbc_lblEmail.fill = GridBagConstraints.BOTH;
        gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
        gbc_lblEmail.gridx = 0;
        gbc_lblEmail.gridy = 1;
        panel.add(lblEmail, gbc_lblEmail);
        txtEmail = new JTextField();
        txtEmail.setForeground(new Color(0, 0, 0));
        GridBagConstraints gbc_txtEmail = new GridBagConstraints();
        gbc_txtEmail.fill = GridBagConstraints.BOTH;
        gbc_txtEmail.insets = new Insets(0, 0, 5, 0);
        gbc_txtEmail.gridx = 1;
        gbc_txtEmail.gridy = 1;
        panel.add(txtEmail, gbc_txtEmail);
        JLabel lblPassword = new JLabel("Şifre:");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        GridBagConstraints gbc_lblPassword = new GridBagConstraints();
        gbc_lblPassword.fill = GridBagConstraints.BOTH;
        gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
        gbc_lblPassword.gridx = 0;
        gbc_lblPassword.gridy = 2;
        panel.add(lblPassword, gbc_lblPassword);
        txtPassword = new JPasswordField();
        txtPassword.setForeground(new Color(0, 0, 0));
        GridBagConstraints gbc_txtPassword = new GridBagConstraints();
        gbc_txtPassword.fill = GridBagConstraints.BOTH;
        gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
        gbc_txtPassword.gridx = 1;
        gbc_txtPassword.gridy = 2;
        panel.add(txtPassword, gbc_txtPassword);
        
        JButton btnUpdate = new JButton("Güncelle");
         btnUpdate.setBackground(new Color(135, 206, 235));
         btnUpdate.setForeground(new Color(128, 0, 0));
         btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 13));
               
                        btnUpdate.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String username = txtUsername.getText();
                                updateUserData(username);  
                            }
                        });
                        GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
                        gbc_btnUpdate.fill = GridBagConstraints.BOTH;
                        gbc_btnUpdate.gridx = 1;
                        gbc_btnUpdate.gridy = 3;
                        panel.add(btnUpdate, gbc_btnUpdate);

        getContentPane().add(panel, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        String currentUsername = "testUser";  
        SwingUtilities.invokeLater(() -> {
            profilsayfasi frame = new profilsayfasi(currentUsername);
            frame.setVisible(true);
        });
    }

}

