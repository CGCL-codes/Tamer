    public void put(final byte[] data, boolean append) throws JSchException, SftpException {
        getChannel().put(new ByteArrayInputStream(data), escape(slashPath), append ? ChannelSftp.APPEND : ChannelSftp.OVERWRITE);
    }
