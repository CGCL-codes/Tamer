    private void loadTemplate() throws Exception {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        FileInputStream fi = new FileInputStream(templateLocation);
        int read = 0;
        byte[] bytes = new byte[1024];
        read = fi.read(bytes);
        while (read > -1) {
            ba.write(bytes, 0, read);
            read = fi.read(bytes);
        }
        fi.close();
        page = new StringBuffer();
        page.append(ba.toString());
    }
