    public void run() {
        if (client == null) {
            return;
        }
        if (!client.isRunning) {
            client.start();
        }
        try {
            if (client.connection.getResponseCode() / 100 != 2) {
                error();
            }
            int contentLength = client.connection.getContentLength();
            if (contentLength < 1) {
                error();
            }
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }
            InputStream in = client.getInputStream();
            if (in == null) {
                in = client.connection.getInputStream();
            }
            if (in == null) {
                if (!client.isRunning) {
                    client.start();
                }
                in = client.getConnection().getInputStream();
            }
            if (in == null) {
                error();
            }
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            for (; status == DOWNLOADING; ) {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            this.error();
            this.exception = e;
        } finally {
            client.stop();
        }
    }
