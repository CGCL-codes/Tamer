    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        okayButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        okayButton.setText("Okay");
        okayButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });
        jPanel1.add(okayButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cancelButton);
        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);
        area.setColumns(20);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setRows(5);
        area.setText("This file already exists.  Are you sure you want to overwrite?");
        area.setWrapStyleWord(true);
        jScrollPane1.setViewportView(area);
        jPanel2.add(jScrollPane1);
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        pack();
    }
