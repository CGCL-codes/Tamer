    public void launchEditor(final SutEditor editor) {
        if (!validateSUTFile()) {
            return;
        }
        WaitDialog.launchWaitDialog("SUT editing ...", null, "(close sut editor to continue)", false);
        (new Thread() {

            public void run() {
                try {
                    if (editor == null) {
                        return;
                    }
                    Document doc = null;
                    try {
                        doc = editor.editSut(SutFactory.getInstance().getSutInstance().getOriginalDocument(), true);
                        if (doc == null) {
                            return;
                        }
                        File sutFile = SutFactory.getInstance().getSutFile();
                        FileUtils.saveDocumentToFile(doc, sutFile);
                        SutFactory.getInstance().setSut(sutFile.getName());
                        copySutToTestsFolder(sutFile);
                        ListenerstManager.getInstance().sutChanged(sutFile.getName());
                    } catch (Exception e) {
                        ErrorPanel.showErrorDialog("SUT Editor Fail", StringUtils.getStackTrace(e), ErrorLevel.Error);
                        return;
                    }
                } finally {
                    WaitDialog.endWaitDialog();
                }
            }

            private void copySutToTestsFolder(File sutFile) {
                File classDir = new File(JSystemProperties.getCurrentTestsPath());
                if (sutFile.getAbsolutePath().startsWith(classDir.getAbsolutePath())) {
                    File sutSrcFile = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.TESTS_SOURCE_FOLDER) + sutFile.getAbsolutePath().substring(classDir.getAbsolutePath().length()));
                    try {
                        FileUtils.copyFile(sutFile, sutSrcFile);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Failed updating SUT file", e);
                    }
                }
            }
        }).start();
    }
