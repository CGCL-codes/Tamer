    public static HttpRequest create(HttpExchange exchange) throws org.owasp.oss.httpserver.HttpHandlerException, IOException {
        Method method;
        log.info("Creating HttpRequest object, with following properties:");
        if (exchange.getRequestMethod().equals("GET")) method = Method.GET; else if (exchange.getRequestMethod().equals("POST")) method = Method.POST; else if (exchange.getRequestMethod().equals("PUT")) method = Method.PUT; else if (exchange.getRequestMethod().equals("DELETE")) method = Method.DELETE; else throw new org.owasp.oss.httpserver.HttpHandlerException("Could not create HttpRequest, no vaild method");
        log.info("\t Method: " + method);
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        log.info("\t Path: " + path);
        Headers headers = exchange.getRequestHeaders();
        Map<String, String> parameters = new HashMap<String, String>();
        String cookieStr = headers.getFirst("Cookie");
        if (cookieStr != null && cookieStr.length() > 0) HttpRequest.parseCookiesParameters(cookieStr, parameters);
        InputStream body = exchange.getRequestBody();
        String bodyLenStr = headers.getFirst("Content-Length");
        log.info("\t Body length: " + bodyLenStr);
        byte[] bodyBytes = null;
        if (bodyLenStr != null) {
            int bodyLen = Integer.parseInt(bodyLenStr);
            bodyBytes = new byte[bodyLen];
            ByteArrayOutputStream os = new ByteArrayOutputStream(bodyLen);
            int readByteNum = 0;
            while ((readByteNum = body.read(bodyBytes)) > 0) {
                os.write(bodyBytes, 0, readByteNum);
            }
            bodyBytes = os.toByteArray();
            if (bodyBytes != null) HttpRequest.parsePostParameters(new ByteArrayInputStream(bodyBytes), bodyBytes.length, parameters);
        }
        return new HttpRequest(method, path, bodyBytes, parameters, exchange.getPrincipal());
    }
