    public static void makeKey(Bucket entropy, byte[] key, int offset, int len) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        synchronized (ctx) {
            ctx.digest();
            int ic = 0;
            InputStream in;
            while (len > 0) {
                ic++;
                for (int i = 0; i < ic; i++) ctx.update((byte) 0);
                in = entropy.getInputStream();
                for (int i = 0; (i = in.read(buffer)) > 0; ) ctx.update(buffer, 0, buffer.length);
                in.close();
                int bc;
                if (len > ctx.digestSize() >> 3) {
                    ctx.digest(true, key, offset);
                    bc = ctx.digestSize() >> 3;
                } else {
                    byte[] hash = ctx.digest();
                    bc = Math.min(len, hash.length);
                    System.arraycopy(hash, 0, key, offset, bc);
                }
                offset += bc;
                len -= bc;
            }
        }
        wipe(buffer);
    }
