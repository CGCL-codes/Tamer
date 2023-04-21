    public void handle(String url, String arg1, HttpRequest request, HttpResponse response) throws HttpException, IOException {
        if (ConfigServer.spamatoFactory.getInstance() == null) {
            HtmlWriter writer = new HtmlWriter(request.getOutputStream(), null);
            writer.writeLnIndent("<html>");
            writer.writeLnIndent("<head>");
            writer.writeLnIndent("<title>Spamato</title>");
            writer.writeLnIndent("<meta http-equiv='cache-control' content='no-cache'>");
            writer.writeLnIndent("<meta http-equiv='pragma' content='no-cache'>");
            writer.writeLnIndent("<meta http-equiv='expires' content='0'>");
            writer.writeLnIndent("<link rel='stylesheet' type='text/css' href='/css/spamato.css' />");
            writer.writeLnIndent("</head>");
            writer.writeLnIndent("<body>");
            writer.writeLnIndent("Spamato is not yet ready to serve your requests. Please try again in a few seconds.");
            writer.writeLnIndent("</body>");
            writer.writeLnIndent("</html>");
            writer.close();
        } else {
            InetAddress remoteHost = request.getHttpConnection().getRemoteInetAddress();
            if (!isAllowedHost(remoteHost)) {
                System.err.println("[SecurityHandler] Blocking request from: " + remoteHost.getHostName() + " (" + remoteHost.getHostAddress() + ")");
                response.sendError(403);
            }
        }
    }
