    protected void writeContent(File file) throws IOException {
        InputStream in = getUri().toURL().openStream();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
