    @Override
    public int write(char[] buf, int startOffset, int maxLen, boolean flushIt) throws IOException {
        if (!isOpen()) throw new EOFException("No current channel to write to");
        if (maxLen < 0) throw new IOException("Bad/Illegal write max. len=" + maxLen);
        if (maxLen > 0) {
            final byte[] wa = getCacheArray();
            if ((null == wa) || (wa.length <= Byte.MAX_VALUE)) throw new SocketException("write(chars)[" + startOffset + " - " + (startOffset + maxLen) + ") bad internal read buffer");
            for (int writeLen = 0; writeLen < maxLen; ) {
                final int remLen = Math.min((maxLen - writeLen), wa.length), toWrite = StringUtil.toASCIIBytes(buf, writeLen, remLen, wa, 0);
                final int nWritten = writeBytes(wa, 0, toWrite, false);
                if (nWritten != toWrite) throw new IOException("Partial write mismatch (" + nWritten + " <> " + toWrite + ")");
                writeLen += nWritten;
            }
        }
        if (flushIt) flush();
        return maxLen;
    }
