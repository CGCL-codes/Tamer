    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceLoader loader = _getResourceLoader(request);
        String resourcePath = getResourcePath(request);
        URL url = loader.getResource(resourcePath);
        if (url == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        _setHeaders(connection, response);
        InputStream in = connection.getInputStream();
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[_BUFFER_SIZE];
        try {
            _pipeBytes(in, out, buffer);
        } finally {
            try {
                in.close();
            } finally {
                out.close();
            }
        }
    }
