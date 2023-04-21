    public void createTestSuite() {
        BufferedWriter output = null;
        String outputFileName = mTestSuiteDir + File.separator + mTestName + File.separator + StaticConfiguration.TEST_SCRIPT_FILENAME;
        File destFile = new File(outputFileName);
        if (destFile.exists()) {
            if (JOptionPane.showConfirmDialog(null, "Test " + mTestName + " already exists. Do you want to overwrite the already existing test?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return;
        }
        try {
            String templateFile = TEMPLATE_DIR + File.separator + StaticConfiguration.TEST_SCRIPT_FILENAME;
            String strContents = getTemplateContent(templateFile);
            strContents = strContents.replace("[$TEST_NAME]", mTestName);
            File outputFile = new File(outputFileName);
            outputFile.getParentFile().mkdirs();
            output = new BufferedWriter(new FileWriter(outputFile));
            output.append(strContents);
            output.close();
            copyTestData(TEMPLATE_DIR, mTestSuiteDir + File.separator + mTestName);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    logger.error("Error during creation of TestScript: " + ex.getMessage());
                }
            } else {
                logger.error("Error during creation of TestScript");
            }
        }
    }
