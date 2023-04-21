    @Override
    public int getGid() throws IOException {
        try {
            return getChannel().stat(escape(slashPath)).getGId();
        } catch (SftpException e) {
            throw new IOException(e);
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
