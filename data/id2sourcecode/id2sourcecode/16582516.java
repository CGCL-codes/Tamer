    protected synchronized void connect() throws IOException {
        if (state == STATE_CONNECTED) {
            if (connection == null) {
                throw new IOException("Invalid State. No connection in state STATE_CONNECTED");
            }
            return;
        } else {
            state = STATE_CONNECTED;
        }
        connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod(this.requestMethod);
    }
