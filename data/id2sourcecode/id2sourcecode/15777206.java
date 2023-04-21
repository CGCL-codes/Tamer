    @Override
    public void setGid(int gid) throws IOException {
        String str;
        SftpATTRS stat;
        ChannelSftp channel;
        try {
            if (isDirectory()) {
                str = getRoot().exec("chgrp", Integer.toString(gid), slashPath);
                if (str.length() > 0) {
                    throw new IOException("chgrp failed:" + str);
                }
            } else {
                channel = getChannel();
                stat = channel.stat(escape(slashPath));
                stat.setUIDGID(stat.getUId(), gid);
                channel.setStat(escape(slashPath), stat);
            }
        } catch (JSchException e) {
            throw new IOException(e);
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }
