    @Override
    public boolean isFile() throws ExistsException {
        try {
            return !getChannel().stat(escape(slashPath)).isDir();
        } catch (SftpException e) {
            return noSuchFile(e);
        } catch (JSchException e) {
            return noSuchFile(e);
        }
    }
