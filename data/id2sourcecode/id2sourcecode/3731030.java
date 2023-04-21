    private boolean handleResource(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {
        String path = _request.getRequestURI();
        path = path.substring(_request.getContextPath().length());
        URL url = this.getClass().getClassLoader().getResource(path);
        if (url == null) {
            _response.sendError(404);
            return false;
        }
        URLConnection connection = url.openConnection();
        IOUtilities.flow(connection.getInputStream(), _response.getOutputStream());
        _response.setContentType(connection.getContentType());
        _response.setContentLength(connection.getContentLength());
        _response.setCharacterEncoding(connection.getContentEncoding());
        _response.getOutputStream().flush();
        _response.getOutputStream().close();
        _response.flushBuffer();
        return true;
    }
