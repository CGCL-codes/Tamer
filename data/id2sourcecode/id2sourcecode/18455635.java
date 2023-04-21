    static void downloadFile(final String url, final String fileName, boolean forceOverwrite, SyncAction updateReceiver) throws IOException {
        log("Downloading: " + fileName + " from: " + url);
        HttpConnection httpConnection = null;
        FileConnection fileConnection = null;
        InputStream httpInStream = null;
        OutputStream fileOutStream = null;
        try {
            httpConnection = (HttpConnection) Connector.open(url + JsonHttpHelper.URL_SUFFIX);
            httpConnection.setRequestMethod(HttpConnection.GET);
            httpConnection.setRequestProperty("Connection", "close");
            int status = httpConnection.getResponseCode();
            if (status == HttpConnection.HTTP_OK) {
                long contentLength = Long.parseLong(httpConnection.getHeaderField("Content-Length"));
                fileConnection = (FileConnection) Connector.open(fileName, Connector.READ_WRITE);
                if (fileConnection.exists()) {
                    if (forceOverwrite) {
                        fileConnection.delete();
                    } else {
                        try {
                            log("Sever: " + Long.toString(contentLength) + " Client: " + Long.toString(fileConnection.fileSize()));
                            if (contentLength != fileConnection.fileSize()) {
                                fileConnection.delete();
                            } else {
                                httpConnection.close();
                                if (updateReceiver != null) updateReceiver.setStatus("Skip duplicate");
                                return;
                            }
                        } catch (Exception ex) {
                            if (fileConnection.exists()) fileConnection.delete();
                        }
                    }
                }
                fileConnection.create();
                httpInStream = httpConnection.openInputStream();
                fileOutStream = fileConnection.openOutputStream();
                long totalRead = 0;
                byte[] readBuf = new byte[BUFFER_SIZE];
                while (true) {
                    int read = httpInStream.read(readBuf);
                    totalRead += read;
                    if (read == -1) break;
                    fileOutStream.write(readBuf, 0, read);
                    if (updateReceiver != null) updateReceiver.setStatus(Integer.toString(calculatePercent(totalRead, contentLength)) + "% done");
                }
                fileOutStream.flush();
                if (updateReceiver != null) updateReceiver.setStatus("Complete");
            }
        } finally {
            try {
                if (httpInStream != null) httpInStream.close();
            } catch (Exception error) {
            }
            try {
                if (fileOutStream != null) fileOutStream.close();
            } catch (Exception error) {
            }
            try {
                if (httpConnection != null) httpConnection.close();
            } catch (Exception error) {
            }
            try {
                if (fileConnection != null) fileConnection.close();
            } catch (Exception error) {
            }
        }
    }
