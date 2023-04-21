    @Override
    protected void doGet(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {
        String context = _request.getContextPath();
        String path = _request.getRequestURI();
        URL url = null;
        ClassLoader loader = loaders.get(context);
        if (loader != null) {
            url = loader.getResource(path);
        }
        if (url == null) {
            _response.sendError(404);
            return;
        }
        URLConnection connection = url.openConnection();
        IOUtilities.flow(connection.getInputStream(), _response.getOutputStream());
        _response.setContentType(connection.getContentType());
        _response.setContentLength(connection.getContentLength());
        _response.setCharacterEncoding(connection.getContentEncoding());
    }
