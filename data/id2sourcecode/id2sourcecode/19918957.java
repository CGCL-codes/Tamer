    public static void rollingHashPad(OutputStream out, long paddingLen, SHA1 ctx) throws IOException {
        byte[] hashbuf = new byte[ctx.digestSize() >> 3];
        ByteArrayOutputStream pad = new ByteArrayOutputStream();
        while (paddingLen > 0) {
            ctx.digest(false, hashbuf, 0);
            ctx.update(hashbuf, 0, hashbuf.length);
            pad.write(hashbuf, 0, hashbuf.length);
            if (paddingLen < pad.size()) {
                byte[] tmp = pad.toByteArray();
                out.write(tmp, 0, (int) paddingLen);
                paddingLen = 0;
            } else {
                pad.writeTo(out);
                paddingLen -= pad.size();
            }
        }
    }
