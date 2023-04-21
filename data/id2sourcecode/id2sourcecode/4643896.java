    public SftpResult pwd(SftpSession session) {
        SftpResultImpl result = new SftpResultImpl();
        SftpSessionImpl sessionImpl = (SftpSessionImpl) session;
        if (!sessionImpl.getDirChanged()) {
            result.setExtension(sessionImpl.getCurrentPath());
            result.setSuccessFalg(true);
        } else {
            ChannelSftp channelSftp = sessionImpl.getChannelSftp();
            try {
                String currentPath = channelSftp.pwd();
                result.setExtension(currentPath);
                result.setSuccessFalg(true);
                sessionImpl.setCurrentPath(currentPath);
            } catch (com.jcraft.jsch.SftpException e) {
                log.error("command pwd failed.", e);
                result.setErrorMessage(e.toString());
                result.setErrorCode(e.id);
            }
        }
        return result;
    }
