    private void createContents(final Shell shell) {
        shell.setLayout(new GridLayout(3, true));
        Label lText = new Label(shell, SWT.NONE);
        lText.setText(Messages.OverwriteDialog_Already_Saved_Two);
        GridData data = new GridData();
        data.horizontalSpan = 3;
        lText.setLayoutData(data);
        Button bFirst = new Button(shell, SWT.PUSH);
        bFirst.setText(Messages.OverwriteDialog_First_Set);
        data = new GridData(GridData.FILL_HORIZONTAL);
        bFirst.setLayoutData(data);
        bFirst.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                setSelection(1);
                shell.close();
            }
        });
        Button bSecond = new Button(shell, SWT.PUSH);
        bSecond.setText(Messages.OverwriteDialog_Second_Set);
        data = new GridData(GridData.FILL_HORIZONTAL);
        bSecond.setLayoutData(data);
        bSecond.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                setSelection(2);
                shell.close();
            }
        });
        Button bCancel = new Button(shell, SWT.PUSH);
        bCancel.setText(Messages.OverwriteDialog_Cancel);
        data = new GridData(GridData.FILL_HORIZONTAL);
        bCancel.setLayoutData(data);
        bCancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                setSelection(-1);
                shell.close();
            }
        });
        shell.setDefaultButton(bCancel);
    }
