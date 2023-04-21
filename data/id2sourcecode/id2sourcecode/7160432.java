    public void sync(boolean metadata) throws IOException {
        if (mReadOnly) {
            return;
        }
        RandomAccessFile file = accessFile();
        try {
            file.getChannel().force(metadata);
        } finally {
            yieldFile(file);
        }
    }
