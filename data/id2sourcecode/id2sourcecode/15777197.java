    @Override
    public void mklink(String target) throws LinkException {
        try {
            checkNotExists();
            getParent().checkDirectory();
            getChannel().symlink(target, escape(slashPath));
        } catch (SftpException e) {
            throw new LinkException(this, e);
        } catch (JSchException e) {
            throw new LinkException(this, e);
        } catch (IOException e) {
            throw new LinkException(this, e);
        }
    }
