    public static String exportTheFile(HttpServletRequest req, HttpServletResponse res, String sDownloadLink) {
        String sResponse = new String();
        try {
            String sFilePath = new String();
            String sFileName = new String();
            if (sDownloadLink.indexOf(new String("/")) > -1) {
                sFilePath = sDownloadLink.substring(0, sDownloadLink.lastIndexOf("/"));
                sFileName = sDownloadLink.substring(sDownloadLink.lastIndexOf("/") + 1);
            } else {
                sFilePath = sDownloadLink.substring(0, sDownloadLink.lastIndexOf("\\"));
                sFileName = sDownloadLink.substring(sDownloadLink.lastIndexOf("\\") + 1);
            }
            if (req.getServletPath().endsWith(sFileName)) {
                sResponse = "File Not Found";
            }
            sFileName = URLDecoder.decode(sFileName, "UTF-8");
            File file = new File(sFilePath, sFileName);
            if (!file.exists()) {
                sResponse = "File Not Found";
            }
            String contentType = URLConnection.guessContentTypeFromName(sFileName);
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
                res.setHeader("Content-disposition", "attachment; filename=\"" + sFileName + "\"");
                output = new BufferedOutputStream(res.getOutputStream());
                while (contentLength-- > 0) {
                    output.write(input.read());
                }
                output.flush();
            } catch (IOException e) {
                logger.debug(e);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        logger.debug(e);
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        logger.debug(e);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e);
        }
        return sResponse;
    }
