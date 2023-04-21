    private synchronized void writeImpl(byte[] data, int offset, int length) throws IOException {
        if (writer == null) {
            writer = Thread.currentThread();
        }
        if (eof || closed) {
            throw new IOException("Stream closed");
        } else {
            int written = 0;
            try {
                do {
                    int available = checkedAvailable(WRITER);
                    int contiguous = capacity - (writex % capacity);
                    int amount = (length > available) ? available : length;
                    if (amount > contiguous) {
                        System.arraycopy(data, offset, buffer, writex % capacity, contiguous);
                        System.arraycopy(data, offset + contiguous, buffer, 0, amount - contiguous);
                    } else {
                        System.arraycopy(data, offset, buffer, writex % capacity, amount);
                    }
                    processed(WRITER, amount);
                    written += amount;
                } while (written < length);
            } catch (InterruptedIOException ex) {
                ex.bytesTransferred = written;
                throw ex;
            }
        }
    }
