import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class adminurunYonetimi extends JFrame {
    private JTextField txtUrunAdi, txtFiyat, txtStok;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;
    private int selectedRowId = -1;

    public adminurunYonetimi() {
        setTitle("Ürün Yönetimi Ekranı");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Bağlantı kur
        baglantiKur();

        // Tema ve Renkler
        getContentPane().setBackground(new Color(240, 240, 250));
        UIManager.put("Button.background", new Color(170, 205, 250));
        UIManager.put("Button.foreground", Color.DARK_GRAY);
        UIManager.put("Panel.background", new Color(220, 230, 250));
        UIManager.put("Label.foreground", Color.DARK_GRAY);
        UIManager.put("Table.background", new Color(240, 240, 250));
        UIManager.put("Table.foreground", Color.DARK_GRAY);
        UIManager.put("Table.gridColor", new Color(200, 200, 230));
        UIManager.put("TableHeader.background", new Color(170, 205, 250));
        UIManager.put("TableHeader.foreground", Color.DARK_GRAY);

        // Üst Panel (Form Alanı)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(170, 205, 250)), "Ürün Bilgileri", 0, 0, new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUrunAdi = new JLabel("Ürün Adı:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUrunAdi, gbc);

        txtUrunAdi = new JTextField();
        gbc.gridx = 1;
        formPanel.add(txtUrunAdi, gbc);

        JLabel lblFiyat = new JLabel("Fiyat:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblFiyat, gbc);

        txtFiyat = new JTextField();
        gbc.gridx = 1;
        formPanel.add(txtFiyat, gbc);

        JLabel lblStok = new JLabel("Stok Miktarı:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblStok, gbc);

        txtStok = new JTextField();
        gbc.gridx = 1;
        formPanel.add(txtStok, gbc);

        JButton btnEkle = new JButton("Yeni Ürün Ekle");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(btnEkle, gbc);

        JButton btnGuncelle = new JButton("Ürünü Düzenle");
        gbc.gridx = 1;
        formPanel.add(btnGuncelle, gbc);

        JButton btnSil = new JButton("Ürünü Sil");
        gbc.gridx = 2;
        formPanel.add(btnSil, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Alt Panel (Tablo)
        model = new DefaultTableModel(new String[]{"ID", "Ürün Adı", "Fiyat", "Stok"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(170, 205, 250)));
        add(sp, BorderLayout.CENTER);

        // Buton Olayları
        btnEkle.addActionListener(e -> urunEkle());
        btnGuncelle.addActionListener(e -> urunGuncelle());
        btnSil.addActionListener(e -> urunSil());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                selectedRowId = (int) table.getValueAt(row, 0);
                txtUrunAdi.setText(table.getValueAt(row, 1).toString());
                txtFiyat.setText(table.getValueAt(row, 2).toString());
                txtStok.setText(table.getValueAt(row, 3).toString());
            }
        });

        urunleriListele();
    }

    private void baglantiKur() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/user_management", "root", "*Crn123.*");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı Bağlantı Hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void urunEkle() {
        try {
            String urunAdi = txtUrunAdi.getText().trim();
            double fiyat = Double.parseDouble(txtFiyat.getText().trim());
            int stok = Integer.parseInt(txtStok.getText().trim());

            if (urunAdi.isEmpty() || fiyat <= 0 || stok < 0) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bilgileri girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO urunlist (urun_adi, fiyat, stok) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, urunAdi);
            pst.setDouble(2, fiyat);
            pst.setInt(3, stok);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!");
            urunleriListele();
            temizle();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen fiyat ve stok için geçerli sayılar girin!", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void urunGuncelle() {
        if (selectedRowId == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen düzenlemek için bir ürün seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String urunAdi = txtUrunAdi.getText().trim();
            double fiyat = Double.parseDouble(txtFiyat.getText().trim());
            int stok = Integer.parseInt(txtStok.getText().trim());

            String sql = "UPDATE urunlist SET urun_adi = ?, fiyat = ?, stok = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, urunAdi);
            pst.setDouble(2, fiyat);
            pst.setInt(3, stok);
            pst.setInt(4, selectedRowId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün başarıyla güncellendi!");
            urunleriListele();
            temizle();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen fiyat ve stok için geçerli sayılar girin!", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void urunSil() {
        if (selectedRowId == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir ürün seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "DELETE FROM urunlist WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedRowId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün başarıyla silindi!");
            urunleriListele();
            temizle();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void urunleriListele() {
        try {
            model.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM urunlist");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("urun_adi"), rs.getDouble("fiyat"), rs.getInt("stok")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı Hatası: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void temizle() {
        txtUrunAdi.setText("");
        txtFiyat.setText("");
        txtStok.setText("");
        selectedRowId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new adminurunYonetimi().setVisible(true));
    }
}
