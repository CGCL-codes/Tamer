    private SftpResult put(SftpSession session, String localFilename, String remoteFilename, BaseProgressListener progressListener) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            channelSftp.put(localFilename, remoteFilename, progressListener);
            result.setSuccessFalg(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command put failed", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
