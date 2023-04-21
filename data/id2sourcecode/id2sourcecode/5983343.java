    @Override
    public void process(Socket clientSocket, Map<String, String> header, Map<String, String> request) {
        String location = header.get(RequestHandler.REQUEST_LOCATION);
        File requestPath = new File(getServer().getDocumentRoot(), location);
        PrintWriter out = null;
        if (getLogger().isDebugEnabled()) getLogger().info("doServeFile(\"" + location + "\")");
        try {
            if (location.compareTo(RequestHandler.REQUEST_DEFAULT_URL) == 0) {
                File defaultURL = new File(getServer().getDocumentRoot(), RequestHandler.REQUEST_WEBIF_ROOT);
                if (header.containsKey(RequestHandler.HTTP_LANGUAGE)) {
                    String parts[] = header.get(RequestHandler.HTTP_LANGUAGE).split(",");
                    String lParts[];
                    File chk;
                    for (String lang : parts) {
                        lParts = lang.split(";");
                        lang = lParts[0];
                        getLogger().info("user accepts language: " + lang);
                        chk = new File(getServer().getDocumentRoot(), new StringBuilder(RequestHandler.REQUEST_WEBIF_BASE).append(lang).append(RequestHandler.REQUEST_WEBIF_EXT).toString());
                        if (chk.exists() && chk.isFile() && chk.canRead()) {
                            defaultURL = chk;
                            break;
                        }
                        getLogger().info("no match for: " + chk.getAbsolutePath());
                    }
                }
                requestPath = defaultURL;
            }
            if (requestPath.getCanonicalPath().startsWith(getServer().getDocumentRoot().getCanonicalPath())) {
                if (getLogger().isDebugEnabled()) getLogger().info("should serve file: " + requestPath.getCanonicalPath());
                if (requestPath.isFile() && requestPath.canRead()) {
                    FileInputStream fis = new FileInputStream(requestPath);
                    OutputStream os = new BufferedOutputStream(clientSocket.getOutputStream());
                    byte[] buf = new byte[1000000];
                    int bytesRead = 0;
                    if (requestPath.getName().endsWith(".html") || requestPath.getName().endsWith(".js") || requestPath.getName().endsWith(".css")) {
                        out = new PrintWriter(os);
                        String mimeType = MIME_HTML;
                        if (requestPath.getName().endsWith(".js")) mimeType = MIME_JS; else if (requestPath.getName().endsWith(".css")) mimeType = MIME_CSS;
                        initResponse(out, mimeType, new Date(requestPath.lastModified()), requestPath.length(), "de");
                        while ((bytesRead = fis.read(buf)) > 0) {
                            String text = new String(buf, 0, bytesRead, Charset.forName("UTF-8"));
                            out.print(text);
                        }
                        out.flush();
                        fis.close();
                        fis = null;
                        buf = null;
                    } else {
                        while ((bytesRead = fis.read(buf)) > 0) os.write(buf, 0, bytesRead);
                        os.flush();
                        fis.close();
                        fis = null;
                        buf = null;
                    }
                } else {
                    out = new PrintWriter(clientSocket.getOutputStream());
                    getProtocolHandler().err404(out, location);
                    out.close();
                    ;
                }
            } else {
                out = new PrintWriter(clientSocket.getOutputStream());
                getProtocolHandler().err404(out, location);
                out.close();
                ;
            }
        } catch (Throwable t) {
            try {
                out = new PrintWriter(clientSocket.getOutputStream());
                getProtocolHandler().err404(out, location);
                out.close();
                ;
            } catch (Throwable st) {
                getLogger().fatal("failed to process request [" + requestPath + "]", st);
            }
        }
    }
