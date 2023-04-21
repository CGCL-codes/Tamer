    public void exportQuestions() {
        JFileChooser jNewQuestionsChooser = new JFileChooser();
        File newQuestionFile;
        jNewQuestionsChooser.setCurrentDirectory(new File(getStudyFile().getParent(), "/Questions/"));
        jNewQuestionsChooser.addChoosableFileFilter(writeQuestionFilter);
        jNewQuestionsChooser.setDialogTitle("Save Custom Questions As...");
        if (JFileChooser.APPROVE_OPTION == jNewQuestionsChooser.showSaveDialog(parent)) {
            try {
                newQuestionFile = ((ExtensionFileFilter) writeQuestionFilter).getCorrectFileName(jNewQuestionsChooser.getSelectedFile());
                if (!newQuestionFile.createNewFile()) {
                    int confirm = JOptionPane.showConfirmDialog(parent, "<HTML><h2>Question File already exists at this location.</h2>" + "<p>Shall I overwrite it?</p></html>", "Overwrite Questions File", JOptionPane.OK_CANCEL_OPTION);
                    if (confirm != JOptionPane.OK_OPTION) {
                        throw new IOException("Won't overwrite " + newQuestionFile.getName());
                    }
                }
                writeAllQuestions(newQuestionFile);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Unable to create question file.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
