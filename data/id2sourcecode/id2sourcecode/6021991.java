    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        ChartController cc = ChartController.get();
        File cacheDir = cc.getCacheRoot();
        File img = new File(cacheDir, pathInfo);
        if (!img.isFile()) {
            resp.sendError(404);
            return;
        }
        InputStream is = new FileInputStream(img);
        OutputStream os = resp.getOutputStream();
        resp.setContentLength((int) img.length());
        resp.setContentType("image/png");
        try {
            byte[] buf = new byte[4096];
            int r;
            while ((r = is.read(buf)) > 0) os.write(buf, 0, r);
        } finally {
            is.close();
        }
    }
