    public IdentifyResult identify(DigitalObject digitalObject, List<Parameter> parameters) {
        if (digitalObject.getContent() == null) {
            return this.returnWithErrorMessage("The Content of the DigitalObject should not be NULL.", 1);
        }
        if (!FileServiceSetup.isWindows()) {
            return this.returnWithErrorMessage("OS detected not windows based, this service only runs on windows.", 1);
        }
        if (!FileServiceSetup.isCygwinFileDetected()) {
            return this.returnWithErrorMessage("Cygwin file.exe not found at location given in cygwin.file.location property.", 1);
        }
        byte[] binary = FileUtils.writeInputStreamToBinary(digitalObject.getContent().read());
        File tmpInFile = FileUtils.writeByteArrayToTempFile(binary);
        String[] commands = new String[] { FileServiceSetup.getFileLocation(), "-i", "-b", tmpInFile.getAbsolutePath() };
        ProcessRunner runner = new ProcessRunner();
        runner.setCommand(Arrays.asList(commands));
        runner.run();
        int retCode = runner.getReturnCode();
        if (retCode != 0) {
            return this.returnWithErrorMessage(runner.getProcessErrorAsString(), retCode);
        }
        String mime = runner.getProcessOutputAsString().trim();
        if (mime.indexOf(FileServiceSetup.getProperties().getProperty("cygwin.message.nofile")) != -1) {
            FileIdentify._log.debug("File failed to find an error");
            return this.returnWithErrorMessage(mime, 1);
        }
        ServiceReport rep = new ServiceReport(Type.INFO, Status.SUCCESS, "OK");
        List<URI> types = new ArrayList<URI>();
        URI mimeURI = FormatRegistryFactory.getFormatRegistry().createMimeUri(mime);
        types.add(mimeURI);
        return new IdentifyResult(types, IdentifyResult.Method.MAGIC, rep);
    }
