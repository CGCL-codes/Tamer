    protected boolean queryOverwrite(final List files) {
        MessageDialog dialog = new MessageDialog(new Shell(), "Overwrite existing file in $jarplug tree?", null, "The following files are already present in the workspace under the " + JARPLUG + " directory. " + "Do you wish to overwrite them?", MessageDialog.QUESTION, new String[] { "Yes", "No" }, 0) {

            protected Control createCustomArea(Composite parent) {
                GridData gd = new GridData(GridData.FILL_HORIZONTAL);
                gd.widthHint = 400;
                gd.heightHint = 300;
                Text text = new Text(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
                text.setEditable(false);
                text.setLayoutData(gd);
                for (int i = 0; i < files.size(); i++) {
                    text.append(files.get(i) + "\n");
                }
                return text;
            }
        };
        int result = dialog.open();
        return result == MessageDialog.OK;
    }
