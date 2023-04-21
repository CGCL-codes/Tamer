    protected void setUpDisplay() {
        shell = new Shell(getDisplay(), SWT.SHELL_TRIM);
        shell.setText(getName());
        shell.setLayout(new GridLayout(2, false));
        new Label(shell, SWT.NONE).setText("Simple read/write:");
        simpleRWCombo = new Combo(shell, SWT.SIMPLE | SWT.BORDER);
        simpleRWCombo.setItems(entries);
        simpleRWCombo.setData("id", "simple, read/write");
        new Label(shell, SWT.NONE).setText("Simple read-only:");
        simpleROCombo = new Combo(shell, SWT.SIMPLE | SWT.READ_ONLY | SWT.BORDER);
        simpleROCombo.setItems(entries);
        simpleROCombo.setData("id", "simple, read-only");
        new Label(shell, SWT.NONE).setText("Drop-down read/write:");
        dropDownRWCombo = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER);
        dropDownRWCombo.setItems(entries);
        dropDownRWCombo.setData("id", "drop-down, read/write");
        new Label(shell, SWT.NONE).setText("Drop-down read-only:");
        dropDownROCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
        dropDownROCombo.setItems(entries);
        dropDownROCombo.setData("id", "drop-down, read-only");
        shell.pack();
        shell.open();
        shell.forceActive();
    }
