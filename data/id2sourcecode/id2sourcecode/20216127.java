        public CacheEntry(URL url) throws IOException {
            file = null;
            lastModified = System.currentTimeMillis();
            InputStream in = url.openStream();
            int totLen = 0;
            int copyLen = 0;
            byte[] tempBuffer = new byte[1024];
            do {
                copyLen = in.read(tempBuffer);
                if (copyLen > 0) {
                    byte[] newFileBuf = new byte[totLen + copyLen];
                    if (filebuffer != null) System.arraycopy(filebuffer, 0, newFileBuf, 0, totLen);
                    System.arraycopy(tempBuffer, 0, newFileBuf, totLen, copyLen);
                    totLen += copyLen;
                    filebuffer = newFileBuf;
                }
            } while (copyLen >= 0);
        }
