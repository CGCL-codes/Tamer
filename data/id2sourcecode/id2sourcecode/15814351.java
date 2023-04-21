    public static int copyStream(int limit, InputStream in, OutputStream out, int bufSize) throws IOException {
        if (limit == -1) {
            copyStream(in, out, bufSize);
            return -1;
        }
        if (limit < 0) {
            throw new IllegalArgumentException("Error limit:" + limit);
        }
        if (limit == 0) {
            return 0;
        }
        bufSize = bufSize <= 0 ? DEFAULT_BUFSIZE : bufSize;
        byte[] buf = new byte[limit > bufSize ? bufSize : limit];
        int allCount = 0;
        int leftCount = limit;
        int readCount = in.read(buf);
        while (readCount > 0) {
            out.write(buf, 0, readCount);
            allCount += readCount;
            leftCount -= readCount;
            if (allCount >= limit) {
                break;
            }
            readCount = in.read(buf, 0, buf.length > leftCount ? leftCount : buf.length);
        }
        return allCount;
    }
