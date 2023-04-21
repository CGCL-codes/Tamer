    public void execute(IFile erdFile, RootModel root, GraphicalViewer viewer) {
        try {
            DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
            String rootDir = dialog.open();
            if (rootDir != null) {
                generate(rootDir, root);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
