    public SftpResult cd(SftpSession session, String path) {
        SftpResultImpl result = new SftpResultImpl();
        SftpSessionImpl sessionImpl = (SftpSessionImpl) session;
        ChannelSftp channelSftp = sessionImpl.getChannelSftp();
        try {
            channelSftp.cd(path);
            result.setSuccessFalg(true);
            sessionImpl.setDirChanged(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command cd failed.", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
