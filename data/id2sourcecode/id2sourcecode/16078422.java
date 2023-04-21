    @SuppressWarnings("unchecked")
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        write = new javax.swing.JToggleButton();
        read = new javax.swing.JToggleButton();
        info = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        progress = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();
        gaps = new javax.swing.JCheckBox();
        dselect = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        tracks = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        head = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        dstep = new javax.swing.JCheckBox();
        rescan = new javax.swing.JCheckBox();
        setTitle("SAMdisk GUI � by Devilmarkus");
        setFocusable(false);
        setResizable(false);
        buttonGroup1.add(write);
        write.setText("Write DSK to Floppy");
        write.setFocusPainted(false);
        write.setFocusable(false);
        buttonGroup1.add(read);
        read.setSelected(true);
        read.setText("Read Floppy to DSK");
        read.setFocusPainted(false);
        read.setFocusable(false);
        text.setColumns(20);
        text.setEditable(false);
        text.setFont(new java.awt.Font("Monospaced", 1, 12));
        text.setRows(5);
        info.setViewportView(text);
        jButton1.setText("Start");
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        progress.setMaximum(80);
        progress.setFocusable(false);
        jButton2.setText("Abort");
        jButton2.setEnabled(false);
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gaps.setText("GAPs");
        gaps.setFocusPainted(false);
        gaps.setFocusable(false);
        dselect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        dselect.setFocusable(false);
        jLabel1.setText("Drive");
        tracks.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "39 Tracks", "42 Tracks", "79 Tracks" }));
        tracks.setFocusable(false);
        jLabel2.setText("Tracks");
        head.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Both", "Head 0", "Head 1" }));
        head.setSelectedIndex(1);
        head.setFocusable(false);
        jLabel3.setText("Head:");
        dstep.setText("Double step");
        dstep.setFocusPainted(false);
        dstep.setFocusable(false);
        rescan.setText("Rescan");
        rescan.setFocusPainted(false);
        rescan.setFocusable(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(info, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(write).addGroup(layout.createSequentialGroup().addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(read)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(gaps).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1)).addComponent(dstep)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(dselect, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2)).addComponent(rescan)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE).addComponent(tracks, 0, 143, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(write).addComponent(read).addComponent(gaps).addComponent(jLabel1).addComponent(dselect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(tracks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2).addComponent(jLabel3).addComponent(head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(dstep).addComponent(rescan)).addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(info, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE).addContainerGap()));
        pack();
    }
