    @SuppressWarnings("unchecked")
    private void doHTTPOperation(HttpServletRequest request, HttpServletResponse response, HttpRequestBase requestBase) throws ServletException, IOException {
        String newURL = getURL(request);
        String params = getParams(request);
        if (params.length() > 0) {
            params = "?" + params;
        }
        String finalUrl = newURL + params;
        logger.info("Sending " + requestBase.getMethod() + " request to :" + finalUrl);
        requestBase.setURI(URI.create(finalUrl));
        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements(); ) {
            String headerName = e.nextElement().toString().toLowerCase();
            String headerValue = request.getHeader(headerName);
            if (getValidHeaderParameters().contains(headerName)) {
                addHeaderParameter(requestBase, headerName, headerValue);
            }
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse httpResponse = httpclient.execute(requestBase);
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null) {
            String contentType = httpEntity.getContentType().getValue();
            response.setContentType(contentType);
            if (NON_CACHEABLE_CONTENT_TYPES.contains(contentType)) {
                response.setHeader(CACHE_CONTROL_PARAM, CACHE_CONTROL_NOCACHE);
            }
            String responseContent = getStreamContent(httpEntity.getContent());
            PrintWriter out = response.getWriter();
            logger.debug("Message:" + responseContent);
            out.println(responseContent);
            out.close();
            logger.info("Getting " + requestBase.getMethod() + " response to :" + newURL);
        } else {
            logger.error("Could not open remote connection to:" + newURL);
        }
    }
