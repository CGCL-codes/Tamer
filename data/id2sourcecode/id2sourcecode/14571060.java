    private void fromInternetToFile(File file, URLConnection connection) throws DownloadException {
        try {
            listener.downloadStarted(connection.getURL().toExternalForm());
            long len = connection.getContentLength();
            InputStream in = connection.getInputStream();
            try {
                FileOutputStream out = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[10240];
                    int totalReadBytes = 0;
                    int readBytes;
                    while ((readBytes = in.read(buffer)) > 0) {
                        out.write(buffer, 0, readBytes);
                        totalReadBytes += readBytes;
                        listener.downloadStep(totalReadBytes, len);
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
            listener.downloadFinished(connection.getURL().toExternalForm());
        } catch (FileNotFoundException e) {
            throw new DownloadException("Downloading the file " + connection.getURL() + " to " + file + " got", e);
        } catch (IOException e) {
            throw new DownloadException("Downloading the file " + connection.getURL() + " to " + file + " got", e);
        }
    }
