    public SftpResult version(SftpSession session) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        result.setExtension(channelSftp.version());
        result.setSuccessFalg(true);
        return result;
    }
