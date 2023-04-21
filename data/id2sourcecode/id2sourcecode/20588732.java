    public void getBinaryContent(URLConnection con, HttpServletResponse response) throws IOException {
        int CAPACITY = 4096;
        InputStream is = con.getInputStream();
        byte[] bytes = new byte[CAPACITY];
        int readCount = 0;
        while ((readCount = is.read(bytes)) > 0) {
            response.getOutputStream().write(bytes, 0, readCount);
        }
        is.close();
    }
