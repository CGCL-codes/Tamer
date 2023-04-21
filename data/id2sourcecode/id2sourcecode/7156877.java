    private void jbInit() throws Exception {
        ass_cmnt.setEditable(false);
        jMenuItem1.setText("Close");
        jMenuItem3.setText("About");
        jMenuItem4.setText("Help");
        jMenu2.setText("Help");
        jMenu1.setText("File");
        jPanelnorth.setLayout(gridLayout1);
        assembleButton.setText("Assemble");
        assembleButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                assembleButton_actionPerformed(e);
            }
        });
        assembleButton.setToolTipText("This assembles this project in the given working directory on ~~~~");
        assembleButton.setActionCommand("Assemble");
        jPanelAssemblyProject.setLayout(borderLayout1);
        removeButton.setText("Remove Selected From Project");
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeButton_actionPerformed(e);
            }
        });
        removeButton.setFont(new java.awt.Font("Dialog", 0, 10));
        removeButton.setToolTipText("This allows to remove tracefiles/primers/artificial sequences from " + "this project");
        addButton.setToolTipText("This allows to add tracefiles/primers/artificial sequences to be " + "added to this project");
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addButton_actionPerformed(e);
            }
        });
        jPanelwest.setLayout(verticalFlowLayout1);
        ass_cmnt.setText("");
        jTextField6_LastModified.setText("");
        jTextField6_LastModified.setEditable(false);
        user_cmnt.setText("");
        jTextField5_CreatedOn.setText("");
        jTextField5_CreatedOn.setEditable(false);
        jTextField4_WorkDir.setText(WORK_DIR_DEFAULT);
        jTextField3_Options.setText(OPTIONS_DEFAULT);
        gridLayout1.setRows(3);
        gridLayout1.setColumns(4);
        jLabel_ass_cmnt.setText("Assembly Comment");
        jTextField1_Name.setText("");
        jTextField1_Name.setToolTipText("");
        jLabel_user_cmnt.setText("User Comment");
        LastModLabel.setText("Last modified");
        CreatedOnLabel.setText("Created on");
        OptionsLabel.setToolTipText("The options for phrad/phrap -- it is recommended to work with the " + "defaults, i.e. leave this blank");
        OptionsLabel.setText("Options");
        WorkDirLabel.setText("Working Directory");
        WorkDirLabel.setToolTipText("The complete path to a directory on CUB where you have read/write " + "access to");
        NameLabel.setToolTipText("");
        NameLabel.setText("Project Name");
        jPanelsouth.setLayout(gridLayout2);
        jPanelcenter.setLayout(paneLayout1);
        saveButton.setToolTipText("This saves this assembly project to the database");
        saveButton.setText("Save Project To DB");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveButton_actionPerformed(e);
            }
        });
        deleteContigsButton.setEnabled(false);
        deleteContigsButton.setFont(new java.awt.Font("Dialog", 0, 10));
        deleteContigsButton.setToolTipText("This deletes from the database");
        deleteContigsButton.setText("Delete Selected From DB");
        deleteContigsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteContigsButton_actionPerformed(e);
            }
        });
        TracefilesPanel.setLayout(borderLayout2);
        ArtificialPanel.setLayout(borderLayout3);
        PrimersPanel.setLayout(borderLayout4);
        VectorsPanel.setLayout(borderLayout5);
        ContigsPanel.setLayout(borderLayout6);
        jPanel6.setLayout(borderLayout7);
        jCheckBoxWriteContigToDb.setToolTipText("This allows to write the resulting contigs  to the database");
        jCheckBoxWriteContigToDb.setSelected(true);
        jCheckBoxWriteContigToDb.setText("Write Contig to DB");
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                jTabbedPane1_stateChanged(e);
            }
        });
        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);
        jMenu1.add(jMenuItem1);
        jMenu2.add(jMenuItem3);
        jMenu2.add(jMenuItem4);
        jPanelsouth.add(jLabel_user_cmnt, null);
        jPanelsouth.add(user_cmnt, null);
        jPanelsouth.add(jLabel_ass_cmnt, null);
        jPanelsouth.add(ass_cmnt, null);
        jPanelAssemblyProject.add(jPanelwest, BorderLayout.EAST);
        jPanelAssemblyProject.add(jPanelsouth, BorderLayout.SOUTH);
        jPanelwest.add(assembleButton, null);
        jPanelwest.add(saveButton, null);
        jPanelwest.add(addButton, null);
        jPanelwest.add(removeButton, null);
        jPanelwest.add(deleteContigsButton, null);
        jPanelAssemblyProject.add(jPanelcenter, BorderLayout.CENTER);
        jPanelcenter.add(jTabbedPane1, new PaneConstraints("jTabbedPane1", "jTabbedPane1", PaneConstraints.ROOT, 1.0f));
        jTabbedPane1.add(TracefilesPanel, TRACEFILEPANEL_NAME);
        TracefilesPanel.add(jScrollPane1, BorderLayout.CENTER);
        jTabbedPane1.add(ArtificialPanel, ARTIFICIALPANEL_NAME);
        ArtificialPanel.add(jScrollPane2, BorderLayout.NORTH);
        jTabbedPane1.add(PrimersPanel, PRIMERSPANEL_NAME);
        PrimersPanel.add(jScrollPane3, BorderLayout.NORTH);
        jTabbedPane1.add(VectorsPanel, VECTORSPANEL_NAME);
        VectorsPanel.add(jScrollPane4, BorderLayout.NORTH);
        jTabbedPane1.add(ContigsPanel, CONTIGSPANEL_NAME);
        ContigsPanel.add(jScrollPane5, BorderLayout.NORTH);
        jPanel6.add(jScrollPane6, BorderLayout.NORTH);
        jPanelAssemblyProject.add(jPanelnorth, BorderLayout.NORTH);
        jPanelnorth.add(NameLabel, null);
        jPanelnorth.add(jTextField1_Name, null);
        jPanelnorth.add(CreatedOnLabel, null);
        jPanelnorth.add(jTextField5_CreatedOn, null);
        jPanelnorth.add(OptionsLabel, null);
        jPanelnorth.add(jTextField3_Options, null);
        jPanelnorth.add(LastModLabel, null);
        jPanelnorth.add(jTextField6_LastModified, null);
        jPanelnorth.add(WorkDirLabel, null);
        jPanelnorth.add(jTextField4_WorkDir, null);
        getContentPane().add(jPanelAssemblyProject, BorderLayout.CENTER);
        jPanelwest.add(jCheckBoxWriteContigToDb, null);
        jPanelwest.add(jLabelAssembStatus, null);
        menuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        menuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        menuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);
        addTables();
        updateDisplay();
    }
