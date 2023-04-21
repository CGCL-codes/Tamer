    private boolean saveProject(File file, boolean alwaysAskFilename) {
        if (file == null || alwaysAskFilename) {
            JFileChooser dialog = new JFileChooser(file);
            dialog.setFileFilter(new FileNameExtensionFilter("Tree workbench files", "twb"));
            if (dialog.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return false;
            file = dialog.getSelectedFile();
            if (file.exists()) {
                int ret = JOptionPane.showConfirmDialog(this, "The file '" + file.getAbsolutePath() + "' already exists, do you want to overwrite it?", "Overwrite file", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ret != JOptionPane.YES_OPTION) return false;
            } else {
                if (!file.getName().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".twb");
                }
            }
        }
        try {
            List<Editor> failed = new ArrayList<Editor>();
            StringBuffer failBuffer = new StringBuffer();
            for (Editor e : this.openEditors.values()) {
                if (!e.saveToItem()) {
                    failed.add(e);
                    failBuffer.append("  - ");
                    failBuffer.append(e.getItem().getName());
                    failBuffer.append("\n");
                }
            }
            if (failed.size() > 0) {
                JOptionPane.showMessageDialog(this, "Failed to save the following items:\n" + failBuffer.toString() + "\n Project save aborted.", "Save failure", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            this.currentProject.saveToFile(file);
            this.projectFile = file;
            Settings.addRecentFile(file.getPath());
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save the project: " + ex.toString(), "Save failure", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
