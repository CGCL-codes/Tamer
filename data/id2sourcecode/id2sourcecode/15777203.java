    @Override
    public int getUid() throws IOException {
        try {
            return getChannel().stat(escape(slashPath)).getUId();
        } catch (SftpException e) {
            throw new IOException(e);
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
