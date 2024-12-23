import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class adminScreen {

    private JFrame frmEnvosale;
    private Connection connection;
    private String username;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "*Crn123.*");
            System.out.println("Veritabanına bağlanıldı.");
            if (connection != null) {
                System.out.println("Veritabanı bağlantısı başarılı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı bağlantı hatası! Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public adminScreen() {
        connectToDatabase();
        initialize();
    }

    public adminScreen(String username) {
        this.username = username;
        connectToDatabase();
        initialize();
    }

    private void initialize() {
        frmEnvosale = new JFrame();
        frmEnvosale.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/crnck/eclipse-workspace/GUI.oop/GUI.oop/src/images/invoices.png"));
        frmEnvosale.setTitle("E-INVOSALE");
        frmEnvosale.setBounds(100, 100, 800, 600);
        frmEnvosale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmEnvosale.getContentPane().setLayout(new BorderLayout());
        frmEnvosale.getContentPane().setBackground(Color.WHITE);

        JMenuBar menuBar = new JMenuBar();
        frmEnvosale.setJMenuBar(menuBar);

        JMenu adminMenu = new JMenu("Admin Paneli");
        adminMenu.setFont(new Font("Roboto", Font.BOLD, 14));

        menuBar.add(adminMenu);


        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(200, 600)); // Yan menüyü 200px genişliğinde yapalım
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(new Color(245, 245, 245));  // Daha açık renk
        frmEnvosale.getContentPane().add(sideMenu, BorderLayout.WEST);


        createSideMenuButton(sideMenu, "Ürün Yönetimi", new Color(198, 224, 235), "urunYonetimi");
        createSideMenuButton(sideMenu, "Kullanıcı Yönetimi", new Color(255, 216, 216), "kullaniciYonetimi");
        createSideMenuButton(sideMenu, "Satış ve Fatura Raporları", new Color(255, 239, 186), "satisRaporlari");
        createSideMenuButton(sideMenu, "Profil", new Color(254, 220, 170), "profil");
        createSideMenuButton(sideMenu, "Çıkış Yap", new Color(255, 160, 145), "exit");


        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frmEnvosale.getContentPane().add(cardPanel, BorderLayout.CENTER);


        createCardPage("Satış ve Fatura Raporları Sayfası", "satisRaporlari");
        createCardPage("Ürün Yönetimi Sayfası", "urunYonetimi");
        createCardPage("Kullanıcı Yönetimi Sayfası", "kullaniciYonetimi");
        createCardPage("Profil Sayfası", "profil");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1000, 100));
        topPanel.setBackground(new Color(10, 51, 102)); // Dark Blue
        frmEnvosale.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel lblWelcome = new JLabel();
        lblWelcome.setFont(new Font("Roboto", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setHorizontalAlignment(SwingConstants.LEFT);
        lblWelcome.setText(getWelcomeText());
        topPanel.add(lblWelcome, BorderLayout.WEST);

        JLabel lblSalesSummary = new JLabel();
        lblSalesSummary.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSalesSummary.setForeground(Color.WHITE);
        lblSalesSummary.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSalesSummary.setText(getSalesSummaryText());
        topPanel.add(lblSalesSummary, BorderLayout.CENTER);
    }

    private void createSideMenuButton(JPanel sideMenu, String text, Color bgColor, String targetPage) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding ekleyelim
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //buton üzerine gelince renk değişimi
        button.addMouseListener(new MouseAdapter() {
 
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        // Butona tıklanınca gösterilecek ekran
        button.addActionListener(e -> {
            if ("exit".equals(targetPage)) {
                int confirm = JOptionPane.showConfirmDialog(null, "Çıkmak istediğinizden emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            } else {
                showNotification("Ekran açılıyor: " + text, targetPage);
            }
        });
        sideMenu.add(button);
        sideMenu.add(Box.createRigidArea(new Dimension(0, 10))); // Butonlar arasında boşluk
    }

    private void createCardPage(String label, String name) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255)); 
        panel.add(new JLabel(label));
        cardPanel.add(panel, name);
    }

    private void showNotification(String message, String targetPanel) {
        JDialog notification = new JDialog();
        notification.setUndecorated(true);
        notification.setSize(300, 50);
        notification.getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Roboto", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(new Color(198, 224, 235)); 
        label.setForeground(Color.BLACK);

        notification.getContentPane().add(label, BorderLayout.CENTER);
        notification.setLocationRelativeTo(frmEnvosale);
        notification.setVisible(true);

        Timer timer = new Timer(3000, e -> {
            notification.dispose();

            switch (targetPanel) {
                case "urunYonetimi":
                    adminurunYonetimi adminurun = new adminurunYonetimi();
                    adminurun.setVisible(true);
                    break;
                case "kullaniciYonetimi":
                    KullanıcıYönetimiadmin kullanıcıadmin = new KullanıcıYönetimiadmin();
                    kullanıcıadmin.setVisible(true);
                    break;
                case "satisRaporlari":
                    SalesReport satısrapor = new SalesReport();
                    satısrapor.setVisible(true);
                    break;
                case "profil":
                    profilsayfasi profile = new profilsayfasi(username);
                    profile.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Geçersiz ekran!", "Hata", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private String getWelcomeText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String date = sdf.format(new Date());
        return "Hoş geldiniz, " + username + ". Bugün: " + date;
    }

    private String getSalesSummaryText() {
        double todaySales = getTodaySales();
        int totalStock = getTotalStock();
        return "Bugünkü satış toplamı: " + todaySales + " TL | Toplam stok: " + totalStock;
    }

    private double getTodaySales() {
        double todaySales = 0;
        String query = "SELECT SUM(toplam_fiyat) AS total_sales FROM satislar WHERE DATE(satis_tarihi) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                todaySales = rs.getDouble("total_sales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Bugünkü satışları alırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return todaySales;
    }

    private int getTotalStock() {
        int totalStock = 0;
        String query = "SELECT SUM(stok) AS total_stock FROM urunlist";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalStock = rs.getInt("total_stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Toplam stoğu alırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return totalStock;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                adminScreen window = new adminScreen("Admin");
                window.frmEnvosale.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setVisible(boolean b) {
        frmEnvosale.setVisible(b);
    }
}
