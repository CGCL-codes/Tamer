    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            String xmlParam = request.getParameter("name");
            if ((xmlParam != null)) {
                ServletOutputStream stream = null;
                BufferedInputStream buf = null;
                try {
                    String realPath = getServletContext().getRealPath("");
                    String filePath = realPath + AttachmentUploadHandler.DEFAULT_FILE_LOCATION;
                    String fullPath = "" + filePath + xmlParam;
                    File xmlfile = new File(fullPath);
                    stream = response.getOutputStream();
                    response.addHeader("Content-Disposition", "attachment; filename=" + xmlParam);
                    response.setContentLength((int) xmlfile.length());
                    FileInputStream input = new FileInputStream(xmlfile);
                    buf = new BufferedInputStream(input);
                    int readBytes = 0;
                    while ((readBytes = buf.read()) != -1) stream.write(readBytes);
                } catch (IOException ioe) {
                    throw new ServletException(ioe.getMessage());
                } finally {
                    if (stream != null) stream.close();
                    if (buf != null) buf.close();
                }
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><head><title>Error</title></head>\n" + "<body><h1>FopServlet Error</h1><h3>No 'fo' " + "request param given.</body></html>");
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
