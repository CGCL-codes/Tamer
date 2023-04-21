    protected void copyFile(InputStream is, String dir, String name) throws IOException {
        OutputStream os = new FileOutputStream(dir + name);
        int i;
        while ((i = is.read()) != -1) os.write(i);
        os.close();
    }
