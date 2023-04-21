    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel3 = new javax.swing.JLabel();
        overwrite = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        targetdir = new javax.swing.JTextField();
        osPanel = new javax.swing.JPanel();
        renameTarget = new javax.swing.JTextField();
        renameTargetChoice = new javax.swing.JCheckBox();
        unpack = new javax.swing.JCheckBox();
        jLabel3.setText("Target Directory");
        overwrite.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Update", "Overwrite", "Ask True", "Ask False" }));
        overwrite.setToolTipText("<html>Update: if the file is already installed, it will be overwritten;<br>Ask True, Ask False: ask the user what to do and supply default value for non-interactive use;<br>Update. the new file is only installed if it's modification time is newer than the modification time of the already existing file</html>");
        jLabel6.setText("Overwrite");
        targetdir.setText("$INSTALL_PATH/");
        targetdir.setToolTipText("The destination directory");
        javax.swing.GroupLayout osPanelLayout = new javax.swing.GroupLayout(osPanel);
        osPanel.setLayout(osPanelLayout);
        osPanelLayout.setHorizontalGroup(osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 273, Short.MAX_VALUE));
        osPanelLayout.setVerticalGroup(osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 237, Short.MAX_VALUE));
        osPanel = new OSPanel();
        renameTarget.setToolTipText("The destination file name");
        renameTarget.setEnabled(false);
        renameTargetChoice.setText("Rename Target File");
        renameTargetChoice.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameTargetChoiceActionPerformed(evt);
            }
        });
        unpack.setText("Unpack");
        unpack.setToolTipText("If the file is an archive, its content will be unpacked and put as individual files in the folder");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(unpack, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(renameTargetChoice).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(renameTarget, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel3)).addGap(50, 50, 50).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(overwrite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(targetdir, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))).addComponent(osPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(renameTargetChoice).addComponent(renameTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(unpack).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(targetdir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(overwrite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(osPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }
