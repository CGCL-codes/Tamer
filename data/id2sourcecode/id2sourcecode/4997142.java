    private void jbInit() throws Exception {
        jScrollPane.getViewport().setBackground(new Color(204, 204, 204));
        this.setClosable(true);
        this.setJMenuBar(menuBar);
        this.setMaximizable(true);
        this.setResizable(true);
        heightButton.setText("Update Line Height");
        heightButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                heightButton_actionPerformed(e);
            }
        });
        buttonPanel.setLayout(verticalLayout1);
        minvalue = actualmin = exp.getMinExpValue();
        maxvalue = actualmax = exp.getMaxExpValue();
        centervalue = minvalue + (maxvalue - minvalue) / 2;
        colorLabel = new ColorLabel((double) minvalue, (double) maxvalue, (double) minvalue, (double) maxvalue, (double) centervalue, Color.green, Color.black, Color.red);
        colorLabel.setText("colorLabel");
        colorLabel.showLabels();
        table.setRowHeight(10);
        int n = 3;
        mSlider = new MThumbSlider(n, 0, 1000);
        mSlider.setUI(new MetalMThumbSliderUI());
        heightField.setColumns(5);
        heightField.setText("" + table.getRowHeight());
        if (useTree) ((MetricTree) side).setLineHeight(10); else ((DissimListPanel) side).setLineHeight(10);
        table.paintTable(minvalue, centervalue, maxvalue, table.getRowHeight());
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        fileMenu.setText("File");
        saveMenu.setText("Save Image As...");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveMenu_actionPerformed(e);
            }
        });
        saveGrpMenu.setText("Save Selected As Group...");
        saveGrpMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveGrpMenu_actionPerformed(e);
            }
        });
        printMenu.setText("Print");
        printMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        printMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                printMenu_actionPerformed(e);
            }
        });
        closeMenu.setText("Close");
        closeMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
        closeMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeMenu_actionPerformed(e);
            }
        });
        editMenu.setText("Edit");
        decimalMenu.setText("Decimal Places");
        decimalMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                decimalMenu_actionPerformed(e);
            }
        });
        colorMenu.setText("Color");
        rgmenu.setText("Red/Green");
        rgmenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rgmenu_actionPerformed(e);
            }
        });
        graymenu.setText("Grayscale");
        graymenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graymenu_actionPerformed(e);
            }
        });
        setParams();
        buttonPanel.add(sliderPanel, null);
        buttonPanel.add(colorLabel, null);
        buttonPanel.add(heightPanel, null);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        JPanel p = new JPanel(new FlowLayout());
        side.setPreferredSize(new Dimension(colwidth, table.getRowCount() * 10));
        viewport = new JViewport();
        viewport.setView(side);
        viewport.setPreferredSize(new Dimension(colwidth, table.getRowCount() * 10));
        viewport.setScrollMode(viewport.SIMPLE_SCROLL_MODE);
        jScrollPane.getViewport().add(table, null);
        jScrollPane.setRowHeader(viewport);
        this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        heightPanel.add(heightField, null);
        heightPanel.add(heightButton, null);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(colorMenu);
        fileMenu.add(saveGrpMenu);
        fileMenu.add(saveMenu);
        fileMenu.add(printMenu);
        fileMenu.addSeparator();
        fileMenu.add(closeMenu);
        editMenu.add(decimalMenu);
        colorMenu.add(rgmenu);
        colorMenu.add(graymenu);
        repaint();
    }
