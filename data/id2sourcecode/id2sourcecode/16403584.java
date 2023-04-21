    public OutputStream getOutputStream(final File file) throws XAException, FileNotFoundException {
        if (xidThreadLocal.get() == null) {
            throw new XAException();
        }
        OutputStream os = new OutputStream() {

            @Override
            public void close() throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                if (lockedXid != null && lockedXid != xidThreadLocal.get()) {
                    throw new IOException("This directory [" + directory + "] has been locked by anthoer thread, you can't write it");
                }
                lockedXid = xidThreadLocal.get();
                byte[] data = modifiedDataMapping.get(xidThreadLocal.get()).get(file);
                int srcOff = 0;
                if (data == null) {
                    data = new byte[len];
                } else {
                    byte[] old = data;
                    data = new byte[data.length + len];
                    srcOff = old.length;
                    for (int i = 0; i < old.length; i++) {
                        data[i] = old[i];
                    }
                }
                for (int i = srcOff; i < data.length; i++) {
                    data[i] = b[i - srcOff];
                }
                modifiedDataMapping.get(xidThreadLocal.get()).put(file, data);
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }

            @Override
            public void write(int b) throws IOException {
                write(new byte[] { (byte) b });
            }
        };
        return os;
    }
