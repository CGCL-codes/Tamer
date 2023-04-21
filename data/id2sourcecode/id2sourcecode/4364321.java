    public void writeResponse(HttpServletRequest arg0, HttpServletResponse response) throws IOException {
        if (url == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            log.warn("No response found for request.");
            return;
        }
        if (contentType != null) response.setContentType(contentType);
        if (filename != null) response.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + filename);
        InputStream is;
        String surl = url.toString();
        long fileSize;
        OutputStream os = response.getOutputStream();
        if (surl.startsWith("file:")) {
            String fileName = url.getFile();
            File file = new File(fileName);
            fileSize = file.length();
            response.setContentLength((int) fileSize);
            if (memoryMap == null || memoryMap == false) {
                log.info("Using stream file {} of size {}", fileName, fileSize);
                streamFile(os, new FileInputStream(file), (int) Math.min(fileSize, bufSize));
            } else {
                log.info("Using memory mapped file {} of size {}", fileName, fileSize);
                memoryMapFile(os, fileName, bufSize);
            }
        } else {
            URLConnection conn = url.openConnection();
            log.info("Reading from URL connection " + surl);
            fileSize = conn.getContentLength();
            is = conn.getInputStream();
            if (fileSize > 0) {
                log.info("Returning {} bytes for file {}", fileSize, url);
                response.setContentLength((int) fileSize);
                bufSize = (int) Math.min(fileSize, bufSize);
            } else {
            }
            streamFile(os, is, bufSize);
        }
    }
