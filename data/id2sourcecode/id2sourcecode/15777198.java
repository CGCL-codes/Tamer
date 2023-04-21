    @Override
    public String readLink() throws ReadLinkException {
        try {
            return getChannel().readlink(escape(slashPath));
        } catch (SftpException e) {
            throw new ReadLinkException(this, e);
        } catch (JSchException e) {
            throw new ReadLinkException(this, e);
        }
    }
