    public void downloadPartFiles(Component parentComp, URL url, String installDir) throws IOException {
        updateNote("Downloading: " + url.getFile());
        FileOutputStream out = new FileOutputStream(zipFilename);
        URLConnection conn = url.openConnection();
        int size = conn.getContentLength();
        InputStream in = new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
        int len = 0;
        int lenRead = 0;
        while ((len = in.read(buffer, 0, COPY_BUFFER)) > 0) {
            out.write(buffer, 0, len);
            lenRead += len;
            updateProgress((int) ((size - (size - lenRead)) * (500.0f / size)));
        }
        in.close();
        out.flush();
        unzip(parentComp, zipFilename, installDir);
    }
