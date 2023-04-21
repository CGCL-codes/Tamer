    private File createTempFile(URL icon) {
        File file = null;
        byte[] buffer = new byte[2048];
        try {
            InputStream in = icon.openStream();
            file = File.createTempFile("st4j_icon_", extension);
            file.deleteOnExit();
            FileOutputStream out = new FileOutputStream(file);
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
