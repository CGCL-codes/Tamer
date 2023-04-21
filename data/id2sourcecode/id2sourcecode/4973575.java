    public static byte[] rewriteScript(URLConnection con, long sid, String host, ProxyRunData data, String resource, String base) throws IOException {
        int CAPACITY = 4096;
        Configuration config = Configuration.getInstance();
        InputStream is = con.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] bytes = new byte[CAPACITY];
        FileOutputStream fos = null;
        boolean logging = config.getEnableContentLog();
        if (logging) {
            String fileName = data.getServlet().getServletContext().getRealPath(config.getLogLocation());
            fos = new FileOutputStream(fileName, true);
            WebPageHelper.writeHeader(fos, resource);
        }
        int readCount = 0;
        while ((readCount = is.read(bytes)) > 0) {
            buffer.write(bytes, 0, readCount);
            if (logging) fos.write(bytes, 0, readCount);
        }
        if (logging) fos.close();
        is.close();
        String script = buffer.toString();
        if (sid == -1) {
        }
        return script.getBytes();
    }
