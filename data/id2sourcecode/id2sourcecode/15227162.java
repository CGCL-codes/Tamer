    public void download(HttpServletResponse response, File file) throws IOException {
        response.setContentType(file.getMimeType());
        response.setContentLength(file.getSize());
        response.setHeader("Content-Disposition", "filename=\"" + file.getLogicalName() + "\"");
        try {
            java.io.File downloadFile = new java.io.File(file.getConfigRepositoryPath(), file.getPhysicalName());
            FileInputStream input = new FileInputStream(downloadFile);
            ServletOutputStream output = response.getOutputStream();
            byte[] buffer = new byte[10240];
            int read;
            while ((read = input.read(buffer)) > 0) {
                output.write(buffer, 0, read);
            }
            close(input);
            close(output);
        } catch (FileNotFoundException e) {
            logger.warning("Problem downloading a file: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
