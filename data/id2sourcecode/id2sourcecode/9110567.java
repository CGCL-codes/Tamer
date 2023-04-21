    public Control createDialogArea(Composite parent) {
        initializeComposite(parent);
        createLabel(Messages.format(FormatterMessages.AlreadyExistsDialog_dialog_label, fProfile.getName()));
        fRenameRadio = createRadioButton(FormatterMessages.AlreadyExistsDialog_rename_radio_button_desc);
        fNameText = createTextField();
        fOverwriteRadio = createRadioButton(FormatterMessages.AlreadyExistsDialog_overwrite_radio_button_desc);
        fRenameRadio.setSelection(true);
        fNameText.setText(fProfile.getName());
        fNameText.setSelection(0, fProfile.getName().length());
        fNameText.setFocus();
        fNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                doValidation();
            }
        });
        fRenameRadio.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                fNameText.setEnabled(true);
                fNameText.setFocus();
                fNameText.setSelection(0, fNameText.getText().length());
                doValidation();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        fOverwriteRadio.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                fNameText.setEnabled(false);
                doValidation();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        updateStatus(fDuplicate);
        applyDialogFont(fComposite);
        return fComposite;
    }
