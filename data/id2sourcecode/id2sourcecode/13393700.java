    @Post
    public void acceptObject(String object) {
        FileOutputStream fout = null;
        FileLock lock = null;
        try {
            fout = new FileOutputStream(this.app.getStorageDirectory() + File.separator + cachedFileName);
            lock = fout.getChannel().lock();
            fout.write(object.getBytes());
        } catch (FileNotFoundException ex) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        } catch (IOException ex) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        } finally {
            try {
                fout.close();
                lock.release();
            } catch (IOException ex) {
            }
        }
    }
