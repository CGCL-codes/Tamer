    public void playback(PagedLogPlayback callback) throws DBException, TransactionException {
        try {
            callback.beginPlayback();
            if (exists()) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                FileChannel fc = raf.getChannel();
                long fileLen = raf.length();
                while (raf.getFilePointer() < fileLen) {
                    byte event = raf.readByte();
                    switch(event) {
                        case EVENT_START:
                            callback.start(raf.readLong());
                            break;
                        case EVENT_WRITE:
                            long transactionID = raf.readLong();
                            long offset = raf.readLong();
                            int size = raf.readInt();
                            byte[] b = new byte[size];
                            raf.read(b);
                            ByteBuffer buffer = ByteBuffer.wrap(b);
                            callback.write(transactionID, offset, buffer);
                            break;
                        case EVENT_COMMIT:
                            callback.commit(raf.readLong());
                            break;
                        case EVENT_CANCEL:
                            callback.cancel(raf.readLong());
                            break;
                        case EVENT_CHKPNT:
                            callback.checkpoint();
                            break;
                    }
                }
                raf.close();
            }
        } catch (IOException e) {
            throw new DBException(FaultCodes.GEN_CRITICAL_ERROR, "Error playing back " + file.getName(), e);
        } finally {
            callback.endPlayback();
        }
    }
