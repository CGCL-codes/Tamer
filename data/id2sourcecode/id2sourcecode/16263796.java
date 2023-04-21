    public void getFile(Connection connection, RemoteFile rFile, String lFile) {
        try {
            Channel channel = getChannel(rFile);
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            FileOutputStream fos = new FileOutputStream(lFile);
            sftpChannel.get(rFile.getCompletePath(), fos);
            fos.flush();
            fos.close();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
