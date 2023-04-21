    public void execute(IFile erdFile, RootModel root, GraphicalViewer viewer) {
        WizardDialog dialog = new WizardDialog(null, new DDLWizard(erdFile, root));
        dialog.open();
    }
