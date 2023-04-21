    @Override
    public int getMode() throws IOException {
        try {
            return getChannel().stat(escape(slashPath)).getPermissions() & 0777;
        } catch (SftpException e) {
            throw new IOException(e);
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
