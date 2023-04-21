    private void writeToClient(HttpServletRequest request, HttpServletResponse response, String fileName, InputStream is) throws IOException {
        String contentType = request.getSession().getServletContext().getMimeType(fileName);
        logCat.info("::writeToClient- writing to client:" + fileName + " ct=" + contentType);
        if (!Util.isNull(contentType)) {
            response.setContentType(contentType);
        }
        response.setHeader("Cache-control", "private");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\"");
        ServletOutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        int read;
        while ((read = is.read(b)) != -1) out.write(b, 0, read);
        out.close();
    }
