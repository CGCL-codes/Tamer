    public SftpResult lpwd(SftpSession session) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        String currentPath = channelSftp.lpwd();
        result.setExtension(currentPath);
        result.setSuccessFalg(true);
        return result;
    }
