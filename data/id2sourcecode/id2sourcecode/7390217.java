    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        log.debug(req.getQueryString());
        StringBuilder errorMsg = new StringBuilder();
        User user = this.authenticateRequest(req, errorMsg);
        if (user == null) {
            response.setContentType("text/html");
            response.getWriter().println("User not logged in!\n" + errorMsg.toString());
            return;
        }
        String fileType = req.getParameter("fileType");
        fileType = this.removeRandomNumber(fileType);
        log.debug("\nfileType is: " + fileType + "\nuser is: " + user.getUserName());
        initInputStream(req, response, fileType);
        if (this.inputStream == null) {
            response.setContentType("text/html");
            response.getWriter().println("Invalid file type requested: " + fileType);
            log.error("Invalid file type requested: " + fileType);
            return;
        }
        if (fileType.endsWith("PNG")) response.setContentType("image/png"); else if (fileType.endsWith("PDF")) response.setContentType("application/pdf"); else if (fileType.endsWith("TSV")) response.setContentType("text/tab-separated-values"); else if (fileType.endsWith("CSV")) response.setContentType("text/comma-separated-values"); else if (fileType.endsWith("TraML")) response.setContentType("text/xml"); else if (fileType.endsWith("APML")) response.setContentType("text/xml"); else response.setContentType("text/html");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            ServletOutputStream outStr = response.getOutputStream();
            in = new BufferedInputStream(this.inputStream);
            out = new BufferedOutputStream(outStr);
            byte[] buf = new byte[2048];
            int readBytes = 0;
            while ((readBytes = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, readBytes);
            }
        } catch (IOException e) {
            log.error("Error opening file:", e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
            }
        }
    }
