    private void createTempFile() throws ZipException, IOException {
        String fileName = location.getFile();
        String prefix = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.indexOf("."));
        String suffix = fileName.substring(fileName.indexOf("."));
        this.tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tempFile);
        InputStream inputStream = location.openStream();
        BufferedInputStream bufIS = new BufferedInputStream(inputStream);
        byte[] buf = new byte[BUFFER_SIZE];
        int len = BUFFER_SIZE;
        byte b;
        while ((len = bufIS.read(buf, 0, len)) > 0) fos.write(buf, 0, len);
        fos.close();
        System.out.println("prefix = " + prefix + " suffix = " + suffix + " location  = " + tempFile.getAbsolutePath());
    }
