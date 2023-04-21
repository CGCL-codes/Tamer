    public Packet(byte[] arr) {
        this.header = arr[0];
        this.data = new byte[3];
        for (int i = 0; i < 3; i++) {
            this.data[i] = arr[i + 1];
        }
    }
