    public static void downloadFile(URL url, String filename) throws IOException {
        InputStream stream = url.openConnection().getInputStream();
        byte[] buffer = new byte[BUFSIZE];
        FileOutputStream fout = new FileOutputStream(filename);
        for (int bytesRead = 0; bytesRead >= 0; bytesRead = stream.read(buffer, 0, BUFSIZE)) fout.write(buffer, 0, bytesRead);
        fout.close();
    }
