    @Override
    public Node mkdir() throws MkdirException {
        try {
            getChannel().mkdir(escape(slashPath));
            return this;
        } catch (SftpException e) {
            throw new MkdirException(this, e);
        } catch (JSchException e) {
            throw new MkdirException(this, e);
        }
    }
