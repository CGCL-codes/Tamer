    public synchronized byte[] encrypt(byte[] buf, int off, int len, ContentType type) throws SSLException, OverflowException {
        if (deflater != null) {
            byte[] buf2 = new byte[1024];
            ByteArrayOutputStream bout = new ByteArrayOutputStream(len >>> 1);
            deflater.setInput(buf, off, len);
            deflater.finish();
            len = 0;
            while ((len = deflater.deflate(buf2)) > 0) bout.write(buf2, 0, len);
            if (bout.size() > fragmentLength + 1024) throw new OverflowException("deflated data too large");
            buf = bout.toByteArray();
            off = 0;
            len = buf.length;
            deflater.reset();
        }
        byte[] mac = new byte[0];
        if (outMac != null) {
            outMac.update((byte) (outSequence >>> 56));
            outMac.update((byte) (outSequence >>> 48));
            outMac.update((byte) (outSequence >>> 40));
            outMac.update((byte) (outSequence >>> 32));
            outMac.update((byte) (outSequence >>> 24));
            outMac.update((byte) (outSequence >>> 16));
            outMac.update((byte) (outSequence >>> 8));
            outMac.update((byte) outSequence);
            outMac.update((byte) type.getValue());
            if (version != ProtocolVersion.SSL_3) {
                outMac.update((byte) version.getMajor());
                outMac.update((byte) version.getMinor());
            }
            outMac.update((byte) (len >>> 8));
            outMac.update((byte) len);
            outMac.update(buf, off, len);
            mac = outMac.digest();
            outMac.reset();
        }
        outSequence++;
        byte[] pad = new byte[0];
        if (outCipher != null) {
            int padLen = outCipher.currentBlockSize() - ((len + mac.length + 1) % outCipher.currentBlockSize());
            if (version != ProtocolVersion.SSL_3 && session.random != null) {
                padLen += (Math.abs(session.random.nextInt()) & 7) * outCipher.currentBlockSize();
                while (padLen > 255) {
                    padLen -= outCipher.currentBlockSize();
                }
            }
            pad = new byte[padLen + 1];
            Arrays.fill(pad, (byte) padLen);
        }
        final int fraglen = len + mac.length + pad.length;
        if (outCipher != null) {
            byte[] buf2 = new byte[fraglen];
            System.arraycopy(buf, off, buf2, 0, len);
            System.arraycopy(mac, 0, buf2, len, mac.length);
            System.arraycopy(pad, 0, buf2, len + mac.length, pad.length);
            int bs = outCipher.currentBlockSize();
            for (int i = 0; i < fraglen; i += bs) {
                outCipher.update(buf2, i, buf2, i);
            }
            return buf2;
        } else if (outRandom != null) {
            byte[] buf2 = new byte[fraglen];
            transformRC4(buf, off, len, buf2, 0, outRandom);
            transformRC4(mac, 0, mac.length, buf2, len, outRandom);
            return buf2;
        } else {
            if (mac.length == 0) {
                return Util.trim(buf, off, len);
            } else {
                return Util.concat(Util.trim(buf, off, len), mac);
            }
        }
    }
