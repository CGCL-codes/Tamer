    @Override
    public void setLastModified(long millis) throws SetLastModifiedException {
        try {
            getChannel().setMtime(escape(slashPath), (int) (millis / 1000));
        } catch (SftpException e) {
            throw new SetLastModifiedException(this, e);
        } catch (JSchException e) {
            throw new SetLastModifiedException(this, e);
        }
    }
