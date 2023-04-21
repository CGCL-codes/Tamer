    protected void setUpDisplay() {
        shell = new Shell(getDisplay(), SWT.SHELL_TRIM);
        shell.setText(getName());
        shell.setLayout(new GridLayout(2, false));
        new Label(shell, SWT.NONE).setText("Standard read/write:");
        rwCombo = new CCombo(shell, SWT.BORDER);
        rwCombo.setItems(entries);
        rwCombo.setData("id", "standard, read/write");
        new Label(shell, SWT.NONE).setText("Standard read-only:");
        roCombo = new CCombo(shell, SWT.READ_ONLY | SWT.BORDER);
        roCombo.setItems(entries);
        roCombo.setData("id", "standard, read-only");
        new Label(shell, SWT.NONE).setText("Flat read/write:");
        rwFlatCombo = new CCombo(shell, SWT.FLAT | SWT.BORDER);
        rwFlatCombo.setItems(entries);
        rwFlatCombo.setData("id", "flat, read/write");
        new Label(shell, SWT.NONE).setText("Flat read-only:");
        roFlatCombo = new CCombo(shell, SWT.FLAT | SWT.READ_ONLY | SWT.BORDER);
        roFlatCombo.setItems(entries);
        roFlatCombo.setData("id", "flat, read-only");
        shell.pack();
        shell.open();
        shell.forceActive();
    }
