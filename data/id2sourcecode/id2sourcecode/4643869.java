    public SftpResult chgrp(SftpSession session, int gid, String path) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            channelSftp.chgrp(gid, path);
            result.setSuccessFalg(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command chgrp failed.", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
