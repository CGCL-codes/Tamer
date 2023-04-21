    public void writeTo(OutputStream Destination) throws IOException {
        int BytesRemaining = getBytesRemaining();
        if (BytesRemaining > 0) {
            Destination.write(this.data, this.readPointer, BytesRemaining);
            this.readPointer = this.data.length;
        }
    }
