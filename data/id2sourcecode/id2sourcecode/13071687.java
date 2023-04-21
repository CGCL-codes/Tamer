    public static void writeInputStreamToFile(InputStream in, String fileName) throws IOException {
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        if (read >= 0) {
            File file = new File(fileName);
            if (!file.exists()) {
                File dir = file.getParentFile();
                if (dir != null && !dir.exists()) {
                    dir.mkdirs();
                }
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            while (read >= 0) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            out.flush();
            out.close();
        }
    }
