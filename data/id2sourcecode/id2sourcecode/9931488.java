    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        URL requestURL = new URL(request.getRequestURL().toString());
        if (checkSameOrigin) {
            String referer = request.getHeader("Referer");
            if (StringUtils.isBlank(referer)) return;
            URL url = new URL(referer);
            if (!url.getProtocol().equalsIgnoreCase(requestURL.getProtocol()) || !url.getHost().equalsIgnoreCase(requestURL.getHost()) || url.getPort() != requestURL.getPort()) return;
        }
        String uri = requestURL.toString();
        String target = null;
        int index = uri.indexOf("http://", uri.indexOf("://") + 1);
        if (index > 0) target = uri.substring(index);
        if (target == null) {
            index = uri.indexOf("https://", uri.indexOf("://") + 1);
            if (index > 0) target = uri.substring(index);
        }
        if (target == null) return;
        String queryString = request.getQueryString();
        StringBuilder sb = new StringBuilder();
        sb.append(target);
        if (StringUtils.isNotBlank(queryString)) {
            sb.append('?');
            sb.append(queryString);
        }
        uri = sb.toString();
        HttpRequestBase httpRequest;
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            httpRequest = new HttpGet(uri);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            httpRequest = new HttpPost(uri);
        } else if ("PUT".equalsIgnoreCase(request.getMethod())) {
            httpRequest = new HttpPut(uri);
        } else if ("DELETE".equalsIgnoreCase(request.getMethod())) {
            httpRequest = new HttpDelete(uri);
        } else {
            httpRequest = new HttpGet(uri);
        }
        if (httpRequest instanceof HttpEntityEnclosingRequestBase) {
            Enumeration<String> en = request.getParameterNames();
            if (en.hasMoreElements()) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                while (en.hasMoreElements()) {
                    String name = en.nextElement();
                    for (String value : request.getParameterValues(name)) if (queryString == null || !queryString.contains(name + "=")) nvps.add(new BasicNameValuePair(name, value));
                }
                ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            }
        }
        HttpEntity entity = null;
        try {
            HttpResponse rsp = httpClient.execute(httpRequest);
            StatusLine sl = rsp.getStatusLine();
            if (sl.getStatusCode() >= 300) {
                response.sendError(sl.getStatusCode(), sl.getReasonPhrase());
                httpRequest.abort();
                return;
            }
            entity = rsp.getEntity();
            if (entity != null) {
                for (Header h : httpRequest.getAllHeaders()) response.setHeader(h.getName(), h.getValue());
                entity.writeTo(response.getOutputStream());
            }
        } catch (Exception e) {
            httpRequest.abort();
            e.printStackTrace();
        }
    }
