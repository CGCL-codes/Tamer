    synchronized void write(byte[] arr, int offset, int length) throws IOException {
        synchronized (writeLock) {
            if (readerClosed || writerClosed) throw new IOException("Stream closed");
            if (growBuf && (length > availableSpace())) growBuf(length - availableSpace());
            while (length > 0) {
                while (availableSpace() == 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }
                int availableSpace = availableSpace();
                int amountToWrite = length > availableSpace ? availableSpace : length;
                int part1Size = buf.length - writeIndex >= amountToWrite ? amountToWrite : buf.length - writeIndex;
                int part2Size = amountToWrite - part1Size > 0 ? amountToWrite - part1Size : 0;
                System.arraycopy(arr, offset, buf, writeIndex, part1Size);
                System.arraycopy(arr, offset + part1Size, buf, 0, part2Size);
                offset += amountToWrite;
                length -= amountToWrite;
                writeIndex = (writeIndex + amountToWrite) % buf.length;
                notifyAll();
            }
        }
    }
