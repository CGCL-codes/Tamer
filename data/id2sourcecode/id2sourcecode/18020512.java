    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        finishButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ProcletModels pmodelsInst = ProcletModels.getInstance();
        java.util.List<ProcletPort> ports = pmodelsInst.getPorts();
        java.util.List<PortConnection> pconnsList = PortConnections.getInstance().getPortConnections();
        java.util.List<ProcletPort> portsUnconn = new ArrayList<ProcletPort>();
        boolean found = false;
        for (ProcletPort port : ports) {
            found = false;
            for (PortConnection pconn : pconnsList) {
                if (pconn.getIPort().getPortID().equals(port.getPortID()) || pconn.getOPort().getPortID().equals(port.getPortID())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                portsUnconn.add(port);
            }
        }
        destPortComboBox = new javax.swing.JComboBox(portsUnconn.toArray());
        sourcePortComboBox = new javax.swing.JComboBox(portsUnconn.toArray());
        PortConnections pconnsInst = PortConnections.getInstance();
        java.util.List<String> channels = pconnsInst.getChannels();
        channelComboBox = new javax.swing.JComboBox(channels.toArray());
        jLabel3 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Add External Interaction"));
        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });
        jLabel1.setText("source");
        jLabel2.setText("destination");
        jLabel3.setText("channel");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(finishButton).addContainerGap()).addGroup(jPanel1Layout.createSequentialGroup().addComponent(sourcePortComboBox, 0, 149, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(destPortComboBox, 0, 149, Short.MAX_VALUE)).addContainerGap()))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(sourcePortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(destPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)).addGap(27, 27, 27).addComponent(finishButton).addContainerGap(22, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        pack();
    }
