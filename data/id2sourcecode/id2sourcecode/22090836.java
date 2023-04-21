    protected synchronized void read() throws IOException {
        this.readBuffer.clear();
        int bytesRead = this.client.read(this.readBuffer);
        if (bytesRead < 0) {
            throw new IOException("Reached end of stream");
        } else if (bytesRead == 0) {
            return;
        }
        this.storage.write(this.readBuffer.array(), 0, bytesRead);
        while (this.input.available() > 0) {
            if (this.waitingForLength) {
                if (this.input.available() > 2) {
                    this.length = this.input.readShort();
                    this.waitingForLength = false;
                } else {
                    break;
                }
            } else {
                if (this.input.available() >= this.length) {
                    byte[] data = new byte[this.length];
                    this.input.readFully(data);
                    this.addReceivedPacket(data);
                    this.waitingForLength = true;
                } else {
                    break;
                }
            }
        }
    }
