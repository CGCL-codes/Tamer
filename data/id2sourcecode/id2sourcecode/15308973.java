    protected synchronized void receive(int b) throws IOException {
        if (b < 0) {
            b = 256 + b;
        }
        this.data[this.write] = b;
        this.write++;
        if (this.write >= this.data.length) {
            this.write = 0;
        }
        if (this.write == this.read) {
            throw new IOException("Buffer overflow in NewPipedInputStream");
        }
        this.available++;
    }
