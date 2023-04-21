    public void execute(MojoInfo mojoInfo) throws NsisActionExecutionException {
        int logVerbosity = 0;
        Log log = mojoInfo.getLog();
        if (log.isErrorEnabled()) {
            logVerbosity = 1;
        }
        if (log.isWarnEnabled()) {
            logVerbosity = 2;
        }
        if (log.isInfoEnabled()) {
            logVerbosity = 3;
        }
        if (log.isDebugEnabled()) {
            logVerbosity = 4;
        }
        Commandline commandline = new Commandline();
        commandline.setExecutable(getNsisExecutablePath(mojoInfo));
        commandline.createArg().setValue("/V" + logVerbosity);
        commandline.createArg().setValue(OP_FILE_SETUP_MUI);
        commandline.setWorkingDirectory(mojoInfo.getWorkDirectory());
        log.info("About to execute: " + commandline.toString());
        try {
            int response = CommandLineUtils.executeCommandLine(commandline, new DefaultConsumer(), new DefaultConsumer());
            if (response != 0) {
                throw new NsisActionExecutionException("NSIS Compiler returned error: " + response);
            }
            File outputFile = new File(mojoInfo.getWorkDirectory(), mojoInfo.getNsisProject().getInstallerSettings().getOutFile());
            FileUtils.copyFileToDirectory(outputFile, new File(mojoInfo.getProject().getBuild().getDirectory()));
            mojoInfo.getProjectHelper().attachArtifact(mojoInfo.getProject(), "exe", "setup", new File(mojoInfo.getProject().getBuild().getDirectory(), outputFile.getName()));
        } catch (CommandLineException e) {
            log.error("Executable failed", e);
            throw new NsisActionExecutionException("Executable failed", e);
        } catch (IOException e) {
            log.error("Error copying final output", e);
            throw new NsisActionExecutionException("Error copying final output", e);
        }
    }
