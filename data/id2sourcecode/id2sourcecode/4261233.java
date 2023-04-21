    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setAttribute(Globals.SSI_FLAG_ATTR, "true");
        ByteArrayServletOutputStream basos = new ByteArrayServletOutputStream();
        ResponseIncludeWrapper responseIncludeWrapper = new ResponseIncludeWrapper(config.getServletContext(), req, res, basos);
        chain.doFilter(req, responseIncludeWrapper);
        responseIncludeWrapper.flushOutputStreamOrWriter();
        byte[] bytes = basos.toByteArray();
        String contentType = responseIncludeWrapper.getContentType();
        if (contentTypeRegEx.matcher(contentType).matches()) {
            String encoding = res.getCharacterEncoding();
            SSIExternalResolver ssiExternalResolver = new SSIServletExternalResolver(config.getServletContext(), req, res, isVirtualWebappRelative, debug, encoding);
            SSIProcessor ssiProcessor = new SSIProcessor(ssiExternalResolver, debug);
            Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes), encoding);
            ByteArrayOutputStream ssiout = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(ssiout, encoding));
            long lastModified = ssiProcessor.process(reader, responseIncludeWrapper.getLastModified(), writer);
            writer.flush();
            bytes = ssiout.toByteArray();
            if (expires != null) {
                res.setDateHeader("expires", (new java.util.Date()).getTime() + expires.longValue() * 1000);
            }
            if (lastModified > 0) {
                res.setDateHeader("last-modified", lastModified);
            }
            res.setContentLength(bytes.length);
            Matcher shtmlMatcher = shtmlRegEx.matcher(responseIncludeWrapper.getContentType());
            if (shtmlMatcher.matches()) {
                String enc = shtmlMatcher.group(1);
                res.setContentType("text/html" + ((enc != null) ? enc : ""));
            }
        }
        OutputStream out = null;
        try {
            out = res.getOutputStream();
        } catch (IllegalStateException e) {
        }
        if (out == null) {
            res.getWriter().write(new String(bytes));
        } else {
            out.write(bytes);
        }
    }
