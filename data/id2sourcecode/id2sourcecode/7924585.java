    public void run() {
        InputStream in = null;
        try {
            HttpURLConnection connection = client.getConnection();
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }
            bytes.setPosition(downloaded);
            in = connection.getInputStream();
            while (status == DOWNLOADING) {
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                bytes.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
            connection.disconnect();
        } catch (Exception e) {
            error();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }
