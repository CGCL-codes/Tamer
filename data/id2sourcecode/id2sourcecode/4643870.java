    public SftpResult chmod(SftpSession session, int mode, String path) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            channelSftp.chmod(mode, path);
            result.setSuccessFalg(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command chmod failed.", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
