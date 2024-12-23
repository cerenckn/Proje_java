import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class LoginScreen {

    private JTextField txtRegisterPrompt;

    public LoginScreen() {
//frame  oluşturalım
        JFrame frame = new JFrame("Giriş Yap");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/crnck/eclipse-workspace/GUI.oop/GUI.oop/src/images/invoices.png"));
        frame.setSize(512, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.getContentPane().setLayout(null);
        

        setBackgroundImage(frame);
        setLogo(frame);

        setupUIComponents(frame);

        frame.setVisible(true);
    }

    private void setupUIComponents(JFrame frame) {

        JLabel welcomeLabel = createLabel("E-INVOSALE", 129, 50, 339, 48, new Font("Arial", Font.BOLD, 24), new Color(47, 79, 79));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(welcomeLabel);

        JLabel userLabel = createLabel("Kullanıcı Adı:", 20, 160, 120, 30, new Font("Tahoma", Font.BOLD, 15), null);
        frame.getContentPane().add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(129, 163, 265, 30);
        userField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 250)));
        frame.getContentPane().add(userField);


        JLabel passLabel = createLabel("Şifre:", 20, 197, 120, 30, new Font("Tahoma", Font.BOLD, 15), null);
        frame.getContentPane().add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(129, 200, 265, 30);
        passField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 250)));
        frame.getContentPane().add(passField);


        JButton loginButton = createButton("Giriş Yap", 264, 235, 130, 40, new Color(70, 130, 180), new Color(100, 149, 237));
        loginButton.addActionListener(e -> handleLogin(userField, passField, frame));
        frame.getContentPane().add(loginButton);
        

        txtRegisterPrompt = new JTextField("Sisteme kayıtlı değilseniz kayıt olunuz.");
        txtRegisterPrompt.setBackground(new Color(245, 245, 245));
        txtRegisterPrompt.setBounds(129, 275, 265, 30);
        txtRegisterPrompt.setEditable(false);
        txtRegisterPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtRegisterPrompt.setBorder(null);
        frame.getContentPane().add(txtRegisterPrompt);
        
        JButton registerButton = createButton("Kayıt Ol", 264, 315, 130, 40, new Color(230, 182, 193), new Color(255, 105, 180));
        registerButton.addActionListener(e -> openRegistrationScreen());
        frame.getContentPane().add(registerButton);
    }

    private JLabel createLabel(String text, int x, int y, int width, int height, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        if (font != null) label.setFont(font);
        if (color != null) label.setForeground(color);
        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void setBackgroundImage(JFrame frame) {
        JLabel backgroundLabel = new JLabel("");
        backgroundLabel.setIcon(new ImageIcon(""));
        backgroundLabel.setBounds(0, 0, 540, 442);
        frame.getContentPane().add(backgroundLabel);
    }

    private void setLogo(JFrame frame) {
        JLabel logoLabel = new JLabel(new ImageIcon("C:/Users/crnck/eclipse-workspace/GUI.oop/GUI.oop/src/images/invoices.png"));
        logoLabel.setBounds(20,20, 150, 100);
        frame.getContentPane().add(logoLabel);
    }
        //Giriş kontrolü
    private void handleLogin(JTextField userField, JPasswordField passField, JFrame frame) {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showNotification("Kullanıcı adı ve şifre boş bırakılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String role = validateLogin(username, password);
        if (role != null) {
            showNotification("Giriş başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();

            if (role.equalsIgnoreCase("Admin")) {
                openAdminScreen(username);
            } else if (role.equalsIgnoreCase("Kullanıcı")) {
                openUserScreen(username);
            }
        } else {
            showNotification("Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
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
    //role uygun pencere açtıran fonksiyonlar
    private void openAdminScreen(String username) {
        adminScreen adminScreen = new adminScreen(username);
        adminScreen.setVisible(true);
    }

    private void openUserScreen(String username) {
        UserScreen userScreen = new UserScreen(username);
        userScreen.setVisible(true);
    }

    private void openRegistrationScreen() {
        KayıtOlEkranı registrationScreen = new KayıtOlEkranı();
        registrationScreen.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }

	public void setVisible(boolean b) {
		LoginScreen frame = null;
		frame.setVisible(b);
		
	}

}