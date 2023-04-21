    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) _source.openConnection();
            connection.setRequestProperty("Range", "bytes=" + getDone() + "-");
            connection.connect();
            if (connection.getResponseCode() < 200 || connection.getResponseCode() > 299) {
                error("Response Code");
                return;
            }
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error("Content Length");
                return;
            }
            if (getSize() == -1) {
                setSize(contentLength);
            }
            file = new RandomAccessFile(_destination, "rw");
            file.seek(_done);
            stream = connection.getInputStream();
            setState(State.ACTIVE);
            while (_state == State.ACTIVE) {
                byte buffer[];
                if (_size - _done > BUFFER_SIZE) {
                    buffer = new byte[BUFFER_SIZE];
                } else {
                    buffer = new byte[_size - _done];
                }
                int read = stream.read(buffer);
                if (read == -1) {
                    break;
                }
                file.write(buffer, 0, read);
                addDone(read);
            }
            if (_state == State.ACTIVE) {
                setState(State.FINISHED);
            }
        } catch (IOException e) {
            error(e.toString());
        } finally {
            if (file != null) {
                try {
                    file.close();
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }
