    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getParameter(RETURN_FILE_URL_GET_PARAMETER);
        File file = new File(directory.getPath() + File.separatorChar + filename);
        if (file.exists() && !file.isDirectory()) {
            PrintWriter out = null;
            BufferedInputStream buf = null;
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            try {
                out = response.getWriter();
                buf = new BufferedInputStream(new FileInputStream(file));
                int readBytes = 0;
                while ((readBytes = buf.read()) != -1) {
                    out.write(readBytes);
                }
            } catch (IOException e) {
                throw new ServletException(e.getMessage());
            } finally {
                if (out != null) {
                    out.close();
                }
                if (buf != null) {
                    buf.close();
                }
            }
        } else {
            response.setContentType("text/html");
            Writer outHtml = response.getWriter();
            outHtml.write("<html><body>");
            outHtml.write("<p>There is no such file as " + filename);
            outHtml.write("</body></html>");
        }
    }
