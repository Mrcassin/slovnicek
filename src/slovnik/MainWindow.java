package slovnik;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author student
 */
public class MainWindow extends javax.swing.JFrame {

    private final DefaultTableModel model;
    private Connection spojeni;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        model = (DefaultTableModel) tabulka.getModel();
        if (!dbConnection()) {
            System.exit(0);
        }
        listData(getAllRecords());
    }

    private boolean dbConnection() {
        try {
            spojeni = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/slovnik?useUnicode=true&characterEncoding=utf-8", "root", "");
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Nedošlo k připojení databáze",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private ResultSet getAllRecords() {
        ResultSet vysledky = null;
        try {
            PreparedStatement dotaz = spojeni.prepareStatement("SELECT * FROM slovnicek");
            vysledky = dotaz.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Chyba při komunikaci s databází",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        return vysledky;
    }

    private void listData(ResultSet data) {
        /* Odstranění všech řádků z tabulky */
        for (int i = tabulka.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        try {
            /* Vložení řádků do tabulky a jejich naplnění daty ze získané dynamické sady */
            while (data.next()) {
                int id = data.getInt(1);
                String cesky = data.getString("cs");
                String anglicky = data.getString("en");
                model.addRow(new Object[]{id, cesky, anglicky});
            }
            /* Zapnutí nebo vypnutí tlačítek Změnit a Smazat v závislosti na existenci záznamů
 (řádků) v tabulce */
            if (tabulka.getRowCount() > 0) {
                tabulka.setRowSelectionInterval(0, 0);
                /* Označení prvního řádku tabulky */
                update.setEnabled(true);
                delete.setEnabled(true);
            } else {
                update.setEnabled(false);
                delete.setEnabled(false);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Chyba při komunikaci s databází",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int insertRecord(String enWord, String csWord) {
        int numRows = 0;
        try {
            PreparedStatement dotaz
                    = spojeni.prepareStatement("INSERT INTO slovnicek (cs, en) VALUES (?, ?)");
            dotaz.setString(1, csWord);
            dotaz.setString(2, enWord);
            numRows = dotaz.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Chyba při komunikaci s databází",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        return numRows;
    }

    private int updateRecord(int id, String enWord, String csWord) {
        int numRows = 0;
        try {
            PreparedStatement dotaz
                    = spojeni.prepareStatement("UPDATE slovnicek SET cs=?, en=? WHERE id=?");
            dotaz.setString(1, csWord);
            dotaz.setString(2, enWord);
            dotaz.setInt(3, id);
            numRows = dotaz.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Chyba při komunikaci s databází",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        return numRows;
    }

    private int deleteRecord(int id) {
        int numRows = 0;
        try {
            PreparedStatement dotaz = spojeni.prepareStatement(
                    "DELETE FROM slovnicek WHERE id =  ?");
            dotaz.setInt(1, id);
            numRows = dotaz.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Chyba při komunikaci s databází",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        return numRows;
    }

    private ResultSet searchEn(String enWord) {
        ResultSet vysledky = null;
        try {
            PreparedStatement dotaz = spojeni.prepareStatement(
                    "SELECT * FROM slovnicek WHERE en =  ?");
            dotaz.setString(1, enWord);
            vysledky = dotaz.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Chyba při komunikaci s databází",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        return vysledky;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        insert = new javax.swing.JButton();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        search = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabulka.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "cs", "en"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabulka);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        insert.setText("Nový");
        insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed(evt);
            }
        });

        update.setText("Změnit");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setText("Snazat");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        search.setText("Hledej");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(insert)
                .addGap(18, 18, 18)
                .addComponent(update)
                .addGap(18, 18, 18)
                .addComponent(delete)
                .addGap(18, 18, 18)
                .addComponent(search)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insert)
                    .addComponent(update)
                    .addComponent(delete)
                    .addComponent(search))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertActionPerformed
        String[] slova = {"", ""};
        wordsJDialog slovaDialog = new wordsJDialog(this, true, slova);
        /* Zobrazení dialogového okna metodou showDialog() */
        if (slovaDialog.showDialog().equalsIgnoreCase("OK")) {
            insertRecord(slovaDialog.getAnglicky(), slovaDialog.getCesky());
        }
        listData(this.getAllRecords());
    }//GEN-LAST:event_insertActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        int id = (int) tabulka.getModel().getValueAt(tabulka.getSelectedRow(), 0);
        String[] slova = {tabulka.getModel().getValueAt(tabulka.getSelectedRow(), 1).toString(),
            tabulka.getModel().getValueAt(tabulka.getSelectedRow(), 2).toString()};
        wordsJDialog slovaDialog = new wordsJDialog(this, true, slova);
        if (slovaDialog.showDialog().equalsIgnoreCase("OK")) {
            updateRecord(id, slovaDialog.getAnglicky(), slovaDialog.getCesky());
        }
        listData(this.getAllRecords());
    }//GEN-LAST:event_updateActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        int id = (int) tabulka.getModel().getValueAt(tabulka.getSelectedRow(), 0);
        deleteRecord(id);
        listData(this.getAllRecords());
    }//GEN-LAST:event_deleteActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        String hledany = JOptionPane.showInputDialog(this, "Zadej anglické slovo");
        try {
            ResultSet data = searchEn(hledany);
            data.next();
            int id = data.getInt(1);
            String cesky = data.getString("cs");
            String anglicky = data.getString("en");
            JOptionPane.showMessageDialog(this, "Anglicky: \"" + anglicky + "\"\nČesky: \""
                    + cesky + "\"", "Hledané slovo", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Záznam nebyl nalezen",
                    "Chyba", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_searchActionPerformed

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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton delete;
    private javax.swing.JButton insert;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton search;
    private javax.swing.JTable tabulka;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
