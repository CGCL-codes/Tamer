    public SftpResult rmdir(SftpSession session, String path) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            channelSftp.rmdir(path);
            result.setSuccessFalg(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command rmdir failed.", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
