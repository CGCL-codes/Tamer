    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        OutputStream out = resp.getOutputStream();
        InputStream in = wsdlUrl.openStream();
        try {
            byte[] buffer = new byte[1024];
            for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } finally {
            in.close();
        }
    }
