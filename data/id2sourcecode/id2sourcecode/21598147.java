    public final int read(byte buf[], int ofs, int len) throws java.io.IOException {
        if (this.isConsuming()) {
            int avail = this.consumable();
            if (0 < avail) {
                int many = java.lang.Math.min(avail, len);
                int read = this.in.read(buf, ofs, many);
                if (0 < read) {
                    if (this.traceread) {
                        this.trace.write(buf, ofs, read);
                    }
                    this.count += read;
                }
                return read;
            } else return -1;
        } else return this.in.read(buf, ofs, len);
    }
