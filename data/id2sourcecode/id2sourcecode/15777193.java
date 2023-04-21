    @Override
    public boolean exists() throws ExistsException {
        try {
            getChannel().lstat(escape(slashPath));
            return true;
        } catch (SftpException e) {
            return noSuchFile(e);
        } catch (JSchException e) {
            return noSuchFile(e);
        }
    }
