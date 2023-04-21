    private void addListeners() {
        PropertiesListener myPropListener = new PropertiesListener(this);
        txtFileName.addPropertyChangeListener(myPropListener);
        addWindowListener(new WindowList());
        btnFileDialog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File fileInput = new File(txtFileName.getText());
                fc.setSelectedFile(fileInput);
                int returnVal = fc.showOpenDialog(getMe());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    strfileName = fc.getSelectedFile().getAbsolutePath();
                    txtFileName.setText(strfileName);
                    loadHexEditor(true);
                }
            }
        });
        btnLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File fileInput = new File(txtFileName.getText());
                if (fileInput.isFile()) {
                    strfileName = txtFileName.getText();
                    loadHexEditor(true);
                } else {
                    fc.setSelectedFile(fileInput);
                    int returnVal = fc.showOpenDialog(getMe());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        strfileName = fc.getSelectedFile().getAbsolutePath();
                        txtFileName.setText(strfileName);
                        loadHexEditor(true);
                    }
                }
            }
        });
        btnSaveAs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getMe().hexEditor.getTransferHandler();
                fc.setSelectedFile(new File(txtFileName.getText()));
                int returnVal = fc.showSaveDialog(getMe());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (fc.getSelectedFile().exists()) {
                        int confirm = JOptionPane.showConfirmDialog(getMe(), "<html>The selected file already exists.<br>Do you want to overwrite?", "File already exits", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    strfileName = fc.getSelectedFile().getAbsolutePath();
                    txtFileName.setText(strfileName);
                    new SaveContent(getMe(), txtFileName.getText(), hexEditor.getByteContent());
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtFileName.getText().trim().length() == 0) {
                    btnSaveAs.doClick();
                } else {
                    strfileName = txtFileName.getText();
                    new SaveContent(getMe(), txtFileName.getText(), hexEditor.getByteContent());
                }
            }
        });
        btnSizeEmpty.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hexEditor.setByteContent(new byte[0]);
                byteContent = new byte[0];
                byteContent = new byte[(Integer) (txtSizeEmptyField.getValue())];
                Arrays.fill(byteContent, (byte) 0);
                loadHexEditor(false);
            }
        });
        btnReSizeLength.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                byte[] muByteContent = new byte[(Integer) txtSizeEmptyField.getValue()];
                Arrays.fill(muByteContent, (byte) 0);
                System.arraycopy(byteContent, 0, muByteContent, 0, Math.min(muByteContent.length, byteContent.length));
                byteContent = muByteContent;
                loadHexEditor(false);
            }
        });
        CaretPositionListener cpl1 = new CaretPositionListener(txtSizeEmptyField);
        cpl1.setDynamicFormatting(true);
        PaneChangeListener changes = new PaneChangeListener();
        tabPnHexEdit.addChangeListener(changes);
    }
