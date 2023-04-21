    @Override
    public void setMode(int mode) throws IOException {
        SftpATTRS stat;
        ChannelSftp channel;
        try {
            channel = getChannel();
            stat = channel.stat(escape(slashPath));
            stat.setPERMISSIONS(mode);
            channel.setStat(escape(slashPath), stat);
        } catch (SftpException e) {
            throw new IOException(e);
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
