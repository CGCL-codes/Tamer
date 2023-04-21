    public static void processNonHTML(HttpServletResponse response, HttpMethodBase method) throws IOException {
        logger.debug("Processing a non HTML document");
        InputStream is = new BufferedInputStream(method.getResponseBodyAsStream());
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[BUFFER_BLOCK_SIZE];
        int read = is.read(buffer);
        while (read >= 0) {
            if (read > 0) {
                os.write(buffer, 0, read);
            }
            read = is.read(buffer);
        }
        buffer = null;
        is.close();
        os.close();
    }
