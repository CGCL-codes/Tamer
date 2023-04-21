    public static File newFile(String title, String filename, String filetype, String suffix, File startDir, JFrame parent, boolean enclosingDir) throws FileCreateException {
        JFileChooser jNewStudyChooser = new JFileChooser();
        File newFile = null;
        File dirFile;
        String projectPath = null;
        String projectName = null;
        FileFilter newFileFilter = new ExtensionFileFilter(title, suffix);
        jNewStudyChooser.addChoosableFileFilter(newFileFilter);
        jNewStudyChooser.setDialogTitle(title);
        jNewStudyChooser.setSelectedFile(new File(filename));
        if (startDir == null) {
            jNewStudyChooser.setCurrentDirectory(new File("./"));
        } else {
            jNewStudyChooser.setCurrentDirectory(startDir);
        }
        try {
            if (JFileChooser.APPROVE_OPTION == jNewStudyChooser.showSaveDialog(parent)) {
                projectPath = jNewStudyChooser.getSelectedFile().getParent();
                projectName = jNewStudyChooser.getSelectedFile().getName();
                if (enclosingDir) {
                    if (projectName.indexOf(".") != -1) {
                        projectName = projectName.substring(0, projectName.indexOf("."));
                    }
                    try {
                        String folder = projectPath.substring(projectPath.lastIndexOf(File.separator) + 1);
                        if (!folder.equals(projectName)) {
                            dirFile = new File(projectPath, projectName);
                            dirFile.mkdir();
                            projectPath = dirFile.getPath();
                        }
                    } catch (SecurityException e) {
                        JOptionPane.showMessageDialog(parent, "Unable to create directory.", "New File Error", JOptionPane.ERROR_MESSAGE);
                        throw new FileCreateException(false);
                    }
                }
                newFile = new File(projectPath, projectName);
                newFile = ((ExtensionFileFilter) newFileFilter).getCorrectFileName(newFile);
                try {
                    if (!newFile.createNewFile()) {
                        int confirm = JOptionPane.showConfirmDialog(parent, "<HTML><h2>" + filetype + " File already exists at this location.</h2>" + "<p>Shall I overwrite it?</p></html>", "Overwrite " + filetype + " File", JOptionPane.OK_CANCEL_OPTION);
                        if (confirm != JOptionPane.OK_OPTION) {
                            throw new FileCreateException(false);
                        }
                    }
                } catch (IOException ex) {
                    throw new FileCreateException(true);
                }
            }
        } catch (FileCreateException e) {
            if (e.report) {
                JOptionPane.showMessageDialog(parent, "Unable to create " + filetype + " file.");
            }
            newFile = null;
        }
        return newFile;
    }
