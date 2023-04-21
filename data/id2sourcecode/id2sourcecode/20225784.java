    public void dumpHttpHeader() throws IOException {
        if (this.httpHeaderStream == null) {
            return;
        }
        for (int available = this.httpHeaderStream.available(); this.httpHeaderStream != null && (available = this.httpHeaderStream.available()) > 0; ) {
            byte[] buffer = new byte[available];
            int read = read(buffer, 0, available);
            System.out.write(buffer, 0, read);
        }
    }
