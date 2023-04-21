    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        configButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        flipList = new javax.swing.JList();
        addFlip = new javax.swing.JButton();
        delFlip = new javax.swing.JButton();
        flipProperty = new javax.swing.JButton();
        genPDF = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jSeparator3 = new javax.swing.JSeparator();
        thumbnail = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newFlip = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem3 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flip Maker");
        setLocationByPlatform(true);
        setResizable(false);
        jLabel1.setText("Liste des FlipBooks");
        configButton.setText("Config. Page");
        configButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configButtonActionPerformed(evt);
            }
        });
        flipList.setModel(new javax.swing.DefaultListModel());
        flipList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        flipList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                flipListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(flipList);
        addFlip.setText("Ajouter");
        addFlip.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFlipActionPerformed(evt);
            }
        });
        delFlip.setText("Retirer");
        delFlip.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delFlipActionPerformed(evt);
            }
        });
        flipProperty.setText("Propriétés");
        flipProperty.setEnabled(false);
        flipProperty.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flipPropertyActionPerformed(evt);
            }
        });
        genPDF.setText("PDF !!!!");
        genPDF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genPDFActionPerformed(evt);
            }
        });
        fileMenu.setText("Fichier");
        newFlip.setAction(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                newFlip();
            }
        });
        newFlip.setText("Nouveau Flip");
        fileMenu.add(newFlip);
        fileMenu.add(jSeparator2);
        jMenuItem1.setAction(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                openFbmFile();
            }
        });
        jMenuItem1.setText("Ouvrir...");
        fileMenu.add(jMenuItem1);
        jMenuItem2.setAction(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                saveFbmFile();
            }
        });
        jMenuItem2.setText("Enregistrer...");
        fileMenu.add(jMenuItem2);
        fileMenu.add(jSeparator1);
        jMenuItem3.setAction(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.exit(0);
            }
        });
        jMenuItem3.setText("Quitter");
        fileMenu.add(jMenuItem3);
        jMenuBar1.add(fileMenu);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(delFlip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addFlip, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(flipProperty, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(configButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(genPDF).addComponent(thumbnail))).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()).addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(addFlip).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(delFlip).addGap(18, 18, 18).addComponent(flipProperty)).addComponent(thumbnail)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(genPDF).addComponent(configButton))).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }
