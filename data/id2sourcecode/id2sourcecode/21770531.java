    public void readData(MultipartFormField field, DelimitedBufferedInputStream in, byte[] delim) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[BUFSIZE];
        int count;
        try {
            while ((count = in.readToDelimiter(buff, 0, buff.length, delim)) > 0) out.write(buff, 0, count);
            if (count == -1) throw new IOException("Didn't find boundary whilst reading field data");
            data = out.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                out.close();
                out = null;
            } catch (Exception e) {
                ;
            }
        }
    }
