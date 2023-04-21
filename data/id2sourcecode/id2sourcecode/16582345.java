    public static void sendDebugPage(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            List values = requestHeaders.get(key);
            String s = key + " = " + values.toString() + "\n";
            responseBody.write(s.getBytes());
        }
        Map<String, Object> as = exchange.getHttpContext().getAttributes();
        keySet = as.keySet();
        iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String s = key + " = " + as.get(key) + "\n";
            responseBody.write(s.getBytes());
        }
        responseBody.write("HttpPrincipal:\n".getBytes());
        HttpPrincipal principal = exchange.getPrincipal();
        if (principal != null) responseBody.write(principal.toString().getBytes());
        responseBody.write("Body:\n".getBytes());
        InputStream body = exchange.getRequestBody();
        while (body.available() > 0) responseBody.write(body.read());
        responseBody.close();
    }
