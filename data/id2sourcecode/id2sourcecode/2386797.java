    public int buildObjectId(byte[] buf, int startOffset, byte asnType, int[] oids) throws AsnEncodingException {
        if ((buf.length - startOffset) < 1) throw new AsnEncodingException("Buffer overflow error");
        int[] toEncode = oids;
        int begin = startOffset;
        if (oids.length < 2) {
            toEncode = new int[2];
            toEncode[0] = 0;
            toEncode[1] = 0;
        }
        if (toEncode[0] < 0 || toEncode[0] > 2) throw new AsnEncodingException("Invalid Object Identifier");
        if (toEncode[1] < 0 || toEncode[1] > 40) throw new AsnEncodingException("Invalid Object Identifier");
        buf[startOffset++] = (byte) (toEncode[0] * 40 + toEncode[1]);
        int oidNdx = 2;
        while (oidNdx < toEncode.length) {
            int oid = toEncode[oidNdx++];
            if (oid < 127) {
                if ((buf.length - startOffset) < 1) throw new AsnEncodingException("Buffer overflow error");
                buf[startOffset++] = (byte) oid;
            } else {
                int mask = 0, bits = 0;
                int tmask = 0, tbits = 0;
                tmask = 0x7f;
                tbits = 0;
                while (tmask != 0) {
                    if ((oid & tmask) != 0) {
                        mask = tmask;
                        bits = tbits;
                    }
                    tmask <<= 7;
                    tbits += 7;
                }
                while (mask != 0x7f) {
                    if ((buf.length - startOffset) < 1) throw new AsnEncodingException("Buffer overflow error");
                    buf[startOffset++] = (byte) (((oid & mask) >>> bits) | HIGH_BIT);
                    mask = (mask >>> 7);
                    bits -= 7;
                    if (mask == 0x01e00000) mask = 0x0fe00000;
                }
                if ((buf.length - startOffset) < 1) throw new AsnEncodingException("Insufficent buffer space");
                buf[startOffset++] = (byte) (oid & mask);
            }
        }
        int pivot = startOffset;
        int asnLength = pivot - begin;
        int end = buildHeader(buf, pivot, asnType, asnLength);
        try {
            rotate(buf, begin, pivot, end);
        } catch (ArrayIndexOutOfBoundsException err) {
            throw new AsnEncodingException("Insufficent buffer space");
        }
        return end;
    }
