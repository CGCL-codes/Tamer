    public void removeFile(RemoteFile rFile) {
        try {
            Channel channel = getChannel(rFile);
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.rm(rFile.getCompletePath());
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
