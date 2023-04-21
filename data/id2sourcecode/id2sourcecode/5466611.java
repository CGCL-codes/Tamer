    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        byte[] buf = new byte[4096];
        int l = 0;
        OutputStream os = resp.getOutputStream();
        InputStream is = getServletContext().getResource("/admin.html").openStream();
        while ((l = is.read(buf)) > 0) os.write(buf, 0, l);
        is.close();
    }
