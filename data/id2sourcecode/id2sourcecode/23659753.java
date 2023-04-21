    public Bbuf readFrom(java.io.File fi) throws java.io.IOException {
        if (fi.exists()) {
            long filen = fi.length();
            int buflen;
            if (filen > Integer.MAX_VALUE) throw new IllegalArgumentException("File is too large to read into a single buffer."); else {
                buflen = (int) filen;
                if (4096 < buflen) buflen = 4096;
            }
            java.io.FileInputStream in = new java.io.FileInputStream(fi);
            try {
                int read;
                byte[] readbuf = new byte[buflen];
                while (0 < filen) {
                    read = in.read(readbuf, 0, buflen);
                    if (0 < read) {
                        this.write(readbuf, 0, read);
                        filen -= read;
                    } else return this;
                }
            } finally {
                in.close();
            }
        }
        return this;
    }
