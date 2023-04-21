    public synchronized byte[] decrypt(byte[] fragment, ProtocolVersion version, ContentType type) throws MacException, OverflowException, SSLException {
        boolean badPadding = false;
        if (inCipher != null) {
            int bs = inCipher.currentBlockSize();
            for (int i = 0; i < fragment.length; i += bs) {
                inCipher.update(fragment, i, fragment, i);
            }
            int padLen = fragment[fragment.length - 1] & 0xFF;
            int len = fragment.length - padLen - 1;
            if (version == ProtocolVersion.SSL_3) {
                if (padLen >= bs) {
                    badPadding = true;
                }
            } else {
                for (int i = len; i < fragment.length; i++) {
                    if ((fragment[i] & 0xFF) != padLen) {
                        badPadding = true;
                    }
                }
            }
            fragment = Util.trim(fragment, len);
        } else if (inRandom != null) {
            transformRC4(fragment, 0, fragment.length, fragment, 0, inRandom);
        }
        if (inMac != null) {
            inMac.update((byte) (inSequence >>> 56));
            inMac.update((byte) (inSequence >>> 48));
            inMac.update((byte) (inSequence >>> 40));
            inMac.update((byte) (inSequence >>> 32));
            inMac.update((byte) (inSequence >>> 24));
            inMac.update((byte) (inSequence >>> 16));
            inMac.update((byte) (inSequence >>> 8));
            inMac.update((byte) inSequence);
            inMac.update((byte) type.getValue());
            if (version != ProtocolVersion.SSL_3) {
                inMac.update((byte) version.getMajor());
                inMac.update((byte) version.getMinor());
            }
            int macLen = inMac.macSize();
            int fragLen = fragment.length - macLen;
            inMac.update((byte) (fragLen >>> 8));
            inMac.update((byte) fragLen);
            inMac.update(fragment, 0, fragLen);
            byte[] mac = inMac.digest();
            inMac.reset();
            for (int i = 0; i < macLen; i++) {
                if (fragment[i + fragLen] != mac[i]) {
                    throw new MacException();
                }
            }
            if (badPadding) {
                throw new MacException();
            }
            fragment = Util.trim(fragment, fragLen);
        }
        if (inflater != null) {
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bout = new ByteArrayOutputStream(fragment.length << 1);
            inflater.setInput(fragment);
            int len;
            try {
                while ((len = inflater.inflate(buf)) > 0) {
                    bout.write(buf, 0, len);
                    if (bout.size() > fragmentLength + 1024) throw new OverflowException("inflated data too large");
                }
            } catch (DataFormatException dfe) {
                throw new SSLException(String.valueOf(dfe));
            }
            fragment = bout.toByteArray();
            inflater.reset();
        }
        inSequence++;
        return fragment;
    }
