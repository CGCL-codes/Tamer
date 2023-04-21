    public void populateChildren(RemoteFile remoteFolder) {
        List<RemoteFile> files = new ArrayList<RemoteFile>();
        if (remoteFolder == null || remoteFolder.getConnection() == null) {
            return;
        }
        try {
            Channel channel = getChannel(remoteFolder);
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            Vector<Object> objs = sftpChannel.ls(remoteFolder.getCompletePath());
            for (Object obj : objs) {
                if (obj instanceof ChannelSftp.LsEntry) {
                    ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) obj;
                    if (!(".".equals(entry.getFilename()) || "..".equals(entry.getFilename()))) {
                        RemoteFile rf = new RemoteFile();
                        rf.setCompletePath(sftpChannel.pwd() + "/" + entry.getFilename());
                        rf.setName(entry.getFilename());
                        com.jcraft.jsch.SftpATTRS attrs = entry.getAttrs();
                        if (attrs.isLink()) {
                            rf.setCompletePath(sftpChannel.readlink(rf.getCompletePath()));
                            SftpATTRS lnAttr = sftpChannel.lstat(rf.getCompletePath());
                            if (lnAttr.isDir()) {
                                rf.setFile(false);
                            } else {
                                rf.setFile(true);
                            }
                        } else {
                            if (attrs.isDir()) {
                                rf.setFile(false);
                            } else {
                                rf.setFile(true);
                            }
                        }
                        rf.setParent(remoteFolder);
                        rf.setConnection(remoteFolder.getConnection());
                        rf.setChannel(sftpChannel);
                        files.add(rf);
                    }
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        Collections.sort(files);
        remoteFolder.setChildren(files);
    }
