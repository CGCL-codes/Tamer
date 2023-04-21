    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        selectedLb = new javax.swing.JLabel();
        readCb = new javax.swing.JCheckBox();
        writeCb = new javax.swing.JCheckBox();
        okBt = new javax.swing.JButton();
        cancelBt = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(hu.rsc.svnAdmin.SVNAdmin.class).getContext().getResourceMap(PermissionDialog.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        selectedLb.setText(resourceMap.getString("selectedLb.text"));
        selectedLb.setName("selectedLb");
        readCb.setText(resourceMap.getString("readCb.text"));
        readCb.setName("readCb");
        writeCb.setText(resourceMap.getString("writeCb.text"));
        writeCb.setName("writeCb");
        okBt.setText(resourceMap.getString("okBt.text"));
        okBt.setName("okBt");
        okBt.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtActionPerformed(evt);
            }
        });
        cancelBt.setText(resourceMap.getString("cancelBt.text"));
        cancelBt.setName("cancelBt");
        cancelBt.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(selectedLb, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(88, 88, 88).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(writeCb).addComponent(readCb)))).addContainerGap(27, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(198, Short.MAX_VALUE).addComponent(okBt, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(cancelBt).addGap(65, 65, 65)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(selectedLb, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(26, 26, 26).addComponent(readCb).addGap(18, 18, 18).addComponent(writeCb).addGap(84, 84, 84).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okBt).addComponent(cancelBt)).addContainerGap(51, Short.MAX_VALUE)));
        pack();
    }
