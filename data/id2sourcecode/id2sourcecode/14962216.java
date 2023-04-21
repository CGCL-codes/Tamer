        public void write(char[] cbuf, int off, int len) throws IOException {
            while (len > 0) {
                synchronized (CircularCharBuffer.this) {
                    if (writerClosed) throw new IOException("Writer has been closed; cannot write to a closed Writer.");
                    if (readerClosed) throw new IOException("Buffer closed by Reader; cannot write to a closed buffer.");
                    int spaceLeft = spaceLeft();
                    while (infinite && spaceLeft < len) {
                        resize();
                        spaceLeft = spaceLeft();
                    }
                    if (!blockingWrite && spaceLeft < len) throw new BufferOverflowException("CircularCharBuffer is full; cannot write " + len + " characters");
                    int realLen = Math.min(len, spaceLeft);
                    int firstLen = Math.min(realLen, buffer.length - writePosition);
                    int secondLen = Math.min(realLen - firstLen, buffer.length - markPosition - 1);
                    int written = firstLen + secondLen;
                    if (firstLen > 0) {
                        System.arraycopy(cbuf, off, buffer, writePosition, firstLen);
                    }
                    if (secondLen > 0) {
                        System.arraycopy(cbuf, off + firstLen, buffer, 0, secondLen);
                        writePosition = secondLen;
                    } else {
                        writePosition += written;
                    }
                    if (writePosition == buffer.length) {
                        writePosition = 0;
                    }
                    off += written;
                    len -= written;
                }
                if (len > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception x) {
                        throw new IOException("Waiting for available space in buffer interrupted.");
                    }
                }
            }
        }
