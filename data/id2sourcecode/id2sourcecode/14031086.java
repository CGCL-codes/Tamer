    public void saveAsStudyFile() {
        JFileChooser jNewQuestionsChooser = new JFileChooser("Save Study As...");
        File newStudyFile;
        boolean complete = false;
        if (getStudyFile() != null) jNewQuestionsChooser.setCurrentDirectory(getStudyFile().getParentFile());
        jNewQuestionsChooser.addChoosableFileFilter(studyFilter);
        while (!complete) {
            if (JFileChooser.APPROVE_OPTION == jNewQuestionsChooser.showSaveDialog(parent)) {
                try {
                    int confirm = JOptionPane.OK_OPTION;
                    newStudyFile = ((ExtensionFileFilter) studyFilter).getCorrectFileName(jNewQuestionsChooser.getSelectedFile());
                    if (!newStudyFile.createNewFile()) {
                        if (newStudyFile.canWrite()) {
                            confirm = JOptionPane.showConfirmDialog(parent, "<HTML><h3>A Study File already exists at this location.</h3>" + "<p>Shall I overwrite it?</p></html>", "Overwrite Study Package File", JOptionPane.OK_CANCEL_OPTION);
                        } else {
                            confirm = JOptionPane.showConfirmDialog(parent, "<HTML><h2>An <b>Active</b> Study File already exists at this location.</h2>" + "<p>If you overwrite it, any interviews created with it will be unreadable!</p>" + "<p>Shall I overwrite it?</p></html>", "Overwrite Study Package File", JOptionPane.OK_CANCEL_OPTION);
                        }
                    }
                    if (confirm == JOptionPane.OK_OPTION) {
                        if (!newStudyFile.canWrite()) {
                            throw (new java.io.IOException());
                        }
                        StudyWriter sw = new StudyWriter(newStudyFile);
                        getStudy().setStudyId(System.currentTimeMillis());
                        sw.setStudy(getStudy());
                        setCurrentStudy(newStudyFile, getStudy());
                        complete = true;
                        Preferences prefs = Preferences.userNodeForPackage(getClass());
                        prefs.put(FILE_PREF, newStudyFile.getParent());
                    }
                } catch (java.io.IOException e) {
                    JOptionPane.showMessageDialog(parent, "Unable to write to study file. Study not saved.");
                    throw new RuntimeException(e);
                }
            } else {
                complete = true;
            }
        }
    }
