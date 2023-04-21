    @Override
    protected void init() {
        panel = new JPanel();
        JLabel localUserNameLabel = new JLabel(labels.get("configtool_local_name"));
        localUserName = new JTextField();
        localUserName.getDocument().addDocumentListener(documentChanged);
        localURLLabel = new JCheckBox(labels.get("traserconfigeditor_local_url"));
        localURLLabel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                localURL.setEnabled(localURLLabel.isSelected());
                panelChanged();
            }
        });
        localURL = new JTextField();
        localURL.getDocument().addDocumentListener(documentChanged);
        exportPartnerData = new JButton(labels.get("traserconfigeditor_export_data"));
        exportPartnerData.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doExportData();
            }
        });
        JLabel localUserPrivateLabel = new JLabel(labels.get("configtool_local_privatekey"));
        JLabel localUserCertLabel = new JLabel(labels.get("configtool_local_cert"));
        localUserPrivate = new JLabel(labels.get("configtool_local_absent"));
        localUserPrivate.setOpaque(true);
        localUserPrivate.setForeground(Color.RED);
        localUserCert = new JLabel(labels.get("configtool_local_absent"));
        localUserCert.setOpaque(true);
        localUserCert.setForeground(Color.RED);
        localUserPrivateFile = new JTextField();
        localUserPrivateFile.setEditable(false);
        localUserCertFile = new JTextField();
        localUserCertFile.setEditable(false);
        JButton localSetpw = new JButton(labels.get("configtool_local_setpw"));
        localSetpw.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doSetPassword();
            }
        });
        localGenkey = new JButton(labels.get("configtool_local_genkey"));
        localGenkey.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doGenerateKeypair();
            }
        });
        localImportKey = new JButton(labels.get("configtool_local_importkey"));
        localImportKey.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doSelectFileFor(localUserPrivateFile);
            }
        });
        localExportKey = new JButton(labels.get("configtool_local_exportkey"));
        localExportKey.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doExportLocalPrivateKey();
            }
        });
        localImportCert = new JButton(labels.get("configtool_local_importcert"));
        localImportCert.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doSelectFileFor(localUserCertFile);
            }
        });
        localExportCert = new JButton(labels.get("configtool_local_exportcert"));
        localExportCert.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doExportLocalCertificate(localUserName.getText());
            }
        });
        GroupLayout gl = new GroupLayout(panel);
        panel.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        gl.setHorizontalGroup(gl.createParallelGroup(Alignment.TRAILING).addGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(Alignment.LEADING).addComponent(localUserNameLabel).addComponent(localURLLabel).addComponent(localUserPrivateLabel).addComponent(localUserCertLabel)).addGroup(gl.createParallelGroup(Alignment.LEADING).addComponent(localUserName).addComponent(localURL).addComponent(localUserPrivate).addComponent(localUserPrivateFile).addComponent(localUserCert).addComponent(localUserCertFile))).addGroup(gl.createSequentialGroup().addComponent(localSetpw).addComponent(localGenkey).addComponent(exportPartnerData)).addGroup(gl.createSequentialGroup().addComponent(localImportKey).addComponent(localExportKey)).addGroup(gl.createSequentialGroup().addComponent(localImportCert).addComponent(localExportCert)));
        gl.setVerticalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localUserNameLabel).addComponent(localUserName)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localURLLabel).addComponent(localURL)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localSetpw).addComponent(localGenkey).addComponent(exportPartnerData)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localUserPrivateLabel).addComponent(localUserPrivate)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localUserPrivateFile)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localImportKey).addComponent(localExportKey)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localUserCertLabel).addComponent(localUserCert)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localUserCertFile)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(localImportCert).addComponent(localExportCert)));
        gl.linkSize(SwingConstants.HORIZONTAL, localImportKey, localExportKey);
        gl.linkSize(SwingConstants.HORIZONTAL, localImportCert, localExportCert);
    }
