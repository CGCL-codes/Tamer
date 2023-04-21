    public synchronized long fileSize() throws DbException {
        assert (file != null);
        try {
            return file.getChannel().size();
        } catch (IOException e) {
            throw new DbException(DbErrorCode.IOERR, e);
        }
    }
