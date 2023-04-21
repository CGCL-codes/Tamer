    public void copyFileFromRemoteMachineToLocalMachine(File source, File destination) throws Exception {
        String fileName = source.getPath();
        File f = new File("" + System.currentTimeMillis());
        f.deleteOnExit();
        remoteHostClient.setPromptOn(isPrompt());
        remoteHostClient.setAscii(isAscii());
        remoteHostClient.copyFileFromRemoteClientToLocalMachine(fileName, f.getName());
        File transferredFilePath = new File(getFtpServerHome(), f.getName());
        if (!transferredFilePath.exists()) {
            throw new Exception("File transfer completed successfully but file was not found on local file system. Please check whether there is another none aqua active FTP" + "server on this machine. If so either shut it down or update the ftpServerHome to the server's home.");
        }
        FileUtils.copyFile(transferredFilePath, destination);
    }
