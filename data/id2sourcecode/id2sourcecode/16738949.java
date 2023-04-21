    protected synchronized void store() throws DataStoreException {
        try {
            super.write(this.data, readerWriter.getOutputStream());
            this.fileExistsOnDisk = true;
        } catch (DataStoreException d) {
            throw new DataStoreException("Error writing data to:" + readerWriter.toString(), d);
        }
    }
