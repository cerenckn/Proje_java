import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class UserScreen {

    private JFrame frmEnvosale;
    private Connection connection;
    private JPanel mainPanel; // Ana panel
    private CardLayout cardLayout; // CardLayout ile ekranlar arasında geçiş
    private JPanel profilPage; // Profil sayfası
    private JPanel loginPage; // Giriş ekranı

    public UserScreen(String username) {
        initialize();
        connectToDatabase();
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

    // Kullanıcı ekranını başlatma
    private void initialize() {
        frmEnvosale = new JFrame();
        frmEnvosale.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\crnck\\Downloads\\WhatsApp Image 2024-12-18 at 14.55.57.jpeg"));
        frmEnvosale.setTitle("E-INVOSALE");
        frmEnvosale.setBounds(100, 100, 800, 600);
        frmEnvosale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmEnvosale.getContentPane().setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        frmEnvosale.setJMenuBar(menuBar);

        JMenu userMenu = new JMenu("Kullanıcı Paneli");
        userMenu.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        menuBar.add(userMenu);

        JPanel sideMenu = new JPanel();
        sideMenu.setBounds(10, 115, 189, 415);
        frmEnvosale.getContentPane().add(sideMenu);

        JButton btnMakeSale = new JButton("Satış Yap");
        btnMakeSale.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnMakeSale.setBackground(new Color(173, 216, 230));
        btnMakeSale.addActionListener(e -> showMakeSale());

        JButton btnCreateInvoice = new JButton("Fatura Oluştur");
        btnCreateInvoice.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCreateInvoice.setBackground(new Color(204, 204, 153));
        btnCreateInvoice.addActionListener(e -> showCreateInvoice());
        
                JButton btnProfile = new JButton("Profil");
                btnProfile.setFont(new Font("Tahoma", Font.BOLD, 13));
                btnProfile.setBackground(new Color(255, 204, 204));
                btnProfile.addActionListener(e -> showProfilePage()); 
                
                JButton btnExit = new JButton("Çıkış Yap");
                btnExit.setBounds(20, 479, 200, 100);
                btnExit.setFont(new Font("Tahoma", Font.BOLD, 14));
                btnExit.setBackground(new Color(255, 255, 204));
                btnExit.addActionListener(e -> {
                 frmEnvosale.setVisible(false);  // Admin ekranını gizle
                 LoginScreen loginScreen = new LoginScreen();  // Yeni giriş ekranını göster
               loginScreen.setVisible(true);
                });
                GroupLayout gl_sideMenu = new GroupLayout(sideMenu);
                gl_sideMenu.setHorizontalGroup(
                	gl_sideMenu.createParallelGroup(Alignment.LEADING)
                		.addGroup(gl_sideMenu.createSequentialGroup()
                			.addGroup(gl_sideMenu.createParallelGroup(Alignment.LEADING)
                				.addGroup(gl_sideMenu.createSequentialGroup()
                					.addGap(7)
                					.addComponent(btnMakeSale, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
                				.addGroup(gl_sideMenu.createSequentialGroup()
                					.addGap(7)
                					.addComponent(btnCreateInvoice, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
                				.addGroup(gl_sideMenu.createSequentialGroup()
                					.addGap(7)
                					.addComponent(btnProfile, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
                				.addGroup(Alignment.TRAILING, gl_sideMenu.createSequentialGroup()
                					.addGap(7)
                					.addComponent(btnExit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                			.addContainerGap())
                );
                gl_sideMenu.setVerticalGroup(
                	gl_sideMenu.createParallelGroup(Alignment.LEADING)
                		.addGroup(gl_sideMenu.createSequentialGroup()
                			.addGap(7)
                			.addComponent(btnMakeSale, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                			.addGap(7)
                			.addComponent(btnCreateInvoice, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                			.addGap(7)
                			.addComponent(btnProfile, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                			.addPreferredGap(ComponentPlacement.UNRELATED)
                			.addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                			.addGap(73))
                );
                sideMenu.setLayout(gl_sideMenu);
                
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(-150, -118, 346, 368);
        frmEnvosale.getContentPane().add(lblNewLabel);
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\crnck\\Downloads\\Free (3).png"));
    }

    // Satış Yap ekranı
    private void showMakeSale() {
        showNotification("Satış ekranı açıldı."); // Bildirim göster
        SatısEkranıUser satisEkrani = new SatısEkranıUser();
        satisEkrani.setVisible(true);
    }


    // Fatura Oluştur ekranı
    private void showCreateInvoice() {
        showNotification("Fatura oluşturma ekranı açıldı."); // Bildirim göster
        FaturaOluşturmaEkranı faturaEkrani = new FaturaOluşturmaEkranı();
        faturaEkrani.setVisible(true);
    }


    // Profil sayfasına git
    private void showProfilePage() {
        showNotification("Profil sayfası açıldı."); // Bildirim göster
        profilsayfasi profilpage= new profilsayfasi(null);
        profilpage.setVisible(true);
    }


    // Çıkış işlemi
 // Çıkış işlemi
    private void logout() {
        showNotification("Çıkış yapılıyor..."); // Bildirim göster
        cardLayout.show(mainPanel, "Login");
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.setVisible(true);
    }


 // Sağ üstte kısa bir bildirim gösterir
    private void showNotification(String message) {
        JDialog notification = new JDialog(frmEnvosale, false);
        notification.setUndecorated(true);
        notification.setSize(300, 50);
        notification.getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 153)); // Açık sarı arkaplan
        label.setForeground(Color.BLACK);

        notification.getContentPane().add(label, BorderLayout.CENTER);

        // Sağ üst köşede konumlandır
        Point location = frmEnvosale.getLocationOnScreen();
        notification.setLocation(location.x + frmEnvosale.getWidth() - 320, location.y + 20);

        notification.setVisible(true);

        // 3 saniye sonra bildirimi otomatik kapat
        Timer timer = new Timer(1000, e -> notification.dispose());
        timer.setRepeats(false);
        timer.start();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UserScreen window = new UserScreen(null);
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

