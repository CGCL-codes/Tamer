    public void downloadFile(String url) throws MalformedURLException, IOException {
        File f = new File(getFilePath(url));
        if (f.exists()) {
            return;
        }
        BufferedInputStream in = new java.io.BufferedInputStream(new URL(url).openStream());
        createURLFolders(url);
        f.createNewFile();
        FileOutputStream fos = new java.io.FileOutputStream(f);
        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte data[] = new byte[1024];
        while (in.read(data, 0, 1024) >= 0) {
            bout.write(data);
        }
        bout.close();
        in.close();
    }
