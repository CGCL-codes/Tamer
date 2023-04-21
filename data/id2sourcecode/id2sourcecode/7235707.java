    private InputStream readFully(InputStream in, String charset, StringBuilder sb) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Reader reader = new InputStreamReader(new FilterInputStream(in) {

            @Override
            public int read() throws IOException {
                int read = super.read();
                if (read >= 0) bos.write(read);
                return read;
            }

            @Override
            public int read(byte[] arg0, int arg1, int arg2) throws IOException {
                int read = super.read(arg0, arg1, arg2);
                if (read > 0) bos.write(arg0, arg1, read);
                return read;
            }
        }, charset);
        int read;
        while ((read = reader.read()) >= 0) {
            sb.append((char) read);
        }
        bos.close();
        return new ByteArrayInputStream(bos.toByteArray());
    }
