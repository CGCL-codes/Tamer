    public void serveResource(ServletContext context, HttpServletRequest request, HttpServletResponse response, String resourceUri) throws IOException {
        _initDebug(context);
        File tempdir = (File) context.getAttribute("javax.servlet.context.tempdir");
        URL url = findResource(tempdir, XhtmlConstants.STYLES_CACHE_DIRECTORY + resourceUri);
        if (url == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        _setHeaders(context, connection, response);
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
