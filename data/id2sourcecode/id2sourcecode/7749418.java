    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        scanPanel = new javax.swing.JPanel();
        scanButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        cyclePortButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        deviceField = new javax.swing.JTextField();
        deviceString0 = new javax.swing.JTextField();
        deviceString1 = new javax.swing.JTextField();
        vidpiddidPanel = new javax.swing.JPanel();
        writeVIDPIDDIDButton = new javax.swing.JButton();
        VIDtextField = new javax.swing.JTextField();
        PIDtextField = new javax.swing.JTextField();
        DIDtextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        firmwareDownloadPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        filenameTextField = new javax.swing.JTextField();
        chooseFileButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        writeEEPROMRadioButton = new javax.swing.JRadioButton();
        writeRAMRadioButton = new javax.swing.JRadioButton();
        eraseButton = new javax.swing.JButton();
        downloadFirmwareButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        deviceIDPanel = new javax.swing.JPanel();
        writeDeviceIDButton = new javax.swing.JButton();
        writeDeviceIDTextField = new javax.swing.JTextField();
        USBAERmini2panel = new javax.swing.JPanel();
        monSeqCPLDFirmwareButton = new javax.swing.JButton();
        monSeqFX2FirmwareButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        monSeqFX2FirmwareButtonJTAG = new javax.swing.JButton();
        CPLDDownloadPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        CPLDfilenameField = new javax.swing.JTextField();
        chooseCPLDFileButton = new javax.swing.JButton();
        downloadCPLDFirmwareButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CypressFX2EEPROM");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        scanPanel.setLayout(new javax.swing.BoxLayout(scanPanel, javax.swing.BoxLayout.LINE_AXIS));
        scanButton.setText("Scan for device");
        scanButton.setToolTipText("Looks for CypressFX2 device");
        scanButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanButtonActionPerformed(evt);
            }
        });
        scanPanel.add(scanButton);
        closeButton.setText("Close");
        closeButton.setEnabled(false);
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        scanPanel.add(closeButton);
        cyclePortButton.setText("Cycle port");
        cyclePortButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cyclePortButtonActionPerformed(evt);
            }
        });
        scanPanel.add(cyclePortButton);
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
        deviceField.setEditable(false);
        deviceField.setText("no device");
        deviceField.setToolTipText("Device");
        jPanel3.add(deviceField);
        deviceString0.setEditable(false);
        deviceString0.setToolTipText("String descriptor 0");
        jPanel3.add(deviceString0);
        deviceString1.setEditable(false);
        deviceString1.setToolTipText("String descriptor 1");
        jPanel3.add(deviceString1);
        scanPanel.add(jPanel3);
        vidpiddidPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("VID/PID/DID (C0 load)"));
        writeVIDPIDDIDButton.setText("Write C0 load VIDPIDDID to EEPROM");
        writeVIDPIDDIDButton.setToolTipText("writes only VID/PID/DID to flash memory EEPROM for CypressFX2 C0 load (ram download of code from host)");
        writeVIDPIDDIDButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeVIDPIDDIDButtonActionPerformed(evt);
            }
        });
        VIDtextField.setColumns(5);
        VIDtextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        VIDtextField.setToolTipText("hex format value for USB vendor ID");
        VIDtextField.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        PIDtextField.setColumns(5);
        PIDtextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        PIDtextField.setToolTipText("hex format value for USB product ID");
        PIDtextField.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        DIDtextField.setColumns(5);
        DIDtextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        DIDtextField.setToolTipText("hex format value for device ID (optional)");
        DIDtextField.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 10));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 10));
        jLabel2.setText("<html>This panel will write a VID/PID/DID to the device EEPROM. The device must have firmware loaded already that can write the EEPROM. </html>");
        org.jdesktop.layout.GroupLayout vidpiddidPanelLayout = new org.jdesktop.layout.GroupLayout(vidpiddidPanel);
        vidpiddidPanel.setLayout(vidpiddidPanelLayout);
        vidpiddidPanelLayout.setHorizontalGroup(vidpiddidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(vidpiddidPanelLayout.createSequentialGroup().add(vidpiddidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(vidpiddidPanelLayout.createSequentialGroup().addContainerGap().add(jLabel2)).add(vidpiddidPanelLayout.createSequentialGroup().add(writeVIDPIDDIDButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(VIDtextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(PIDtextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(DIDtextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap(269, Short.MAX_VALUE)));
        vidpiddidPanelLayout.setVerticalGroup(vidpiddidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, vidpiddidPanelLayout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(vidpiddidPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(writeVIDPIDDIDButton).add(VIDtextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(PIDtextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(DIDtextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(12, 12, 12)));
        firmwareDownloadPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("EEPROM firmware (C2 load)"));
        firmwareDownloadPanel.setAlignmentX(1.0F);
        chooseFileButton.setText("Choose...");
        chooseFileButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseFileButtonActionPerformed(evt);
            }
        });
        buttonGroup1.add(writeEEPROMRadioButton);
        writeEEPROMRadioButton.setText("Write to EEPROM");
        writeEEPROMRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        writeEEPROMRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        writeEEPROMRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeEEPROMRadioButtonActionPerformed(evt);
            }
        });
        buttonGroup1.add(writeRAMRadioButton);
        writeRAMRadioButton.setText("Write to RAM");
        writeRAMRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        writeRAMRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        writeRAMRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeRAMRadioButtonActionPerformed(evt);
            }
        });
        eraseButton.setText("Erase EEPROM");
        eraseButton.setToolTipText("Erases the EEPROM to blank state (device must first have firmware that can write the EEPROM)");
        eraseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eraseButtonActionPerformed(evt);
            }
        });
        downloadFirmwareButton.setText("Download firmware");
        downloadFirmwareButton.setToolTipText("Program EEPROM or write Cypress RAM, depending on file type");
        downloadFirmwareButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadFirmwareButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().add(writeEEPROMRadioButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 293, Short.MAX_VALUE).add(writeRAMRadioButton).add(69, 69, 69).add(eraseButton).add(18, 18, 18).add(downloadFirmwareButton).add(91, 91, 91)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(writeEEPROMRadioButton).add(writeRAMRadioButton)).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(eraseButton).add(downloadFirmwareButton))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jLabel1.setText("<html>Select a firmware file here. <p><strong>For EEPROM programming, the device must already be running firmware that can program the EEPROM.</strong><p> iic files are for writing to the EEPROM but require that the device have some firmware loaded that can write the EEPROM.<p>bix (binary) and hex (intel format) files are for writing to RAM.</html>");
        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createSequentialGroup().addContainerGap().add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup().add(filenameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 792, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(chooseFileButton))).add(jLabel1)).addContainerGap(7, Short.MAX_VALUE)));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(filenameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(chooseFileButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
        org.jdesktop.layout.GroupLayout firmwareDownloadPanelLayout = new org.jdesktop.layout.GroupLayout(firmwareDownloadPanel);
        firmwareDownloadPanel.setLayout(firmwareDownloadPanelLayout);
        firmwareDownloadPanelLayout.setHorizontalGroup(firmwareDownloadPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(firmwareDownloadPanelLayout.createSequentialGroup().addContainerGap().add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 908, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(115, Short.MAX_VALUE)));
        firmwareDownloadPanelLayout.setVerticalGroup(firmwareDownloadPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(firmwareDownloadPanelLayout.createSequentialGroup().add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        deviceIDPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Serial Number String (4 characters for DVS128, 8 characters for USBAERmini2)"));
        deviceIDPanel.setLayout(new javax.swing.BoxLayout(deviceIDPanel, javax.swing.BoxLayout.LINE_AXIS));
        writeDeviceIDButton.setText("Write Serial Number string");
        writeDeviceIDButton.setEnabled(false);
        writeDeviceIDButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeDeviceIDButtonActionPerformed(evt);
            }
        });
        deviceIDPanel.add(writeDeviceIDButton);
        writeDeviceIDTextField.setColumns(20);
        writeDeviceIDTextField.setEnabled(false);
        writeDeviceIDTextField.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        deviceIDPanel.add(writeDeviceIDTextField);
        USBAERmini2panel.setBorder(javax.swing.BorderFactory.createTitledBorder("USBAERmini2 firmware"));
        monSeqCPLDFirmwareButton.setText("Mon/Seq CPLD Firmware");
        monSeqCPLDFirmwareButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monSeqCPLDFirmwareButtonActionPerformed(evt);
            }
        });
        monSeqFX2FirmwareButton.setText("FX2 Firmware");
        monSeqFX2FirmwareButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monSeqFX2FirmwareButtonActionPerformed(evt);
            }
        });
        monSeqFX2FirmwareButtonJTAG.setText("FX2LP Firmware with JTAG support");
        monSeqFX2FirmwareButtonJTAG.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monSeqFX2FirmwareButtonJTAGActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout USBAERmini2panelLayout = new org.jdesktop.layout.GroupLayout(USBAERmini2panel);
        USBAERmini2panel.setLayout(USBAERmini2panelLayout);
        USBAERmini2panelLayout.setHorizontalGroup(USBAERmini2panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(USBAERmini2panelLayout.createSequentialGroup().add(monSeqCPLDFirmwareButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(monSeqFX2FirmwareButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(monSeqFX2FirmwareButtonJTAG).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 452, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(118, Short.MAX_VALUE)));
        USBAERmini2panelLayout.setVerticalGroup(USBAERmini2panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(USBAERmini2panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(monSeqFX2FirmwareButton).add(monSeqCPLDFirmwareButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE).add(monSeqFX2FirmwareButtonJTAG)).add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE));
        CPLDDownloadPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("CPLD firmware download"));
        CPLDDownloadPanel.setAlignmentX(1.0F);
        chooseCPLDFileButton.setText("Choose...");
        chooseCPLDFileButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseCPLDFileButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().addContainerGap().add(CPLDfilenameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 791, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(chooseCPLDFileButton).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(CPLDfilenameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(chooseCPLDFileButton)).add(51, 51, 51)));
        downloadCPLDFirmwareButton.setText("Download firmware");
        downloadCPLDFirmwareButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadCPLDFirmwareButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout CPLDDownloadPanelLayout = new org.jdesktop.layout.GroupLayout(CPLDDownloadPanel);
        CPLDDownloadPanel.setLayout(CPLDDownloadPanelLayout);
        CPLDDownloadPanelLayout.setHorizontalGroup(CPLDDownloadPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(CPLDDownloadPanelLayout.createSequentialGroup().add(CPLDDownloadPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(CPLDDownloadPanelLayout.createSequentialGroup().add(328, 328, 328).add(downloadCPLDFirmwareButton)).add(CPLDDownloadPanelLayout.createSequentialGroup().addContainerGap().add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap(113, Short.MAX_VALUE)));
        CPLDDownloadPanelLayout.setVerticalGroup(CPLDDownloadPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(CPLDDownloadPanelLayout.createSequentialGroup().add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(downloadCPLDFirmwareButton).addContainerGap(12, Short.MAX_VALUE)));
        fileMenu.setText("File");
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        jMenuBar1.add(fileMenu);
        setJMenuBar(jMenuBar1);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(scanPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 664, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(CPLDDownloadPanel, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(firmwareDownloadPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(USBAERmini2panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(deviceIDPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 943, Short.MAX_VALUE).add(vidpiddidPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(scanPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(vidpiddidPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(firmwareDownloadPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(deviceIDPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(USBAERmini2panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(CPLDDownloadPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }
