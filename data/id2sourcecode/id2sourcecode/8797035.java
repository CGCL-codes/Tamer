    public void serveFromPath(HttpServletRequest request, HttpServletResponse response, File path, String resource) throws IOException, ServletException {
        File file = new File(path, resource);
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            if (!file.getCanonicalPath().startsWith(path.getCanonicalPath())) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, file.toString());
            } else if (file.isFile()) {
                response.setContentType(getContentType(resource));
                if (request.getParameter("download") != null) {
                    response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(resource, Config.getMng().getEncoding()));
                    response.addHeader("Content-Type", "application/force-download");
                    response.addHeader("Content-Type", "application/octet-stream");
                    response.addHeader("Content-Type", "application/download");
                    response.addHeader("Content-Description", "File Transfer");
                }
                response.addHeader("Content-Length", "" + file.length());
                int total = IOTools.copy(new FileInputStream(file), response.getOutputStream());
            } else if (file.isDirectory()) {
                request.setAttribute("dir", file);
                serveJsp("/static/filelist.jsp", request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
