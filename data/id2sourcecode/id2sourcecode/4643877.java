    public void disconnect(SftpSession session) {
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            channelSftp.getSession().disconnect();
        } catch (JSchException e) {
            String error = "Failed to disconnect.";
            log.error(error, new SftpException(error, e));
        }
    }
