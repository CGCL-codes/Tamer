    public void newStudyFiles() {
        Preferences prefs = Preferences.userNodeForPackage(EgoNet.class);
        JFileChooser jNewStudyChooser = new JFileChooser();
        File dirFile, newStudyFile;
        String projectPath = null;
        String projectName = null;
        jNewStudyChooser.addChoosableFileFilter(studyFilter);
        jNewStudyChooser.setDialogTitle("Select Study Path");
        if (getStudyFile() != null) {
            jNewStudyChooser.setCurrentDirectory(getStudyFile().getParentFile());
        } else {
            File directory = new File(prefs.get(FILE_PREF, "."));
            jNewStudyChooser.setCurrentDirectory(directory);
        }
        try {
            if (JFileChooser.APPROVE_OPTION == jNewStudyChooser.showSaveDialog(egoNet.getFrame())) {
                projectPath = jNewStudyChooser.getSelectedFile().getParent();
                projectName = jNewStudyChooser.getSelectedFile().getName();
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
                    JOptionPane.showMessageDialog(egoNet.getFrame(), "Unable to create study directories.", "New Study Error", JOptionPane.ERROR_MESSAGE);
                    throw new IOException("Cannot create study directory for " + projectPath);
                }
                try {
                    newStudyFile = new File(projectPath, projectName);
                    newStudyFile = ((ExtensionFileFilter) studyFilter).getCorrectFileName(newStudyFile);
                    if (!newStudyFile.createNewFile()) {
                        int confirm = JOptionPane.showConfirmDialog(egoNet.getFrame(), "<HTML><h2>Study already exists at this location.</h2>" + "<p>Shall I overwrite it?</p></html>", "Overwrite Study File", JOptionPane.OK_CANCEL_OPTION);
                        if (confirm != JOptionPane.OK_OPTION) {
                            throw new IOException("Won't overwrite " + newStudyFile.getName());
                        } else {
                            newStudyFile.delete();
                            newStudyFile.createNewFile();
                        }
                    }
                    Study study = new Study();
                    study.setStudyId(System.currentTimeMillis());
                    egoNet.setStudy(study);
                    setStudyFile(newStudyFile);
                    egoNet.getStudy().setStudyName(projectName);
                    StudyWriter sw = new StudyWriter(newStudyFile);
                    sw.setStudy(study);
                    studyFileInUse = false;
                    prefs.put(FILE_PREF, newStudyFile.getParent());
                } catch (java.io.IOException e) {
                    JOptionPane.showMessageDialog(egoNet.getFrame(), "Unable to create study file.", "File Error", JOptionPane.ERROR_MESSAGE);
                    throw new IOException(e);
                }
                try {
                    dirFile = new File(projectPath, "Statistics");
                    dirFile.mkdir();
                    dirFile = new File(projectPath, "Interviews");
                    dirFile.mkdir();
                } catch (SecurityException e) {
                    JOptionPane.showMessageDialog(egoNet.getFrame(), "Unable to create study directories.", "New Study Error", JOptionPane.ERROR_MESSAGE);
                    throw new IOException(e);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(egoNet.getFrame(), "Study not created.");
            setStudyFile(null);
        }
    }
