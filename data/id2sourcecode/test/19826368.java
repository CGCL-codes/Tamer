    @Override
    public void close() {
        if (isClosed) return;
        super.close();
        if (randomAccessFile != null) {
            try {
                if (size() == 0) {
                    randomAccessFile.close();
                    if (file != null) file.delete(); else fso.deleteFile(filename);
                } else {
                    randomAccessFile.seek(randomAccessFile.length());
                    randomAccessFile.writeInt(size);
                    randomAccessFile.writeLong(readOffset);
                    randomAccessFile.close();
                }
                randomAccessFile = null;
            } catch (IOException ioe) {
                throw new WrappingRuntimeException(ioe);
            }
        }
    }
