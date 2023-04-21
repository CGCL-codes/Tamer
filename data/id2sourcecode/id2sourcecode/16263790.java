    public void putFile(File lFile, RemoteFile rFolder) {
        try {
            Channel channel = getChannel(rFolder);
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.put(lFile.getAbsolutePath(), rFolder.getCompletePath());
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
