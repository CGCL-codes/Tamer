    synchronized int readImpl(byte[] b, int off, int len, boolean isSpeaker) {
        int avail, oldLag;
        if (isSpeaker) {
            avail = availableReadSpeaker();
            oldLag = speakerLag;
        } else {
            avail = availableReadRecorder();
            oldLag = recorderLag;
        }
        if (isSpeaker) {
            hasReadSpeaker = true;
        } else {
            hasReadRecorder = true;
        }
        int si = getEffectiveSize();
        int fs = getFormat().getFrameSize();
        if (fs < 1) fs = 1;
        int maxRead = len;
        if (maxRead > avail) maxRead = avail;
        maxRead = Utils.align(maxRead, fs);
        if (DEBUG) {
            if (off + maxRead > b.length || off + len > b.length) {
                Debug.out("## Illegal params to CircularBuffer.readImpl: b[" + b.length + "], off=" + off + "  len=" + len + "  maxRead=" + maxRead);
            }
        }
        if (isSpeaker) {
            speakerLag -= maxRead;
        } else {
            recorderLag -= maxRead;
        }
        int readPos = ((writePos - oldLag) + si) % si;
        len = maxRead;
        while (len > 0) {
            int thisLen = len;
            if ((readPos + thisLen) > si) {
                thisLen = si - readPos;
            }
            if (DEBUG_IO) {
                Debug.out("Reading from buffer[" + buffer.length + "], readPos=" + readPos + " to " + (readPos + thisLen - 1));
                Debug.out("          to b[" + b.length + "], off=" + off + " to " + (off + thisLen - 1));
            }
            if (DEBUG) {
                if (readPos + thisLen > buffer.length || thisLen < 0 || readPos < 0) {
                    Debug.out("## Error: CircularBuffer.readImpl: copying from buffer[" + buffer.length + "] readPos=" + readPos + " to " + (readPos + thisLen - 1));
                }
                if (off + thisLen > b.length || thisLen < 0 || off < 0) {
                    Debug.out("## Error: CircularBuffer.readImpl: copying to b[" + b.length + "] off=" + off + " to " + (off + thisLen - 1));
                }
            }
            System.arraycopy(buffer, readPos, b, off, thisLen);
            readPos += thisLen;
            off += thisLen;
            if (readPos >= si + 1 - fs) {
                readPos -= si;
            }
            len -= thisLen;
        }
        return maxRead;
    }
