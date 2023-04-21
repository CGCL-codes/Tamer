    public void readRow(byte[] data) throws IOException {
        int i, num;
        byte b;
        if (bufOffset >= bufSize) {
            if (stripLeft == 0) {
                seekNewStrip();
            }
            num = Math.min(bufSize, stripLeft);
            readFully(buf, bufSize - num, num);
            bufOffset = bufSize - num;
            stripLeft -= num;
            if (suckyPCdata && (stream.bitsPerSmp == 16)) {
                for (i = 0; i < bufSize; i += 2) {
                    b = data[i];
                    data[i] = data[i + 1];
                    data[i + 1] = b;
                }
            }
        }
        System.arraycopy(buf, bufOffset, data, 0, data.length);
        bufOffset += data.length;
        stream.rowsRead++;
        if (invert) {
            for (i = 0; i < data.length; i++) {
                data[i] = (byte) ~data[i];
            }
        }
    }
