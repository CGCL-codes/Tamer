    private synchronized void connect() throws IOException {
        if (this.state == STATE_CONNECTED) {
            if (this.theConnection == null) {
                throw new IOException("Invalid State. No connection in state STATE_CONNECTED");
            }
            return;
        } else {
            this.state = STATE_CONNECTED;
        }
        this.theConnection = (HttpURLConnection) this.url.openConnection();
        this.theConnection.setRequestMethod(this.requestMethod);
    }
