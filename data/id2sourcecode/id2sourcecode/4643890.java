    public SftpResult ls(SftpSession session, String path) {
        SftpResultImpl result = new SftpResultImpl();
        ChannelSftp channelSftp = ((SftpSessionImpl) session).getChannelSftp();
        try {
            String absoultPath = remoteAbsolutePath(session, path);
            List<SftpFileImpl> fileList = new Vector<SftpFileImpl>();
            Vector<ChannelSftp.LsEntry> vv = channelSftp.ls(path);
            Iterator<ChannelSftp.LsEntry> it = vv.iterator();
            while (it.hasNext()) {
                fileList.add(new SftpFileImpl(it.next(), absoultPath));
            }
            result.setExtension(fileList);
            result.setSuccessFalg(true);
        } catch (com.jcraft.jsch.SftpException e) {
            log.error("command ls failed.", e);
            result.setErrorMessage(e.toString());
            result.setErrorCode(e.id);
        }
        return result;
    }
