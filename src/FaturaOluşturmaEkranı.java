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

    public class  FaturaOluşturmaEkranı extends JFrame {
    private JTextField tutarField;
    private JTextField dateField;

    public FaturaOluşturmaEkranı() {
        setTitle("Fatura Oluşturma");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Header
        JLabel headerLabel = new JLabel("Fatura Oluşturma");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(54, 54, 54));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);

        // Invoice Number
        JLabel invoiceLabel = new JLabel("Fatura No:");
        invoiceLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(invoiceLabel, gbc);

        JTextField invoiceField = new JTextField(generateInvoiceNumber());
        invoiceField.setEditable(false);
        invoiceField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(invoiceField, gbc);

        // Date
        JLabel dateLabel = new JLabel("Tarih:");
        dateLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(dateLabel, gbc);

        dateField = new JTextField(getCurrentDate());
        dateField.setEditable(true); // Tarih değiştirilebilir
        dateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(dateField, gbc);

        // Total Amount
        JLabel tutarLabel = new JLabel("Tutar:");
        tutarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(tutarLabel, gbc);

        tutarField = new JTextField();
        tutarField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(tutarField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton saveButton = new JButton("Kaydet");
        saveButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        saveButton.setBackground(new Color(173, 216, 230));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tutarField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(FaturaOluşturmaEkranı.this, "Tutar alanı boş bırakılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                } else {
                    saveInvoice(invoiceField.getText(), dateField.getText(), tutarField.getText());
                }
            }
        });
        buttonPanel.add(saveButton);

        JButton printButton = new JButton("Yazdır");
        printButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        printButton.setBackground(new Color(144, 238, 144));
        printButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Yazdırma özelliği henüz eklenmedi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE));
        buttonPanel.add(printButton);

        // Add Panels to Frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
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
        String url = "jdbc:mysql://localhost:3306/user_management"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "*Crn123.*"; // Update with your database password

        String query = "INSERT INTO invoices (numara, tarih, tutar) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, numara);
            preparedStatement.setString(2, tarih);
            preparedStatement.setString(3, tutar);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Fatura başarıyla kaydedildi!", "Başarı", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fatura kaydedilirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FaturaOluşturmaEkranı::new);
    }
}
