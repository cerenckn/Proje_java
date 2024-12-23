import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserScreen {
    private String username;
    private JFrame frmEnvosale;
    private Connection connection;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel profilPage;

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

    public UserScreen() {
        connectToDatabase();
        initialize();
    }

    public UserScreen(String username) {
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

        JMenuBar menuBar = new JMenuBar();
        frmEnvosale.setJMenuBar(menuBar);

        JMenu userMenu = new JMenu("Kullanıcı Paneli");
        userMenu.setFont(new Font("Roboto", Font.BOLD, 14));
        menuBar.add(userMenu);

        // Top Panel with updated style
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(800, 100));
        topPanel.setBackground(new Color(0, 51, 102)); // Dark Blue
        frmEnvosale.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel lblWelcome = new JLabel();
        lblWelcome.setFont(new Font("Roboto", Font.BOLD, 18));
        lblWelcome.setHorizontalAlignment(SwingConstants.LEFT);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        lblWelcome.setText(getWelcomeText());
        topPanel.add(lblWelcome, BorderLayout.WEST);

        // Side menu panel using GroupLayout
        JPanel sideMenu = new JPanel();
        sideMenu.setBackground(new Color(255, 255, 255)); // White background
        GroupLayout sideMenuLayout = new GroupLayout(sideMenu);
        sideMenu.setLayout(sideMenuLayout);

        JButton btnMakeSale = createStyledButton("Satış Yap", new Color(173, 216, 230), e -> showMakeSale());
        JButton btnCreateInvoice = createStyledButton("Fatura Oluştur", new Color(204, 204, 153), e -> showCreateInvoice());
        JButton btnProfile = createStyledButton("Profil", new Color(255, 204, 204), e -> showProfilePage());
        JButton btnExit = createStyledButton("Çıkış Yap", new Color(255, 255, 204), e -> logout());

        // Setting GroupLayout for the sideMenu panel
        sideMenuLayout.setAutoCreateGaps(true);
        sideMenuLayout.setAutoCreateContainerGaps(true);

        sideMenuLayout.setHorizontalGroup(
            sideMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(btnMakeSale, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCreateInvoice, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProfile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        sideMenuLayout.setVerticalGroup(
            sideMenuLayout.createSequentialGroup()
                .addComponent(btnMakeSale)
                .addComponent(btnCreateInvoice)
                .addComponent(btnProfile)
                .addComponent(btnExit)
        );

        sideMenu.setPreferredSize(new Dimension(200, 600));  // Set a fixed width for the side menu
        frmEnvosale.getContentPane().add(sideMenu, BorderLayout.WEST);

        // Main content area for opened pages (Satış, Fatura, Profil, etc.)
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Initialize pages and add to mainPanel
        profilPage = new JPanel();
        profilPage.setBackground(Color.WHITE); // Set a default background color for the profile page
        mainPanel.add(profilPage, "Profile");

        // Add mainPanel (with pages) to the center of the frame
        frmEnvosale.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Set background image (centered)
        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(80, 80, 200, 368);
        ImageIcon icon = new ImageIcon("C:/Users/crnck/eclipse-workspace/GUI.oop/GUI.oop/src/images/invoices.png"); // Path to your image
        lblNewLabel.setIcon(icon);

        // Center the image using a layout manager (using BorderLayout for simplicity)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(lblNewLabel, BorderLayout.CENTER);
        frmEnvosale.getContentPane().add(imagePanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(150, 50)); 
        button.addActionListener(listener);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private String getWelcomeText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String date = sdf.format(new Date());
        return "Hoş geldiniz,"+ username+ ".                                                                              Bugün: " + date;
    }

    private void showMakeSale() {
        showNotification("Satış ekranı açıldı.");
        SatısEkranıUser satisEkrani = new SatısEkranıUser();
        satisEkrani.setVisible(true);
    }

    private void showCreateInvoice() {
        showNotification("Fatura oluşturma ekranı açıldı.");
        FaturaOluşturmaEkranı faturaEkrani = new FaturaOluşturmaEkranı();
        faturaEkrani.setVisible(true);
    }

    private void showProfilePage() {
        showNotification("Profil sayfası açıldı.");
        profilsayfasi profilpage = new profilsayfasi(username);
        profilpage.setVisible(true);
    }

    private void logout() {
        showNotification("Çıkış yapılıyor...");

        frmEnvosale.dispose();
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.setVisible(true);
    }

    private void showNotification(String message) {
        JDialog notification = new JDialog(frmEnvosale, false);
        notification.setUndecorated(true);
        notification.setSize(300, 50);
        notification.getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 153)); // Light yellow background
        label.setForeground(Color.BLACK);

        notification.getContentPane().add(label, BorderLayout.CENTER);

        Point location = frmEnvosale.getLocationOnScreen();
        notification.setLocation(location.x + frmEnvosale.getWidth() - 320, location.y + 20);

        notification.setVisible(true);

        Timer timer = new Timer(3000, e -> notification.dispose());
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
        frmEnvosale.setVisible(b);
    }
}
