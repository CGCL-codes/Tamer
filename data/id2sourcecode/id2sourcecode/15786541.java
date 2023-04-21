    public void toHTML(OutputStream out, File template) throws IOException {
        FileInputStream fis = new FileInputStream(template);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) out.write(buf, 0, i);
        fis.close();
    }
