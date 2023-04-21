    public String getHTMLContent(URLConnection con, ProxyRunData data, String resource) throws IOException {
        int CAPACITY = 4096;
        InputStream is = con.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Configuration config = Configuration.getInstance();
        FileOutputStream fos = null;
        boolean logging = config.getEnableContentLog();
        if (logging) {
            if (data != null) {
                String fileName = data.getServlet().getServletContext().getRealPath(config.getLogLocation());
                fos = new FileOutputStream(fileName, true);
                WebPageHelper.writeHeader(fos, resource);
            }
        }
        byte[] bytes = new byte[CAPACITY];
        int readCount = 0;
        int total = 0;
        while ((readCount = is.read(bytes)) > 0) {
            buffer.write(bytes, 0, readCount);
            if (logging) {
                fos.write(bytes, 0, readCount);
            }
            total += readCount;
        }
        if (logging) {
            fos.close();
        }
        is.close();
        return buffer.toString();
    }
