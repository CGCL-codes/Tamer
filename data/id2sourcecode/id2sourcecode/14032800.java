    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!this.supportPut) response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        final long startTime = System.currentTimeMillis();
        final ParameterResolver parameterResolver = new ParameterResolver(request);
        final File file = resolveName(parameterResolver);
        int httpStatusCode = getHttpStatusCode();
        if (!file.exists()) httpStatusCode = HttpServletResponse.SC_CREATED;
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(request.getInputStream());
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[16384];
            int l;
            while ((l = is.read(buffer)) >= 0) os.write(buffer, 0, l);
            response.setStatus(httpStatusCode);
        } finally {
            if (os != null) os.close();
            if (is != null) is.close();
            if (isDebugMode()) log(getProfilingMessage(request, startTime));
        }
    }
