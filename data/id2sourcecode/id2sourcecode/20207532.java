    public static void downloadFile(String url, String filename) throws Exception {
        BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
        byte[] data = new byte[1024];
        int x = 0;
        while ((x = inputStream.read(data, 0, 1024)) >= 0) bufferedOutputStream.write(data, 0, x);
        bufferedOutputStream.close();
        inputStream.close();
    }
