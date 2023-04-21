    public static boolean copyStreams(InputStream in, OutputStream out) {
        if (in == null || out == null) return false;
        try {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            try {
                in.close();
            } catch (IOException ex1) {
            }
            try {
                out.close();
            } catch (IOException ex1) {
            }
            e.printStackTrace();
        }
        return false;
    }
