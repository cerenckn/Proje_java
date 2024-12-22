import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class adminurunYonetimi extends JFrame {
    private JTextField txtUrunAdi, txtFiyat, txtStok;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;
    private int selectedRowId = -1;

    public adminurunYonetimi() {
        // Pencere Başlığı ve Boyut
        setTitle("Ürün Yönetimi Ekranı");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Bağlantı Kur
        baglantiKur();

        // GUI Öğeleri
        JLabel lblUrunAdi = new JLabel("Ürün Adı:");
        lblUrunAdi.setBounds(20, 20, 100, 25);
        add(lblUrunAdi);

        txtUrunAdi = new JTextField();
        txtUrunAdi.setBounds(120, 20, 150, 25);
        add(txtUrunAdi);

        JLabel lblFiyat = new JLabel("Fiyat:");
        lblFiyat.setBounds(20, 60, 100, 25);
        add(lblFiyat);

        txtFiyat = new JTextField();
        txtFiyat.setBounds(120, 60, 150, 25);
        add(txtFiyat);

        JLabel lblStok = new JLabel("Stok Miktarı:");
        lblStok.setBounds(20, 100, 100, 25);
        add(lblStok);

        txtStok = new JTextField();
        txtStok.setBounds(120, 100, 150, 25);
        add(txtStok);

        JButton btnEkle = new JButton("Yeni Ürün Ekle");
        btnEkle.setBounds(20, 150, 120, 25);
        add(btnEkle);

        JButton btnGuncelle = new JButton("Ürünü Düzenle");
        btnGuncelle.setBounds(150, 150, 120, 25);
        add(btnGuncelle);

        JButton btnSil = new JButton("Ürünü Sil");
        btnSil.setBounds(280, 150, 120, 25);
        add(btnSil);

        // JTable ve Model
        model = new DefaultTableModel(new String[]{"ID", "Ürün Adı", "Fiyat", "Stok"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 200, 540, 150);
        add(sp);

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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı Bağlantı Hatası!");
        }
    }

    private void urunEkle() {
        try {
            String sql = "INSERT INTO urunlist (urun_adi, fiyat, stok) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            String urunAdi = txtUrunAdi.getText();
            double fiyat = Double.parseDouble(txtFiyat.getText());
            int stok = Integer.parseInt(txtStok.getText());

            if (urunAdi.isEmpty() || fiyat <= 0 || stok < 0) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bilgileri girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pst.setString(1, urunAdi);
            pst.setDouble(2, fiyat);
            pst.setInt(3, stok);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!");
            urunleriListele();
            temizle();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen fiyat ve stok için geçerli sayılar girin!", "Hata", JOptionPane.ERROR_MESSAGE);
        }catch (SQLException e) {
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
            String sql = "UPDATE urunlist SET urun_adi = ?, fiyat = ?, stok = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            String urunAdi = txtUrunAdi.getText();
            double fiyat = Double.parseDouble(txtFiyat.getText());
            int stok = Integer.parseInt(txtStok.getText());

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
        try {
            String sql = "DELETE FROM urunlist WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedRowId);
            pst.executeUpdate();
            urunleriListele();
            temizle();
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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

