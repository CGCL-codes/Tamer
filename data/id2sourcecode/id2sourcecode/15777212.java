    public void get(OutputStream out) throws IOException {
        try {
            getChannel().get(escape(slashPath), out);
        } catch (SftpException e) {
            if (e.id == 2 || e.id == 4) {
                throw new FileNotFoundException(getPath());
            }
            throw new IOException(e);
        } catch (JSchException e) {
            throw new IOException(e);
        }
    }
