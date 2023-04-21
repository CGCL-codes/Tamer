    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!this.supportGet) response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        final long startTime = System.currentTimeMillis();
        final ParameterResolver parameterResolver = new ParameterResolver(request);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(resolveName(parameterResolver)));
            os = response.getOutputStream();
            byte[] buffer = new byte[response.getBufferSize()];
            int l;
            while ((l = is.read(buffer)) >= 0) os.write(buffer, 0, l);
            response.setStatus(getHttpStatusCode());
        } finally {
            if (os != null) os.close();
            if (is != null) is.close();
            if (isDebugMode()) log(getProfilingMessage(request, startTime));
        }
    }
