    @SuppressWarnings("unchecked")
    private void initComponents() {
        classnameLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        executeButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        classnameLabel.setText(runner.getName());
        classnameLabel.setToolTipText(runner.getPath());
        add(classnameLabel);
        typeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "On demand", "After init", "Before init" }));
        typeComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });
        add(typeComboBox);
        executeButton.setText("Execute");
        executeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeButtonActionPerformed(evt);
            }
        });
        add(executeButton);
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        add(deleteButton);
        openButton.setText("Open");
        openButton.setEnabled(Desktop.isDesktopSupported());
        openButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        add(openButton);
    }
