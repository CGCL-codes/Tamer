    private void configureUI(final boolean createMode) {
        bnFileChooser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                VirtualFile defaultFile = (tfFile.getText().length() == 0) ? null : RevuVfsUtils.findFile(tfFile.getText());
                VirtualFile vFile = fileChooser.selectFileToSave(defaultFile);
                if (vFile != null) {
                    tfFile.setText(vFile.getPath());
                }
            }
        });
        DefaultListCellRenderer comboRenderer = createComboReviewRenderer();
        cbReviewCopy.setRenderer(comboRenderer);
        cbReviewLink.setRenderer(comboRenderer);
        DocumentAdapter textFieldsListener = new DocumentAdapter() {

            public void textChanged(DocumentEvent event) {
                setOKActionEnabled((!createMode) || ((tfName.getText().trim().length() > 0) && (tfFile.getText().trim().length() > 0) && (fileTextField.getSelectedFile() != null) && (fileTextField.getSelectedFile().isDirectory())));
            }
        };
        tfName.getDocument().addDocumentListener(textFieldsListener);
        tfFile.getDocument().addDocumentListener(textFieldsListener);
        rbTypeBlank.setVisible(createMode);
        rbTypeBlank.setSelected(createMode);
        rbTypeCopy.setSelected(!createMode);
        lbTitle.setVisible(createMode);
        tfName.setVisible(createMode);
        lbFile.setVisible(createMode);
        tfFile.setVisible(createMode);
        bnFileChooser.setVisible(createMode);
        cbReviewCopy.setEnabled(!createMode);
        cbReviewLink.setEnabled(false);
        ActionListener radioTypeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cbReviewLink.setEnabled(rbTypeLink.isSelected());
                cbReviewCopy.setEnabled(rbTypeCopy.isSelected());
            }
        };
        rbTypeBlank.addActionListener(radioTypeListener);
        rbTypeCopy.addActionListener(radioTypeListener);
        rbTypeLink.addActionListener(radioTypeListener);
        setOKActionEnabled(!createMode);
        setTitle(RevuBundle.message(createMode ? "projectSettings.review.importDialog.create.title" : "projectSettings.review.importDialog.update.title"));
        init();
        pack();
    }
