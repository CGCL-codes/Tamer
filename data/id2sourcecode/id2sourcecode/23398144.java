    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        BufferedInputStream buf = null;
        ServletOutputStream stream = null;
        try {
            if (!docs.isDirectory()) throw new Exception("Documents directory does not exist or is not a directory");
            String fileName = req.getParameter("path");
            if (fileName == null || fileName.isEmpty()) throw new Exception("Invalid or non-existent file parameter");
            if (!fileName.startsWith(File.separator)) fileName = File.separator.concat(fileName);
            File file = new File(docs.getAbsolutePath() + fileName);
            if (!file.exists()) throw new Exception("The requested file does not exist.");
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String type = fileNameMap.getContentTypeFor(file.getName());
            res.setContentType(type);
            res.addHeader("Content-Disposition", String.format("filename=\"%s\"", file.getName()));
            Long size = file.getTotalSpace();
            res.setContentLength(size.intValue());
            int readBytes = 0;
            stream = res.getOutputStream();
            buf = new BufferedInputStream(new FileInputStream(file));
            while ((readBytes = buf.read()) != -1) stream.write(readBytes);
        } catch (Exception e) {
            logger.error("Problem fetching file", e);
            throw new ServletException("Problem fetching file - " + e.getMessage());
        } finally {
            try {
                buf.close();
                stream.close();
            } catch (Exception e) {
            }
        }
    }
