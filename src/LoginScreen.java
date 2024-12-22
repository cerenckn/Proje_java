
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;

public class LoginScreen {
	private JTextField txtSistemeKaytlDeilseniz;

    /**
     * @wbp.parser.entryPoint
     */
    public LoginScreen() {
        // Ana çerçeve oluşturma
        JFrame frmEnvosale = new JFrame("Giriş Yap");
        frmEnvosale.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\crnck\\Downloads\\WhatsApp Image 2024-12-18 at 14.55.57.jpeg"));
        frmEnvosale.setTitle("E-INVOSALE");
        frmEnvosale.getContentPane().setForeground(new Color(224, 255, 255));
        frmEnvosale.setSize(512, 400);
        frmEnvosale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmEnvosale.getContentPane().setLayout(null);
        frmEnvosale.setLocationRelativeTo(null); 
        
        JLabel hello=new JLabel("UYGULAMAYA");
        hello.setForeground(new Color(47, 79, 79));
        hello.setBackground(new Color(0, 100, 0));
        hello.setBounds(129, 37, 339, 48);
        hello.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        hello.setHorizontalAlignment(SwingConstants.CENTER);
        frmEnvosale.getContentPane().add(hello);
        
        // Kullanıcı adı etiketi ve alanı
        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        userLabel.setBounds(20, 160, 120, 30);
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        frmEnvosale.getContentPane().add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(129, 163, 265, 30);
        frmEnvosale.getContentPane().add(userField);

        // Şifre etiketi ve alanı
        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setBounds(20, 197, 120, 30);
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        frmEnvosale.getContentPane().add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(129, 200, 265, 30);
        frmEnvosale.getContentPane().add(passField);

        // Giriş yap butonu
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setBounds(264, 235, 130, 30);
        loginButton.setBackground(new Color(230, 230, 250));
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        frmEnvosale.getContentPane().add(loginButton);
        
        txtSistemeKaytlDeilseniz = new JTextField("Sisteme kayıtlı değilseniz kayıt olunuz.");
        txtSistemeKaytlDeilseniz.setBackground(new Color(245, 245, 245));
        txtSistemeKaytlDeilseniz.setBounds(129, 275, 265, 30);
        txtSistemeKaytlDeilseniz.setEditable(false);
        txtSistemeKaytlDeilseniz.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        frmEnvosale.getContentPane().add(txtSistemeKaytlDeilseniz);
        txtSistemeKaytlDeilseniz.setColumns(10);
        
        JButton registerbtn = new JButton("Kayıt Ol");
        registerbtn.setBounds(264, 315, 130, 30);
        registerbtn.setBackground(new Color(255, 182, 193));
        registerbtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        registerbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                KayıtOlEkranı kayıtOlEkranı = new KayıtOlEkranı();
                kayıtOlEkranı.setVisible(true);
            }
        });
        frmEnvosale.getContentPane().add(registerbtn);
        
        JLabel lblHogeldiniz = new JLabel("HOŞGELDİNİZ");
        lblHogeldiniz.setForeground(new Color(47, 79, 79));
        lblHogeldiniz.setBackground(new Color(255, 128, 192));
        lblHogeldiniz.setHorizontalAlignment(SwingConstants.CENTER);
        lblHogeldiniz.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblHogeldiniz.setBounds(183, 61, 339, 48);
        frmEnvosale.getContentPane().add(lblHogeldiniz);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\crnck\\Downloads\\Free (6).png"));
        lblNewLabel.setBounds(0, 0, 540, 442);
        frmEnvosale.getContentPane().add(lblNewLabel);
        

        // Giriş butonu tıklama işlemi
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                showNotification("Kullanıcı adı ve şifre boş bırakılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String role = validateLogin(username, password);
            if (role != null) {
                showNotification("Giriş başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            	frmEnvosale.dispose(); // Giriş ekranını kapat
                // Role göre ekran açma
                if (role.equalsIgnoreCase("Admin")) {
                	adminScreen yoneticiekrani=new adminScreen(username);
                	yoneticiekrani.setVisible(true);
                } else if (role.equalsIgnoreCase("Kullanıcı")) {
                    UserScreen kullaniciekrani=new UserScreen(username);
                    kullaniciekrani.setVisible(true);           
                }

            } else {
                showNotification("Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frmEnvosale.setVisible(true);
    }

    private void showNotification(String message, String title, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType);
        JDialog dialog = pane.createDialog(null, title);
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        Timer timer = new Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    private String validateLogin(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT role FROM test_users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            } else {
                return null;
            }
        } catch (SQLException e) {
            showNotification("Veritabanı hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
