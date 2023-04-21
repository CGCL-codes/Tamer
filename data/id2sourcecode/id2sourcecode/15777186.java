    @Override
    public long length() throws LengthException {
        try {
            SftpATTRS attrs = getChannel().stat(escape(slashPath));
            if (attrs.isDir()) {
                throw new LengthException(this, new IOException("file expected"));
            }
            return attrs.getSize();
        } catch (SftpException e) {
            throw new LengthException(this, e);
        } catch (JSchException e) {
            throw new LengthException(this, e);
        }
    }
