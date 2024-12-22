import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FaturaOluşturmaEkranı extends JFrame {
    private JTextField textField;

    public FaturaOluşturmaEkranı() {
        JFrame frame = new JFrame("Fatura Oluşturma");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Pencereyi ekranın ortasında aç

        JLabel invoiceLabel = new JLabel("Fatura No:");
        invoiceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        invoiceLabel.setBounds(20, 20, 100, 25);
        JTextField invoiceField = new JTextField(generateInvoiceNumber()); // Fatura numarası otomatik oluştur
        invoiceField.setEditable(false);
        invoiceField.setBounds(130, 22, 276, 36);

        JLabel dateLabel = new JLabel("Tarih:");
        dateLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        dateLabel.setBounds(20, 73, 100, 25);
        JTextField dateField = new JTextField(getCurrentDate());
        dateField.setBounds(130, 75, 276, 36);

        JLabel lblToplamTutar = new JLabel("Tutar:");
        lblToplamTutar.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblToplamTutar.setBounds(20, 121, 100, 36);
        textField = new JTextField(20);
        textField.setBounds(130, 121, 276, 36);

        JButton btnSave = new JButton("KAYDET");
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnSave.setBounds(297, 167, 109, 31);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInvoice(invoiceField.getText(), dateField.getText(), textField.getText());
            }
        });

        JButton btnPrint = new JButton("YAZDIR");
        btnPrint.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnPrint.setBounds(297, 208, 109, 31);

        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(invoiceLabel);
        frame.getContentPane().add(invoiceField);
        frame.getContentPane().add(dateLabel);
        frame.getContentPane().add(dateField);
        frame.getContentPane().add(lblToplamTutar);
        frame.getContentPane().add(textField);
        frame.getContentPane().add(btnSave);
        frame.getContentPane().add(btnPrint);

        frame.setVisible(true);
    }

    private String generateInvoiceNumber() {
        Random random = new Random();
        return "INV-" + (100000 + random.nextInt(900000));
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    private void saveInvoice(String numara, String tarih, String tutar) {
        String url = "jdbc:mysql://localhost:3306/user_management"; // Veritabanı URL'sini değiştirin
        String user = "root"; // MySQL kullanıcı adı
        String password = "*Crn123.*"; // MySQL şifresi

        String query = "INSERT INTO invoices (numara, tarih, tutar) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, numara);
            preparedStatement.setString(2, tarih); 
            preparedStatement.setString(3, tutar);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Fatura başarıyla kaydedildi!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fatura kaydedilirken hata oluştu: " + e.getMessage());
        }
    }
 
    public static void main(String[] args) {
        new FaturaOluşturmaEkranı();
    }
}
