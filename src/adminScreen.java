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
        frmEnvosale.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\path\\to\\your\\icon.png"));
        frmEnvosale.setTitle("E-INVOSALE");
        frmEnvosale.setBounds(100, 100, 1000, 700);
        frmEnvosale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frmEnvosale.setJMenuBar(menuBar);

        JMenu adminMenu = new JMenu("Admin Paneli");
        adminMenu.setBackground(new Color(245, 245, 245));
        adminMenu.setFont(new Font("Roboto", Font.BOLD, 14));
        menuBar.add(adminMenu);

        frmEnvosale.getContentPane().setLayout(new BorderLayout());

        // Sol Menü
        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(220, 600));
        sideMenu.setLayout(null);
        sideMenu.setBackground(new Color(248, 248, 248));
        frmEnvosale.getContentPane().add(sideMenu, BorderLayout.WEST);

        // Butonlar
        createSideMenuButton(sideMenu, "Ürün Yönetimi", 10, 90, new Color(198, 224, 235), "urunYonetimi");
        createSideMenuButton(sideMenu, "Kullanıcı Yönetimi", 100, 90, new Color(255, 216, 216), "kullaniciYonetimi");
        createSideMenuButton(sideMenu, "Satış ve Fatura Raporları", 190, 90, new Color(255, 239, 186), "satisRaporlari");
        createSideMenuButton(sideMenu, "Profil", 280, 90, new Color(254, 220, 170), "profil");
        createSideMenuButton(sideMenu, "Çıkış Yap", 370, 90, new Color(255, 160, 145), "exit");

        // CardLayout için Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frmEnvosale.getContentPane().add(cardPanel, BorderLayout.CENTER);

        // Sayfalar
        JPanel satisRaporlariPanel = new JPanel();
        satisRaporlariPanel.setBackground(new Color(255, 255, 255));
        satisRaporlariPanel.add(new JLabel("Satış ve Fatura Raporları Sayfası"));
        cardPanel.add(satisRaporlariPanel, "satisRaporlari");

        JPanel urunYonetimiPanel = new JPanel();
        urunYonetimiPanel.setBackground(new Color(255, 255, 255));
        urunYonetimiPanel.add(new JLabel("Ürün Yönetimi Sayfası"));
        cardPanel.add(urunYonetimiPanel, "urunYonetimi");

        JPanel kullaniciYonetimiPanel = new JPanel();
        kullaniciYonetimiPanel.setBackground(new Color(255, 255, 255));
        kullaniciYonetimiPanel.add(new JLabel("Kullanıcı Yönetimi Sayfası"));
        cardPanel.add(kullaniciYonetimiPanel, "kullaniciYonetimi");

        JPanel profilPanel = new JPanel();
        profilPanel.setBackground(new Color(255, 255, 255));
        profilPanel.add(new JLabel("Profil Sayfası"));
        cardPanel.add(profilPanel, "profil");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1000, 100));
        topPanel.setBackground(new Color(198, 224, 235));
        frmEnvosale.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel lblWelcome = new JLabel();
        lblWelcome.setFont(new Font("Roboto", Font.BOLD, 16));
        lblWelcome.setForeground(Color.BLACK);
        lblWelcome.setHorizontalAlignment(SwingConstants.LEFT);
        lblWelcome.setText(getWelcomeText());
        topPanel.add(lblWelcome, BorderLayout.WEST);

        JLabel lblSalesSummary = new JLabel();
        lblSalesSummary.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblSalesSummary.setForeground(Color.BLACK);
        lblSalesSummary.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSalesSummary.setText(getSalesSummaryText());
        topPanel.add(lblSalesSummary, BorderLayout.CENTER);

        JLabel lblUsdInfo = new JLabel();
        lblUsdInfo.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblUsdInfo.setForeground(Color.BLACK);
        lblUsdInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUsdInfo.setText(""
        		+ "USD Kuru: " + getUsdExchangeRate() + " TL");
        topPanel.add(lblUsdInfo, BorderLayout.SOUTH);
    }
    // Menü butonları için yardımcı metot
    private void createSideMenuButton(JPanel sideMenu, String text, int yPosition, int height, Color bgColor, String targetPage) {
        JButton button = new JButton(text);
        button.setBounds(10, yPosition, 200, height);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Buton üzerine gelince renk değişimi
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        // Butona tıklanınca gösterilecek ekran
        button.addActionListener(e -> {
            if ("exit".equals(targetPage)) {
                int confirm = JOptionPane.showConfirmDialog(null, "Çıkmak istediğinizden emin misiniz?", "Çıkış", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0); // Uygulamayı kapatır
                }
            } else {
                showNotification("Ekran açılıyor: " + text, targetPage);
            }
        });
        sideMenu.add(button);
    }

    private void showNotification(String message, String targetPanel) {
        JDialog notification = new JDialog();
        notification.setUndecorated(true);
        notification.setSize(300, 50);
        notification.getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Roboto", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(new Color(198, 224, 235)); // Soft pastel arka plan
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
        return "Hoş geldiniz," + username + ". Bugün: " + date;
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


    private int getTotalSalesCount() {
        int totalSalesCount = 0;
        String query = "SELECT COUNT(*) AS total_sales_count FROM satislar WHERE DATE(satis_tarihi) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalSalesCount = rs.getInt("total_sales_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Bugünkü satış sayısını alırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return totalSalesCount;
    }

    private double getAverageSalesPrice() {
        double avgSalesPrice = 0;
        String query = "SELECT AVG(toplam_fiyat) AS avg_sales_price FROM satislar WHERE DATE(satis_tarihi) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                avgSalesPrice = rs.getDouble("avg_sales_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ortalama satış fiyatını alırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return avgSalesPrice;
    }

    private String getUsdExchangeRate() {
        double usdRate = 35.22; // Bu değeri manuel olarak değiştirebilirsiniz.
        return String.format("%.2f", usdRate);
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