    public static void getFileFromUrl(String url, String outputFilePath) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte data[] = new byte[1024];
        int count = in.read(data, 0, 1024);
        while (count > 0) {
            bout.write(data, 0, count);
            count = in.read(data, 0, 1024);
        }
        bout.close();
        in.close();
    }
