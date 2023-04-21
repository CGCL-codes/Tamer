    public void removeFolder(RemoteFile rFolder) {
        try {
            Channel channel = getChannel(rFolder);
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.rmdir(rFolder.getCompletePath());
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
