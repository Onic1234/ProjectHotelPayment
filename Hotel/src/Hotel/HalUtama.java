/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Hotel;




import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author Acer
 */
public class HalUtama extends javax.swing.JFrame {
    DefaultTableModel model = new DefaultTableModel();
    
    private void addEventListeners() {
    // Listener untuk JDateChooser Check In
        txtIn.addPropertyChangeListener("date", evt -> hitungTotalHarga());

        // Listener untuk JDateChooser Check Out
        txtOut.addPropertyChangeListener("date", evt -> hitungTotalHarga());

        // Listener untuk TextField ID Kamar
        txtIdKamar.addActionListener(evt -> hitungTotalHarga());
    }

    
    private void hitungTotalHarga() {
        try {
            // Pastikan semua input yang dibutuhkan sudah diisi
            if (txtIn.getDate() == null || txtOut.getDate() == null || txtIdKamar.getText().isEmpty()) {
                txtTotal.setText("0");
                return;
            }

            // Hitung lama menginap
            java.util.Date checkIn = txtIn.getDate();
            java.util.Date checkOut = txtOut.getDate();
            long diffInMillies = Math.abs(checkOut.getTime() - checkIn.getTime());
            long days = diffInMillies / (1000 * 60 * 60 * 24);

            // Ambil harga kamar berdasarkan ID Kamar
            String sql = "SELECT price FROM rooms WHERE id_room = ?";
            Connection conn = DatabaseConnection.configDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtIdKamar.getText());
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double hargaKamar = rs.getDouble("price");
                double totalHarga = days * hargaKamar;
                txtTotal.setText(String.valueOf(totalHarga));
            } else {
                txtTotal.setText("0");
                JOptionPane.showMessageDialog(this, "ID Kamar tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HalUtama.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghitung total harga: " + ex.getMessage());
        }
    }


     public void cari(String str){
        model = (DefaultTableModel) TableBooking.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        TableBooking.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
     
    public void search(String str){
        model = (DefaultTableModel) TableRooms.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        TableRooms.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }

     private void tampilkan_dataKamar(){
       DefaultTableModel model = new DefaultTableModel();
       model.addColumn("ID");
       model.addColumn("No Kamar");
       model.addColumn("Type Kamar");
       model.addColumn("Fasilitas");
       model.addColumn("Harga");
       model.addColumn("Status");
       
       try{
           int no = 1;
           String sql = "SELECT * FROM rooms";
           java.sql.Connection conn = (Connection)DatabaseConnection.configDB();
           java.sql.Statement stm = conn.createStatement();
           java.sql.ResultSet res = stm.executeQuery(sql);
           
           while(res.next()){
               model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), 
               res.getString(4), res.getString(5), res.getString(6)});
           }
           TableRooms.setModel(model);
       }catch(SQLException ex){
           Logger.getLogger(Admin_Page.class.getName()).log(Level.SEVERE, null,ex);
       }
    }
    
        private void tampilkan_data() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("NIK");
            model.addColumn("Nama");
            model.addColumn("Email");
            model.addColumn("No Hp");
            model.addColumn("ID Kamar");
            model.addColumn("Check In");
            model.addColumn("Check Out");
            model.addColumn("Total Harga");
            model.addColumn("Status");

            try {
                // Koneksi ke database
                String sql = "SELECT booking.*, rooms.status AS status FROM booking " +
                             "JOIN rooms ON booking.id_booking = rooms.id_room";
                Connection conn = DatabaseConnection.configDB();
                java.sql.Statement stm = conn.createStatement();
                java.sql.ResultSet res = stm.executeQuery(sql);

                while (res.next()) {
                    // Ambil nilai tanggal dari ResultSet
                    java.sql.Date sqlCheckIn = res.getDate("check_in");
                    java.sql.Date sqlCheckOut = res.getDate("check_out");

                    // Format tanggal menjadi String
                    String formattedCheckIn = sqlCheckIn != null ? dateFormat.format(sqlCheckIn) : "-";
                    String formattedCheckOut = sqlCheckOut != null ? dateFormat.format(sqlCheckOut) : "-";

                    // Tambahkan baris ke model
                    model.addRow(new Object[]{
                        res.getString("id_booking"),
                        res.getString("nik"),
                        res.getString("nama"),
                        res.getString("email"),
                        res.getString("no_hp"),
                        res.getString("id_room"),
                        formattedCheckIn,
                        formattedCheckOut,
                        res.getString("total_price"),
                        res.getString("status_booking") // Tambahkan status kamar
                    });

                    // Tampilkan data pertama ke komponen GUI (contoh)
                    if (model.getRowCount() == 1) {
                        java.util.Date utilCheckIn = sqlCheckIn != null ? new java.util.Date(sqlCheckIn.getTime()) : null;
                        java.util.Date utilCheckOut = sqlCheckOut != null ? new java.util.Date(sqlCheckOut.getTime()) : null;

                        txtIn.setDate(utilCheckIn);
                        txtOut.setDate(utilCheckOut);

                        txtNIK.setText(res.getString("nik"));
                        txtNama.setText(res.getString("nama"));
                        txtEmail.setText(res.getString("email"));
                        txtNoHp.setText(res.getString("no_hp"));
                        txtIdKamar.setText(res.getString("id_room"));
                        txtTotal.setText(res.getString("total_price"));
                        cmbStatus.setSelectedItem(res.getString("status_booking")); // Set status kamar
                    }
                }

                // Set model ke JTable
                TableBooking.setModel(model);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    private void kosongkan_form(){
            txtIdBooking.setText(null);
            txtNIK.setText(null);
            txtNama.setText(null);
            txtEmail.setText(null);
            txtNoHp.setText(null);
            txtIdKamar.setText(null);
            txtIn.setDate(null);
            txtOut.setDate(null);
            txtTotal.setText(null);
            cmbStatus.setSelectedItem(0);
        };
    
    private boolean isKamarAvailable(String idRoom, java.util.Date checkIn, java.util.Date checkOut) {
    try {
        // Konversi tanggal ke tipe java.sql.Date
        java.sql.Date sqlCheckIn = new java.sql.Date(checkIn.getTime());
        java.sql.Date sqlCheckOut = new java.sql.Date(checkOut.getTime());

        // Query untuk memeriksa konflik pemesanan
        String sql = "SELECT COUNT(*) AS total FROM booking " +
                     "WHERE id_room = ? " +
                     "AND (check_in <= ? AND check_out >= ?)";

        Connection conn = DatabaseConnection.configDB();
        java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, idRoom);
        pstm.setDate(2, sqlCheckOut); // Check Out baru harus lebih besar dari Check In yang sudah ada
        pstm.setDate(3, sqlCheckIn); // Check In baru harus lebih kecil dari Check Out yang sudah ada

        java.sql.ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            int totalConflicts = rs.getInt("total");
            // Jika ada konflik (total > 0), kamar tidak tersedia
            return totalConflicts == 0;
        }

    } catch (SQLException ex) {
        Logger.getLogger(HalUtama.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return false; // Jika terjadi kesalahan atau ada konflik, anggap kamar tidak tersedia
}
    public HalUtama() {
        initComponents();
        tampilkan_data();
        kosongkan_form();
        tampilkan_dataKamar();
        hitungTotalHarga();
        addEventListeners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField8 = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtIdBooking = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtNIK = new javax.swing.JTextField();
        txtIdKamar = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        txtNoHp = new javax.swing.JTextField();
        btnPesan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableBooking = new javax.swing.JTable();
        txtOut = new com.toedter.calendar.JDateChooser();
        txtIn = new com.toedter.calendar.JDateChooser();
        btnUpdate = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtCariBoooking = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableRooms = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Form Pemesanan");

        jLabel2.setText("ID Booking");

        jLabel3.setText("NIK");

        jLabel4.setText("Nama");

        jLabel5.setText("Email");

        jLabel6.setText("No Hp");

        jLabel7.setText("ID Kamar");

        jLabel8.setText("Check In");

        jLabel9.setText("Check Out");

        jLabel10.setText("Total Harga");

        txtIdBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdBookingActionPerformed(evt);
            }
        });

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        btnPesan.setText("Pesan");
        btnPesan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesanActionPerformed(evt);
            }
        });

        TableBooking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NIK", "Nama", "Email", "No Hp", "Id Kamar", "Check In", "Check Out", "Total Harga", "Status"
            }
        ));
        TableBooking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableBookingMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableBooking);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDel.setText("Delete");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        jLabel11.setText("Cari");

        txtCariBoooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariBoookingActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel13.setText("Status");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Avaiable", "Booked", " " }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(379, 379, 379)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBack))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCariBoooking, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(79, 79, 79))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtIdBooking)
                        .addComponent(txtNama)
                        .addComponent(txtNIK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addComponent(txtIdKamar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEmail)
                        .addComponent(txtOut, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPesan)
                            .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)))
                    .addComponent(txtIn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnUpdate)
                        .addGap(38, 38, 38)
                        .addComponent(btnDel)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCariBoooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtIdBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNIK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtIdKamar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUpdate)
                        .addComponent(btnDel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(btnPesan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Pemesanan", jPanel1);

        TableRooms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "No Kamar", "Type Kamar", "Fasilitas", "Harga", "Status"
            }
        ));
        jScrollPane2.setViewportView(TableRooms);

        jLabel12.setText("Cari");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );

        jTabbedPane1.addTab("Cek Kamar", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdBookingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdBookingActionPerformed

    private void btnPesanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesanActionPerformed
        // TODO add your handling code here:
        try {
        // Ambil nilai tanggal dari JDateChooser
            java.util.Date utilCheckIn = txtIn.getDate();
            java.util.Date utilCheckOut = txtOut.getDate();

            // Validasi input tanggal
            if (utilCheckIn == null || utilCheckOut == null) {
                JOptionPane.showMessageDialog(this, "Harap pilih tanggal Check In dan Check Out.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Konversi tanggal ke java.sql.Date
            java.sql.Date sqlCheckIn = new java.sql.Date(utilCheckIn.getTime());
            java.sql.Date sqlCheckOut = new java.sql.Date(utilCheckOut.getTime());

            if (!isKamarAvailable(txtIdKamar.getText(), utilCheckIn, utilCheckOut)) {
                JOptionPane.showMessageDialog(this, "Kamar sudah dipesan pada tanggal tersebut!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
    }
            // Siapkan query dengan parameter
            String sql = "INSERT INTO booking (id_booking, nik, nama, email, no_hp, id_room, check_in, check_out, total_price,"
                         + " status_booking) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection conn = DatabaseConnection.configDB();
            java.sql.PreparedStatement pstm = conn.prepareStatement(sql);

            // Set parameter query
            pstm.setString(1, txtIdBooking.getText());
            pstm.setString(2, txtNIK.getText());
            pstm.setString(3, txtNama.getText());
            pstm.setString(4, txtEmail.getText());
            pstm.setString(5, txtNoHp.getText());
            pstm.setString(6, txtIdKamar.getText());
            pstm.setDate(7, sqlCheckIn);
            pstm.setDate(8, sqlCheckOut);
            pstm.setString(9, txtTotal.getText());
            pstm.setString(10, cmbStatus.getSelectedItem().toString());

            // Eksekusi query
            pstm.executeUpdate();

            // Notifikasi sukses
            JOptionPane.showMessageDialog(null, "Proses Pemesanan Berhasil");

            // Refresh data dan kosongkan form
            tampilkan_data();
            kosongkan_form();
        } catch (SQLException ex) {
            Logger.getLogger(HalUtama.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPesanActionPerformed

    private void TableBookingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableBookingMouseClicked
        // TODO add your handling code here:
        int tabel = TableBooking.rowAtPoint(evt.getPoint());
        
        String id_booking = TableBooking.getValueAt(tabel, 0).toString();
        txtIdBooking.setText(id_booking);
        
        String nik = TableBooking.getValueAt(tabel, 1).toString();
        txtNIK.setText(nik);
        
        String nama = TableBooking.getValueAt(tabel, 2).toString();
        txtNama.setText(nama);
        
        String email = TableBooking.getValueAt(tabel, 3).toString();
        txtEmail.setText(email);
        
        String no_hp = TableBooking.getValueAt(tabel, 4).toString();
        txtNoHp.setText(no_hp);
        
        String id_room = TableBooking.getValueAt(tabel, 5).toString();
        txtIdKamar.setText(id_room);
        
        // Konversi nilai tanggal (String) ke java.util.Date
        try {
            String check_in = TableBooking.getValueAt(tabel, 6).toString();
            java.util.Date utilCheckIn = new SimpleDateFormat("yyyy-MM-dd").parse(check_in);
            txtIn.setDate(utilCheckIn);

            String check_out = TableBooking.getValueAt(tabel, 7).toString();
            java.util.Date utilCheckOut = new SimpleDateFormat("yyyy-MM-dd").parse(check_out);
            txtOut.setDate(utilCheckOut);
        } catch (ParseException ex) {
            Logger.getLogger(HalUtama.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        String total_price = TableBooking.getValueAt(tabel, 8).toString();
        txtTotal.setText(total_price);
        
        String status_booking = TableBooking.getValueAt(tabel, 9).toString();
        cmbStatus.setSelectedItem(status_booking);
    }//GEN-LAST:event_TableBookingMouseClicked

    private void txtCariBoookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariBoookingActionPerformed
        // TODO add your handling code here:
        String searchString = txtCariBoooking.getText();
        cari(searchString);
    }//GEN-LAST:event_txtCariBoookingActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        // TODO add your handling code here:
         try{
            String sql = "DELETE FROM booking WHERE id_booking='"+txtIdBooking.getText()+"'";
            
            java.sql.Connection conn = (Connection)DatabaseConnection.configDB();
            java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
            JOptionPane.showMessageDialog(null, "Hapus Data Booking Berhasil");
            kosongkan_form();
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error : " + e.getMessage());
       }
            tampilkan_data();
            kosongkan_form();
    }//GEN-LAST:event_btnDelActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:                                       
    try {
        // Validasi input
        if (txtIn.getDate() == null || txtOut.getDate() == null || txtIdKamar.getText().isEmpty() || txtTotal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua data sebelum mengupdate.", "Peringatan",
            JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari JDateChooser
        java.util.Date utilCheckIn = txtIn.getDate();
        java.util.Date utilCheckOut = txtOut.getDate();

        // Konversi ke java.sql.Date untuk database
        java.sql.Date checkIn = new java.sql.Date(utilCheckIn.getTime());
        java.sql.Date checkOut = new java.sql.Date(utilCheckOut.getTime());

        // Hitung ulang total harga (agar sesuai dengan tanggal baru)
        hitungTotalHarga();
        String totalHarga = txtTotal.getText();

        // Update data di database
        String sql = "UPDATE booking SET nik = ?, nama = ?, email = ?, no_hp = ?, id_room = ?, "
                     + "check_in = ?, check_out = ?, total_price = ? WHERE id_booking = ?";
        Connection conn = DatabaseConnection.configDB();
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);

        // Set nilai baru ke query
        pst.setString(1, txtNIK.getText());
        pst.setString(2, txtNama.getText());
        pst.setString(3, txtEmail.getText());
        pst.setString(4, txtNoHp.getText());
        pst.setString(5, txtIdKamar.getText());
        pst.setDate(6, checkIn); // Tanggal Check In baru
        pst.setDate(7, checkOut); // Tanggal Check Out baru
        pst.setString(8, totalHarga); // Total harga baru
        pst.setString(9, txtIdBooking.getText()); // ID booking untuk identifikasi

        // Eksekusi query update
        pst.executeUpdate();

        // Tampilkan pesan sukses
        JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");

        // Refresh tabel dan kosongkan form
        tampilkan_data();
        kosongkan_form();

    } catch (SQLException ex) {
        Logger.getLogger(HalUtama.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        new login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        String searchString1 = txtSearch.getText();
        search(searchString1);
    }//GEN-LAST:event_txtSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HalUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HalUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HalUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HalUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HalUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableBooking;
    private javax.swing.JTable TableRooms;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnPesan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField txtCariBoooking;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdBooking;
    private javax.swing.JTextField txtIdKamar;
    private com.toedter.calendar.JDateChooser txtIn;
    private javax.swing.JTextField txtNIK;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoHp;
    private com.toedter.calendar.JDateChooser txtOut;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
