    public final void testDoPoll() throws Exception {
        File inputFile = new File(Helpers.testPDFInputBurstDocumentPath);
        File watchedDir = new File(Helpers.testOutputPollFolder);
        FileUtils.copyFileToDirectory(inputFile, watchedDir);
        Settings settings = new Settings() {

            public String getOutputFolder() {
                return Helpers.testOutputOutputFolder;
            }

            ;

            public String getPollFolder() {
                return Helpers.testOutputPollFolder;
            }

            ;

            public String getBackupFolder() {
                return Helpers.testOutputBackupFolder;
            }

            ;
        };
        settings.loadSettings(Helpers.testConfigFile);
        job = new CliJob(settings) {

            protected String getPollPidFilePath() {
                return Helpers.testOutputTempFolder + "/poll.pid";
            }

            ;

            protected String getJobFilePath(String filePath, String jobType) {
                return Helpers.testOutputTempFolder + "/" + Utils.getJobFileName(filePath, "burst");
            }

            ;

            protected PdfBurster getBurster(String filePath) throws Exception {
                return new PdfBurster() {

                    protected void executeController() throws Exception {
                        scripting.setRoots(new String[] { Helpers.testScriptsFolder });
                        scripting.executeScript(Scripts.CONTROLLER, this.ctx);
                        ctx.variables = new Variables("burst.pdf", ctx.settings.getLanguage(), ctx.settings.getCountry(), ctx.settings.getNumberOfUserVariables());
                    }

                    ;
                };
            }
        };
        Thread pollPidChecker = new Thread() {

            public void run() {
                try {
                    Thread.sleep(10000);
                    File pollPidFile = new File(Helpers.testOutputTempFolder + "/poll.pid");
                    pollPidFile.delete();
                } catch (InterruptedException e) {
                }
            }
        };
        pollPidChecker.start();
        job.doPoll(null);
        assertTrue(true);
    }
