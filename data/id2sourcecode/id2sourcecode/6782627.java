    public static void uploadFile(HttpServletResponse response, File fileName, boolean gunzip) throws IOException {
        if (!gunzip) {
            response.setContentLength(new Long(fileName.length()).intValue());
        }
        OutputStream out = null;
        InputStream is = null;
        byte[] buffer = new byte[512];
        try {
            out = response.getOutputStream();
            is = new FileInputStream(fileName);
            if (gunzip) {
                is = new GZIPInputStream(is);
            }
            int read = is.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = is.read(buffer);
            }
        } finally {
            out.close();
            if (is != null) {
                is.close();
            }
        }
    }
