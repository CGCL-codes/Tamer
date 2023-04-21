    public ExitCode setupParameters(ParameterSet parameterSet) {
        ProjectSaverParameters parameters = (ProjectSaverParameters) parameterSet;
        String path = (String) parameters.getParameterValue(ProjectSaverParameters.lastDirectory);
        File lastPath = null;
        if (path != null) lastPath = new File(path);
        ProjectSaveDialog dialog = new ProjectSaveDialog(lastPath, helpID);
        dialog.setVisible(true);
        ExitCode exitCode = dialog.getExitCode();
        if (exitCode == ExitCode.OK) {
            File selectedFile = dialog.getSelectedFile();
            String lastDirectory = dialog.getCurrentDirectory();
            if (!selectedFile.getName().endsWith(".mzmine")) {
                selectedFile = new File(selectedFile.getPath() + ".mzmine");
            }
            if (selectedFile.exists()) {
                int selectedValue = JOptionPane.showConfirmDialog(MZmineCore.getDesktop().getMainFrame(), selectedFile.getName() + " already exists, overwrite ?", "Question...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (selectedValue != JOptionPane.YES_OPTION) return ExitCode.CANCEL;
            }
            parameters.setParameterValue(ProjectSaverParameters.lastDirectory, lastDirectory);
            parameters.setParameterValue(ProjectSaverParameters.projectFile, selectedFile.getPath());
        }
        return exitCode;
    }
