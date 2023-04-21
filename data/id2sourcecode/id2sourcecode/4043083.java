    public static void download(String address, String fileName, ProgressIndicator progress) throws Exception {
        URL url = new URL(address);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int numRead;
        int total = conn.getContentLength();
        int completed = 0;
        if (progress != null) {
            progress.started(total);
        }
        while ((numRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
            completed += numRead;
            if (progress != null) {
                progress.progress(completed);
            }
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (progress != null) {
            progress.finished();
        }
    }
