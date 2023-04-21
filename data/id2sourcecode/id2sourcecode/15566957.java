    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        ServletInputStream in = req.getInputStream();
        byte[] buffer = new byte[1024];
        for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
        in.close();
        return;
    }
