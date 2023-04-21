        public void write(int c) throws IOException {
            boolean written = false;
            while (!written) {
                synchronized (CircularCharBuffer.this) {
                    if (writerClosed) throw new IOException("Writer has been closed; cannot write to a closed Writer.");
                    if (readerClosed) throw new IOException("Buffer closed by Reader; cannot write to a closed buffer.");
                    int spaceLeft = spaceLeft();
                    while (infinite && spaceLeft < 1) {
                        resize();
                        spaceLeft = spaceLeft();
                    }
                    if (!blockingWrite && spaceLeft < 1) throw new BufferOverflowException("CircularCharBuffer is full; cannot write 1 character");
                    if (spaceLeft > 0) {
                        buffer[writePosition] = (char) (c & 0xffff);
                        writePosition++;
                        if (writePosition == buffer.length) {
                            writePosition = 0;
                        }
                        written = true;
                    }
                }
                if (!written) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception x) {
                        throw new IOException("Waiting for available space in buffer interrupted.");
                    }
                }
            }
        }
