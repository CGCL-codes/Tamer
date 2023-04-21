    public static void createFileFromStream(File f, String name) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream(f);
        byte buffer[] = new byte[4096];
        int read = 0;
        do {
            read = is.read(buffer);
            if (read != -1) {
                bout.write(buffer, 0, read);
            }
        } while (read != -1);
        fos.write(bout.toByteArray());
        fos.flush();
        fos.close();
    }
