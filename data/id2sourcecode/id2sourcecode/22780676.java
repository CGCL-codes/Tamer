    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/loginfail.do");
            } catch (IOException e) {
            }
            return null;
        }
        String sDocId = String.valueOf(req.getParameter("doc_id"));
        String sAppPath = String.valueOf(session.getAttribute(PropertyFileConst.APPLICATION_ROOT_PATH));
        try {
            String sResumePath = getFilePath(req, sDocId);
            sResumePath = sAppPath + sResumePath;
            log.info("sResumePath : " + sResumePath);
            String filePath = sResumePath.substring(0, sResumePath.lastIndexOf('/'));
            log.info("filePath : " + filePath);
            String fileName = sResumePath.substring(sResumePath.lastIndexOf('/') + 1);
            log.info("fileName: " + fileName);
            if (req.getServletPath().endsWith(fileName)) {
                log.debug("File Not Found.");
                res.sendRedirect("/FileNotFoundError.do");
                return null;
            }
            fileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                log.debug("File Not Found.");
                res.sendRedirect("/FileNotFoundError.do");
                return null;
            }
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try {
                input = new BufferedInputStream(new FileInputStream(file));
                int contentLength = input.available();
                res.reset();
                res.setContentLength(contentLength);
                res.setContentType(contentType);
                res.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
                output = new BufferedOutputStream(res.getOutputStream());
                while (contentLength-- > 0) {
                    output.write(input.read());
                }
                output.flush();
            } catch (IOException e) {
                log.debug(e.getMessage());
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
