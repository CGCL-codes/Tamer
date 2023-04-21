    public NodeImpl do_transform(NodeImpl cpRootNode) {
        try {
            HttpServletResponse response = _env.getResponse();
            response.setContentType(cpRootNode.getAttribute("contenttype", "text/html"));
            if (!cpRootNode.getAttribute("attachmentFilename").trim().equals("")) {
                response.setHeader("Content-Disposition", "Attachment;filename=" + cpRootNode.getAttribute("attachmentFilename"));
            }
            if (cpRootNode.getAttribute("documentInfoMedium").equalsIgnoreCase("file")) {
                byte[] buffer = new byte[16 * 1024];
                int len = 0;
                FileInputStream inStream = new FileInputStream(path);
                do {
                    len = inStream.read(buffer);
                    if (len >= 0) response.getOutputStream().write(buffer, 0, len);
                } while (len >= 0);
            } else if (cpRootNode.getAttribute("documentInfoMedium").equalsIgnoreCase("string")) {
                StringReader inStream = new StringReader(string);
                PrintWriter writer = response.getWriter();
                int nextChar = -1;
                while ((nextChar = inStream.read()) != -1) writer.write(nextChar);
            }
            _env.setDocumentName("Stream");
            return null;
        } catch (Exception exx) {
            _env.writeLog("Exception in AppDataStream/eval", exx);
            return null;
        }
    }
