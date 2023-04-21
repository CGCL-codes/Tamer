    protected InputStream doGetInputStream() throws Exception {
        synchronized (fileSystem) {
            final ChannelSftp channel = fileSystem.getChannel();
            try {
                return new SftpInputStream(channel, channel.get(relPath));
            } finally {
            }
        }
    }
