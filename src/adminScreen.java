import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import com.toedter.calendar.JCalendar;

public class adminScreen {

    private JFrame frmEnvosale;
    private Connection connection;
    private String username;

    public adminScreen() {
    	initialize();
        connectToDatabase();
    }

    public adminScreen(String username) { 
    	this.username=username;
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

    // Menü kısımları
    private void initialize() {
        frmEnvosale = new JFrame();
        frmEnvosale.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\path\\to\\your\\icon.png"));  // İkonu doğru bir şekilde yerleştirdiğinizden emin olun
        frmEnvosale.setTitle("E-INVOSALE");
        frmEnvosale.setBounds(100, 100, 800, 600);
        frmEnvosale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
        
        
        JMenuBar menuBar = new JMenuBar();
        frmEnvosale.setJMenuBar(menuBar);

        JMenu adminMenu = new JMenu(" Admin Paneli");
        adminMenu.setBackground(new Color(255, 228, 225));
        adminMenu.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        menuBar.add(adminMenu);
        frmEnvosale.getContentPane().setLayout(new CardLayout(0, 0));

        JPanel sideMenu = new JPanel();
        frmEnvosale.getContentPane().add(sideMenu, "name_1011268305790600");

        JButton btnProductManagement = new JButton("Ürün Yönetimi");
        btnProductManagement.setBounds(0, 126, 185, 78);
        btnProductManagement.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnProductManagement.setBackground(new Color(204, 153, 153));
        btnProductManagement.addActionListener(e -> showNotification("Ürün Yönetimi ekranı açıldı.", new adminurunYonetimi()));
        sideMenu.setLayout(null);
        sideMenu.add(btnProductManagement);

        JButton btnUserManagement = new JButton("Kullanıcı Yönetimi");
        btnUserManagement.setBounds(0, 207, 185, 78);
        btnUserManagement.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnUserManagement.setBackground(new Color(255, 204, 204));
        btnUserManagement.addActionListener(e -> showNotification("Kullanıcı Yönetimi ekranı açıldı.", new KullanıcıYönetimiadmin()));
        sideMenu.add(btnUserManagement);
        
                JButton btnProfil = new JButton("Profil");
                btnProfil.setBounds(0, 288, 185, 61);
                btnProfil.setFont(new Font("Tahoma", Font.BOLD, 14));
                btnProfil.setBackground(SystemColor.inactiveCaption);
                btnProfil.addActionListener(e -> showNotification("Profil sayfasına gidiliyor.", new profilsayfasi(null)));
                sideMenu.add(btnProfil);
        
                JButton btnExit = new JButton("Çıkış Yap");
                btnExit.setBounds(0, 352, 185, 61);
                btnExit.setFont(new Font("Tahoma", Font.BOLD, 14));
                btnExit.setBackground(new Color(255, 255, 204));
                btnExit.addActionListener(e -> {
                 frmEnvosale.setVisible(false);  // Admin ekranını gizle
                 LoginScreen loginScreen = new LoginScreen();  // Yeni giriş ekranını göster
               loginScreen.setVisible(true);
                });
                sideMenu.add(btnExit);

        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\crnck\\Downloads\\Free (3).png"));
        lblNewLabel.setBounds(-149, -170, 334, 480);
        sideMenu.add(lblNewLabel);
    }

    private void showNotification(String message, JFrame targetFrame) {
        JDialog notification = new JDialog();
        notification.setUndecorated(true);
        notification.setSize(300, 50);
        notification.getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 204));
        label.setForeground(Color.BLACK);

        notification.getContentPane().add(label, BorderLayout.CENTER);
        notification.setLocationRelativeTo(frmEnvosale);
        Point location = frmEnvosale.getLocationOnScreen();
        notification.setLocation(location.x + frmEnvosale.getWidth() - 320, location.y + 20);
        notification.setVisible(true);

        Timer timer = new Timer(3000, e -> {
            notification.dispose();
            if (targetFrame != null) {
                targetFrame.setVisible(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                adminScreen window = new adminScreen();
                window.frmEnvosale.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    } 

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
