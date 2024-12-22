import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class KullanıcıYönetimiadmin extends JFrame {
    private JTable tableKullanicilar;
    private DefaultTableModel model;
    private JTextField txtKullaniciAdi, txtEmail, txtSifre;
    private JButton btnEkle, btnGuncelle, btnSil;
    private Connection conn; 
    private int selectedRowId = -1;

    public KullanıcıYönetimiadmin() {
        setTitle("Admin Kullanıcı Yönetimi");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        baglantiKur();

        // Kullanıcı Listesi
        JLabel lblKullaniciListesi = new JLabel("Mevcut Kullanıcılar:");
        lblKullaniciListesi.setBounds(20, 20, 150, 25);
        add(lblKullaniciListesi);

        model = new DefaultTableModel(new String[]{"ID", "Kullanıcı Adı", "Email", "Şifre"}, 0);
        tableKullanicilar = new JTable(model);
        JScrollPane sp = new JScrollPane(tableKullanicilar);
        sp.setBounds(20, 50, 650, 200);
        add(sp);

        // Form Alanları
        JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
        lblKullaniciAdi.setBounds(20, 270, 100, 25);
        add(lblKullaniciAdi);

        txtKullaniciAdi = new JTextField();
        txtKullaniciAdi.setBounds(130, 270, 150, 25);
        add(txtKullaniciAdi);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 310, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 310, 150, 25);
        add(txtEmail);

        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setBounds(20, 350, 100, 25);
        add(lblSifre);

        txtSifre = new JTextField();
        txtSifre.setBounds(130, 350, 150, 25);
        add(txtSifre);

        // Butonlar
        btnEkle = new JButton("Kullanıcı Ekle");
        btnEkle.setBounds(300, 270, 150, 30);
        add(btnEkle);

        btnGuncelle = new JButton("Kullanıcıyı Güncelle");
        btnGuncelle.setBounds(300, 310, 150, 30);
        add(btnGuncelle);

        btnSil = new JButton("Kullanıcıyı Sil");
        btnSil.setBounds(300, 350, 150, 30);
        add(btnSil);

        tableKullanicilar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableKullanicilar.getSelectedRow();
                selectedRowId = (int) tableKullanicilar.getValueAt(selectedRow, 0);
                txtKullaniciAdi.setText(tableKullanicilar.getValueAt(selectedRow, 1).toString());
                txtEmail.setText(tableKullanicilar.getValueAt(selectedRow, 2).toString());
                txtSifre.setText(tableKullanicilar.getValueAt(selectedRow, 3).toString());
            }
        });

        btnEkle.addActionListener(e -> kullaniciEkle());
        btnGuncelle.addActionListener(e -> kullaniciGuncelle());
        btnSil.addActionListener(e -> kullaniciSil());

        kullaniciListesiniYukle();
    }
 
    private void baglantiKur() { 
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/user_management", "root", "*Crn123.*");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı Bağlantısı Hatası!");
        } 
    }

    private void kullaniciListesiniYukle() {
        try {
            model.setRowCount(0); 
            String sql = "SELECT id, username, email, password FROM test_users WHERE role = 'kullanıcı'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Kullanıcı listesi yüklenirken bir hata oluştu!");
        }
    }


    private void kullaniciEkle() {
        try {
            String kullaniciAdi = txtKullaniciAdi.getText();
            String email = txtEmail.getText();
            String sifre = txtSifre.getText();

            if (kullaniciAdi.isEmpty() || email.isEmpty() || sifre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tüm alanları doldurunuz!");
                return;
            }

            String sql = "INSERT INTO test_users (username, password, role, email) VALUES (?, ?, 'kullanıcı', ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kullaniciAdi);
            pst.setString(2, sifre);
            pst.setString(3, email);
            pst.executeUpdate();

            kullaniciListesiniYukle();
            temizle();
            JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla eklendi!");
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Bu kullanıcı adı veya email zaten kullanılıyor!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Kullanıcı eklenirken bir hata oluştu!");
        }
    }


    private void kullaniciGuncelle() {
        try {
            if (selectedRowId == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen bir kullanıcı seçin!");
                return;
            }

            String kullaniciAdi = txtKullaniciAdi.getText();
            String email = txtEmail.getText();
            String sifre = txtSifre.getText();

            if (kullaniciAdi.isEmpty() || email.isEmpty() || sifre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tüm alanları doldurunuz!");
                return;
            }

            String sql = "UPDATE test_users SET username = ?, password = ?, email = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kullaniciAdi);
            pst.setString(2, sifre);
            pst.setString(3, email);
            pst.setInt(4, selectedRowId);
            pst.executeUpdate();

            kullaniciListesiniYukle();
            temizle();
            JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla güncellendi!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Kullanıcı güncellenirken bir hata oluştu!");
        }
    }

    private void kullaniciSil() {
        try {
            if (selectedRowId == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen bir kullanıcı seçin!");
                return;
            }

            String sql = "DELETE FROM test_users WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedRowId);
            pst.executeUpdate();

            kullaniciListesiniYukle();
            temizle();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    private void temizle() {
        txtKullaniciAdi.setText("");
        txtEmail.setText("");
        txtSifre.setText("");
        selectedRowId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KullanıcıYönetimiadmin().setVisible(true));
    }
}
