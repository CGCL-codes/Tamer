    @Override
    public void setUid(int uid) throws IOException {
        String str;
        SftpATTRS stat;
        ChannelSftp channel;
        try {
            if (isDirectory()) {
                str = getRoot().exec("chown", Integer.toString(uid), escape(slashPath));
                if (str.length() > 0) {
                    throw new IOException("chown failed:" + str);
                }
            } else {
                channel = getChannel();
                stat = channel.stat(escape(slashPath));
                stat.setUIDGID(uid, stat.getGId());
                channel.setStat(escape(slashPath), stat);
            }
        } catch (JSchException e) {
            throw new IOException(e);
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }
