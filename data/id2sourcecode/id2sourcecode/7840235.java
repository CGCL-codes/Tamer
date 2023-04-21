    public static void generateRequirementReport(final Requirements requirements, final String pdfFileName, final String pdfLaTeXFileName) {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                File tempFile;
                try {
                    tempFile = File.createTempFile("requirements", ".tex");
                    requirements.writeToFile(tempFile.getAbsolutePath());
                    execute(pdfLaTeXFileName, tempFile.getAbsolutePath());
                    String generatedFile = tempFile.getAbsolutePath();
                    generatedFile = generatedFile.substring(0, generatedFile.length() - 4);
                    generatedFile += ".pdf";
                    File file = new File(generatedFile);
                    if (file.exists()) {
                        FileUtils.copyFile(file, new File(pdfFileName));
                        ProductPropertyChangeSupport.getInstance().requirementsPdfFileGenerated(pdfFileName);
                    } else {
                        LOGGER.error("File '" + generatedFile + "' does not exist.");
                    }
                    tempFile.delete();
                } catch (IOException e) {
                    LOGGER.error("Sprint report cannot be created.", e);
                }
            }
        });
        thread.start();
    }
