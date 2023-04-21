    @Override
    public long getLastModified() throws GetLastModifiedException {
        try {
            return 1000L * getChannel().stat(escape(slashPath)).getMTime();
        } catch (SftpException e) {
            throw new GetLastModifiedException(this, e);
        } catch (JSchException e) {
            throw new GetLastModifiedException(this, e);
        }
    }
