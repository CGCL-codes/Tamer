    private void run(InputStream is) throws Throwable {
        SHA1String = null;
        SHA1Bytes = null;
        int b;
        StringBuilder sb;
        int length;
        byte[] stringData;
        int depth = 0;
        boolean inHash = false;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        while ((b = is.read()) != -1) {
            switch(b) {
                case 'd':
                    ++depth;
                    if (inHash) {
                        md.update((byte) b);
                    }
                    break;
                case 'l':
                    ++depth;
                    if (inHash) {
                        md.update((byte) b);
                    }
                    break;
                case 'e':
                    if (inHash && depth == 1) {
                        SHA1Bytes = md.digest();
                        SHA1String = org.bintrotter.Utils.byteArrayToURLString(SHA1Bytes);
                        return;
                    }
                    if (inHash) {
                        md.update((byte) b);
                    }
                    --depth;
                    break;
                case 'i':
                    if (inHash) {
                        md.update((byte) b);
                    }
                    while ((b = is.read()) != 'e') {
                        if (inHash) {
                            md.update((byte) b);
                        }
                    }
                    if (inHash) {
                        md.update((byte) b);
                    }
                    break;
                default:
                    if (b < '0' || b > '9') throw new Exception("unexpected token" + b);
                    sb = new StringBuilder();
                    for (; ; ) {
                        sb.append((char) b);
                        if (inHash) {
                            md.update((byte) b);
                        }
                        b = is.read();
                        if (b < '0' || b > '9') break;
                    }
                    if (b != ':') throw new Exception("unexpected token" + b);
                    if (inHash) {
                        md.update((byte) b);
                    }
                    length = Integer.parseInt(sb.toString());
                    stringData = new byte[length];
                    if (is.read(stringData) != length) throw new Exception("error reading string");
                    if (inHash) {
                        md.update(stringData);
                    }
                    if (depth == 1 && !inHash) {
                        if (Arrays.equals("info".getBytes(), stringData)) inHash = true;
                    }
                    break;
            }
        }
    }
