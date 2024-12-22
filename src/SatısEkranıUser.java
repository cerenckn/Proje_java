import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;

public class SatısEkranıUser extends JFrame {
    protected static final JTextComponent txtProductName = null;
	private JTable tableUrunler, tableSepet;
    private DefaultTableModel modelUrunler, modelSepet;
    private JTextField txtAdet, txtToplamFiyat;
    private JButton btnSatisYap, btnSepetiTemizle;
    private Connection conn;

    public  SatısEkranıUser() {
    	setBackground(new Color(255, 255, 255));
    	getContentPane().setBackground(new Color(255, 255, 255));
        setTitle("Satış Ekranı");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        baglantiKur();

        // Ürün Listesi
        JLabel lblUrunler = new JLabel("Stoktaki Ürünler:");
        lblUrunler.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblUrunler.setBackground(new Color(153, 0, 51));
        lblUrunler.setBounds(20, 20, 150, 25);
        getContentPane().add(lblUrunler);

        modelUrunler = new DefaultTableModel(new String[]{"ID", "Ürün Adı", "Fiyat", "Stok"}, 0);
        tableUrunler = new JTable(modelUrunler);
        JScrollPane spUrunler = new JScrollPane(tableUrunler);
        spUrunler.setBounds(10, 61, 350, 200);
        getContentPane().add(spUrunler);

        // Sepet Listesi
        JLabel lblSepet = new JLabel("Sepetteki Ürünler:");
        lblSepet.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblSepet.setBounds(400, 20, 150, 25);
        getContentPane().add(lblSepet);

        modelSepet = new DefaultTableModel(new String[]{"Ürün Adı", "Adet", "Fiyat"}, 0);
        tableSepet = new JTable(modelSepet);
        JScrollPane spSepet = new JScrollPane(tableSepet);
        spSepet.setBounds(400, 50, 350, 200);
        getContentPane().add(spSepet);

        // Adet Girişi ve Toplam Fiyat
        JLabel lblAdet = new JLabel("Adet:");
        lblAdet.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblAdet.setBounds(20, 302, 50, 25);
        getContentPane().add(lblAdet);

        txtAdet = new JTextField();
        txtAdet.setForeground(new Color(0, 0, 0));
        txtAdet.setBackground(new Color(255, 255, 255));
        txtAdet.setBounds(139, 303, 181, 26);
        getContentPane().add(txtAdet);

        JLabel lblToplamFiyat = new JLabel("Toplam Fiyat:");
        lblToplamFiyat.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblToplamFiyat.setBounds(20, 337, 100, 25);
        getContentPane().add(lblToplamFiyat);

        txtToplamFiyat = new JTextField();
        txtToplamFiyat.setBackground(new Color(255, 255, 255));
        txtToplamFiyat.setBounds(139, 339, 120, 25);
        txtToplamFiyat.setEditable(false);
        getContentPane().add(txtToplamFiyat);

        // Butonlar
        btnSatisYap = new JButton("Satış Yap");
        btnSatisYap.setBackground(new Color(153, 204, 204));
        btnSatisYap.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnSatisYap.setForeground(SystemColor.menuText);
        btnSatisYap.setBounds(20, 394, 120, 30);
        getContentPane().add(btnSatisYap);

        btnSepetiTemizle = new JButton("Sepeti Temizle");
        btnSepetiTemizle.setBackground(new Color(204, 153, 204));
        btnSepetiTemizle.setForeground(new Color(178, 34, 34));
        btnSepetiTemizle.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnSepetiTemizle.setBounds(170, 394, 150, 30);
        getContentPane().add(btnSepetiTemizle);
         
             // Add this code in your GUI setup
        JTextField txtSearch = new JTextField(20);  // Text field for search
        txtSearch.setBounds(160, 26, 200, 25);
        getContentPane().add(txtSearch);
                // Add an ActionListener to filter products as the user types
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterUrunler(txtSearch.getText());
            }
        });
        // Olaylar
        tableUrunler.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableUrunler.getSelectedRow();
                String fiyat = tableUrunler.getValueAt(selectedRow, 2).toString();
                String adet = txtAdet.getText();
                if (!adet.isEmpty()) {
                    double toplam = Double.parseDouble(fiyat) * Integer.parseInt(adet);
                    txtToplamFiyat.setText(String.valueOf(toplam));
                }
            }
        });

        btnSatisYap.addActionListener(e -> satisYap());
        stoktakiUrunleriYukle();
        btnSepetiTemizle.addActionListener(e -> sepetiTemizle());
    }
    private void baglantiKur() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/user_management", "root", "*Crn123.*");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void stoktakiUrunleriYukle() {
        try {
            modelUrunler.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM urunlist WHERE stok > 0");
            while (rs.next()) {
                modelUrunler.addRow(new Object[]{rs.getInt("id"), rs.getString("urun_adi"), rs.getDouble("fiyat"), rs.getInt("stok")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void satisYap() {
        int selectedRow = tableUrunler.getSelectedRow();
        if (selectedRow == -1 || txtAdet.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen ürün seçin ve adet girin!");
            return;
        }

        try {
            // Verileri al
            int urunId = (int) tableUrunler.getValueAt(selectedRow, 0);
            String urunAdi = tableUrunler.getValueAt(selectedRow, 1).toString();
            double fiyat = Double.parseDouble(tableUrunler.getValueAt(selectedRow, 2).toString());
            int adet = Integer.parseInt(txtAdet.getText());
            int mevcutStok = (int) tableUrunler.getValueAt(selectedRow, 3);

            // Stok kontrolü
            if (adet > mevcutStok) {
                JOptionPane.showMessageDialog(this, "Stokta yeterli ürün bulunmamaktadır.");
                return;
            }

            // Toplam fiyat hesapla
            double toplam = fiyat * adet;

            // Veritabanı işlem başlangıcı
            conn.setAutoCommit(false);

            // Satışı veritabanına kaydet
            String sqlSatis = "INSERT INTO satislar (urun_id, urun_adi, adet, toplam_fiyat, satis_tarihi) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtSatis = conn.prepareStatement(sqlSatis)) {
                stmtSatis.setInt(1, urunId);
                stmtSatis.setString(2, urunAdi);
                stmtSatis.setInt(3, adet);
                stmtSatis.setDouble(4, toplam);
                stmtSatis.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
                stmtSatis.executeUpdate();
            }

            // Stok güncelleme
            String sqlStokGuncelle = "UPDATE urunlist SET stok = stok - ? WHERE id = ?";
            try (PreparedStatement stmtStok = conn.prepareStatement(sqlStokGuncelle)) {
                stmtStok.setInt(1, adet);
                stmtStok.setInt(2, urunId);
                stmtStok.executeUpdate();
            }

            // Veritabanı işlem tamamla
            conn.commit();

            // Sepete ekleme
            modelSepet.addRow(new Object[]{urunAdi, adet, toplam});

            // Ürün listesini yenile
            stoktakiUrunleriYukle();

            JOptionPane.showMessageDialog(this, "Satış başarıyla gerçekleştirildi!");
        } catch (SQLException e) {
            try {
                conn.rollback(); // Hata durumunda işlemi geri al
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Satış sırasında bir hata oluştu: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir sayı girin!");
        } finally {
            try {
                conn.setAutoCommit(true); // Otomatik işlem modunu geri yükle
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterUrunler(String searchText) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modelUrunler); // assuming modelUrunler is your table model
        tableUrunler.setRowSorter(sorter);

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);  // Show all rows if no search term is entered
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));  // Case-insensitive search
        }
    

    // Table click listener to update the product name and quantity
    tableUrunler.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int selectedRow = tableUrunler.getSelectedRow();
            if (selectedRow != -1) {
                // When a product is selected, display the product name in txtProductName
                String selectedProductName = tableUrunler.getValueAt(selectedRow, 1).toString();
               txtProductName.setText(selectedProductName);  // Set the selected product name
            }
        }
    });
    }
    private void sepetiTemizle() {
        modelSepet.setRowCount(0);
        txtToplamFiyat.setText("");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new  SatısEkranıUser().setVisible(true));
        
    }
} 
