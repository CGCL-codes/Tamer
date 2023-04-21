    private void proxyRequest(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        HttpClient httpClient = new DefaultHttpClient();
        HttpRequestBase method = null;
        if (request.getMethod().equals("GET")) {
            method = new HttpGet(url);
        } else if (request.getMethod().equals("POST")) {
            method = new HttpPost(url);
            Enumeration<?> paramNames = request.getParameterNames();
            HttpParams params = new BasicHttpParams();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement().toString();
                params.setParameter(paramName, request.getParameter(paramName));
            }
            method.setParams(params);
        } else {
            throw new Exception("Supports GET and POST methods only.");
        }
        try {
            HttpResponse proxyResponse = httpClient.execute(method);
            response.setStatus(proxyResponse.getStatusLine().getStatusCode());
            HeaderIterator headers = proxyResponse.headerIterator();
            while (headers.hasNext()) {
                Header header = headers.nextHeader();
                if ("Server".equalsIgnoreCase(header.getName())) {
                    continue;
                }
                if ("Transfer-Encoding".equalsIgnoreCase(header.getName()) && "chunked".equalsIgnoreCase(header.getValue())) {
                    continue;
                }
                response.setHeader(header.getName(), header.getValue());
            }
            proxyResponse.getEntity().writeTo(out);
            out.flush();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
