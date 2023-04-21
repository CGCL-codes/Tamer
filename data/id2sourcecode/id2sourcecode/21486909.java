    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        GetMethod method = null;
        try {
            HttpClient client = ProxyManager.manager().httpClient();
            method = new GetMethod(url);
            method.setRequestHeader("Range", "bytes=" + downloaded + "-");
            int responce = client.executeMethod(method);
            if (responce / 100 != 2) error();
            Header header = method.getResponseHeader("Content-Length");
            int contentLength = Integer.parseInt(header.getValue());
            if (contentLength < 1) error();
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }
            file = new RandomAccessFile(getFilePath(url), "rw");
            file.seek(downloaded);
            stream = method.getResponseBodyAsStream();
            while (status == DOWNLOADING) {
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) buffer = new byte[MAX_BUFFER_SIZE]; else buffer = new byte[size - downloaded];
                int read = stream.read(buffer);
                if (read == -1) break;
                file.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (IOException ex) {
            error();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
