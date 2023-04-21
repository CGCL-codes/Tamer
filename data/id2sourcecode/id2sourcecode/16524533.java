    void send(java.io.OutputStream os, byte position, DataHandler dh, final long maxchunk) throws java.io.IOException {
        java.io.InputStream in = null;
        try {
            long dataSize = getDataSize();
            in = dh.getInputStream();
            byte[] readbuf = new byte[64 * 1024];
            int bytesread;
            sendHeader(os, position, dataSize, (byte) 0);
            long totalsent = 0;
            do {
                bytesread = in.read(readbuf);
                if (bytesread > 0) {
                    os.write(readbuf, 0, bytesread);
                    totalsent += bytesread;
                }
            } while (bytesread > -1);
            os.write(pad, 0, dimePadding(totalsent));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
