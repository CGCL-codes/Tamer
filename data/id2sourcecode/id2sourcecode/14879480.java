    public static File extractToTempFile(InputStream input) throws IOException {
        File tempFile = File.createTempFile("driver", ".jar");
        tempFile.deleteOnExit();
        BufferedInputStream is = new BufferedInputStream(input);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile));
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = is.read(buf, 0, 1024)) != -1) os.write(buf, 0, count);
        is.close();
        os.close();
        return tempFile;
    }
