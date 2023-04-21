    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        previewDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        btnPreviewClose = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblPosition = new javax.swing.JLabel();
        lblPage = new javax.swing.JLabel();
        tfPage = new javax.swing.JTextField();
        lblPosLLX = new javax.swing.JLabel();
        tfPosLLX = new javax.swing.JTextField();
        lblPosLLY = new javax.swing.JLabel();
        tfPosLLY = new javax.swing.JTextField();
        lblPosURX = new javax.swing.JLabel();
        tfPosURX = new javax.swing.JTextField();
        lblPosURY = new javax.swing.JLabel();
        tfPosURY = new javax.swing.JTextField();
        lblSettings = new javax.swing.JLabel();
        lblDisplayMode = new javax.swing.JLabel();
        cbDisplayMode = new javax.swing.JComboBox();
        chkbL2TextDefault = new javax.swing.JCheckBox();
        lblL4Text = new javax.swing.JLabel();
        tfL4Text = new javax.swing.JTextField();
        chkbL4TextDefault = new javax.swing.JCheckBox();
        lblImgPath = new javax.swing.JLabel();
        tfImgPath = new javax.swing.JTextField();
        btnImgPathBrowse = new javax.swing.JButton();
        lblBgImgPath = new javax.swing.JLabel();
        tfBgImgPath = new javax.swing.JTextField();
        btnBgImgPathBrowse = new javax.swing.JButton();
        lblBgImgScale = new javax.swing.JLabel();
        tfBgImgScale = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        lblPageBounds = new javax.swing.JLabel();
        lblPosLLXBounds = new javax.swing.JLabel();
        lblPosLLYBounds = new javax.swing.JLabel();
        lblL2Text = new javax.swing.JLabel();
        lblL2TextFontSize = new javax.swing.JLabel();
        tfL2TextFontSize = new javax.swing.JTextField();
        btnPreview = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taL2Text = new javax.swing.JTextArea();
        chkbAcro6Layers = new javax.swing.JCheckBox();
        previewDialog.setModal(true);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        btnPreviewClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/back16.png")));
        btnPreviewClose.setText("Close");
        btnPreviewClose.setMinimumSize(new java.awt.Dimension(50, 20));
        btnPreviewClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnPreviewClose, gridBagConstraints);
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/prev16.png")));
        btnPrevious.setMinimumSize(new java.awt.Dimension(50, 20));
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnPrevious, gridBagConstraints);
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/next16.png")));
        btnNext.setMinimumSize(new java.awt.Dimension(50, 20));
        btnNext.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(btnNext, gridBagConstraints);
        previewDialog.getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        lblPosition.setText("Position");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        getContentPane().add(lblPosition, gridBagConstraints);
        lblPage.setLabelFor(lblPage);
        lblPage.setText("Page");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblPage, gridBagConstraints);
        tfPage.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfPage.setText("1");
        tfPage.setMinimumSize(new java.awt.Dimension(70, 20));
        tfPage.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfPage, gridBagConstraints);
        lblPosLLX.setLabelFor(tfPosLLX);
        lblPosLLX.setText("Lower Left X");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblPosLLX, gridBagConstraints);
        tfPosLLX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfPosLLX.setText("0.0");
        tfPosLLX.setMinimumSize(new java.awt.Dimension(70, 20));
        tfPosLLX.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfPosLLX, gridBagConstraints);
        lblPosLLY.setLabelFor(tfPosLLY);
        lblPosLLY.setText("Lower Left Y");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblPosLLY, gridBagConstraints);
        tfPosLLY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfPosLLY.setText("0.0");
        tfPosLLY.setMinimumSize(new java.awt.Dimension(70, 20));
        tfPosLLY.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfPosLLY, gridBagConstraints);
        lblPosURX.setLabelFor(tfPosURX);
        lblPosURX.setText("Upper Right X");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblPosURX, gridBagConstraints);
        tfPosURX.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfPosURX.setText("100.0");
        tfPosURX.setMinimumSize(new java.awt.Dimension(70, 20));
        tfPosURX.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfPosURX, gridBagConstraints);
        lblPosURY.setLabelFor(tfPosURY);
        lblPosURY.setText("Upper Right Y");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblPosURY, gridBagConstraints);
        tfPosURY.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfPosURY.setText("100.0");
        tfPosURY.setMinimumSize(new java.awt.Dimension(70, 20));
        tfPosURY.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfPosURY, gridBagConstraints);
        lblSettings.setText("Settings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        getContentPane().add(lblSettings, gridBagConstraints);
        lblDisplayMode.setLabelFor(cbDisplayMode);
        lblDisplayMode.setText("Display");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblDisplayMode, gridBagConstraints);
        cbDisplayMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbDisplayMode.setMinimumSize(new java.awt.Dimension(200, 20));
        cbDisplayMode.setPreferredSize(new java.awt.Dimension(200, 20));
        cbDisplayMode.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDisplayModeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(cbDisplayMode, gridBagConstraints);
        chkbL2TextDefault.setText("Default");
        chkbL2TextDefault.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkbL2TextDefault.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkbL2TextDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkbL2TextDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(chkbL2TextDefault, gridBagConstraints);
        lblL4Text.setLabelFor(tfL4Text);
        lblL4Text.setText("Status text");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblL4Text, gridBagConstraints);
        tfL4Text.setMinimumSize(new java.awt.Dimension(200, 20));
        tfL4Text.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfL4Text, gridBagConstraints);
        chkbL4TextDefault.setText("Default");
        chkbL4TextDefault.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkbL4TextDefault.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkbL4TextDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkbL4TextDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(chkbL4TextDefault, gridBagConstraints);
        lblImgPath.setLabelFor(tfImgPath);
        lblImgPath.setText("Image");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblImgPath, gridBagConstraints);
        tfImgPath.setMinimumSize(new java.awt.Dimension(200, 20));
        tfImgPath.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfImgPath, gridBagConstraints);
        btnImgPathBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/fileopen16.png")));
        btnImgPathBrowse.setText("Browse");
        btnImgPathBrowse.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnImgPathBrowse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgPathBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(btnImgPathBrowse, gridBagConstraints);
        lblBgImgPath.setLabelFor(tfBgImgPath);
        lblBgImgPath.setText("Background image");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblBgImgPath, gridBagConstraints);
        tfBgImgPath.setMinimumSize(new java.awt.Dimension(200, 20));
        tfBgImgPath.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfBgImgPath, gridBagConstraints);
        btnBgImgPathBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/fileopen16.png")));
        btnBgImgPathBrowse.setText("Browse");
        btnBgImgPathBrowse.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnBgImgPathBrowse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBgImgPathBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        getContentPane().add(btnBgImgPathBrowse, gridBagConstraints);
        lblBgImgScale.setLabelFor(tfBgImgScale);
        lblBgImgScale.setText("Background image scale");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblBgImgScale, gridBagConstraints);
        tfBgImgScale.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfBgImgScale.setText("-1.0");
        tfBgImgScale.setMinimumSize(new java.awt.Dimension(70, 20));
        tfBgImgScale.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfBgImgScale, gridBagConstraints);
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/back16.png")));
        btnClose.setText("Close");
        btnClose.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 5, 2);
        getContentPane().add(btnClose, gridBagConstraints);
        lblPageBounds.setText("1 - 10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        getContentPane().add(lblPageBounds, gridBagConstraints);
        lblPosLLXBounds.setText("0.0 - 20.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        getContentPane().add(lblPosLLXBounds, gridBagConstraints);
        lblPosLLYBounds.setText("0.0 - 20.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        getContentPane().add(lblPosLLYBounds, gridBagConstraints);
        lblL2Text.setLabelFor(taL2Text);
        lblL2Text.setText("Signature text");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblL2Text, gridBagConstraints);
        lblL2TextFontSize.setLabelFor(tfL2TextFontSize);
        lblL2TextFontSize.setText("Signature text size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(lblL2TextFontSize, gridBagConstraints);
        tfL2TextFontSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tfL2TextFontSize.setText("10.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(tfL2TextFontSize, gridBagConstraints);
        btnPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jsignpdf/preview16.png")));
        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(btnPreview, gridBagConstraints);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(24, 48));
        taL2Text.setColumns(20);
        taL2Text.setRows(5);
        jScrollPane1.setViewportView(taL2Text);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);
        chkbAcro6Layers.setSelected(true);
        chkbAcro6Layers.setText("Acrobat 6 layer mode");
        chkbAcro6Layers.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkbAcro6Layers.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkbAcro6Layers.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkbAcro6LayersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(chkbAcro6Layers, gridBagConstraints);
        pack();
    }
