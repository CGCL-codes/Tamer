    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        javax.swing.filechooser.FileFilter fileFilter = new ChemFileFilter();
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setApproveButtonText("Save");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Please select a file name to save as");
        String curFileName = getFileName();
        File curFile = null;
        if (null != curFileName) {
            curFile = new File(curFileName);
            if (curFile.exists()) {
                fileChooser.setSelectedFile(curFile);
            }
        }
        int result = fileChooser.showSaveDialog(mMainFrame);
        if (JFileChooser.APPROVE_OPTION == result) {
            boolean doSave = false;
            File outputFile = fileChooser.getSelectedFile();
            String fileName = outputFile.getAbsolutePath();
            if (outputFile.exists()) {
                if (null == curFile || curFileName.equals(fileName)) {
                    doSave = true;
                } else {
                    SimpleTextArea textArea = new SimpleTextArea("The file you selected already exists:\n" + fileName + "\nThe save operation will overwrite this file.\nAre you sure you want to proceed?");
                    JOptionPane optionPane = new JOptionPane();
                    optionPane.setMessage(textArea);
                    optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                    optionPane.setOptionType(JOptionPane.YES_NO_OPTION);
                    optionPane.createDialog(mMainFrame, "Overwrite existing file?").setVisible(true);
                    Integer response = (Integer) optionPane.getValue();
                    if (null != response && response.intValue() == JOptionPane.YES_OPTION) {
                        doSave = true;
                    } else {
                        if (null == response) {
                            handleCancel("save");
                        }
                    }
                }
            } else {
                doSave = true;
            }
            if (doSave && null != curFileName && !(curFileName.equals(fileName))) {
                setParserAliasLabel(null);
                setModelBuilder(null);
            }
            if (doSave) {
                saveEditBufferToFile(fileName);
                mMainApp.updateMenus();
                mMainApp.setCurrentDirectory(outputFile.getParentFile());
                mMainApp.setCurrentFile(outputFile);
                HistoryUtilImpl.updateFileHistory(outputFile);
            }
        }
    }
