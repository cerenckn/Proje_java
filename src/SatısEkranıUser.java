import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.sql.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SatısEkranıUser extends JFrame {
    private JTable tableUrunler, tableSepet;
    private DefaultTableModel modelUrunler, modelSepet;
    private JTextField txtAdet, txtToplamFiyat, txtSearch;
    private JButton btnSepeteEkle, btnSatisYap, btnSepetiTemizle, btnUrunCikar;
    private Connection conn;

    public SatısEkranıUser() {
        setTitle("Satış Ekranı");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(255, 253, 250));  // Soft pastel background

        // Initialize connection and models
        initializeConnection();
        initializeTableModels();

        // Initialize UI components
        initializeComponents();

        // Load available products into the table
        loadProducts();
    }

    private void initializeConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/user_management", "root", "*Crn123.*");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableModels() {
        modelUrunler = new DefaultTableModel(new String[]{"ID", "Ürün Adı", "Fiyat", "Stok"}, 0);
        modelSepet = new DefaultTableModel(new String[]{"Ürün ID", "Ürün Adı", "Adet", "Fiyat", "Toplam"}, 0);
    }

    private void initializeComponents() {
        // Labels and Tables
        JLabel lblUrunler = createLabel("Stoktaki Ürünler:", 20, 20, new Color(85, 98, 112)); // Soft grayish color
        getContentPane().add(lblUrunler);

        tableUrunler = new JTable(modelUrunler);
        tableUrunler.setBackground(new Color(240, 240, 240)); // Light pastel gray
        tableUrunler.setForeground(new Color(33, 37, 41)); // Dark gray text
        tableUrunler.setSelectionBackground(new Color(184, 205, 255)); // Light blue selection
        JScrollPane spUrunler = new JScrollPane(tableUrunler);
        spUrunler.setBounds(10, 61, 400, 200);
        getContentPane().add(spUrunler);

        JLabel lblSepet = createLabel("Sepetteki Ürünler:", 450, 20, new Color(85, 98, 112)); // Soft grayish color
        getContentPane().add(lblSepet);

        tableSepet = new JTable(modelSepet);
        tableSepet.setBackground(new Color(240, 240, 240)); // Light pastel gray
        tableSepet.setForeground(new Color(33, 37, 41)); // Dark gray text
        tableSepet.setSelectionBackground(new Color(184, 205, 255)); // Light blue selection
        JScrollPane spSepet = new JScrollPane(tableSepet);
        spSepet.setBounds(450, 61, 400, 200);
        getContentPane().add(spSepet);

        // Adet and Toplam Fiyat Text Fields
        JLabel lblAdet = createLabel("Adet:", 20, 302, new Color(85, 98, 112)); // Soft grayish color
        getContentPane().add(lblAdet);

        txtAdet = new JTextField();
        txtAdet.setBounds(70, 303, 100, 25);
        txtAdet.setBackground(new Color(255, 255, 255)); // White background
        txtAdet.setForeground(new Color(33, 37, 41)); // Dark gray text
        txtAdet.setBorder(BorderFactory.createLineBorder(new Color(186, 210, 234))); // Soft blue border
        getContentPane().add(txtAdet);

        JLabel lblToplamFiyat = createLabel("Toplam Fiyat:", 450, 280, new Color(85, 98, 112)); // Soft grayish color
        getContentPane().add(lblToplamFiyat);

        txtToplamFiyat = new JTextField();
        txtToplamFiyat.setEditable(false);
        txtToplamFiyat.setBounds(580, 280, 120, 25);
        txtToplamFiyat.setBackground(new Color(255, 255, 255)); // White background
        txtToplamFiyat.setForeground(new Color(33, 37, 41)); // Dark gray text
        txtToplamFiyat.setBorder(BorderFactory.createLineBorder(new Color(186, 210, 234))); // Soft blue border
        getContentPane().add(txtToplamFiyat);

        // Buttons with hover effects and styled backgrounds
        btnSepeteEkle = createStyledButton("Sepete Ekle", 20, 350, new Color(52, 152, 219), new Color(41, 128, 185)); // Pastel blue
        getContentPane().add(btnSepeteEkle);

        btnUrunCikar = createStyledButton("Ürün Çıkar", 150, 350, new Color(243, 156, 18), new Color(211, 84, 0)); // Pastel orange
        getContentPane().add(btnUrunCikar);

        btnSepetiTemizle = createStyledButton("Sepeti Temizle", 450, 320, new Color(46, 204, 113), new Color(39, 174, 96)); // Pastel green
        getContentPane().add(btnSepetiTemizle);

        btnSatisYap = createStyledButton("Satış Yap", 620, 320, new Color(231, 76, 60), new Color(192, 57, 43)); // Pastel red
        getContentPane().add(btnSatisYap);

        // Search text field with padding and styling
        txtSearch = new JTextField();
        txtSearch.setBounds(160, 26, 200, 25);
        txtSearch.setBackground(new Color(230, 230, 250)); // White background
        txtSearch.setForeground(new Color(128, 128, 128)); // Dark gray text
        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(186, 210, 234))); // Soft blue border
        getContentPane().add(txtSearch);
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterProducts(txtSearch.getText());
            }
        });

        // Add action listeners to buttons
        btnSepeteEkle.addActionListener(e -> addToCart());
        btnUrunCikar.addActionListener(e -> removeFromCart());
        btnSepetiTemizle.addActionListener(e -> clearCart());
        btnSatisYap.addActionListener(e -> completeSale());
    }

    private JLabel createLabel(String text, int x, int y, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Tahoma", Font.BOLD, 15));
        label.setBounds(x, y, 150, 25);
        label.setForeground(color);
        return label;
    }

    private JButton createStyledButton(String text, int x, int y, Color defaultColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 30);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(defaultColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
        });
        return button;
    }

    private void loadProducts() {
        try {
            modelUrunler.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM urunlist WHERE stok > 0");
            while (rs.next()) {
                modelUrunler.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("urun_adi"),
                        rs.getDouble("fiyat"),
                        rs.getInt("stok")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToCart() {
        int selectedRow = tableUrunler.getSelectedRow();
        if (selectedRow == -1 || txtAdet.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen ürün seçin ve adet girin!");
            return;
        }

        try {
            int urunId = (int) tableUrunler.getValueAt(selectedRow, 0);
            String urunAdi = tableUrunler.getValueAt(selectedRow, 1).toString();
            double fiyat = Double.parseDouble(tableUrunler.getValueAt(selectedRow, 2).toString());
            int adet = Integer.parseInt(txtAdet.getText());
            int mevcutStok = (int) tableUrunler.getValueAt(selectedRow, 3);

            if (adet > mevcutStok) {
                JOptionPane.showMessageDialog(this, "Stokta yeterli ürün yok!");
                return;
            }

            double toplam = fiyat * adet;
            modelSepet.addRow(new Object[]{urunId, urunAdi, adet, fiyat, toplam});
            txtToplamFiyat.setText(String.valueOf(calculateTotal()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir sayı girin!");
        }
    }

    private void removeFromCart() {
        int selectedRow = tableSepet.getSelectedRow();
        if (selectedRow != -1) {
            modelSepet.removeRow(selectedRow);
            txtToplamFiyat.setText(String.valueOf(calculateTotal()));
        }
    }

    private void clearCart() {
        modelSepet.setRowCount(0);
        txtToplamFiyat.setText("0");
    }

    private void completeSale() {
        try {
            conn.setAutoCommit(false);
            double totalAmount = calculateTotal();

            String invoiceSql = "INSERT INTO invoices (numara, tarih, tutar) VALUES (?, ?, ?)";
            try (PreparedStatement invoiceStmt = conn.prepareStatement(invoiceSql, Statement.RETURN_GENERATED_KEYS)) {
                invoiceStmt.setString(1, "INV" + System.currentTimeMillis());
                invoiceStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                invoiceStmt.setDouble(3, totalAmount);
                invoiceStmt.executeUpdate();

                ResultSet rs = invoiceStmt.getGeneratedKeys();
                if (rs.next()) {
                    int invoiceId = rs.getInt(1);
                    for (int i = 0; i < modelSepet.getRowCount(); i++) {
                        int urunId = (int) modelSepet.getValueAt(i, 0);
                        String urunAdi = modelSepet.getValueAt(i, 1).toString();
                        int adet = (int) modelSepet.getValueAt(i, 2);
                        double toplam = (double) modelSepet.getValueAt(i, 4);

                        String saleSql = "INSERT INTO satislar (urun_id, urun_adi, adet, toplam_fiyat, satis_tarihi) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement saleStmt = conn.prepareStatement(saleSql)) {
                            saleStmt.setInt(1, urunId);
                            saleStmt.setString(2, urunAdi);
                            saleStmt.setInt(3, adet);
                            saleStmt.setDouble(4, toplam);
                            saleStmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
                            saleStmt.executeUpdate();
                        }

                        String stockSql = "UPDATE urunlist SET stok = stok - ? WHERE id = ?";
                        try (PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                            stockStmt.setInt(1, adet);
                            stockStmt.setInt(2, urunId);
                            stockStmt.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Satış ve fatura başarıyla tamamlandı!");
            clearCart();
            loadProducts();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Satış sırasında hata oluştu: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private double calculateTotal() {
        double total = 0;
        for (int i = 0; i < modelSepet.getRowCount(); i++) {
            total += (double) modelSepet.getValueAt(i, 4);
        }
        return total;
    }

    private void filterProducts(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelUrunler);
        tableUrunler.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SatısEkranıUser().setVisible(true));
    }
}
