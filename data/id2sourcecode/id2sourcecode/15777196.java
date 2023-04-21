    @Override
    public boolean isLink() throws ExistsException {
        try {
            return getChannel().lstat(escape(slashPath)).isLink();
        } catch (SftpException e) {
            return noSuchFile(e);
        } catch (JSchException e) {
            return noSuchFile(e);
        }
    }
