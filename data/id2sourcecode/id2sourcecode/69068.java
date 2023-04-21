    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        ServletOutputStream out = res.getOutputStream();
        InputStream in = null;
        try {
            URL url = new URL(req.getParameter("location"));
            in = url.openStream();
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = in.read(buffer)) != -1) out.write(buffer, 0, bytes_read);
        } catch (MalformedURLException e) {
            System.err.println(e.toString());
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        }
    }
