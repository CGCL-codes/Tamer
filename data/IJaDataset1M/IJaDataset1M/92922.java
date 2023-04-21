package edu.unc.safegui.safegui;

import javax.swing.JOptionPane;
import edu.unc.safegui.workers.RWorker;

/**
 *
 * @author  Myroslav Sypa
 */
public class RStatusWindow extends javax.swing.JFrame {

    private static RStatusWindow instance = null;

    public static RStatusWindow getInstance() {
        if (instance == null) instance = new RStatusWindow();
        return instance;
    }

    private boolean fl = true;

    /** Creates new form RStatusWindow */
    protected RStatusWindow() {
        initComponents();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SAFEicon.gif")).getImage());
        jProgressBar1.setVisible(false);
    }

    public void setFl(boolean f) {
        fl = f;
    }

    public boolean getFl() {
        return fl;
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        setTitle("R Status ");
        setLocationByPlatform(true);
        setResizable(false);
        jPanel1.setBackground(new java.awt.Color(0, 146, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SafeClear.jpg")));
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jProgressBar1.setBackground(new java.awt.Color(255, 255, 255));
        jProgressBar1.setForeground(new java.awt.Color(255, 102, 0));
        jProgressBar1.setIndeterminate(true);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SafeCloses.jpg")));
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(19, 19, 19).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)).addContainerGap()));
        jPanel2.setBackground(new java.awt.Color(0, 146, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Verdana", 0, 12));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(24, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        kill();
    }

    public void kill() {
        this.dispose();
        clearStatus();
        SAFEGUI.getInstance().getGlassPane().setVisible(false);
        SAFEGUI.getInstance().setVisible(true);
        RWorker.getInstance().stop();
        fl = false;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        clearStatus();
    }

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SafeClear2.jpg")));
    }

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SafeClear.jpg")));
    }

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SafeClose2s.jpg")));
    }

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/unc/safegui/img/SafeCloses.jpg")));
    }

    public void clearStatus() {
        jTextArea1.setText("");
    }

    public void setStatus(String str) {
        jTextArea1.setText(jTextArea1.getText() + "\r\n" + str);
        this.setTitle(str.trim());
        jTextArea1.getCaret().setDot(jTextArea1.getText().length());
        jScrollPane1.scrollRectToVisible(jTextArea1.getVisibleRect());
        if (str.contains("Error") && fl) {
            JOptionPane.showMessageDialog(null, "Error from R: " + str, "Error inside R", JOptionPane.ERROR_MESSAGE);
            kill();
        }
    }

    public void changeProgressVisdibility(boolean fl) {
        if (fl) jProgressBar1.setVisible(true); else jProgressBar1.setVisible(false);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JProgressBar jProgressBar1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;
}
