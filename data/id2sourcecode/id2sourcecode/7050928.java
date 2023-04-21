    void readFile(ByteBuffer buffer) {
        RandomAccessFile raf = null;
        FileChannel fc;
        try {
            raf = (RandomAccessFile) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                public Object run() {
                    try {
                        return new RandomAccessFile(platName, "r");
                    } catch (FileNotFoundException fnfe) {
                    }
                    return null;
                }
            });
            fc = raf.getChannel();
            while (buffer.remaining() > 0 && fc.read(buffer) != -1) {
            }
        } catch (NullPointerException npe) {
        } catch (ClosedChannelException e) {
            try {
                if (raf != null) {
                    raf.close();
                    raf = null;
                }
            } catch (IOException ioe) {
            }
            Thread.interrupted();
            readFile(buffer);
        } catch (IOException e) {
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                }
            }
        }
    }
