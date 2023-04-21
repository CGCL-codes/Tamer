    public void run() {
        byte[] bytes = new byte[2352];
        while (buffer.isOpen()) {
            try {
                int read = ais.read(bytes);
                if (read == -1) {
                    close();
                } else {
                    buffer.write(bytes, 0, read);
                }
            } catch (IOException ioe) {
                try {
                    close();
                } catch (IOException ioe2) {
                }
            }
        }
        synchronized (this) {
            thread = null;
            notifyAll();
        }
    }
