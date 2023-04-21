    public synchronized int read() throws IOException {
        if (this.available <= 0) {
            return -1;
        }
        if (this.read == this.write) {
            return -1;
        }
        this.available--;
        int value = this.data[this.read];
        this.read++;
        if (this.read >= this.data.length) {
            this.read = 0;
        }
        return value;
    }
