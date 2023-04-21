    public void executeRemoteCommand(String dirName, String command, Map<String, String> envVars) throws ExecutorException {
        logger.info("Asked to run remote command " + command);
        if (!isSandBoxUp()) {
            IllegalStateException illegalStateException = new IllegalStateException("VServer environment is not running. Can not execute commands.");
            throw new ExecutorException(illegalStateException);
        }
        Map<String, String> clone = new HashMap<String, String>();
        clone.putAll(envVars);
        clone.remove(WorkerConstants.ENV_PLAYPEN);
        clone.remove(WorkerConstants.ENV_STORAGE);
        clone.put(WorkerConstants.ENV_PLAYPEN, playPenVmDir);
        clone.put(WorkerConstants.ENV_STORAGE, storageVmDir);
        File remoteScript = unixFolderUtil.createScript(command, host_playpen_path, clone);
        try {
            FileUtils.copyFile(remoteScript, ourGridAppFile);
        } catch (IOException e) {
            throw new ExecutorException("Unable to create remote execution script", e);
        }
        executeRemoteCommand(playPenVmDir);
    }
