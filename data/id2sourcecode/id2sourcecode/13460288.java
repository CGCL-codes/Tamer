    public void doReadBody() throws IOException {
        int length = super.getContentLength();
        if (length > 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(length);
            InputStream in = super.getInputStream();
            byte[] bodyByte = new byte[length];
            int remain = length;
            while (remain > 0) {
                int readLen = in.read(bodyByte);
                if (readLen <= 0) {
                    break;
                }
                out.write(bodyByte, 0, readLen);
                remain -= readLen;
            }
            this._bodyInfoData = out.toByteArray();
            out.close();
        }
    }
