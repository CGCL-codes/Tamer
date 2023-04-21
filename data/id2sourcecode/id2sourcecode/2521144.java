    public static void saveStreamToFile(InputStream in, File outFile) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            byte[] buf = new byte[4096];
            int bytes_read;
            while ((bytes_read = in.read(buf)) != -1) out.write(buf, 0, bytes_read);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
