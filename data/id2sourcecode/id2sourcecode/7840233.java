    public static void generateSprintReport(final Sprints sprints, final String pdfFileName, final String pdfLaTeXFileName) {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                File tempFile;
                try {
                    tempFile = File.createTempFile("sprintreport", ".tex");
                    sprints.writeToFile(tempFile.getAbsolutePath());
                    execute(pdfLaTeXFileName, tempFile.getAbsolutePath());
                    String generatedFile = tempFile.getAbsolutePath();
                    generatedFile = generatedFile.substring(0, generatedFile.length() - 4);
                    generatedFile += ".pdf";
                    File file = new File(generatedFile);
                    if (file.exists()) {
                        FileUtils.copyFile(file, new File(pdfFileName));
                        ProductPropertyChangeSupport.getInstance().sprintPdfFileGenerated(pdfFileName);
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
