    public synchronized void write(byte[] b, int off, int len) throws IOException {
        int si = getEffectiveSize();
        int fs = getFormat().getFrameSize();
        if (fs < 1) fs = 1;
        len = Utils.align(len, fs);
        availRead += len;
        if (availRead > si) availRead = si;
        speakerLag += len;
        recorderLag += len;
        if (speakerLag > si) {
            if (hasReadSpeaker) {
                if (VERBOSE) Debug.out("speaker buffer underrun");
                hasReadSpeaker = false;
            }
            speakerLag = si;
        }
        if (recorderLag > si) {
            if (hasReadRecorder) {
                if (VERBOSE) Debug.out("recorder buffer underrun");
                hasReadRecorder = false;
            }
            recorderLag = si;
        }
        while (len > 0) {
            int thisLen = len;
            if ((len + writePos) > si) {
                thisLen = si - writePos;
            }
            if (DEBUG_IO) {
                Debug.out("Writing from b[" + b.length + "], off=" + off + " to " + (off + thisLen - 1));
                Debug.out("          to buffer[" + buffer.length + "], writePos=" + writePos + " to " + (writePos + thisLen - 1));
            }
            if (DEBUG) {
                if (off + thisLen > b.length || thisLen < 0 || off < 0) {
                    Debug.out("## Error: CircularBuffer.readImpl: copying from b[" + b.length + "] off=" + off + " to " + (off + thisLen - 1));
                }
                if (writePos + thisLen > buffer.length || thisLen < 0 || writePos < 0) {
                    Debug.out("## Error: CircularBuffer.write: copying to buffer[" + buffer.length + "] readPos=" + writePos + " to " + (writePos + thisLen - 1));
                }
            }
            System.arraycopy(b, off, buffer, writePos, thisLen);
            writePos += thisLen;
            off += thisLen;
            if (writePos >= si + 1 - fs) {
                writePos -= si;
            }
            len -= thisLen;
        }
        synchronized (speakerReaderLock) {
            speakerReaderLock.notifyAll();
        }
        synchronized (recorderReaderLock) {
            recorderReaderLock.notifyAll();
        }
    }
