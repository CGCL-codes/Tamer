    public int redirectAll(OutputStream out) throws IOException {
        int total = 0;
        int read;
        while (true) {
            read = read(buf);
            if (read < 0) {
                if (autoClose) out.close();
                return total;
            }
            out.write(buf, 0, read);
            if (autoFlush) out.flush();
            total += read;
        }
    }
