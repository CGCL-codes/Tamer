    public static File downloadFile(String urlString, String downloadTo, ProgressHandler progress) {
        File targetFile = null;
        URL url;
        InputStream in = null;
        BufferedInputStream bin = null;
        FileOutputStream out = null;
        BufferedOutputStream bout = null;
        try {
            url = new URL(urlString);
            URLConnection conn = url.openConnection();
            progress.setSize(conn.getContentLength());
            in = conn.getInputStream();
            bin = new BufferedInputStream(in);
            targetFile = new File(downloadTo);
            out = new FileOutputStream(targetFile);
            bout = new BufferedOutputStream(out);
            byte[] buffer = new byte[BUFFER];
            int bytesRead;
            int totalBytes = 0;
            long time = System.currentTimeMillis();
            while ((bytesRead = bin.read(buffer)) > 0) {
                totalBytes += bytesRead;
                bout.write(buffer, 0, bytesRead);
                long time1 = System.currentTimeMillis();
                if (time1 - time >= 1000) {
                    if (progress.isAborted()) return null;
                    time = time1;
                    progress.bytesRead(totalBytes);
                }
            }
        } catch (IOException e) {
            targetFile = null;
        } finally {
            safelyClose(bin, in);
            safelyClose(bout, out);
        }
        return targetFile;
    }
