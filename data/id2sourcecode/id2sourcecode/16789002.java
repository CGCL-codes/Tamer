    public void write(InputStream in) throws IOException {
        int n, length = buf.length;
        while ((n = in.read(buf, count, length - count)) >= 0) if ((count += n) >= length) out.write(buf, count = 0, length);
    }
