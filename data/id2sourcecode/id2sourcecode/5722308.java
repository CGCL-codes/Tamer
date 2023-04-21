    public static void copy(URL url, File file) throws IOException {
        InputStream inputStream = new BufferedInputStream(url.openStream());
        int count;
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] b = new byte[1024];
        try {
            while ((count = inputStream.read(b, 0, 1024)) > 0) {
                outputStream.write(b, 0, count);
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }
