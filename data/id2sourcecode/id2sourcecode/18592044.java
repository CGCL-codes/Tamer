    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        log.debug(req.getQueryString());
        StringBuilder errorMsg = new StringBuilder();
        User user = this.authenticateRequest(req, errorMsg);
        if (user == null) {
            response.setContentType("text/html");
            response.getWriter().println("User not logged in!\n" + errorMsg.toString());
            return;
        }
        String projName = req.getParameter("projName");
        String fileType = req.getParameter("file");
        log.debug("projName is: " + projName + "\nfileName is: " + fileType + "\nuser is: " + user.getLoginName());
        fileType = this.removeRandomNumber(fileType);
        String fileToSend = getFilePath(user, projName, fileType);
        if (fileToSend == null) {
            response.setContentType("text/html");
            response.getWriter().println("Invalid file type requested: " + fileType);
            log.error("Invalid file type requested: " + fileType);
            return;
        }
        if (!(new File(fileToSend).exists())) {
            response.setContentType("text/html");
            response.getWriter().println("File does not exist: " + (new File(fileToSend)).getName());
            log.error("File does not exist: " + fileToSend);
            return;
        }
        log.debug("Sending file: " + fileToSend);
        if (fileType.endsWith("PNG")) response.setContentType("image/png"); else if (fileType.endsWith("PDF")) response.setContentType("application/pdf"); else if (fileType.endsWith("TSV")) response.setContentType("text/tab-separated-values"); else if (fileType.endsWith("APML")) response.setContentType("text/xml"); else response.setContentType("text/html");
        response.setHeader("Content-disposition", "attachment; filename=" + new File(fileToSend).getName());
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            ServletOutputStream outStr = response.getOutputStream();
            in = new BufferedInputStream(new FileInputStream(fileToSend));
            out = new BufferedOutputStream(outStr);
            byte[] buf = new byte[2048];
            int readBytes = 0;
            while ((readBytes = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, readBytes);
            }
        } catch (IOException e) {
            log.error("Error opening file: " + fileToSend, e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
            }
        }
    }
