    private void saveAsset(String name, InputStream is) throws IOException {
        File outputFile = new File(assetsDir.getAbsoluteFile().toString() + File.separator + name);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
        OutputStream os = new FileOutputStream(outputFile);
        byte[] buf = new byte[1024];
        int readBytes;
        while ((readBytes = is.read(buf)) > 0) {
            os.write(buf, 0, readBytes);
        }
    }
