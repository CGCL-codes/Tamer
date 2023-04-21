    @SuppressWarnings("unchecked")
    private void initComponents() {
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newProblemMenuItem = new javax.swing.JMenuItem();
        openProblemMenuItem = new javax.swing.JMenuItem();
        closeProblemMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        fileSeparator1 = new javax.swing.JPopupMenu.Separator();
        exportToPdfMenuItem = new javax.swing.JMenuItem();
        exportForLatexMenuItem = new javax.swing.JMenuItem();
        exportDataToCsvMenuItem = new javax.swing.JMenuItem();
        fileSeparator2 = new javax.swing.JPopupMenu.Separator();
        printMenuItem = new javax.swing.JMenuItem();
        fileSeparator3 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        problemMenu = new javax.swing.JMenu();
        editProblemMenuItem = new javax.swing.JMenuItem();
        editSubProblemsMenuItem = new javax.swing.JMenuItem();
        editConclusionMenuItem = new javax.swing.JMenuItem();
        problemSeparator1 = new javax.swing.JPopupMenu.Separator();
        newDataSetMenuItem = new javax.swing.JMenuItem();
        editDataSetsMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        operationEditorMenuItem = new javax.swing.JMenuItem();
        reloadOperationgsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        settingsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        onlineHelpMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource(Domain.IMAGES_DIR + "logo.png")).getImage());
        setName("mainFrame");
        getContentPane().setLayout(new java.awt.GridLayout(1, 1));
        fileMenu.setText("File");
        fileMenu.setFont(new java.awt.Font("Verdana", 0, 12));
        fileMenu.addMenuListener(new javax.swing.event.MenuListener() {

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                fileMenuMenuSelected(evt);
            }
        });
        newProblemMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newProblemMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        newProblemMenuItem.setText("New Problem...");
        newProblemMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProblemMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newProblemMenuItem);
        openProblemMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openProblemMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        openProblemMenuItem.setText("Open Problem...");
        openProblemMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openProblemMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openProblemMenuItem);
        closeProblemMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        closeProblemMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        closeProblemMenuItem.setText("Close Problem");
        closeProblemMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeProblemMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(closeProblemMenuItem);
        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);
        saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        saveAsMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(fileSeparator1);
        exportToPdfMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        exportToPdfMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        exportToPdfMenuItem.setText("Export to PDF...");
        exportToPdfMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToPdfMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportToPdfMenuItem);
        exportForLatexMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        exportForLatexMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        exportForLatexMenuItem.setText("Export for LaTeX...");
        exportForLatexMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportForLatexMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportForLatexMenuItem);
        exportDataToCsvMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        exportDataToCsvMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        exportDataToCsvMenuItem.setText("Export Data to CSV...");
        exportDataToCsvMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportDataToCsvMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportDataToCsvMenuItem);
        fileMenu.add(fileSeparator2);
        printMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        printMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        printMenuItem.setText("Print...");
        printMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(printMenuItem);
        fileMenu.add(fileSeparator3);
        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        editMenu.setText("Edit");
        editMenu.setFont(new java.awt.Font("Verdana", 0, 12));
        undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        undoMenuItem.setText("Undo");
        undoMenuItem.setEnabled(false);
        undoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(undoMenuItem);
        redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        redoMenuItem.setText("Redo");
        redoMenuItem.setEnabled(false);
        redoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(redoMenuItem);
        menuBar.add(editMenu);
        problemMenu.setText("Problem");
        problemMenu.setFont(new java.awt.Font("Verdana", 0, 12));
        problemMenu.addMenuListener(new javax.swing.event.MenuListener() {

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                problemMenuMenuSelected(evt);
            }
        });
        editProblemMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        editProblemMenuItem.setText("Edit Problem...");
        editProblemMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProblemMenuItemActionPerformed(evt);
            }
        });
        problemMenu.add(editProblemMenuItem);
        editSubProblemsMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        editSubProblemsMenuItem.setText("Edit Sub Problems...");
        editSubProblemsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSubProblemsMenuItemActionPerformed(evt);
            }
        });
        problemMenu.add(editSubProblemsMenuItem);
        editConclusionMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        editConclusionMenuItem.setText("Edit Conclusion...");
        editConclusionMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editConclusionMenuItemActionPerformed(evt);
            }
        });
        problemMenu.add(editConclusionMenuItem);
        problemMenu.add(problemSeparator1);
        newDataSetMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        newDataSetMenuItem.setText("Add Data Set...");
        newDataSetMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDataSetMenuItemActionPerformed(evt);
            }
        });
        problemMenu.add(newDataSetMenuItem);
        editDataSetsMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        editDataSetsMenuItem.setText("Edit Data Sets...");
        editDataSetsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDataSetsMenuItemActionPerformed(evt);
            }
        });
        problemMenu.add(editDataSetsMenuItem);
        menuBar.add(problemMenu);
        toolsMenu.setText("Tools");
        toolsMenu.setFont(new java.awt.Font("Verdana", 0, 12));
        toolsMenu.addMenuListener(new javax.swing.event.MenuListener() {

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                toolsMenuMenuSelected(evt);
            }
        });
        operationEditorMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        operationEditorMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        operationEditorMenuItem.setText("Launch maRla Operation Editor");
        operationEditorMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operationEditorMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(operationEditorMenuItem);
        reloadOperationgsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        reloadOperationgsMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        reloadOperationgsMenuItem.setText("Reload Operations");
        reloadOperationgsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadOperationgsMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(reloadOperationgsMenuItem);
        toolsMenu.add(jSeparator1);
        settingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, java.awt.event.InputEvent.CTRL_MASK));
        settingsMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(settingsMenuItem);
        menuBar.add(toolsMenu);
        helpMenu.setText("Help");
        helpMenu.setFont(new java.awt.Font("Verdana", 0, 12));
        helpMenu.addMenuListener(new javax.swing.event.MenuListener() {

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                helpMenuMenuSelected(evt);
            }
        });
        onlineHelpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        onlineHelpMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        onlineHelpMenuItem.setText("Online Help");
        onlineHelpMenuItem.setEnabled(false);
        onlineHelpMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineHelpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(onlineHelpMenuItem);
        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        aboutMenuItem.setFont(new java.awt.Font("Verdana", 0, 12));
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        pack();
    }
