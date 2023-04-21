    public boolean showMessageBox(Shell aShell, String file) {
        MessageBox aMessageBox = new MessageBox(aShell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
        aMessageBox.setText("File already exists");
        aMessageBox.setMessage("The file " + file + " already exists.\nOverwrite file?");
        return aMessageBox.open() == SWT.YES;
    }
