    public boolean post(HTTPResponse httpRes, InputStream in, long inLen) {
        httpRes.setDate(Calendar.getInstance());
        OutputStream out = getOutputStream();
        try {
            out.write(httpRes.getHeader().getBytes());
            out.write(HTTP.CRLF.getBytes());
            int chunkSize = HTTP.getChunkSize();
            byte readBuf[] = new byte[chunkSize];
            long readCnt = 0;
            int readLen = in.read(readBuf);
            while (0 < readLen && readCnt < inLen) {
                out.write(readBuf, 0, readLen);
                readCnt += readLen;
                readLen = in.read(readBuf);
            }
            out.flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
