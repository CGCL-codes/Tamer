    @Override
    public void run() {
        if (inputUrl != null && outputFile != null) {
            try {
                URLConnection urlConn = inputUrl.openConnection();
                if (urlConn.getContentLength() == -1) {
                    if (rfdl != null) rfdl.notifyFileDownloaded(RemoteFileDownloaderListener.FILEDOWNLOADED_ERROR);
                    isError = true;
                } else {
                    InputStream inputStream = urlConn.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int readedBytes, downloadBytes = 0;
                    if (rfdl != null) {
                        rfdl.notifyFileSize(urlConn.getContentLength());
                        rfdl.notifyFileType(urlConn.getContentType());
                        rfdl.notifyCurrentDownloadedBytes(0);
                    }
                    while (!isStop && !isDone) {
                        while (isPause) Thread.yield();
                        downloadBytes += (readedBytes = inputStream.read(buffer));
                        if (readedBytes > 0) {
                            outputStream.write(buffer, 0, readedBytes);
                            if (rfdl != null) rfdl.notifyCurrentDownloadedBytes(downloadBytes);
                        } else {
                            isDone = true;
                            if (rfdl != null) rfdl.notifyFileDownloaded(RemoteFileDownloaderListener.FILEDOWNLOADED_SUCCESS);
                        }
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (rfdl != null) rfdl.notifyFileDownloaded(RemoteFileDownloaderListener.FILEDOWNLOADED_ERROR);
            }
        }
        isRunning = false;
    }
