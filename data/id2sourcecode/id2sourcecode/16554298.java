    private void replaceIndexHtml() throws IOException {
        File indexHtml = new File(docRoot, "index.html");
        File bak = new File(docRoot, "index.html.bak");
        indexHtml.renameTo(bak);
        FileOutputStream fos = new FileOutputStream(indexHtml);
        InputStream is = getClass().getResourceAsStream("test_index.html");
        int read = is.read();
        while (read != -1) {
            fos.write(read);
            read = is.read();
        }
        fos.close();
        is.close();
    }
