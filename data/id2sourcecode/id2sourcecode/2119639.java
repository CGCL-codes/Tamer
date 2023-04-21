    @Override
    public void execute(Event event) {
        ERDiagram diagram = this.getDiagram();
        ExportToDDLDialog dialog = new ExportToDDLDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), diagram, this.getEditorPart(), this.getGraphicalViewer());
        dialog.open();
        this.refreshProject();
        if (dialog.getExportSetting() != null && !diagram.getDiagramContents().getSettings().getExportSetting().equals(dialog.getExportSetting())) {
            Settings newSettings = (Settings) diagram.getDiagramContents().getSettings().clone();
            newSettings.setExportSetting(dialog.getExportSetting());
            ChangeSettingsCommand command = new ChangeSettingsCommand(diagram, newSettings);
            this.execute(command);
        }
    }
