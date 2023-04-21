    public SftpResult ln(SftpSession session, String src, String link) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            channelSftp.symlink(src, link);
            result.setSuccessFalg(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command ln failed.", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
