    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter(FILE_PARAM);
        File file = new File(this.config.getXsdDir(), fileName);
        if (logger.isDebugEnabled()) {
            logger.debug("sending file " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        int readed = -1;
        while ((readed = fis.read()) != -1) {
            resp.getOutputStream().write(readed);
        }
        resp.getOutputStream().close();
    }
