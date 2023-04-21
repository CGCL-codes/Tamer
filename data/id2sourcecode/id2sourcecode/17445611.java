            public int read(byte[] b, int off, int len) {
                if (!isOpen) return len;
                int bytesLeft = len;
                while (bytesLeft > 0) {
                    int templen = bytesLeft > buf.capacity() ? buf.capacity() : bytesLeft;
                    for (int n = 0; n < floatBuffer.length; n++) floatBuffer[n] = 0;
                    fillBuffer(floatBuffer, templen / format.getFrameSize(), format.getChannels());
                    flView.position(0);
                    flView.put(floatBuffer);
                    buf.position(0);
                    buf.get(b, off, templen);
                    off += buf.capacity();
                    bytesLeft -= templen;
                }
                return len;
            }
