    public File getTemporaryFile() throws IOException {
        File file = File.createTempFile(Constants.APP_NAME.replace(' ', '-'), ".sgf");
        file.deleteOnExit();
        FileOutputStream out = new FileOutputStream(file);
        InputStream in = getInputStream();
        byte[] buf = new byte[256];
        int read = 0;
        while ((read = in.read(buf)) > 0) {
            out.write(buf, 0, read);
        }
        in.close();
        out.close();
        return file;
    }
