    private static void startRecovery(File file) throws Exception {
        logTitle("starting recovering of \"" + file + "\"");
        File backupFile = new File(file.getAbsolutePath() + ".bak");
        log("backup file " + file + " to " + backupFile);
        FileUtils.copyFile(file, backupFile);
        log();
        log("open file ...");
        openProject(file);
        stmt = PersistenceManager.getInstance().getConnection().createStatement();
        getInfos();
        checkPart();
        checkChapter();
        checkStrand();
        checkNoteFields();
        checkPerson();
        checkItem();
        InternalPeer.setDbModelVersion();
        PersistenceManager.getInstance().closeConnection();
        log();
        log("Recovery has been finished.");
        log("Your original file has been saved as " + backupFile);
        JOptionPane.showMessageDialog(RecoveryTool.getInstance(), "The recovery process has been finished." + "\nPlease copy-paste the log text to a file " + "\nin case the recovery was not sucessful. " + "\n\n*** Close this program before starting Storybook ***", "Recovery process finished", JOptionPane.INFORMATION_MESSAGE);
    }
