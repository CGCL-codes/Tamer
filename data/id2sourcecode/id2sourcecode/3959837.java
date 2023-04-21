    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("request=" + request);
            logger.debug("response=" + response);
        }
        String filePath = servlet.getServletContext().getRealPath("WEB-INF/test.txt");
        BufferedInputStream in = null;
        int readData = -1;
        byte[] buffer = new byte[8192];
        try {
            in = new BufferedInputStream(new FileInputStream(new File(filePath)));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((readData = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, readData);
            }
            request.getSession().setAttribute(FILE_BYTE_KEY, out.toByteArray());
            out.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return mapping.findForward("toServlet");
    }
