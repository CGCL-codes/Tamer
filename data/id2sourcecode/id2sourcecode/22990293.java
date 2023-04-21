    public ChannelSftp getChannelFtp() throws JSchException {
        if (channelFtp == null) {
            channelFtp = (ChannelSftp) session.openChannel("sftp");
            channelFtp.connect();
        }
        return channelFtp;
    }
