    public static boolean saveURLToFile(URL url, File file) {
        InputStream in = null;
        OutputStream out = null;
        boolean result = false;
        try {
            in = url.openStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = in.read(buffer)) != -1) out.write(buffer, 0, bytes_read);
            result = true;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        }
        return result;
    }
