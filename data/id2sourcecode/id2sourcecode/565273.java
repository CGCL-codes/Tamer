    private static void copy(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = in.read(buffer)) > -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
