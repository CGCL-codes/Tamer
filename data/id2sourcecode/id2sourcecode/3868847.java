    private void sendFileList(Protocol protocol) throws IOException, ProtocolException, DatabaseException {
        protocol.read(namedChannel.getChannel(), Protocol.FileListReq.class);
        if (dbBackup == null) {
            dbBackup = new DbBackup(feederManager.getEnvImpl());
            dbBackup.startBackup();
        } else {
            feederManager.leaseRenewalCount++;
        }
        String[] files = dbBackup.getLogFilesInBackupSet();
        protocol.write(protocol.new FileListResp(files), namedChannel);
    }
