    public HttpResponse(HttpServerThread thread, DataOutputStream writer) {
        this.writer = writer;
        resultHeader = "HTTP/1.1 200 OK";
        resultHeaderCode = 200;
        outputEncoding = thread.getServer().getSettings().getDefaultResponseCharacterEncoding();
        headers.put("date:", dateFormatterGMT.format(new Date()));
        headers.put("server:", "blommers-it webserver");
        headers.put("connection:", "close");
        headers.put("content-type:", "text/html; charset=" + outputEncoding);
        headers.put("cache-control:", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        headers.put("pragma:", "no-cache");
        this.serverThread = thread;
    }
