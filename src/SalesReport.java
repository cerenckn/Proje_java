import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SalesReport extends JFrame {
    private JComboBox<String> reportTypeComboBox;
    private JTable salesTable;
    private DefaultTableModel tableModel;

    public SalesReport() {
        setTitle("Satış Raporları");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the frame

        // Set a professional font for the GUI
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        // Üst Panel: Seçenekler
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // Align to left with padding
        topPanel.setBackground(Color.WHITE);
        JLabel reportTypeLabel = new JLabel("Rapor Türü:");
        reportTypeLabel.setFont(font);
        reportTypeComboBox = new JComboBox<>(new String[]{"Günlük", "Haftalık", "Aylık"});
        reportTypeComboBox.setFont(font);
        JButton fetchButton = new JButton("Raporu Getir");
        fetchButton.setFont(font);
        fetchButton.setBackground(new Color(0, 123, 255));
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setFocusPainted(false);
        fetchButton.setBorderPainted(false);
        fetchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topPanel.add(reportTypeLabel);
        topPanel.add(reportTypeComboBox);
        topPanel.add(fetchButton);
        add(topPanel, BorderLayout.NORTH);

        // Orta Panel: Tablo
        tableModel = new DefaultTableModel();
        salesTable = new JTable(tableModel);
        salesTable.setFont(font);
        salesTable.setRowHeight(25);
        salesTable.setGridColor(Color.LIGHT_GRAY);
        salesTable.setSelectionBackground(new Color(232, 240, 254));
        salesTable.setSelectionForeground(Color.DARK_GRAY);
        JScrollPane tableScrollPane = new JScrollPane(salesTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Kolonları Ayarla
        tableModel.setColumnIdentifiers(new String[]{"Satış ID", "Ürün ID", "Ürün Adı", "Adet", "Toplam Fiyat", "Satış Tarihi", "Fatura No", "Fatura Tarihi", "Fatura Tutarı"});

        // Buton Dinleyici
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchReportData();
            }
        });
    }

    private void fetchReportData() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String query = "";
        
        if ("Günlük".equals(reportType)) {
            query = """
                    SELECT s.id AS satis_id, s.urun_id, s.urun_adi, s.adet, s.toplam_fiyat, s.satis_tarihi,
                           i.numara AS fatura_no, i.tarih AS fatura_tarihi, i.tutar AS fatura_tutari
                    FROM satislar s
                    LEFT JOIN invoices i ON s.id = i.id
                    WHERE DATE(s.satis_tarihi) = CURDATE()
                    """;
        } else if ("Haftalık".equals(reportType)) {
            query = """
                    SELECT s.id AS satis_id, s.urun_id, s.urun_adi, s.adet, s.toplam_fiyat, s.satis_tarihi,
                           i.numara AS fatura_no, i.tarih AS fatura_tarihi, i.tutar AS fatura_tutari
                    FROM satislar s
                    LEFT JOIN invoices i ON s.id = i.id
                    WHERE YEAR(s.satis_tarihi) = YEAR(CURDATE()) 
                      AND WEEK(s.satis_tarihi, 1) = WEEK(CURDATE(), 1)
                    """;
        } else if ("Aylık".equals(reportType)) {
            query = """
                    SELECT s.id AS satis_id, s.urun_id, s.urun_adi, s.adet, s.toplam_fiyat, s.satis_tarihi,
                           i.numara AS fatura_no, i.tarih AS fatura_tarihi, i.tutar AS fatura_tutari
                    FROM satislar s
                    LEFT JOIN invoices i ON s.id = i.id
                    WHERE MONTH(s.satis_tarihi) = MONTH(CURDATE()) 
                      AND YEAR(s.satis_tarihi) = YEAR(CURDATE())
                    """;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // Eski verileri temizle
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("satis_id"),
                        rs.getInt("urun_id"),
                        rs.getString("urun_adi"),
                        rs.getInt("adet"),
                        rs.getDouble("toplam_fiyat"),
                        rs.getDate("satis_tarihi"),
                        rs.getString("fatura_no"),
                        rs.getDate("fatura_tarihi"),
                        rs.getDouble("fatura_tutari")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veriler getirilemedi: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SalesReport gui = new SalesReport();
            gui.setVisible(true);
        });
    }
}
