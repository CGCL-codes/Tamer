    private byte[] decompress(byte[] data) throws IOException {
        byte[] out;
        byte[] newData = new byte[data.length + 4];
        System.arraycopy(data, 0, newData, 4, data.length);
        newData[0] = 'B';
        newData[1] = 'Z';
        newData[2] = 'h';
        newData[3] = '1';
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            InputStream is = new BZip2CompressorInputStream(new ByteArrayInputStream(newData));
            try {
                while (true) {
                    byte[] buf = new byte[512];
                    int read = is.read(buf, 0, buf.length);
                    if (read == -1) {
                        break;
                    }
                    os.write(buf, 0, read);
                }
            } finally {
                is.close();
            }
            os.flush();
            out = os.toByteArray();
        } finally {
            os.close();
        }
        return out;
    }
