    public void copyTestSuite(String sourceTestDir) {
        String sourceFileName = sourceTestDir + File.separator + StaticConfiguration.TEST_SCRIPT_FILENAME;
        String destFileName = mTestSuiteDir + File.separator + mTestName + File.separator + StaticConfiguration.TEST_SCRIPT_FILENAME;
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            if (JOptionPane.showConfirmDialog(null, "Test " + mTestName + " already exists. Do you want to overwrite the already existing test?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return;
        }
        FileUtilities.copy(sourceFileName, destFileName);
        copyTestData(sourceTestDir, mTestSuiteDir + File.separator + mTestName);
    }
