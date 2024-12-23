import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/crnck/eclipse-workspace/GUI.oop/GUI.oop/src/images/invoices.png"));
        setTitle("Admin Kullanıcı Yönetimi");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ana panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));
        add(panel);

        baglantiKur();

        // Kullanıcı Listesi
        JLabel lblKullaniciListesi = new JLabel("Mevcut Kullanıcılar:");
        lblKullaniciListesi.setBounds(20, 20, 150, 25);
        lblKullaniciListesi.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblKullaniciListesi);

        model = new DefaultTableModel(new String[]{"ID", "Kullanıcı Adı", "Email", "Şifre"}, 0);
        tableKullanicilar = new JTable(model);
        JScrollPane sp = new JScrollPane(tableKullanicilar);
        sp.setBounds(20, 50, 650, 200);
        panel.add(sp);

        // Form Alanları
        addFormFields(panel);

        // Butonlar
        addButtons(panel);

        // Kullanıcı listesine tıklama işlemi
        tableKullanicilar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableKullanicilar.getSelectedRow();
                selectedRowId = (int) tableKullanicilar.getValueAt(selectedRow, 0);
                txtKullaniciAdi.setText(tableKullanicilar.getValueAt(selectedRow, 1).toString());
                txtEmail.setText(tableKullanicilar.getValueAt(selectedRow, 2).toString());
                txtSifre.setText(tableKullanicilar.getValueAt(selectedRow, 3).toString());
            }
        });

        kullaniciListesiniYukle();
    }

    private void addFormFields(JPanel panel) {
        JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
        lblKullaniciAdi.setBounds(20, 270, 100, 25);
        lblKullaniciAdi.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblKullaniciAdi);

        txtKullaniciAdi = new JTextField();
        txtKullaniciAdi.setBounds(130, 270, 200, 25);
        txtKullaniciAdi.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtKullaniciAdi);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 310, 100, 25);
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 310, 200, 25);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtEmail);

        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setBounds(20, 350, 100, 25);
        lblSifre.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblSifre);

        txtSifre = new JTextField();
        txtSifre.setBounds(130, 350, 200, 25);
        txtSifre.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtSifre);
    }

    private void addButtons(JPanel panel) {
        btnEkle = new JButton("Kullanıcı Ekle");
        btnEkle.setBounds(350, 270, 150, 30);
        btnEkle.setFont(new Font("Arial", Font.BOLD, 12));
        btnEkle.setBackground(new Color(85, 204, 0)); // Yeşil renk
        btnEkle.setForeground(Color.WHITE);
        btnEkle.addActionListener(e -> kullaniciEkle());
        panel.add(btnEkle);

        btnGuncelle = new JButton("Kullanıcıyı Güncelle");
        btnGuncelle.setBounds(350, 310, 150, 30);
        btnGuncelle.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuncelle.setBackground(new Color(255, 153, 0)); // Turuncu renk
        btnGuncelle.setForeground(Color.WHITE);
        btnGuncelle.addActionListener(e -> kullaniciGuncelle());
        panel.add(btnGuncelle);

        btnSil = new JButton("Kullanıcıyı Sil");
        btnSil.setBounds(350, 350, 150, 30);
        btnSil.setFont(new Font("Arial", Font.BOLD, 12));
        btnSil.setBackground(new Color(255, 69, 0)); // Kırmızı renk
        btnSil.setForeground(Color.WHITE);
        btnSil.addActionListener(e -> kullaniciSil());
        panel.add(btnSil);
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
            model.setRowCount(0); // Mevcut satırları temizle
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
