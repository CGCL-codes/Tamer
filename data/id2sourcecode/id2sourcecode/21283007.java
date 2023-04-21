    private void createProjectInLocation(final JTextField projectName) {
        if (projectName.getText() != null && !projectName.getText().equals("")) {
            File newFolder = new File(getIds().prefs.getWorkspacePath() + "/" + projectName.getText());
            if (newFolder.exists()) {
                if (!newFolder.isDirectory()) {
                    JOptionPane.showMessageDialog(dialog, "A directory has to be chosen and a project name " + projectName.getText() + " conflicts with an existing file which is not a directory", "No directory selected", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    if (JOptionPane.showConfirmDialog(dialog, "There is already a folder matching the name of the project at " + newFolder.getAbsolutePath() + ". Do you want to reuse it?", "Confirm overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
                        return;
                    }
                }
            } else {
                newFolder.mkdirs();
            }
            dialog.setVisible(false);
            SpecificationTemplateKind stk = SpecificationTemplateKind.NoTemplate;
            if (rad2.isSelected()) stk = SpecificationTemplateKind.HelloWorld;
            if (rad3.isSelected()) stk = SpecificationTemplateKind.GUIAgent;
            if (rad4.isSelected()) stk = SpecificationTemplateKind.Interaction;
            new IAFProjectCreatorSwingTask(projectName.getText(), stk, getIdeUpdater(), getIds(), getResources()).execute();
        } else {
            JOptionPane.showMessageDialog(dialog, "No directory has been chosen. Please, press on the browse button to select one", "No directory selected", JOptionPane.ERROR_MESSAGE);
        }
    }
