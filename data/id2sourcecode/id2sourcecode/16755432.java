    private void copy(InputStream in, OutputStream out, DownloadProgressListener downloadProgressListener, final int iFileSize) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        int totalDownloaded = 0;
        while ((read = in.read(b)) != -1) {
            totalDownloaded += read;
            Log.d("warenix", String.format("downloaded: %d", totalDownloaded));
            if (downloadProgressListener != null) {
                downloadProgressListener.onReceivedProgressUpdate(totalDownloaded, iFileSize);
            }
            out.write(b, 0, read);
        }
    }
