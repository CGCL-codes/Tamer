    private void DownloadFile(String surl, String path) throws MalformedURLException, IOException {
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.net.URL(surl).openStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
        java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int written = 0;
        int x = 0;
        while (((x = in.read(data, 0, 1024)) >= 0) && !canceled) {
            written += 1024;
            progress = CalculatePercentage(written);
            raiseProgressChanged(pc);
            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
        if (canceled) {
            File file = new File(path);
            file.delete();
        }
        completed = true;
        progress = CalculatePercentage(written);
        raiseProgressChanged(pc);
    }
