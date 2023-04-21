    private synchronized ByteBuffer getBuffer() throws FontFormatException {
        MappedByteBuffer mapBuf = (MappedByteBuffer) bufferRef.get();
        if (mapBuf == null) {
            try {
                RandomAccessFile raf = (RandomAccessFile) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                    public Object run() {
                        try {
                            return new RandomAccessFile(platName, "r");
                        } catch (FileNotFoundException ffne) {
                        }
                        return null;
                    }
                });
                FileChannel fc = raf.getChannel();
                fileSize = (int) fc.size();
                mapBuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
                mapBuf.position(0);
                bufferRef = new WeakReference(mapBuf);
                fc.close();
            } catch (NullPointerException e) {
                throw new FontFormatException(e.toString());
            } catch (ClosedChannelException e) {
                Thread.interrupted();
                return getBuffer();
            } catch (IOException e) {
                throw new FontFormatException(e.toString());
            }
        }
        return mapBuf;
    }
