    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (swapOrderBits == 2) {
            if (swapRemainingByte != null && swapRemainingByte.length == 1) {
                buffer.put(b[off]);
                buffer.put(swapRemainingByte[0]);
                off++;
            }
            int modulo = Math.abs(len - off) % swapOrderBits;
            if (modulo != 0) {
                swapRemainingByte = new byte[1];
                len -= modulo;
                System.arraycopy(b, len, swapRemainingByte, 0, modulo);
            }
            for (int i = off; i < len; i += 2) {
                byte temp = b[i];
                b[i] = b[i + 1];
                b[i + 1] = temp;
            }
        }
        buffer.put(b, off, len);
        int remains = buffer.position() - internalMark;
        while (remains > streamableByteNumber || remains > neededByteNumber) {
            if (streamableByteNumber == 0) {
                if (remains > neededByteNumber) {
                    count++;
                    analyzeBuffer(buffer.array(), internalMark, neededByteNumber);
                    if (streamableByteNumber == 0) {
                        throw new IOException("Packet size cannot be Null !");
                    }
                    if (!discard) {
                        beforeChunkSend();
                    }
                } else {
                    buffer.position(internalMark);
                    buffer.compact();
                    buffer.position(remains);
                    internalMark = 0;
                    return;
                }
            }
            if (streamableByteNumber > 0) {
                if (remains >= streamableByteNumber) {
                    if (!discard) {
                        out.write(buffer.array(), internalMark, streamableByteNumber);
                    }
                    internalMark += streamableByteNumber;
                    remains = remains - streamableByteNumber;
                    streamableByteNumber = 0;
                    if (!discard) {
                        afterChunkSend();
                    }
                    if (remains == 0) {
                        buffer.position(0);
                        internalMark = 0;
                    }
                } else {
                    if (!discard) {
                        out.write(buffer.array(), internalMark, remains);
                    }
                    streamableByteNumber = streamableByteNumber - remains;
                    buffer.position(0);
                    internalMark = 0;
                    remains = 0;
                }
            }
        }
    }
