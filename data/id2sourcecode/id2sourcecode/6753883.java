    private void executeRemoteCommand(String dirName, String command, Map<String, String> envVars) throws ExecutorException {
        try {
            String env_storage = envVars.get(WorkerConstants.ENV_STORAGE);
            command = command.replace(env_storage, vmStorage.getName());
            logger.info("Asked to run command " + command);
            Map<String, String> clone = new HashMap<String, String>();
            clone.putAll(envVars);
            clone.remove(WorkerConstants.ENV_PLAYPEN);
            clone.remove(WorkerConstants.ENV_STORAGE);
            File script = unixFolderUtil.createScript(command, dirName, clone);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(virtualEnvPropertiesFile)));
            writer.println(VIRTUAL_ENV_APP_SCRIPT_PROP + "=" + execFile.getName());
            writer.println(VIRTUAL_ENV_APP_EXIT_PROP + "=" + exitStatus.getName());
            writer.println(VIRTUAL_ENV_APP_STDOUT_PROP + "=" + stdOut.getName());
            writer.println(VIRTUAL_ENV_APP_STDERR_PROP + "=" + stdErr.getName());
            writer.println(VIRTUAL_ENV_TERMINATION_FILE_PROP + "=" + terminationFile.getName());
            writer.println(VIRTUAL_ENV_STORAGE_NAME_PROP + "=" + vmStorage.getName());
            try {
                if (writer.checkError()) {
                    throw new IOException("Unable to create Virtual environment");
                }
            } finally {
                writer.close();
            }
            vmStorage.mkdirs();
            FileUtils.copyFile(script, execFile);
            unixFolderUtil.copyStorageFiles(envVars, vmStorage);
        } catch (IOException e) {
            throw new ExecutorException("Unable to create remote execution script", e);
        }
        logger.debug("About to start secure environment");
        ExecutorHandle internalHandle = this.executor.execute(dirName, startvmCmd + " " + vBoxLocation + " " + machineName + " " + ("\"" + playpen.getAbsolutePath() + "\""));
        ExecutorResult result = this.executor.getResult(internalHandle);
        logger.debug("Result: " + result);
        if (result.getExitValue() != 0) {
            cleanup();
            throw new ExecutorException("Unable to start virtual environment \n" + result);
        }
    }
